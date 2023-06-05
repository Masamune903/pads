/**
 * 「指定した商品を最後受け取り場所に配送する配送を取得」
 * 
 * 指定した商品の配送における、届け先が他のどの配達の運び元でもない配達を取得するSQLQuery
 * 
 * ユースケース：「商品の場所を確認する」
 * 
 * @author CY21249 TAKAGI Masamune
 */

package database.executor.delivery;

import java.sql.*;
import database.data.delivery.DeliveryData;
import database.data.product.ProductKey;
import database.executor.AbstractSQLQueryExecutor;

public class GetLastDeliveryOfProduct extends AbstractSQLQueryExecutor<DeliveryData> {
	private final ProductKey product;

	public GetLastDeliveryOfProduct(ProductKey product) {
		this.product = product;
	}

	@Override
	public DeliveryData getResult(ResultSet resSet) throws SQLException {
		if (!resSet.next())
			return null;

		return DeliveryData.fromQueryResult(resSet);
	}

	@Override
	public String getSQLTemplate() {
		/*
			SELECT *
				FROM delivery
				WHERE product_code = {{product_code}}
					AND product_model = {{product_model}}
					AND (to_location) NOT IN (
						-- to_location が、同じ商品を運んでいるいずれの delivery の from_location にも属さない
						SELECT from_location
							FROM delivery as next
							WHERE (next.product_code, next.product_model) IN ((delivery.product_code, delivery.product_model))
						);
		 */

		return "SELECT *"
			+ "	FROM delivery"
			+ "	WHERE product_code = ?"
			+ "		AND product_model = ?"
			+ "		AND (to_location) NOT IN ("
			// to_location が、同じ商品を運んでいるいずれの delivery の from_location にも属さない
			+ "			SELECT from_location"
			+ "				FROM delivery as next"
			+ "				WHERE (next.product_code, next.product_model) IN ((delivery.product_code, delivery.product_model))"
			+ "			);";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, this.product.code);
		pstmt.setString(2, this.product.model.code);
	}

}
