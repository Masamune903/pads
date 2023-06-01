/**
 * @author CY21248 SASAHARA Hayato
 */

package database.executor.product;

import java.sql.*;

import database.executor.*;

import database.data.model.ModelData;
import database.data.model.ModelKey;

public class GetModel extends AbstractSQLQueryExecutor<ModelData> {
	private final ModelKey key;

	public GetModel(ModelKey key) {
		this.key = key;
	}

	@Override
	public String getSQLTemplate() {
		return "SELECT * FROM model"
			+ "	WHERE code = ?";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, this.key.code);
	}

	@Override
	public ModelData getResult(ResultSet resSet) throws SQLException {
		if (!resSet.next())
			return null;

		return ModelData.fromQueryResult(resSet);
	}
}
