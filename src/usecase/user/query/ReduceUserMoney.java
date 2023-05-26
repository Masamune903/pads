package usecase.user.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import mysql_executor.AbstractSQLUpdateExecutor;

public class ReduceUserMoney extends AbstractSQLUpdateExecutor {
	private final int id;
	private final int amount;

	public ReduceUserMoney(int id, int amount) {
		this.id = id;
		this.amount = amount;
	}

	@Override
	public String getSQLTemplate() {
		return "UPDATE user SET money = money - ? WHERE id = ?";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setInt(1, this.amount);
		pstmt.setInt(2, this.id);
	}

}
