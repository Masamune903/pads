package database.data.delivery;

import java.sql.*;

import database.data.location.LocationKey;
import database.data.product.ProductKey;

public class DeliveryKey {
	public final ProductKey product;
	public final LocationKey fromLocation;

	public DeliveryKey(ProductKey product, LocationKey fromLocation) {
		this.product = product;
		this.fromLocation = fromLocation;
	}

	public DeliveryKey(String productCode, String productModel, String fromLocation) {
		this(
			new ProductKey(productCode, productModel),
			new LocationKey(fromLocation));
	}

	public static DeliveryKey fromQueryResult(ResultSet resSet) throws SQLException {
		return new DeliveryKey(
			resSet.getString("product_code"),
			resSet.getString("product_model"),
			resSet.getString("from_location"));
	}
}
