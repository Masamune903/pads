package database.data.model;

import java.sql.*;

public class ModelData {
	public final ModelKey key;
	public final String name;
	public final String category;
	public final int price;
	public final String manufacturer;

	protected ModelData(ModelKey key, String name, String category, int price, String manufacturer) {
		this.key = key;
		this.name = name;
		this.category = category;
		this.price = price;
		this.manufacturer = manufacturer;
	}

	public static ModelData fromQueryResult(ResultSet resSet) throws SQLException {
		return new ModelData(
			ModelKey.fromQueryResult(resSet),
			resSet.getString("model.name"),
			resSet.getString("model.category"),
			resSet.getInt("model.price"),
			resSet.getString("model.manufacturer"));
	}

}
