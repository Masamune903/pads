package database.executor.location.receipt_location;

import java.sql.*;

import database.data.user.UserKey;

public class GetReceiptLocationListByUser extends GetReceiptLocationList {
	private final UserKey user;

	public GetReceiptLocationListByUser(UserKey user) {
		this.user = user;
	}

	@Override
	public String getSQLTemplate() {
		return "SELECT * "
			+ "FROM user, receipt_location, receipt_location_register, location "
			+ "WHERE user.id = receipt_location_register.user "
			+ "AND receipt_location.name = receipt_location_register.location "
			+ "AND receipt_location.name = location.name "
			+ "AND user.id = ?";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setInt(1, this.user.id);
	}

}
