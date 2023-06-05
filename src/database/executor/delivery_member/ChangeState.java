/**
 * 「配達員の状態を更新」
 * 
 * 指定した配達員の状態を更新するSQLUpdate
 * 
 * ユースケース：「配送する」「所属する拠点に帰る」
 * 
 * @author CY21202 MIHARA Yutaro
 */

package database.executor.delivery_member;

import java.sql.*;

import database.executor.*;

import database.data.delivery_member.DeliveryMemberKey;

public class ChangeState extends AbstractSQLUpdateExecutor {
	private final DeliveryMemberKey key;
	private final String state; // null | "wating" | "delivering" | "not_here"

	public ChangeState(DeliveryMemberKey deliveryMember, String state) {
		ChangeState.validateState(state);

		this.key = deliveryMember;
		this.state = state;
	}

	@Override
	public String getSQLTemplate() {
		/* UPDATE delivery_member SET state = ? WHERE code = ?; */
		return "UPDATE delivery_member SET state = ? WHERE code = ?;";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, this.state);
		pstmt.setString(2, this.key.code);
	}

	public static void validateState(String state) {
		if (state != null) {
			switch (state) {
				case "wating":
				case "delivering":
				case "not_here":
					break;
				default:
					throw new RuntimeException(state + "is invalid");
			}
		}
	}
}
