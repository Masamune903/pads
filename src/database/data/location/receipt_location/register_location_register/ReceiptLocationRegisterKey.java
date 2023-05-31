package database.data.location.receipt_location.register_location_register;

import java.sql.*;

import database.data.location.LocationKey;
import database.data.user.UserKey;

public class ReceiptLocationRegisterKey {
	public final UserKey user;
	public final LocationKey location;

	public ReceiptLocationRegisterKey(UserKey user, LocationKey location) {
		this.user = user;
		this.location = location;
	}

	public ReceiptLocationRegisterKey(int user, String location) {
		this(new UserKey(user), new LocationKey(location));
	}

	public static ReceiptLocationRegisterKey fromQueryResult(ResultSet resSet) throws SQLException {
		return new ReceiptLocationRegisterKey(
			resSet.getInt("receipt_location_register.user"),
			resSet.getString("receipt_location_register.location"));
	}
}
