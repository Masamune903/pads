/**
 * @author CY21249 TAKAGI Masamune
 */

package database.data.user;

import java.sql.*;

public class UserKey {
	public final int id;

	public UserKey(int id) {
		this.id = id;
	}

	public static UserKey fromQueryResult(ResultSet resSet) throws SQLException {
		return new UserKey(resSet.getInt("user.id"));
	}
}
