/**
 * @author CY21249 TAKAGI Masamune
 */

package database.data.product;

import java.sql.*;
import database.data.model.ModelKey;

public class ProductKey {
	public final String code;
	public final ModelKey model;

	public ProductKey(String code, ModelKey model) {
		this.code = code;
		this.model = model;
	}

	public ProductKey(String code, String model) {
		this(code, new ModelKey(model));
	}

	public static ProductKey fromQueryResult(ResultSet resSet) throws SQLException {
		return new ProductKey(
			resSet.getString("product.code"),
			resSet.getString("product.model"));
	}
}
