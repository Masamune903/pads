package database.data.location.trsp_hub.warehouse;

import java.sql.*;

import database.data.location.trsp_hub.TrspHubData;

public class WarehouseData extends TrspHubData {
	public final WarehouseKey key;

	protected WarehouseData(WarehouseKey key, String address, double longitude, double latitude) {
		super(key, address, longitude, latitude);
		this.key = key;
	}

	public static WarehouseData fromQueryResult(ResultSet resSet) throws SQLException {
		return new WarehouseData(
			WarehouseKey.fromQueryResult(resSet),
			resSet.getString("location.address"),
			resSet.getDouble("location.longitude"),
			resSet.getDouble("location.latitude"));
	}
}
