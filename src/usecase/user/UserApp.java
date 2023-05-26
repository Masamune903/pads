package usecase.user;

import java.util.*;

import data.*;
import myutil.*;
import usecase.user.query.*;

public class UserApp {
	public final User user;

	public static final Console console = new Console();

	public static void main(String[] args) {
		int userID;
		UserData user;
		while (true) {
			try {
				String userIDStr = console.prompt("ログインするユーザーIDを入力してください");
				userID = Integer.parseInt(userIDStr);

				user = new GetUser(userID).execute();
				if (user != null)
					break;

				console.error("ユーザーIDが存在しません");
				continue;
			} catch (NumberFormatException e) {
				console.error("数値を入力してください");
			}
		}

		new UserApp(new User(user.id)).run();
	}

	public UserApp(User user) {
		this.user = user;
	}

	public void run() {
		while (true) {
			console.log(this.user, "でログインしています");

			int cmd = console.select("操作を選んでください",
					"1. 商品を閲覧する",
					"2. 購入履歴を見る",
					"3. 運送状況を見る",
					"4. ログアウトする");

			switch (cmd) {
				case 1:
					this.selectCategorySection();
					break;
			}

			if (cmd == 4) {
				if (console.confirm("ログアウトしますか？"))
					break;
			}
		}
	}

	private void selectCategorySection() {
		boolean isEnd;
		do {
			final ArrayList<CategoryData> categoryDataList = new GetCategoryList().execute();
			final String[] categoryStrList = new String[categoryDataList.size()];
			int i = 0;
			for (final CategoryData categoryData : categoryDataList)
				categoryStrList[i++] = i + ". " + categoryData.name;

			final int categoryIndex = console.selectWithCancel("閲覧したい商品カテゴリーを選んでください", "0. 戻る", categoryStrList) - 1;
			if (categoryIndex < 0)
				return;

			isEnd = this.selectModelSection(categoryDataList.get(categoryIndex).name);
		} while (!isEnd);
	}

	private boolean selectModelSection(String category) {
		final ArrayList<ModelDataWithProductCount> modelDataList = new GetModelListByCategory(category).execute();

		if (modelDataList.size() == 0) {
			System.out.println("現在、このカテゴリの商品はありません");
			return false;
		}

		final String[] modelStrList = new String[modelDataList.size()];
		int i = 0;
		for (final ModelDataWithProductCount modelData : modelDataList)
			modelStrList[i++] = i + ". [" + modelData.code + "] " + modelData.name + " (" + modelData.manufacturer
					+ ")\n"
					+ "\t在庫: " + modelData.productCount + ", ￥" + modelData.price;

		final int modelIndex = console.selectWithCancel("購入したい商品をお選びください", "0. 戻る", modelStrList) - 1;
		if (modelIndex < 0)
			return false;

		return this.purchaseSection(modelDataList.get(modelIndex));
	}

	private boolean purchaseSection(final ModelDataWithProductCount model) {
		ReceiptLocationData selectedRecLoc = null;
		while (selectedRecLoc == null) {
			UserData ud = this.user.getData();
			if (ud.money < model.price) {
				console.error("残高が不足しているため、商品を購入できません。\n残高: ￥" + ud.money + " , 商品の金額: ￥" + model.price);
				return false;
			}

			console.log("[" + model.code + "]", model.name, "を購入します\n\t価格: ￥" + model.price + "\n\t所持金: ￥" + ud.money + "\n");

			final ArrayList<ReceiptLocationData> userRecLocList = new GetReceiptLocationByUser(this.user.id).execute();
			final String[] userRecLocStrList = new String[userRecLocList.size() + 1];
			int i = 0;
			for (final ReceiptLocationData lecLoc : userRecLocList)
				userRecLocStrList[i++] = i + ". " + lecLoc.name + "\n\t" + lecLoc.address;
			userRecLocStrList[i] = (i + 1) + ". 受け取り場所を追加登録する";

			int lastIdx = userRecLocList.size();

			int recLocAns = console.selectWithCancel("受け取り場所を選択してください", "0. キャンセル", userRecLocStrList) - 1;

			if (recLocAns < 0)
				return false;

			if (recLocAns == lastIdx) {
				this.registerReceiptLocation();
				continue;
			}

			selectedRecLoc = userRecLocList.get(recLocAns);

			console.log("受け取り場所が選択されました。", selectedRecLoc.name, "へお届けします。");

			if (!console.confirm("以下の内容で注文を確定します。\n\t注文商品: " + model.manufacturer + " " + model.name + " (￥" + model.price + ")", "\n\t受取場所:", selectedRecLoc.name))
				continue;
		}

		console.log("購入処理中です...");
		try {
			user.purchase(model.code, model.price, selectedRecLoc.name);
		} catch (Exception e) {
			if (e.getMessage().equals("在庫不足"))
				console.error("在庫がなくなってしまったため、商品を購入できませんでした。");
			else if (e.getMessage().equals("残高不足"))
				console.error("残高が不足しているため、商品を購入できませんでした。");

			return false;
		}

		console.next("購入が完了しました。\n残高: ￥" + this.user.getData().money + "\n商品が指定の場所に届くまでしばらくお待ちください。");

		return true;
	}

	private boolean registerReceiptLocation() {
		console.log("受け取り場所を登録します。");

		while (true) {
			final ArrayList<ReceiptLocationData> recLocList = new GetReceiptLocation().execute();
			final String[] recLocStrList = new String[recLocList.size()];
			int i = 0;
			for (final ReceiptLocationData lecLoc : recLocList)
				recLocStrList[i++] = i + ". " + lecLoc.name + "\n\t" + lecLoc.address;

			int recLocRegisterAns = console.selectWithCancel("リストに登録する受け取り場所を選択してください", "0. キャンセル", recLocStrList) - 1;
			if (recLocRegisterAns < 0)
				return false;

			ReceiptLocationData recLoc = recLocList.get(recLocRegisterAns);

			if (recLoc == null)
				// キャンセル
				return false;
			
			if (!console.confirm(recLoc.name + "を受け取り場所リストに登録します"))
				// やり直し
				continue;
			
			// 登録
			this.user.registerReceiptLocation(recLoc);
			return true;
		}
	}
}
