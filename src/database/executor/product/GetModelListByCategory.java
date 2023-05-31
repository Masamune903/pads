package database.executor.product;

import java.sql.*;

import database.executor.*;

import database.data.model.ModelDataWithProductCount;
import database.data.model_category.ModelCategoryKey;

public class GetModelListByCategory extends AbstractGetListSQLQueryExecutor<ModelDataWithProductCount> {
    private final ModelCategoryKey category;

	public GetModelListByCategory(ModelCategoryKey category) {
        this.category = category;
    }

    @Override
    public String getSQLTemplate() {
        return "SELECT *, COUNT(*)"
            + " FROM model, product"
            + " WHERE model.category = ?"
            + " 	AND product.purchaser IS NULL"
            + " 	AND product.model = model.code"
            + " GROUP BY model.code;";
    }

    @Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, this.category.name);
    }

    @Override
    protected ModelDataWithProductCount createData(ResultSet resSet) throws SQLException {
        return ModelDataWithProductCount.fromQueryResult(resSet);
    }

}