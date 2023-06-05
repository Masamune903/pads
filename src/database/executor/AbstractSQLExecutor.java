/**
 * サブクラスで実装する、SQLの問い合わせまたは更新の文を実行するクラス
 * 
 * @author CY21249 TAKAGI Masamune
 */

package database.executor;

import java.sql.*;

abstract class AbstractSQLExecutor<T> {
	public void preQuery() {}

	/** SQL文を、問い合わせや更新のサブクラスで実装 */
	public abstract String getSQLTemplate();

	/** PreparedStatementに値を埋め込む処理を、問い合わせや更新のサブクラスで実装 */
	public abstract void setQuery(PreparedStatement pstmt) throws SQLException;

	/** 問い合わせまたは更新を実行する */
	protected abstract T queryOrUpdate(PreparedStatement pstmt) throws SQLException;

	/** SQL文を実行し、(問い合わせならgetResult()で作成した)結果を返すクラス */
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
