/**
 * 「配送開始とその時刻を記録」
 * 
 * 指定した配送が始まったときに、その時刻を記録するSQLUpdate
 * 
 * ユースケース：「配送する」
 * 
 * @author CY21202 MIHARA Yutaro
 */

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
		/*
			UPDATE delivery
				SET start_time = ?
				WHERE product_code = ? AND product_model = ? AND from_location = ?;
		 */
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
