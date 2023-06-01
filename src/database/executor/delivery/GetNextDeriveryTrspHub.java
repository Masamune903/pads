/**
 * @author CY21249 TAKAGI Masamune
 */

package database.executor.delivery;

import java.sql.*;

import database.executor.*;
import database.data.location.LocationKey;
import database.data.location.trsp_hub.TrspHubData;

public class GetNextDeriveryTrspHub extends AbstractSQLQueryExecutor<TrspHubData> {
	private final LocationKey currentLocation;
	private final LocationKey targetLocation;

	public GetNextDeriveryTrspHub(LocationKey currLocation, LocationKey targetLocation) {
		this.currentLocation = currLocation;
		this.targetLocation = targetLocation;
	}

	@Override
	public TrspHubData getResult(ResultSet resSet) throws SQLException {
		if (!resSet.next())
			return null;

		return TrspHubData.fromQueryResult(resSet);
	}

	@Override
	public String getSQLTemplate() {
		// 今の拠点より目的地に近い拠点のうち、今の拠点に一番近い拠点
		/*
			SELECT *, 
					POW(current.longitude - location.longitude, 2) + POW(current.latitude - location.latitude, 2) AS distance_curr,
					POW(target.longitude - location.longitude, 2) + POW(target.latitude - location.latitude, 2) AS distance_target
				FROM location AS current, location AS target, location
				WHERE current.name = '福岡倉庫'
					AND target.name = 'ファミリーマート札幌店'
					AND location.name != current.name
					AND location.name IN (SELECT name FROM trsp_hub);
					AND 
						POW(target.longitude - location.longitude, 2) + POW(target.latitude - location.latitude, 2)
							<
						POW(target.longitude - current.longitude, 2) + POW(target.latitude - current.latitude, 2)
				ORDER BY distance_curr;
		*/
		return "SELECT *, "
			+ "		POW(current.longitude - location.longitude, 2) + POW(current.latitude - location.latitude, 2) AS distance_curr,"
			+ "		POW(target.longitude - location.longitude, 2) + POW(target.latitude - location.latitude, 2) AS distance_target"
			+ "	FROM trsp_hub, location AS current, location AS target, location"
			+ "	WHERE location.name = trsp_hub.name"
			+ "		AND current.name = ?"
			+ "		AND target.name = ?"
			+ "		AND location.name != current.name"
			+ "		AND location.name NOT IN (SELECT name FROM warehouse)"
			+ "		AND "
			+ "			POW(target.longitude - location.longitude, 2) + POW(target.latitude - location.latitude, 2)"
			+ "				<"
			+ "			POW(target.longitude - current.longitude, 2) + POW(target.latitude - current.latitude, 2)"
			+ "	ORDER BY distance_curr;";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, this.currentLocation.name);
		pstmt.setString(2, this.targetLocation.name);
	}

}
