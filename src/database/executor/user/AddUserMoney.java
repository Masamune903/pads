/**
 * 「ユーザーの残高を追加する」
 * 
 * 指定したユーザーの残高に指定した金額を追加するSQLUpdate
 * 
 * ユースケース：「残高を追加する」
 * 
 * @author CY21265 MORIMOTO Yuri
 */

package database.executor.user;

import java.sql.*;

import database.executor.*;
import database.data.user.*;

public class AddUserMoney extends AbstractSQLUpdateExecutor {
	private final UserKey key;
	private final int amount;

	public AddUserMoney(UserKey key, int amount) {
		this.key = key;
		this.amount = amount;
	}

	@Override
	public String getSQLTemplate() {
		return "UPDATE user SET money = money + ? WHERE id = ?;";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setInt(1, this.amount);
		pstmt.setInt(2, this.key.id);
	}
}
