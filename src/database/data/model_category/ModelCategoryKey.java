/**
 * @author CY21249 TAKAGI Masamune
 */

package database.data.model_category;

import java.sql.*;

public class ModelCategoryKey {
	public final String name;

	public ModelCategoryKey(String name) {
		this.name = name;
	}

	public static ModelCategoryKey fromQueryResult(ResultSet resSet) throws SQLException {
		return new ModelCategoryKey(resSet.getString("model_category.name"));
	}
}
