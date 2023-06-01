/**
 * @author CY21248 SASAHARA Hayato
 * @editor CY21249 TAKAGI Masamune SQL文の訂正
 */

package database.executor.location.receipt_location;

import java.sql.*;

import database.executor.*;

import database.data.location.receipt_location.ReceiptLocationData;
import database.data.user.UserKey;

public class GetReceiptLocationCandidateList extends AbstractGetListSQLQueryExecutor<ReceiptLocationData> {
	private final UserKey user;

	public GetReceiptLocationCandidateList(UserKey user) {
		this.user = user;
	}

	@Override
	protected ReceiptLocationData createData(ResultSet resSet) throws SQLException {
		return ReceiptLocationData.fromQueryResult(resSet);
	}

	@Override
	public String getSQLTemplate() {
		return "SELECT * "
			+ "	FROM receipt_location, location"
			+ "	WHERE receipt_location.name = location.name"
			+ "		AND location.name NOT IN ("
			+ "			SELECT location"
			+ "				FROM receipt_location_register"
			+ "				WHERE user = ?"
			+ "			);";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setInt(1, this.user.id);
	}

}
