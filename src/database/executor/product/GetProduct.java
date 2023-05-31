package database.executor.product;

import java.sql.*;

import database.executor.*;

import database.data.product.ProductData;
import database.data.product.ProductKey;

public class GetProduct extends AbstractSQLQueryExecutor<ProductData> {
	private final ProductKey key;

	public GetProduct(ProductKey key) {
		this.key = key;
	}

	@Override
	public String getSQLTemplate() {
		return "SELECT * FROM prodcut"
			+ "	WHERE code = ? AND model = ?";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, this.key.code);
		pstmt.setString(2, this.key.model.code);
	}

	@Override
	public ProductData getResult(ResultSet resSet) throws SQLException {
		if (!resSet.next())
			return null;

		return ProductData.fromQueryResult(resSet);
	}
}
