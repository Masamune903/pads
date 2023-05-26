package usecase.user.query;

import java.sql.*;

import data.ProductData;
import mysql_executor.AbstractSQLQueryExecutor;

public class GetAvailableProductByModel extends AbstractSQLQueryExecutor<ProductData> {
	private final String model;	

	public GetAvailableProductByModel(String model) {
		this.model = model;
	}

	@Override
	public String getSQLTemplate() {
		// return "SELECT * FROM product "
		// 	+ "WHERE model = ? "
		// 	+ "AND purchaser IS NULL;";

		return "SELECT * FROM product WHERE model = ? AND purchaser IS NULL;";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, this.model);
	}

	@Override
	public ProductData getResult(ResultSet resSet) throws SQLException {
		if (!resSet.next())	
			return null;
		
		return new ProductData(
			resSet.getString("code"),
			resSet.getString("model")
		);
	}
	
}
