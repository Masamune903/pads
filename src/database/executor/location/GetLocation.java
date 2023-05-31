package database.executor.location;

import java.sql.*;

import database.executor.*;

import database.data.location.LocationData;
import database.data.location.LocationKey;

public class GetLocation extends AbstractSQLQueryExecutor<LocationData> {
	private final LocationKey key;

	public GetLocation(LocationKey key) {
		this.key = key;
	}

	@Override
	public String getSQLTemplate() {
		return "SELECT * FROM user "
			+ "WHERE id = ?";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, this.key.name);
	}

	@Override
	public LocationData getResult(ResultSet resSet) throws SQLException {
		if (!resSet.next())
			return null;

		return LocationData.fromQueryResult(resSet);
	}
}

