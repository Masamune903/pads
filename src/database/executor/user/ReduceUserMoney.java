package database.executor.user;

import java.sql.*;

import database.data.user.UserKey;
import database.executor.*;

public class ReduceUserMoney extends AbstractSQLUpdateExecutor {
	private final UserKey key;
	private final int amount;

	public ReduceUserMoney(UserKey key, int amount) {
		this.key = key;
		this.amount = amount;
	}

	@Override
	public String getSQLTemplate() {
		return "UPDATE user SET money = money - ? WHERE id = ?";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setInt(1, this.amount);
		pstmt.setInt(2, this.key.id);
	}

}
