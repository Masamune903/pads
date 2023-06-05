/**
 * @author CY21249 TAKAGI Masamune
 */

package usecase.user.sub;

import java.util.*;

import database.data.location.receipt_location.*;
import database.data.model.*;
import database.data.model_category.*;
import database.data.product.*;
import database.data.user.*;

import database.executor.product.*;

import myutil.*;

import usecase.user.User;
import usecase.user.UserApp;

public class UserPurchaseApp {
	private final UserApp userApp;
	private final User user;

	private static final Console console = new Console();

	public UserPurchaseApp(UserApp userApp) {
		this.userApp = userApp;
		this.user = userApp.user;
	}

	public void selectCategorySection() {
		// カテゴリ一覧を取得
		final ArrayList<ModelCategoryData> categoryDataList = new GetCategoryList().execute();

		// カテゴリ一覧の表示
		final String[] categoryStrList = new String[categoryDataList.size()];
		int i = 0;
		for (final ModelCategoryData categoryData : categoryDataList)
			categoryStrList[i++] = i + ". " + categoryData.key.name;

		// カテゴリの選択
		final int categoryIndex = console.selectWithCancel("閲覧したい商品カテゴリーを選んでください", "0. 戻る", (Object[]) categoryStrList) - 1;
		if (categoryIndex < 0)
			return;

		// 状態遷移: 進む (商品選択へ)
		this.selectModelSection(categoryDataList.get(categoryIndex));
	}

	public void selectModelSection(ModelCategoryData selectedCategory) {
		// カテゴリの購入可能な商品情報一覧を取得
		final ArrayList<ModelDataWithProductCount> modelDataList = new GetAvailableModelListByCategory(selectedCategory.key).execute();

		if (modelDataList.size() == 0) {
			// カテゴリの購入可能な商品がなかった場合
			System.out.println("現在、このカテゴリの商品はありません");
			// 状態遷移: 戻る (カテゴリ選択へ)
			this.selectCategorySection();
			return;
		}

		// 商品情報の選択肢を作成
		final String[] modelStrList = new String[modelDataList.size()];
		int i = 0;
		for (final ModelDataWithProductCount modelData : modelDataList)
			modelStrList[i++] = i + ". [" + modelData.key.code + "] " + modelData.name + " (" + modelData.manufacturer
				+ ")\n\t在庫: " + modelData.productCount + ", ￥" + modelData.price;

		// 商品情報の選択
		final int modelIndex = console.selectWithCancel("購入したい商品をお選びください", "0. 戻る", (Object[]) modelStrList) - 1;
		if (modelIndex < 0) {
			// 状態遷移: 戻る (カテゴリ選択へ)
			this.selectCategorySection();
			return;
		}

		// 状態遷移: 進む (購入へ)
		this.purchaseSection(selectedCategory, modelDataList.get(modelIndex));
	}

	void purchaseSection(ModelCategoryData selectedCategory, ModelDataWithProductCount selectedModel) {
		UserData userData = this.user.getData();
		// 残高の評価
		if (userData.money < selectedModel.price) {
			console.error("残高が不足しているため、商品を購入できません。\n残高: ￥" + userData.money + " , 商品の金額: ￥" + selectedModel.price);
			// 状態遷移: 戻る (カテゴリ選択へ)
			this.selectModelSection(selectedCategory);
			return;
		}

		console.print("[" + selectedModel.key.code + "]", selectedModel.name,
			"を購入します\n\t価格: ￥" + selectedModel.price + "\n\t所持金: ￥" + userData.money + "\n");

		// 受け取り場所の選択肢の作成
		final ArrayList<ReceiptLocationData> userRecLocList = this.user.fetchReceiptLocationList();
		final String[] userRecLocStrList = new String[userRecLocList.size() + 1];
		int i = 0;
		for (final ReceiptLocationData lecLoc : userRecLocList)
			userRecLocStrList[i++] = i + ". " + lecLoc.key.name + "\n\t" + lecLoc.address;
		userRecLocStrList[i] = (i + 1) + ". 受け取り場所を追加登録する";

		int lastIdx = userRecLocList.size();

		// 受け取り場所の選択
		int recLocAns = console.selectWithCancel("受け取り場所を選択してください", "0. キャンセル", (Object[]) userRecLocStrList) - 1;

		if (recLocAns < 0) {
			// 状態遷移: 戻る (商品選択へ)
			this.selectModelSection(selectedCategory);
			return;
		}

		// 受け取り場所の登録
		if (recLocAns == lastIdx) {
			// 状態遷移: 進む (受け取り場所登録へ)
			this.userApp.registerReceiptLocationSection();
			// 状態遷移: 繰り返し (購入へ)
			this.purchaseSection(selectedCategory, selectedModel);
			return;
		}

		ReceiptLocationData selectedRecLoc = userRecLocList.get(recLocAns);

		// 受け取り場所の確認
		console.print("受け取り場所を", selectedRecLoc.key.name, "に指定しました。");

		// 注文の確認
		if (console.confirm("以下の内容で注文を確定します。\n\t注文商品: " + selectedModel.manufacturer + " " + selectedModel.name + " (￥"
			+ selectedModel.price + ")", "\n\t受取場所:", selectedRecLoc.key.name)) {
			// 状態遷移: 進む (購入確認へ)
			this.purchaseConfirm(selectedCategory, selectedModel, selectedRecLoc);
			return;
		} else {
			console.next("購入をキャンセルしました");
			// 状態遷移: 繰り返し
			this.purchaseSection(selectedCategory, selectedModel);
			return;
		}
	}

	void purchaseConfirm(ModelCategoryData selectedCategory, ModelDataWithProductCount selectedModel, ReceiptLocationData selectedRecLoc) {
		// 購入確定処理
		console.print("購入処理中です...");
		ProductData purchased;
		try {
			purchased = this.user.purchase(selectedModel.key, selectedModel.price, selectedRecLoc.key);
		} catch (Exception e) {
			if (e.getMessage().equals("在庫不足"))
				console.error("在庫がなくなってしまったため、商品を購入できませんでした。");
			else if (e.getMessage().equals("残高不足"))
				console.error("残高が不足しているため、商品を購入できませんでした。");

			// 状態遷移: 戻る (購入へ)
			this.purchaseSection(selectedCategory, selectedModel);
			return;
		}

		// 購入完了のメッセージ
		console.next("購入が完了しました。\n残高: ￥" + this.user.getData().money
			+ "\n\t商品の製品番号: " + purchased.key.code
			+ "\n商品が指定の場所に届くまでしばらくお待ちください。");
	}

}
