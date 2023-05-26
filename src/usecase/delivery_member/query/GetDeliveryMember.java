package usecase.delivery_member.query;

import java.sql.*;
import mysql_executor.AbstractSQLQueryExecutor;
import usecase.delivery_member.DeliveryMemberData;

public class GetDeliveryMember extends AbstractSQLQueryExecutor<DeliveryMemberData> {
	private final String deliveryMember;

	public GetDeliveryMember(String code) {
		this.deliveryMember = code;
	}

	@Override
	public String getSQLTemplate() {
		return "SELECT * FROM delivery_member WHERE code = ?;";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, this.deliveryMember);
	}

	@Override
	public DeliveryMemberData getResult(ResultSet resSet) throws SQLException {
		if (!resSet.next())
			return null;
		
		return new DeliveryMemberData(
			resSet.getString("code"),
			resSet.getString("name")
		);
	}
}
