/**
 * 「指定したユーザーへの、まだ配達終わっていない商品一覧を取得」
 * 
 * 指定したユーザーが購入した商品のうち、配送が終わっていない商品を取得するSQLQuery
 * 
 * ユースケース：「商品の場所を確認する」
 * 
 * @author CY21202 MIHARA Yutaro
 */

package database.executor.delivery;

import java.sql.*;
import database.data.product.*;
import database.data.user.*;
import database.executor.*;

public class GetDeliveryNotFinishedProductListByUser extends AbstractGetListSQLQueryExecutor<ProductDataWithModel> {
	private final UserKey user;

	public GetDeliveryNotFinishedProductListByUser(UserKey user) {
		this.user = user;
	}

	@Override
	protected ProductDataWithModel createData(ResultSet resSet) throws SQLException {
		return ProductDataWithModel.fromQueryResult(resSet);
	}

	@Override
	public String getSQLTemplate() {
		/*
		 SELECT *
			FROM delivery, product, model
			WHERE delivery.product_code = product.code
				AND delivery.product_model = product.model
				AND product.model = model.code
				AND product.purchaser = ?
				AND end_time IS NULL
			GROUP BY product_code, product_model;
		 */
		return "SELECT *"
			+ "	FROM delivery, product, model"
			+ "	WHERE delivery.product_code = product.code"
			+ "		AND delivery.product_model = product.model"
			+ "		AND product.model = model.code"
			+ "		AND product.purchaser = ?"
			+ "		AND end_time IS NULL"
			+ "	GROUP BY product_code, product_model";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setInt(1, this.user.id);
	}

}
