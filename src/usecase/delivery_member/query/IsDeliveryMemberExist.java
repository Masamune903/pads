package usecase.delivery_member.query;

import java.sql.*;

import mysql_executor.AbstractSQLQueryExecutor;

public class IsDeliveryMemberExist extends AbstractSQLQueryExecutor<Boolean> {
	private final String code;	

	public IsDeliveryMemberExist(String code) {
		this.code = code;
	}

	@Override
	public String getSQLTemplate() {
		return "SELECT * FROM delivery_member WHERE code = ?;";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, this.code);
	}

	@Override
	public Boolean getResult(ResultSet resSet) throws SQLException {
		return resSet.next();
	}
}
