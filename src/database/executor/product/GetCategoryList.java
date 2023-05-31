package database.executor.product;

import java.sql.*;

import database.executor.*;

import database.data.model_category.ModelCategoryData;

public class GetCategoryList extends AbstractGetListSQLQueryExecutor<ModelCategoryData> {
	public String getSQLTemplate() {
		return "SELECT name"
			+ "	FROM model_category";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {};

	@Override
	protected ModelCategoryData createData(ResultSet resSet) throws SQLException {
		return ModelCategoryData.fromQueryResult(resSet);
	}
}
