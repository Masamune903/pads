package database.executor.purchase;

import java.sql.*;

import database.executor.*;

import database.data.location.receipt_location.ReceiptLocationKey;
import database.data.product.ProductKey;
import database.data.user.UserKey;

public class PurchaseProduct extends AbstractSQLUpdateExecutor {
	private final ProductKey key;
	private final UserKey purchaser;
	private final int purchasedPrice;
	private final long purchasedTime;
	private final ReceiptLocationKey receiptLocation;

	public PurchaseProduct(ProductKey key, UserKey purchaser, int purchasedPrice, long purchasedTime, ReceiptLocationKey receiptLocation) {
		this.key = key;
		this.purchaser = purchaser;
		this.purchasedPrice = purchasedPrice;
		this.purchasedTime = purchasedTime;
		this.receiptLocation = receiptLocation;
	}

	@Override
	public String getSQLTemplate() {
		return "UPDATE product"
			+ "	SET purchaser = ?,"
			+ "		purchased_price = ?,"
			+ "		purchased_time = ?,"
			+ "		receipt_location = ?"
			+ "	WHERE code = ?"
			+ "		AND model = ?;";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setInt(1, this.purchaser.id);
		pstmt.setInt(2, this.purchasedPrice);
		pstmt.setTimestamp(3, new Timestamp(this.purchasedTime));
		pstmt.setString(4, this.receiptLocation.name);
		pstmt.setString(5, this.key.code);
		pstmt.setString(6, this.key.model.code);
	}

}
