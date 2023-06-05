/**
 * 「配送完了とその時刻を記録」
 * 
 * 指定した配送が完了した時に、その時刻を記録するSQLUpdate
 * 
 * ユースケース：「配送する」
 * 
 * @author CY21202 MIHARA Yutaro
 */

package database.executor.delivery;

import java.sql.*;

import database.executor.*;

import database.data.delivery.DeliveryKey;

/** 配送が終わったとき */
public class SetEndTime extends AbstractSQLUpdateExecutor {
	private final DeliveryKey key;
	private final long endTime;

	public SetEndTime(DeliveryKey key, long endTime) {
		this.key = key;
		this.endTime = endTime;
	}

	@Override
	public String getSQLTemplate() {
		/*
			UPDATE delivery
				SET end_time = ?
				WHERE product_code = ? AND product_model = ? AND from_location = ?;
		 */
		return "UPDATE delivery"
			+ "	SET end_time = ?"
			+ "	WHERE product_code = ? AND product_model = ? AND from_location = ?;";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setTimestamp(1, new Timestamp(this.endTime));
		pstmt.setString(2, this.key.product.code);
		pstmt.setString(3, this.key.product.model.code);
		pstmt.setString(4, this.key.fromLocation.name);
	}
}
