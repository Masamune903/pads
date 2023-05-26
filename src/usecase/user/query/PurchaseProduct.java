package usecase.user.query;

import java.sql.*;

import mysql_executor.AbstractSQLUpdateExecutor;

public class PurchaseProduct extends AbstractSQLUpdateExecutor {
	private final int purchaser;
	private final int purchasedPrice;
	private final long purchasedTime;
	private final String receiptLocation;

	public PurchaseProduct(int purchaser, int purchasedPrice, long purchasedTime, String receiptLocation) {
		this.purchaser = purchaser;
		this.purchasedPrice = purchasedPrice;
		this.purchasedTime = purchasedTime;
		this.receiptLocation = receiptLocation;
	}

	@Override
	public String getSQLTemplate() {
		return "UPDATE product "
				+ "SET "
				+ "purchaser = ?, "
				+ "purchased_price = ?, "
				+ "purchased_time = ?, "
				+ "receipt_location = ?;";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setInt(1, this.purchaser);
		pstmt.setInt(2, this.purchasedPrice);
		pstmt.setTimestamp(3, new Timestamp(this.purchasedTime));
		pstmt.setString(4, this.receiptLocation);
	}

}
