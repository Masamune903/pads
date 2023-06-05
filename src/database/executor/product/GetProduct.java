/**
 * 「商品の情報を取得」
 * 
 * 指定したキーの商品の情報を取得するSQLQuery
 * 
 * ユースケース：「商品の場所を確認する」
 * 
 * @author CY21248 SASAHARA Hayato
 */

package database.executor.product;

import java.sql.*;

import database.executor.*;

import database.data.product.ProductDataWithModel;
import database.data.product.ProductKey;

public class GetProduct extends AbstractSQLQueryExecutor<ProductDataWithModel> {
	private final ProductKey key;

	public GetProduct(ProductKey key) {
		this.key = key;
	}

	@Override
	public String getSQLTemplate() {
		/*
			SELECT * FROM product, model
				WHERE product.model = model.code
					AND product.code = ?
					AND product.model = ?;
		 */
		return "SELECT * FROM product, model"
			+ "	WHERE product.model = model.code"
			+ "		AND product.code = ?"
			+ "		AND product.model = ?;";
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
