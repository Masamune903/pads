package database.data.location.trsp_hub.warehouse;

import java.sql.*;

import database.data.location.trsp_hub.TrspHubKey;

public class WarehouseKey extends TrspHubKey {
	public WarehouseKey(String name) {
		super(name);
	}

	public static WarehouseKey fromQueryResult(ResultSet resSet) throws SQLException {
		return new WarehouseKey(resSet.getString("location.name"));
	}
}
