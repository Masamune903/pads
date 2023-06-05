/**
 * 「ユーザーの受け取り場所一覧を取得」
 * 
 * 指定したユーザーが登録した受け取り場所の一覧を取得するSQLQuery
 * 
 * ユースケース：「商品を購入する」
 * 
 * @author CY21248 SASAHARA Hayato
 */

package database.executor.location.receipt_location;

import java.sql.*;
import database.data.location.receipt_location.ReceiptLocationData;
import database.data.user.UserKey;
import database.executor.AbstractGetListSQLQueryExecutor;

public class GetReceiptLocationListByUser extends AbstractGetListSQLQueryExecutor<ReceiptLocationData> {
	private final UserKey user;

	public GetReceiptLocationListByUser(UserKey user) {
		this.user = user;
	}

	@Override
	public String getSQLTemplate() {
		/*
			SELECT * 
				FROM user, receipt_location, receipt_location_register, location 
				WHERE user.id = receipt_location_register.user 
					AND receipt_location.name = receipt_location_register.location 
					AND receipt_location.name = location.name 
					AND user.id = ?
		 */
		return "SELECT * "
			+ "	FROM user, receipt_location, receipt_location_register, location "
			+ "	WHERE user.id = receipt_location_register.user "
			+ "		AND receipt_location.name = receipt_location_register.location "
			+ "		AND receipt_location.name = location.name "
			+ "		AND user.id = ?";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setInt(1, this.user.id);
	}

	@Override
	protected ReceiptLocationData createData(ResultSet resSet) throws SQLException {
		return ReceiptLocationData.fromQueryResult(resSet);
	}

}
