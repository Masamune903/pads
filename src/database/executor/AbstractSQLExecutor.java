package database.executor;

import java.sql.*;

abstract class AbstractSQLExecutor<T> {
	public void preQuery() {}

	public abstract String getSQLTemplate();

	public abstract void setQuery(PreparedStatement pstmt) throws SQLException;

	protected abstract T queryOrUpdate(PreparedStatement pstmt) throws SQLException;

	protected T executeTemplate() {
		try {
			this.preQuery();

			Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost/pads?useSSL=false&characterEncoding=utf8&useServerPrepStmts=true",
				"root", "");

			PreparedStatement st = conn.prepareStatement(this.getSQLTemplate());

			this.setQuery(st);

			T res = this.queryOrUpdate(st);

			// 終了処理
			st.close();
			conn.close();

			return res;
		} catch (SQLException se) {
			System.out.println("SQL Error 1: " + se.toString() + " " + se.getErrorCode() + " " + se.getSQLState());
			se.printStackTrace();
		} catch (Exception e) {
			System.out.println("Error: " + e.toString() + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}
}
