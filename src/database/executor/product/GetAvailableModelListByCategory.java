/**
 * 「カテゴリにおける、購入可能な商品の取得」
 * 
 * 指定したカテゴリの商品情報のうち、その商品で購入者が未指定のものがある商品情報の一覧を取得するSQLQuery
 * 
 * ユースケース：「商品を購入する」
 * 
 * @author CY21248 SASAHARA Hayato
 */

package database.executor.product;

import java.sql.*;

import database.executor.*;

import database.data.model.ModelDataWithProductCount;
import database.data.model_category.ModelCategoryKey;

public class GetAvailableModelListByCategory extends AbstractGetListSQLQueryExecutor<ModelDataWithProductCount> {
	private final ModelCategoryKey category;

	public GetAvailableModelListByCategory(ModelCategoryKey category) {
		this.category = category;
	}

	@Override
	public String getSQLTemplate() {
		/*
			SELECT *, COUNT(*)
				FROM model, product
				WHERE model.category = ?
					AND product.purchaser IS NULL
					AND product.model = model.code
				GROUP BY model.code
					HAVING COUNT(*) > 0;
		 */
		return "SELECT *, COUNT(*)"
			+ " FROM model, product"
			+ " WHERE model.category = ?"
			+ "		AND product.purchaser IS NULL"
			+ " 	AND product.model = model.code"
			+ " GROUP BY model.code"
			+ "		HAVING COUNT(*) > 0;";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, this.category.name);
	}

	@Override
	protected ModelDataWithProductCount createData(ResultSet resSet) throws SQLException {
		return ModelDataWithProductCount.fromQueryResult(resSet);
	}

}
