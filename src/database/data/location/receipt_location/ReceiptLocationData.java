/**
 * @author CY21249 TAKAGI Masamune
 */

package database.data.location.receipt_location;

import java.sql.*;

import database.data.location.LocationData;

public class ReceiptLocationData extends LocationData {
	public final ReceiptLocationKey key;

	protected ReceiptLocationData(ReceiptLocationKey key, String address, double longitude, double latitude) {
		super(key, address, longitude, latitude);
		this.key = key;
	}

	public static ReceiptLocationData fromQueryResult(ResultSet resSet) throws SQLException {
		return new ReceiptLocationData(
			ReceiptLocationKey.fromQueryResult(resSet),
			resSet.getString("location.address"),
			resSet.getDouble("location.longitude"),
			resSet.getDouble("location.latitude"));
	}
}
