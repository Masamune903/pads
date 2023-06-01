/**
 * @author CY21248 SASAHARA Hayato
 */

package database.executor.location.receipt_location;

import java.sql.*;

import database.executor.*;

import database.data.location.receipt_location.ReceiptLocationData;

public class GetReceiptLocationList extends AbstractGetListSQLQueryExecutor<ReceiptLocationData> {
	@Override
	protected ReceiptLocationData createData(ResultSet resSet) throws SQLException {
		return ReceiptLocationData.fromQueryResult(resSet);
	}

	@Override
	public String getSQLTemplate() {
		return "SELECT * "
			+ "	FROM receipt_location, location "
			+ "	WHERE receipt_location.name = location.name;";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {}

}
