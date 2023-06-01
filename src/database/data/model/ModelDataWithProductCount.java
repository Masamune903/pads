/**
 * @author CY21249 TAKAGI Masamune
 */

package database.data.model;

import java.sql.*;

public class ModelDataWithProductCount {
	public final ModelKey key;
	public final String name;
	public final String category;
	public final int price;
	public final String manufacturer;
	public final int productCount;

	protected ModelDataWithProductCount(ModelKey key, String name, String category, int price, String manufacturer, int productCount) {
		this.key = key;
		this.name = name;
		this.category = category;
		this.price = price;
		this.manufacturer = manufacturer;
		this.productCount = productCount;
	}

	public static ModelDataWithProductCount fromQueryResult(ResultSet resSet) throws SQLException {
		return new ModelDataWithProductCount(
			new ModelKey(resSet.getString("model.code")),
			resSet.getString("model.name"),
			resSet.getString("model.category"),
			resSet.getInt("model.price"),
			resSet.getString("model.manufacturer"),
			resSet.getInt("COUNT(*)"));
	}

}
