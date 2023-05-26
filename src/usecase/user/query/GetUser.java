package usecase.user.query;

import java.sql.*;

import data.*;
import mysql_executor.AbstractSQLQueryExecutor;

public class GetUser extends AbstractSQLQueryExecutor<UserData> {
	private final int id;

	public GetUser(int id) {
		this.id = id;
	}

	@Override
	public String getSQLTemplate() {
		return "SELECT * FROM user "
			+ "WHERE id = ?";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setInt(1, this.id);
	}

	@Override
	public UserData getResult(ResultSet resSet) throws SQLException {
		if (!resSet.next())
			return null;

		return new UserData(
			resSet.getInt("id"),
			resSet.getString("name"),
			resSet.getInt("money")
		);
	}
	
}
