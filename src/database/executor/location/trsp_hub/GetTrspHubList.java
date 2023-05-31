package database.executor.location.trsp_hub;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.data.location.trsp_hub.TrspHubData;
import database.executor.AbstractGetListSQLQueryExecutor;

public class GetTrspHubList extends AbstractGetListSQLQueryExecutor<TrspHubData> {

	@Override
	protected TrspHubData createData(ResultSet resSet) throws SQLException {
		return TrspHubData.fromQueryResult(resSet);
	}

	@Override
	public String getSQLTemplate() {
		return "SELECT * FROM trsp_hub, location"
			+ "	WHERE trsp_hub.name = location.name";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {}

}
