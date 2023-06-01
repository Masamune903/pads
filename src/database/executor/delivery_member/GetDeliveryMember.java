/**
 * @author CY21202 MIHARA Yutaro
 */

package database.executor.delivery_member;

import java.sql.*;

import database.executor.*;

import database.data.delivery_member.DeliveryMemberData;
import database.data.delivery_member.DeliveryMemberKey;

public class GetDeliveryMember extends AbstractSQLQueryExecutor<DeliveryMemberData> {
	private final DeliveryMemberKey key;

	public GetDeliveryMember(DeliveryMemberKey key) {
		this.key = key;
	}

	@Override
	public String getSQLTemplate() {
		return "SELECT * FROM delivery_member WHERE code = ?;";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, this.key.code);
	}

	@Override
	public DeliveryMemberData getResult(ResultSet resSet) throws SQLException {
		if (!resSet.next())
			return null;

		return DeliveryMemberData.fromQueryResult(resSet);
	}
}
