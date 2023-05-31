package usecase.user.sub;

import java.util.*;

import database.data.location.receipt_location.ReceiptLocationData;
import database.data.model.ModelDataWithProductCount;
import database.data.model_category.ModelCategoryData;
import database.data.user.UserData;
import database.executor.location.receipt_location.GetReceiptLocationListByUser;
import database.executor.product.GetCategoryList;
import database.executor.product.GetModelListByCategory;
import module.route_manager.DeliveryPlanCreator;
import myutil.*;
import usecase.user.UserApp;

public class UserPurchaseApp {
	private final UserApp userApp;

	private static final Console console = new Console();

	public UserPurchaseApp(UserApp userApp) {
		this.userApp = userApp;
	}

	public void selectCategorySection() {
		final ArrayList<ModelCategoryData> categoryDataList = new GetCategoryList().execute();

		final String[] categoryStrList = new String[categoryDataList.size()];
		int i = 0;
		for (final ModelCategoryData categoryData : categoryDataList)
			categoryStrList[i++] = i + ". " + categoryData.key.name;

		final int categoryIndex = console.selectWithCancel("閲覧したい商品カテゴリーを選んでください", "0. 戻る", categoryStrList) - 1;
		if (categoryIndex < 0)
			return;

		// 状態遷移: 進む (商品選択へ)
		this.selectModelSection(categoryDataList.get(categoryIndex));
	}

	public void selectModelSection(ModelCategoryData selectedCategory) {
		final ArrayList<ModelDataWithProductCount> modelDataList = new GetModelListByCategory(selectedCategory.key)
			.execute();

		if (modelDataList.size() == 0) {
			System.out.println("現在、このカテゴリの商品はありません");
			// 状態遷移: 戻る (カテゴリ選択へ)
			this.selectCategorySection();
			return;
		}

		final String[] modelStrList = new String[modelDataList.size()];
		int i = 0;
		for (final ModelDataWithProductCount modelData : modelDataList)
			modelStrList[i++] = i + ". [" + modelData.key.code + "] " + modelData.name + " (" + modelData.manufacturer
				+ ")\n\t在庫: " + modelData.productCount + ", ￥" + modelData.price;

		final int modelIndex = console.selectWithCancel("購入したい商品をお選びください", "0. 戻る", modelStrList) - 1;
		if (modelIndex < 0) {
			// 状態遷移: 戻る (カテゴリ選択へ)
			this.selectCategorySection();
			return;
		}

		// 状態遷移: 進む (購入へ)
		this.purchaseSection(selectedCategory, modelDataList.get(modelIndex));
	}

	void purchaseSection(ModelCategoryData selectedCategory, ModelDataWithProductCount selectedModel) {
		UserData ud = this.userApp.user.getData();
		if (ud.money < selectedModel.price) {
			console.error("残高が不足しているため、商品を購入できません。\n残高: ￥" + ud.money + " , 商品の金額: ￥" + selectedModel.price);
			// 状態遷移: 戻る (カテゴリ選択へ)
			this.selectModelSection(selectedCategory);
			return;
		}

		console.print("[" + selectedModel.key.code + "]", selectedModel.name,
			"を購入します\n\t価格: ￥" + selectedModel.price + "\n\t所持金: ￥" + ud.money + "\n");

		final ArrayList<ReceiptLocationData> userRecLocList = new GetReceiptLocationListByUser(this.userApp.user.key)
			.execute();
		final String[] userRecLocStrList = new String[userRecLocList.size() + 1];
		int i = 0;
		for (final ReceiptLocationData lecLoc : userRecLocList)
			userRecLocStrList[i++] = i + ". " + lecLoc.key.name + "\n\t" + lecLoc.address;
		userRecLocStrList[i] = (i + 1) + ". 受け取り場所を追加登録する";

		int lastIdx = userRecLocList.size();

		int recLocAns = console.selectWithCancel("受け取り場所を選択してください", "0. キャンセル", userRecLocStrList) - 1;

		if (recLocAns < 0) {
			// 状態遷移: 戻る (商品選択へ)
			this.selectModelSection(selectedCategory);
			return;
		}

		if (recLocAns == lastIdx) {
			// 状態遷移: 進む (受け取り場所登録へ)
			this.userApp.registerReceiptLocation();
			// 状態遷移: 繰り返し (購入へ)
			this.purchaseSection(selectedCategory, selectedModel);
			return;
		}

		ReceiptLocationData selectedRecLoc = userRecLocList.get(recLocAns);

		console.print("受け取り場所を", selectedRecLoc.key.name, "に指定しました。");

		if (console.confirm("以下の内容で注文を確定します。\n\t注文商品: " + selectedModel.manufacturer + " " + selectedModel.name + " (￥"
			+ selectedModel.price + ")", "\n\t受取場所:", selectedRecLoc.key.name)) {
			// 状態遷移: 進む (購入確認へ)
			this.purchaseConfirm(selectedCategory, selectedModel, selectedRecLoc);
			return;
		} else {
			// 状態遷移: 繰り返し
			this.purchaseSection(selectedCategory, selectedModel);
			return;
		}
	}

	void purchaseConfirm(ModelCategoryData selectedCategory, ModelDataWithProductCount selectedModel,
		ReceiptLocationData selectedRecLoc) {
		console.print("購入処理中です...");
		try {
			this.userApp.user.purchase(selectedModel.key, selectedModel.price, selectedRecLoc.key);
		} catch (Exception e) {
			if (e.getMessage().equals("在庫不足"))
				console.error("在庫がなくなってしまったため、商品を購入できませんでした。");
			else if (e.getMessage().equals("残高不足"))
				console.error("残高が不足しているため、商品を購入できませんでした。");

			// 状態遷移: 戻る (購入へ)
			this.purchaseSection(selectedCategory, selectedModel);
			return;
		}

		console.next("購入が完了しました。\n残高: ￥" + this.userApp.user.getData().money + "\n商品が指定の場所に届くまでしばらくお待ちください。");

		// 状態遷移: 戻る (最初へ)
		this.userApp.selectCommand();
	}

}
