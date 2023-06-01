/**
 * @author CY21202 MIHARA Yutaro
 */

package database.executor.delivery;

import java.sql.*;

import database.data.delivery.*;
import database.executor.*;

public class GetDelivery extends AbstractSQLQueryExecutor<DeliveryData> {
	private final DeliveryKey key;

	public GetDelivery(DeliveryKey key) {
		this.key = key;
	}

	@Override
	public String getSQLTemplate() {
		return "SELECT * FROM delivery WHERE product_code = ? AND product_model = ? AND from_location = ?;";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, this.key.product.code);
		pstmt.setString(2, this.key.product.model.code);
		pstmt.setString(3, this.key.fromLocation.name);
	}

	@Override
	public DeliveryData getResult(ResultSet resSet) throws SQLException {
		if (!resSet.next())
			return null;

		return DeliveryData.fromQueryResult(resSet);
	}
}
