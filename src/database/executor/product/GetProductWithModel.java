/**
 * @author CY21248 SASAHARA Hayato
 */

package database.executor.product;

import java.sql.*;

import database.executor.*;

import database.data.product.ProductDataWithModel;
import database.data.product.ProductKey;

public class GetProductWithModel extends AbstractSQLQueryExecutor<ProductDataWithModel> {
	private final ProductKey key;

	public GetProductWithModel(ProductKey key) {
		this.key = key;
	}

	@Override
	public String getSQLTemplate() {
		return "SELECT * FROM product, model"
			+ "	WHERE product.code = ? AND product.model = ? AND product.model = model.code";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, this.key.code);
		pstmt.setString(2, this.key.model.code);
	}

	@Override
	public ProductDataWithModel getResult(ResultSet resSet) throws SQLException {
		if (!resSet.next())
			return null;

		return ProductDataWithModel.fromQueryResult(resSet);
	}
}
