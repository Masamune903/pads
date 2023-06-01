/**
 * @author CY21249 TAKAGI Masamune
 */

package database.data.delivery_member;

import java.sql.*;

public class DeliveryMemberData {
	public final DeliveryMemberKey key;
	public final String name;

	protected DeliveryMemberData(DeliveryMemberKey key, String name) {
		this.key = key;
		this.name = name;
	}

	public static DeliveryMemberData fromQueryResult(ResultSet resSet) throws SQLException {
		return new DeliveryMemberData(
			DeliveryMemberKey.fromQueryResult(resSet),
			resSet.getString("name"));
	}
}
