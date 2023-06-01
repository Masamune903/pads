/**
 * @author CY21202 MIHARA Yutaro
 */

package database.executor.delivery;

import java.sql.*;

import database.executor.*;
import database.data.delivery.*;
import database.data.delivery_member.*;

public class GetDeliveryInstruction extends AbstractSQLQueryExecutor<DeliveryInstruction> {
	private final DeliveryMemberKey key;

	public GetDeliveryInstruction(DeliveryMemberKey key) {
		this.key = key;
	}

	public String getSQLTemplate() {
		// 自分がすべき配達は、配達が終わっていない( end_time が null )もの
		return "SELECT *\n"
			+ "	FROM delivery\n"
			+ "	WHERE delivery_member = ?\n"
			+ "		AND end_time IS NULL";
	}

	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, this.key.code);
	}

	public DeliveryInstruction getResult(ResultSet resSet) throws SQLException {
		if (!resSet.next())
			return null;

		return DeliveryInstruction.from(DeliveryData.fromQueryResult(resSet));
	}
}
