/**
 * 「ユーザーの情報を取得する」
 * 
 * 指定したキーのユーザーの情報を取得するSQLQuery
 * 
 * ユースケース：「商品を購入する」「残高を追加する」
 * 
 * @author CY21265 MORIMOTO Yuri
 */

package database.executor.user;

import java.sql.*;

import database.executor.*;

import database.data.user.UserData;
import database.data.user.UserKey;

public class GetUser extends AbstractSQLQueryExecutor<UserData> {
	private final UserKey key;

	public GetUser(UserKey key) {
		this.key = key;
	}

	@Override
	public String getSQLTemplate() {
		/* 
			SELECT * FROM user
				WHERE id = ?;
		*/

		return "SELECT * FROM user"
			+ "	WHERE id = ?;";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setInt(1, this.key.id);
	}

	@Override
	public UserData getResult(ResultSet resSet) throws SQLException {
		if (!resSet.next())
			return null;

		return UserData.fromQueryResult(resSet);
	}

}
