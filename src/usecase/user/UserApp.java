/**
 * @author CY21249 TAKAGI Masamune
 */

package usecase.user;

import java.util.*;
import database.data.delivery.*;
import database.data.location.*;
import database.data.location.receipt_location.*;
import database.data.model.*;
import database.data.product.*;
import database.data.user.*;
import database.executor.delivery.*;
import database.executor.location.receipt_location.*;
import database.executor.product.*;
import database.executor.user.*;
import database.executor.purchase.*;
import myutil.*;
import usecase.UsecaseUtil;
import usecase.user.sub.UserPurchaseApp;

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
		this.selectCommand();
	}

	public void selectCommand() {
		console.print(this.user, "でログインしています");

		int cmd = console.select("操作を選んでください",
			"1. 商品を閲覧する",
			"2. 購入履歴を見る",
			"3. 運送状況を見る",
			"4. 運送の詳細を見る",
			"5. ログアウトする");

		switch (cmd) {
			case 1:
				// 状態遷移: 進む (カテゴリ選択へ)
				new UserPurchaseApp(this).selectCategorySection();
				break;
			case 2:
				// 状態遷移: 進む (購入履歴へ)
				this.showPurchaseHistory();
				break;
			case 3:
				// 状態遷移: 進む (運送状況の確認へ)
				this.showPurchasedProductCurrentLocation();
				break;
			case 4:
				// 状態遷移: 進む (運送の詳細へ)
				this.showProductDelivery();
				break;
			case 5:
				if (console.confirm("ログアウトしますか？")) {
					console.print("ログアウトしました");
					return;
				}
				break;
		}

		// 状態遷移: 繰り返し
		selectCommand();
	}

	public boolean registerReceiptLocation() {
		console.print("受け取り場所を登録します。");

		while (true) {
			final ArrayList<ReceiptLocationData> recLocList = new GetReceiptLocationCandidateList(this.user.key).execute();
			final String[] recLocStrList = new String[recLocList.size()];
			int i = 0;
			for (final ReceiptLocationData lecLoc : recLocList)
				recLocStrList[i++] = i + ". " + lecLoc.key.name + "\n\t" + lecLoc.address;

			int recLocRegisterAns = console.selectWithCancel("リストに登録する受け取り場所を選択してください", "0. キャンセル", (Object[]) recLocStrList) - 1;
			if (recLocRegisterAns < 0)
				return false;

			ReceiptLocationData recLoc = recLocList.get(recLocRegisterAns);

			if (recLoc == null)
				// キャンセル
				return false;

			if (!console.confirm(recLoc.key.name + "を受け取り場所リストに登録します"))
				// やり直し
				this.registerReceiptLocation();

			// 登録
			this.user.registerReceiptLocation(recLoc.key);
			return true;
		}
	}

	public void showPurchaseHistory() {
		ArrayList<ProductDataWithModel> productList = new GetPurchaseHistory(this.user.key).execute();

		console.print("購入履歴");
		int priceSum = 0;
		for (ProductDataWithModel product : productList) {
			DeliveryData delivery = new GetLastDeliveryOfProduct(product.key).execute();

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

	public void showPurchasedProductCurrentLocation() {
		ArrayList<ProductKey> productList = this.user.fetchDeliveryNotFinishedProductList();

		for (ProductKey product : productList) {
			DeliveryData delivery = this.user.fetchCurrentDeliveryOf(product);
			if (delivery == null)
				continue;
			if (delivery.endTime != null)
				continue;

			ProductDataWithModel productData = new GetProductWithModel(product).execute();

			console.print(" -", productData.modelData.name, delivery.key.product.code);
			if (delivery.startTime != null)
				console.print("\t", delivery.key.fromLocation.name + " から " + delivery.toLocation.name + " に運送中です");
			else
				console.print("\t", delivery.key.fromLocation.name + " にあります");
		}

		console.next("以上の配送があります。");
	}

	public void showProductDelivery() {
		ArrayList<ProductData> productList = this.user.fetchPurchasedProductList();
		String[] productStrs = new String[productList.size()];
		int i = 0;
		for (ProductData product : productList) {
			ModelData model = new GetModel(product.key.model).execute();
			productStrs[i++] = i + ". " + model.name + " - " + product.key.code;
		}
		int ansIdx = console.select("運送の詳細を見たい商品を選んでください", (Object[]) productStrs) - 1;
		if (ansIdx < 0)
			return;

		ProductData product = productList.get(ansIdx);
		ArrayList<DeliveryData> deliveryList = this.user.fetchDeliveryListOf(product.key);
		DeliveryData currentDelivery = this.user.fetchCurrentDeliveryOf(product.key);
		LocationKey currentLocation = currentDelivery == null ? deliveryList.get(deliveryList.size() - 1).toLocation
			: currentDelivery.startTime == null ? currentDelivery.key.fromLocation
				: currentDelivery.endTime == null ? null : currentDelivery.toLocation;
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
		LocationKey lastLocation = deliveryList.get(deliveryList.size() - 1).toLocation;
		console.next(currentLocation != null && lastLocation.equals(currentLocation) ? " ●" : " -",
			lastLocation.name);
	}
}
