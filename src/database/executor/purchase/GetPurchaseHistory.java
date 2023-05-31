package database.executor.purchase;

import java.sql.*;

import database.executor.*;

import database.data.product.ProductDataWithModel;
import database.data.user.UserKey;

public class GetPurchaseHistory extends AbstractGetListSQLQueryExecutor<ProductDataWithModel> {
	private final UserKey user;

	public GetPurchaseHistory(UserKey user) {
		this.user = user;
	}

	@Override
	protected ProductDataWithModel createData(ResultSet resSet) throws SQLException {
		return ProductDataWithModel.fromQueryResult(resSet);
	}

	@Override
	public String getSQLTemplate() {
		return "SELECT * FROM product, model WHERE product.purchaser = ? AND product.model = model.code;";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setInt(1, this.user.id);
	}

}
