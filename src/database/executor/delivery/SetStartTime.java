package database.executor.delivery;

import java.sql.*;

import database.executor.*;

import database.data.delivery.DeliveryKey;

/** 配送が始まったとき */
public class SetStartTime extends AbstractSQLUpdateExecutor {
	private final DeliveryKey key;
	private final long startTime;

	public SetStartTime(DeliveryKey key, long startTime) {
		this.key = key;
		this.startTime = startTime;
	}


	@Override
	public String getSQLTemplate() {
		return "UPDATE delivery"
			+ "	SET start_time = ?"
			+ "	WHERE product_code = ? AND product_model = ? AND from_location = ?;";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setTimestamp(1, new Timestamp(this.startTime));
		pstmt.setString(2, this.key.product.code);
		pstmt.setString(3, this.key.product.model.code);
		pstmt.setString(4, this.key.fromLocation.name);
	}
}
