package database.data.location.trsp_hub;

import java.sql.*;

import database.data.location.LocationData;

public class TrspHubData extends LocationData {
	public final TrspHubKey key;

	protected TrspHubData(TrspHubKey key, String address, double longitude, double latitude) {
		super(key, address, longitude, latitude);
		this.key = key;
	}

	public static TrspHubData fromQueryResult(ResultSet resSet) throws SQLException {
		return new TrspHubData(
			TrspHubKey.fromQueryResult(resSet),
			resSet.getString("location.address"),
			resSet.getDouble("location.longitude"),
			resSet.getDouble("location.latitude"));
	}
}
