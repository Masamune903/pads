/**
 * 「運送予定を追加」
 * 
 * 配送予定が立った際に、その記録を実行するSQLUpdate
 * 
 * ユースケース：「配送ルートを決める」
 * 
 * @author CY21202 MIHARA Yutaro
 */

package database.executor.delivery;

import java.sql.*;

import database.executor.*;

import database.data.delivery.*;
import database.data.location.*;
import database.data.product.*;

public class AddDelivery extends AbstractSQLUpdateExecutor {
	private final DeliveryKey key;
	private final LocationKey toLocation;

	public AddDelivery(DeliveryKey key, LocationKey toLocation) {
		this.key = key;
		this.toLocation = toLocation;
	}

	public AddDelivery(ProductKey product, LocationKey fromLocation, LocationKey toLocation) {
		this(new DeliveryKey(product, fromLocation), toLocation);
	}

	@Override
	public String getSQLTemplate() {
		return "INSERT delivery VALUES (?, ?, ?, ?, NULL, NULL, NULL);";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, this.key.product.code);
		pstmt.setString(2, this.key.product.model.code);
		pstmt.setString(3, this.key.fromLocation.name);
		pstmt.setString(4, this.toLocation.name);
	}

}
