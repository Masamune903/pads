package database.data.model;

import java.sql.*;

public class ModelKey {
	public final String code;

	public ModelKey(String code) {
		this.code = code;
	}

	public static ModelKey fromQueryResult(ResultSet resSet) throws SQLException {
		return new ModelKey(resSet.getString("model.code"));
	}
}
