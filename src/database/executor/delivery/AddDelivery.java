/**
 * 配送予定が立った際に、その記録を実行するSQLUpdate
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
    private final int order;

    public AddDelivery(DeliveryKey key, LocationKey toLocation, int order) {
        this.key = key;
        this.toLocation = toLocation;
        this.order = order;
    }

    public AddDelivery(ProductKey product, LocationKey fromLocation, LocationKey toLocation, int order) {
        this(new DeliveryKey(product, fromLocation), toLocation, order);
    }

    @Override
    public String getSQLTemplate() {
        return "INSERT delivery VALUES (?, ?, ?, ?, ?, NULL, NULL, NULL);";
    }

    @Override
    public void setQuery(PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, this.key.product.code);
        pstmt.setString(2, this.key.product.model.code);
        pstmt.setString(3, this.key.fromLocation.name);
        pstmt.setString(4, this.toLocation.name);
        pstmt.setInt(5, this.order);
    }

}
