/**
 * 「ユーザーの購入した商品一覧を取得」
 * 
 * 指定したユーザーが購入した商品の一覧を取得するSQLQuery
 * 
 * ユースケース：「注文の履歴を見る」
 * 
 * @author CY21248 SASAHARA Hayato
 */

package database.executor.product;

import java.sql.*;

import database.executor.*;

import database.data.product.ProductDataWithModel;
import database.data.user.UserKey;

public class GetProductListByUser extends AbstractGetListSQLQueryExecutor<ProductDataWithModel> {
	private final UserKey user;

	public GetProductListByUser(UserKey user) {
		this.user = user;
	}

	@Override
	public String getSQLTemplate() {
		/*
			SELECT *
				FROM product, model
				WHERE product.model = model.code
					AND purchaser = ?;
		 */
		return "SELECT *"
			+ "	FROM product, model"
			+ "	WHERE product.model = model.code"
			+ "		AND purchaser = ?;";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setInt(1, this.user.id);
	}

	@Override
	protected ProductDataWithModel createData(ResultSet resSet) throws SQLException {
		return ProductDataWithModel.fromQueryResult(resSet);
	}

}
