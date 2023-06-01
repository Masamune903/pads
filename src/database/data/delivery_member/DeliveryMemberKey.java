/**
 * @author CY21249 TAKAGI Masamune
 */

package database.data.delivery_member;

import java.sql.*;

public class DeliveryMemberKey {
	public final String code;

	public DeliveryMemberKey(String code) {
		this.code = code;
	}

	public static DeliveryMemberKey fromQueryResult(ResultSet resSet) throws SQLException {
		return new DeliveryMemberKey(resSet.getString("code"));
	}
}
