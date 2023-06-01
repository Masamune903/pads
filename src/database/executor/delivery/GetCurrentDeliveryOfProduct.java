/**
 * @author CY21249 TAKAGI Masamune
 */

package database.executor.delivery;

import java.sql.*;
import database.data.delivery.DeliveryData;
import database.data.product.ProductKey;
import database.executor.AbstractSQLQueryExecutor;

public class GetCurrentDeliveryOfProduct extends AbstractSQLQueryExecutor<DeliveryData> {
	private final ProductKey product;

	public GetCurrentDeliveryOfProduct(ProductKey product) {
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
		return "SELECT *"
			+ "	FROM delivery"
			+ "	WHERE product_code = ?"
			+ "		AND product_model = ?"
			+ "		AND end_time IS NULL"
			+ "		AND ((from_location) IN ("
			+ "			SELECT prev.to_location"
			+ "				FROM delivery AS prev"
			+ "				/* 同じ商品の前の配達 (同じ商品を運んでおり、to_location が from_location と一致している) が完了済みである */"
			+ "				WHERE (prev.product_code, prev.product_model) IN ((delivery.product_code, delivery.product_model))"
			+ "					AND prev.to_location = delivery.from_location"
			+ "					AND prev.end_time IS NOT NULL"
			+ "			) OR (0) IN ("
			+ "				SELECT COUNT(*)"
			+ "					FROM delivery AS prev"
			+ "					/* 同じ商品の前の配達 (同じ商品を運んでおり、to_location が from_location と一致している) がない*/"
			+ "					WHERE (prev.product_code, prev.product_model) IN ((delivery.product_code, delivery.product_model))"
			+ "						AND prev.to_location = delivery.from_location"
			+ "			));";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, this.product.code);
		pstmt.setString(2, this.product.model.code);
	}

}
