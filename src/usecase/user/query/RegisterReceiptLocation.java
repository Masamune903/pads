package usecase.user.query;

import java.sql.*;

import mysql_executor.*;

public class RegisterReceiptLocation extends AbstractSQLUpdateExecutor {
	private final int user;
	private final String location;

	public RegisterReceiptLocation(int user, String location) {
		this.user = user;
		this.location = location;
	}

	@Override
	public String getSQLTemplate() {
		return "INSERT receipt_location_register VALUES (?, ?);";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setInt(1, this.user);
		pstmt.setString(2, this.location);
	}
}
