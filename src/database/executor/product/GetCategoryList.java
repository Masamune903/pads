/**
 * 「カテゴリ一覧を取得」
 * 
 * カテゴリ一覧を取得するSQLQuery
 * 
 * ユースケース：「商品を購入する」
 * 
 * @author CY21248 SASAHARA Hayato
 */

package database.executor.product;

import java.sql.*;

import database.executor.*;

import database.data.model_category.ModelCategoryData;

public class GetCategoryList extends AbstractGetListSQLQueryExecutor<ModelCategoryData> {
	public String getSQLTemplate() {
		/* 
			SELECT name
				FROM model_category;
		 */
		return "SELECT name"
			+ "	FROM model_category";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {};

	@Override
	protected ModelCategoryData createData(ResultSet resSet) throws SQLException {
		return ModelCategoryData.fromQueryResult(resSet);
	}
}
