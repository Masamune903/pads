/**
 * 「指定した配送員の配送指示を取得」
 * 
 * 指定した配達員について、自分に指示された配送指示のうちまだ完了していないものを取得するSQLQuery
 * 
 * ユースケース：「運送指示を確認する」
 * 
 * @author CY21202 MIHARA Yutaro
 */

package database.executor.delivery;

import java.sql.*;

import database.executor.*;
import database.data.delivery.*;
import database.data.delivery_member.*;

public class GetDeliveryInstruction extends AbstractSQLQueryExecutor<DeliveryInstruction> {
	private final DeliveryMemberKey key;

	public GetDeliveryInstruction(DeliveryMemberKey key) {
		this.key = key;
	}

	public String getSQLTemplate() {
		// 自分がすべき配達は、配達が終わっていない( end_time が null )もの

		/*
			SELECT *
				FROM delivery
				WHERE delivery_member = ?
					AND end_time IS NULL;
		 */
		return "SELECT *"
			+ "	FROM delivery"
			+ "	WHERE delivery_member = ?"
			+ "		AND end_time IS NULL";
	}

	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, this.key.code);
	}

	public DeliveryInstruction getResult(ResultSet resSet) throws SQLException {
		if (!resSet.next())
			return null;

		return DeliveryInstruction.from(DeliveryData.fromQueryResult(resSet));
	}
}
