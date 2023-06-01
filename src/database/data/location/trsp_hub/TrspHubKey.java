/**
 * @author CY21249 TAKAGI Masamune
 */

package database.data.location.trsp_hub;

import java.sql.*;

import database.data.location.LocationKey;

public class TrspHubKey extends LocationKey {
	public TrspHubKey(String name) {
		super(name);
	}

	public static TrspHubKey fromQueryResult(ResultSet resSet) throws SQLException {
		return new TrspHubKey(resSet.getString("location.name"));
	}
}
