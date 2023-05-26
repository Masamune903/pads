package usecase.user.query;

import java.sql.*;

import data.*;
import mysql_executor.*;

public class GetReceiptLocation extends AbstractGetListSQLQueryExecutor<ReceiptLocationData> {
	@Override
	protected ReceiptLocationData createData(ResultSet resSet) throws SQLException {
		return new ReceiptLocationData(
			resSet.getString("location.name"), 
			resSet.getString("location.address")
		);
	}

	@Override
	public String getSQLTemplate() {
		return "SELECT * "
			+ "FROM receipt_location, location "
			+ "WHERE receipt_location.name = location.name;";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException { }
	
}
