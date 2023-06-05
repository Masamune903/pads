/**
 * 「全ての配達員を取得」
 * 
 * 全ての配達員の情報を取得するSQLQuery
 * 
 * ユースケース：なし(ログイン時に表示)
 * 
 * @author CY21202 MIHARA Yutaro
 */

package database.executor.delivery_member;

import java.sql.*;

import database.executor.*;

import database.data.delivery_member.DeliveryMemberData;

public class GetDeliveryMemberList extends AbstractGetListSQLQueryExecutor<DeliveryMemberData> {
	@Override
	public String getSQLTemplate() {
		return "SELECT * FROM delivery_member;";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {}

	@Override
	protected DeliveryMemberData createData(ResultSet resSet) throws SQLException {
		return DeliveryMemberData.fromQueryResult(resSet);
	}
}
