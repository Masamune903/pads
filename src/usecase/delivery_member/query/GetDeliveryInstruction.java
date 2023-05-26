package usecase.delivery_member.query;

import java.sql.*;

import mysql_executor.AbstractSQLQueryExecutor;
import usecase.delivery_member.DeliveryInstruction;

public class GetDeliveryInstruction extends AbstractSQLQueryExecutor<DeliveryInstruction> {
	private final String deliveryMember;

	public GetDeliveryInstruction(String deliveryMember) {
		this.deliveryMember = deliveryMember;
	}

	public String getSQLTemplate() {
		// 自分がすべき配達は、配達計画にあり、かつ未配達の物
		return "SELECT * "
				+ "FROM delivery_plan as plan "
				+ "WHERE plan.delivery_member = ? "
				+ "	AND (plan.product, plan.from_location) NOT IN ( "
				+ "		SELECT delivery.product, delivery.from_location "
				+ "		FROM delivery "
				+ "		WHERE plan.product = delivery.product "
				+ "			AND plan.from_location = delivery.from_location "
				+ "	);";
	}

	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, this.deliveryMember);
	}

	public DeliveryInstruction getResult(ResultSet resSet) throws SQLException {
		if (!resSet.next())
			return null;

		return new DeliveryInstruction(
			resSet.getString("product"), 
			resSet.getString("from_location"), 
			resSet.getString("to_location")
		);
	}
}
