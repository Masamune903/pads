package usecase.user.query;

import java.sql.*;

import data.ModelDataWithProductCount;
import mysql_executor.*;

public class GetModelListByCategory extends AbstractGetListSQLQueryExecutor<ModelDataWithProductCount> {
    private final String category;

	public GetModelListByCategory(String category) {
        this.category = category;
    }

    @Override
    public String getSQLTemplate() {
        return "SELECT *, COUNT(*) "
            + "FROM model, product "
            + "WHERE model.category = ? "
            + "AND product.purchaser IS NULL "
            + "AND product.model = model.code "
            + "GROUP BY model.code;";
    }

    @Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, this.category);
    }

    @Override
    protected ModelDataWithProductCount createData(ResultSet resSet) throws SQLException {
        return new ModelDataWithProductCount(
            resSet.getString("code"),
            resSet.getString("name"),
            resSet.getString("category"),
            resSet.getInt("price"),
            resSet.getString("manufacturer"),
            resSet.getInt("COUNT(*)")
        );
    }

}