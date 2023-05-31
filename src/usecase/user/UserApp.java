package usecase.user;

import java.util.*;

import database.data.location.receipt_location.*;
import database.data.product.*;
import database.data.user.*;
import database.executor.location.receipt_location.*;
import database.executor.user.*;
import database.executor.purchase.*;
import myutil.*;
import usecase.App;
import usecase.user.sub.UserPurchaseApp;

public class UserApp {
	public final User user;

	public static final Console console = new Console();

	public static void main(String[] args) {
		App.waitUntilSQLServerRunning();

		UserKey userKey;
		while (true) {
			try {
				String userIDStr = console.prompt("ログインするユーザーIDを入力してください");
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
			"4. ログアウトする");

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
				break;
			case 4:
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
			final ArrayList<ReceiptLocationData> recLocList = new GetReceiptLocationList().execute();
			final String[] recLocStrList = new String[recLocList.size()];
			int i = 0;
			for (final ReceiptLocationData lecLoc : recLocList)
				recLocStrList[i++] = i + ". " + lecLoc.key.name + "\n\t" + lecLoc.address;

			int recLocRegisterAns = console.selectWithCancel("リストに登録する受け取り場所を選択してください", "0. キャンセル", recLocStrList) - 1;
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
		for (ProductDataWithModel product : productList) {
			console.print(" -", "[" + product.modelData.key.code + "]", product.modelData.name, " - ", product.code);
			console.print("\t", "￥", product.purchasedPrice, "(" + product.modelData.price + ")");
			console.print("\t", "購入日:", product.purchasedTime);
			console.print("");
		}
		if (productList.size() == 0)
			console.next("まだ購入履歴がありません");
		else
			console.next(productList.size(), "件の購入履歴がありました。");
	}
}
