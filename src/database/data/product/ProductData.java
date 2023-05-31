package database.data.product;

import java.sql.*;

import database.data.location.LocationKey;
import database.data.location.trsp_hub.warehouse.WarehouseKey;
import database.data.user.UserKey;

public class ProductData {
	public final ProductKey key;
	public final WarehouseKey warehouse;
	public final UserKey purchaser; // nullable
	public final Integer purchasedPrice; // nullable
	public final Timestamp purchasedTime; // nullable
	public final LocationKey receiptLocation; // nullable
	public Object code;

	protected ProductData(ProductKey key, WarehouseKey warehouse, UserKey purchaser, Integer purchasedPrice, Timestamp purchasedTime, LocationKey receiptLocation) {
		this.key = key;
		this.warehouse = warehouse;
		this.purchaser = purchaser;
		this.purchasedPrice = purchasedPrice;
		this.purchasedTime = purchasedTime;
		this.receiptLocation = receiptLocation;
	}

	protected ProductData(ProductKey key, String warehouse, String purchaserStr, String purchasedPriceStr, Timestamp purchasedTime, String receiptLocationStr) {
		this(key,
			new WarehouseKey(warehouse),
			purchaserStr == null ? null : new UserKey(Integer.parseInt(purchaserStr)),
			purchasedPriceStr == null ? null : Integer.parseInt(purchasedPriceStr),
			purchasedTime,
			receiptLocationStr == null ? null : new LocationKey(receiptLocationStr));
	}

	public static ProductData fromQueryResult(ResultSet resSet) throws SQLException {
		return new ProductData(
			ProductKey.fromQueryResult(resSet),
			resSet.getString("product.warehouse"),
			resSet.getString("product.purchaser"),
			resSet.getString("product.purchased_price"),
			resSet.getTimestamp("product.purchased_time"),
			resSet.getString("product.receipt_location"));
	}
}
