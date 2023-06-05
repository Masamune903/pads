/**
 * 「商品情報の商品のうち、購入可能な商品を取得」
 * 
 * 指定した商品情報の商品のうち、購入者が未指定の商品を取得するSQLQuery
 * 
 * ユースケース：「商品を購入する」
 * 
 * @author CY21248 SASAHARA Hayato
 */

package database.executor.product;

import java.sql.*;

import database.executor.*;

import database.data.model.ModelKey;
import database.data.product.ProductDataWithModel;

public class GetAvailableProductByModel extends AbstractSQLQueryExecutor<ProductDataWithModel> {
	private final ModelKey model;

	public GetAvailableProductByModel(ModelKey model) {
		this.model = model;
	}

	@Override
	public String getSQLTemplate() {
		/*
			SELECT * FROM product, model
				WHERE model = ?
					AND product.model = model.code
					AND product.purchaser IS NULL;
		*/
		return "SELECT * FROM product, model"
			+ "	WHERE model = ?"
			+ "		AND product.model = model.code"
			+ "		AND product.purchaser IS NULL;";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, this.model.code);
	}

	@Override
	public ProductDataWithModel getResult(ResultSet resSet) throws SQLException {
		if (!resSet.next())
			return null;

		return ProductDataWithModel.fromQueryResult(resSet);
	}

}
