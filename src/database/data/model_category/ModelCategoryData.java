package database.data.model_category;

import java.sql.*;

public class ModelCategoryData {
	public final ModelCategoryKey key;

	protected ModelCategoryData(ModelCategoryKey key) {
		this.key = key;
	}

	public static ModelCategoryData fromQueryResult(ResultSet resSet) throws SQLException {
		return new ModelCategoryData(ModelCategoryKey.fromQueryResult(resSet));
	}
}
