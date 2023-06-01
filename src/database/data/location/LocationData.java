/**
 * @author CY21249 TAKAGI Masamune
 */

package database.data.location;

import java.sql.*;

public class LocationData {
	public final LocationKey key;
	public final String address;
	public final double longitude;
	public final double latitude;

	protected LocationData(LocationKey key, String address, double longitude, double latitude) {
		this.key = key;
		this.address = address;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public static LocationData fromQueryResult(ResultSet resSet) throws SQLException {
		return new LocationData(
			new LocationKey(resSet.getString("location.name")),
			resSet.getString("location.address"),
			resSet.getDouble("location.longitude"),
			resSet.getDouble("location.latitude"));
	}
}
