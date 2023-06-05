/**
 * @author CY21249 TAKAGI Masamune
 */

package usecase.user;

import java.util.*;
import myutil.*;

import usecase.UsecaseUtil;
import usecase.user.sub.UserPurchaseApp;

import database.data.delivery.*;
import database.data.location.*;
import database.data.location.receipt_location.*;
import database.data.product.*;
import database.data.user.*;

import database.executor.user.*;

public class UserApp {
	public final User user;

	public static final Console console = new Console();

	public static void main(String[] args) {
		UsecaseUtil.waitUntilSQLServerRunning();

		UserKey userKey;
		while (true) {
			try {
				String userIDStr = console.prompt("ログインするユーザーIDを入力してください");

				if (userIDStr.equals("exit") || userIDStr.equals("0"))
					return;

				userKey = new UserKey(Integer.parseInt(userIDStr));

				UserData userData = new GetUser(userKey).execute();
				if (userData != null)
					break;

				console.error("ユーザーIDが存在しません");
				continue;
			} catch (NumberFormatException e) {
				console.error("数値を入力してください");
			}
		}

		new UserApp(new User(userKey)).run();

		main(args);
	}

	public UserApp(User user) {
		this.user = user;
	}

	public void run() {
		console.print("ログインしました");
		this.selectSection();
	}

	public void selectSection() {
		UserData user = this.user.getData();
		console.print(user.key.id + " " + user.name, "でログインしています");
		console.print("現在の残金: ￥" + this.user.getData().money);

		int cmd = console.select("操作を選んでください",
			"1. 商品を閲覧する",
			"2. 購入履歴を見る",
			"3. 運送状況を見る",
			"4. 運送の詳細を見る",
			"5. 残金にチャージする",
			"6. ログアウトする");

		switch (cmd) {
			case 1:
				// 状態遷移: 進む (カテゴリ選択へ)
				new UserPurchaseApp(this).selectCategorySection();
				break;
			case 2:
				// 状態遷移: 進む (購入履歴へ)
				this.showPurchaseHistorySection();
				break;
			case 3:
				// 状態遷移: 進む (運送状況の確認へ)
				this.showPurchasedProductCurrentLocationSection();
				break;
			case 4:
				// 状態遷移: 進む (運送の詳細へ)
				this.showProductDeliverySection();
				break;
			case 5:
				// 状態遷移: 進む (所持金の追加へ)
				this.addMoneySection();
				break;
			case 6:
				if (console.confirm("ログアウトしますか？")) {
					console.print("ログアウトしました");
					return;
				}
				break;
		}

		// 状態遷移: 繰り返し
		selectSection();
	}

	public void registerReceiptLocationSection() {
		console.print("受け取り場所に登録する場所を選択します");
		// 受け取り場所の選択肢を作成
		final ArrayList<ReceiptLocationData> recLocList = this.user.fetchReceiptLocationCandidateList();
		final String[] recLocStrList = new String[recLocList.size()];
		int i = 0;
		for (final ReceiptLocationData lecLoc : recLocList)
			recLocStrList[i++] = i + ". " + lecLoc.key.name + "\n\t" + lecLoc.address;

		// 受け取り場所の選択肢を表示
		int recLocRegisterAns = console.selectWithCancel("リストに登録する受け取り場所を選択してください", "0. キャンセル", (Object[]) recLocStrList) - 1;
		if (recLocRegisterAns < 0)
			// キャンセル
			return;

		ReceiptLocationData recLoc = recLocList.get(recLocRegisterAns);

		// 登録の確認
		if (!console.confirm(recLoc.key.name + "を受け取り場所リストに登録します"))
			// やり直し
			this.registerReceiptLocationSection();

		// 登録
		this.user.registerReceiptLocation(recLoc.key);
	}

	public void showPurchaseHistorySection() {
		ArrayList<ProductDataWithModel> productList = this.user.fetchPurchasedProductList();

		console.print("購入履歴");
		int priceSum = 0;
		// 購入履歴のある商品の情報とお届け済みかどうかを表示
		for (ProductDataWithModel product : productList) {
			DeliveryData delivery = this.user.fetchLastDeliveryOf(product.key);

			console.print(" -", (delivery.endTime != null ? "配達済" : "配達中"),
				"[" + product.modelData.key.code + "]", product.modelData.name, " - ", product.key.code);
			console.print("\t", "￥", product.purchasedPrice, "(定価: ￥" + product.modelData.price + ")");
			console.print("\t", "購入日:", product.purchasedTime);
			if (delivery.endTime != null)
				console.print("\t", "お届け日:", delivery.endTime);
			console.print("");

			priceSum += product.purchasedPrice;
		}

		if (productList.size() == 0)
			console.next("まだ購入履歴がありません");
		else {
			console.print(productList.size(), "件の購入履歴がありました。");
			console.next("合計金額:", priceSum);
		}
	}

	public void showPurchasedProductCurrentLocationSection() {
		ArrayList<ProductDataWithModel> productList = this.user.fetchDeliveryNotFinishedProductList();

		// 運送中の商品とその場所を表示
		for (ProductDataWithModel product : productList) {
			DeliveryData delivery = this.user.fetchCurrentDeliveryOf(product.key);
			if (delivery == null)
				continue;
			if (delivery.endTime != null)
				continue;

			console.print(" -", product.modelData.name, delivery.key.product.code);
			if (delivery.startTime != null)
				console.print("\t", delivery.key.fromLocation.name + " から " + delivery.toLocation.name + " に運送中です");
			else
				console.print("\t", delivery.key.fromLocation.name + " にあります");
		}
	}

	public void showProductDeliverySection() {
		// 購入済み商品の選択肢を作成
		ArrayList<ProductDataWithModel> productList = this.user.fetchPurchasedProductList();
		String[] productStrs = new String[productList.size()];
		int i = 0;
		for (ProductDataWithModel product : productList) {
			productStrs[i++] = i + ". " + product.modelData.name + " - " + product.key.code;
		}

		// 詳細を見る商品の選択 
		int ansIdx = console.select("運送の詳細を見たい商品を選んでください", (Object[]) productStrs) - 1;
		if (ansIdx < 0)
			return;
		ProductData product = productList.get(ansIdx);

		// 商品の運送リストを取得
		ArrayList<DeliveryData> deliveryList = this.user.fetchDeliveryListOf(product.key);

		// 現在の運送を取得
		DeliveryData currentDelivery = this.user.fetchCurrentDeliveryOf(product.key);
		LocationKey currentLocation = currentDelivery == null ? deliveryList.get(deliveryList.size() - 1).toLocation
			: currentDelivery.startTime == null ? currentDelivery.key.fromLocation
				: currentDelivery.endTime == null ? null : currentDelivery.toLocation;

		// 運送リストと現在の運送場所を表示
		for (DeliveryData delivery : deliveryList) {
			console.print(currentLocation != null && delivery.key.fromLocation.equals(currentLocation) ? " ●" : " -",
				delivery.key.fromLocation.name);
			if (delivery.endTime != null) {
				console.print("\t┃   配送開始:", delivery.startTime);
				console.print("\t┃      配達員:", delivery.deliveryMember.code);
				console.print("\t┃   配送完了:", delivery.endTime);
			} else if (delivery.startTime != null) {
				console.print("\t┃   配送開始:", delivery.startTime);
				console.print("\t●      配送中…", delivery.deliveryMember.code);
				console.print("\t│");
			} else {
				console.print("\t│");
			}
		}

		// 最後の運送を表示
		LocationKey lastLocation = deliveryList.get(deliveryList.size() - 1).toLocation;
		console.next(currentLocation != null && lastLocation.equals(currentLocation) ? " ●" : " -",
			lastLocation.name);
	}

	public void addMoneySection() {
		console.print("現在の残高: ￥" + this.user.getData().money);
		// チャージ金額の入力
		String amountStr = console.prompt("チャージ金額を入力してください");
		if (amountStr.equals("")) {
			console.next("チャージをキャンセルしました");
			return;
		}

		// 入力の文字列を正の整数に変換
		int amount;
		try {
			amount = Integer.parseInt(amountStr);
			if (amount <= 0) {
				console.error("チャージする金額を正の整数で入力してください");
				this.addMoneySection();
				return;
			}
		} catch (NumberFormatException e) {
			console.error("チャージする金額を正の整数で入力してください");
			this.addMoneySection();
			return;
		}

		// チャージの確認
		if (!console.confirm("￥" + amount + " をチャージしますか？\n\tチャージ後の残高: ￥" + (this.user.getData().money + amount))) {
			console.next("チャージをキャンセルしました");
			return;
		}

		// チャージを実行
		new AddUserMoney(this.user.key, amount).execute();

		console.print("チャージが完了しました");
		console.next("残高: ￥" + this.user.getData().money);
	}
}
