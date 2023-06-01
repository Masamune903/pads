/**
 * @author CY21248 SASAHARA Hayato
 */

package database.executor.product;

import java.sql.*;

import database.executor.*;

import database.data.model.ModelKey;
import database.data.product.ProductData;

public class GetAvailableProductByModel extends AbstractSQLQueryExecutor<ProductData> {
	private final ModelKey model;

	public GetAvailableProductByModel(ModelKey model) {
		this.model = model;
	}

	@Override
	public String getSQLTemplate() {
		return "SELECT * FROM product WHERE model = ? AND purchaser IS NULL;";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, this.model.code);
	}

	@Override
	public ProductData getResult(ResultSet resSet) throws SQLException {
		if (!resSet.next())
			return null;

		return ProductData.fromQueryResult(resSet);
	}

}
