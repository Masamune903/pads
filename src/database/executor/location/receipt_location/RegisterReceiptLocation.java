package database.executor.location.receipt_location;

import java.sql.*;

import database.executor.*;

import database.data.location.receipt_location.register_location_register.ReceiptLocationRegisterKey;

public class RegisterReceiptLocation extends AbstractSQLUpdateExecutor {
	private final ReceiptLocationRegisterKey key;

	public RegisterReceiptLocation(ReceiptLocationRegisterKey key) {
		this.key = key;
	}

	@Override
	public String getSQLTemplate() {
		return "INSERT receipt_location_register VALUES (?, ?);";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setInt(1, this.key.user.id);
		pstmt.setString(2, this.key.location.name);
	}
}
