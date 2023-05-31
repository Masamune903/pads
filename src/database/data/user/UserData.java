package database.data.user;

import java.sql.*;

public class UserData {
	public final UserKey key;
	public final String name;
	public final int money;

	protected UserData(UserKey key, String name, int money) {
		this.key = key;
		this.name = name;
		this.money = money;
	}

	public static UserData fromQueryResult(ResultSet resSet) throws SQLException {
		return new UserData(
			UserKey.fromQueryResult(resSet),
			resSet.getString("user.name"),
			resSet.getInt("user.money"));
	}

}
