/**
 * @author CY21249 TAKAGI Masamune
 */

package database.data.location.receipt_location;

import java.sql.*;
import database.data.location.LocationKey;

public class ReceiptLocationKey extends LocationKey {
	public ReceiptLocationKey(String name) {
		super(name);
	}

	public static ReceiptLocationKey fromQueryResult(ResultSet resSet) throws SQLException {
		return new ReceiptLocationKey(resSet.getString("location.name"));
	}
}
