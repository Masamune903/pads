/**
 * @author CY21248 SASAHARA Hayato
 */

package database.executor.product;

import java.sql.*;

import database.executor.*;

import database.data.product.ProductData;
import database.data.user.UserKey;

public class GetProductListByUser extends AbstractGetListSQLQueryExecutor<ProductData> {
    private final UserKey user;

    public GetProductListByUser(UserKey user) {
        this.user = user;
    }

    @Override
    public String getSQLTemplate() {
        return "SELECT *"
            + " FROM product"
            + " WHERE purchaser = ?;";
    }

    @Override
    public void setQuery(PreparedStatement pstmt) throws SQLException {
        pstmt.setInt(1, this.user.id);
    }

    @Override
    protected ProductData createData(ResultSet resSet) throws SQLException {
        return ProductData.fromQueryResult(resSet);
    }

}
