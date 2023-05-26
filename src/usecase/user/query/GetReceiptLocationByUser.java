package usecase.user.query;

import java.sql.*;

import data.*;

public class GetReceiptLocationByUser extends GetReceiptLocation {
	private final int user;

	public GetReceiptLocationByUser(int user) {
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
		pstmt.setInt(1, this.user);
	}
	
}
