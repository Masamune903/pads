/**
 * 「運送拠点に所属する配達員一覧を取得」
 * 
 * 指定した運送拠点に所属する配達員の一覧を取得するSQLQuery
 * 
 * ユースケース：「配送指示を出す」
 * 
 * @author CY21202 MIHARA Yutaro
 */

package database.executor.delivery_member;

import java.sql.*;
import database.data.delivery_member.DeliveryMemberData;
import database.data.location.trsp_hub.TrspHubKey;
import database.executor.AbstractGetListSQLQueryExecutor;

public class GetDeliveryMemberListByBelongs extends AbstractGetListSQLQueryExecutor<DeliveryMemberData> {
	private final TrspHubKey trspHub;

	public GetDeliveryMemberListByBelongs(TrspHubKey trspHub) {
		this.trspHub = trspHub;
	}

	@Override
	public String getSQLTemplate() {
		return "SELECT * FROM delivery_member WHERE belongs_to = ?;";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, this.trspHub.name);
	}

	@Override
	protected DeliveryMemberData createData(ResultSet resSet) throws SQLException {
		return DeliveryMemberData.fromQueryResult(resSet);
	}
}
