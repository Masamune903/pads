/**
 * 「全ての運送拠点を取得する」
 * 
 * 全ての運送拠点の一覧を取得するSQLQuery
 * 
 * ユースケース：なし (配送指示者の運送拠点選択で表示)
 * 
 * @author CY21248 SASAHARA Hayato
 */

package database.executor.location.trsp_hub;

import java.sql.*;

import database.data.location.trsp_hub.*;
import database.executor.*;

public class GetTrspHubList extends AbstractGetListSQLQueryExecutor<TrspHubData> {

	@Override
	protected TrspHubData createData(ResultSet resSet) throws SQLException {
		return TrspHubData.fromQueryResult(resSet);
	}

	@Override
	public String getSQLTemplate() {
		return "SELECT * FROM trsp_hub, location"
			+ "	WHERE trsp_hub.name = location.name";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {}

}
