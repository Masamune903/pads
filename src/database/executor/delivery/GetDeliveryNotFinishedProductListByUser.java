/**
 * 指定したユーザーが購入した商品のうち、配送が終わっていない商品を取得するSQLQuery
 * 
 * @author CY21202 MIHARA Yutaro
 */

package database.executor.delivery;

import java.sql.*;
import database.data.product.ProductKey;
import database.data.user.UserKey;
import database.executor.AbstractGetListSQLQueryExecutor;

public class GetDeliveryNotFinishedProductListByUser extends AbstractGetListSQLQueryExecutor<ProductKey> {
	private final UserKey user;

	public GetDeliveryNotFinishedProductListByUser(UserKey user) {
		this.user = user;
	}

	@Override
	protected ProductKey createData(ResultSet resSet) throws SQLException {
		return ProductKey.fromQueryResult(resSet);
	}

	@Override
	public String getSQLTemplate() {
		return "SELECT *"
			+ "	FROM delivery, product"
			+ "	WHERE delivery.product_code = product.code"
			+ "		AND delivery.product_model = product.model"
			+ "		AND product.purchaser = ?"
			+ "		AND end_time IS NULL"
			+ " GROUP BY product_code, product_model";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setInt(1, this.user.id);
	}

}
