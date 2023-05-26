package usecase.user.query;

import mysql_executor.*;
import java.sql.*;

import data.CategoryData;

public class GetCategoryList extends AbstractGetListSQLQueryExecutor<CategoryData> {
	public String getSQLTemplate() {
		return "SELECT name\n"
				+ "FROM model_category";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {};

	@Override
	protected CategoryData createData(ResultSet resSet) throws SQLException {
		return new CategoryData(
			resSet.getString("name")
		);
	}
}
