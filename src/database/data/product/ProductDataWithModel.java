package database.data.product;

import java.sql.*;

import database.data.model.ModelData;

public class ProductDataWithModel extends ProductData {
	public final ModelData modelData;

	protected ProductDataWithModel(ProductKey key, String warehouse, String purchaser, String purchasedPriceStr,
		Timestamp purchasedTime, String receiptLocation, ModelData modelData) {
		super(key, warehouse, purchaser, purchasedPriceStr, purchasedTime, receiptLocation);

		this.modelData = modelData;
	}

	public static ProductDataWithModel fromQueryResult(ResultSet resSet) throws SQLException {
		return new ProductDataWithModel(
			ProductKey.fromQueryResult(resSet),
			resSet.getString("product.warehouse"),
			resSet.getString("product.purchaser"),
			resSet.getString("product.purchased_price"),
			resSet.getTimestamp("product.purchased_time"),
			resSet.getString("product.receipt_location"),
			ModelData.fromQueryResult(resSet));
	}

}
