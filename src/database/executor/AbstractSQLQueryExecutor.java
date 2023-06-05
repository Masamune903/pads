/**
 * サブクラスで実装する、T型のデータを取得するSQLクエリを実行するクラス
 * 
 * @author CY21249 TAKAGI Masamune
 */

package database.executor;

import java.sql.*;

public abstract class AbstractSQLQueryExecutor<T> extends AbstractSQLExecutor<T> {
	public abstract T getResult(ResultSet resSet) throws SQLException;

	@Override
	protected T queryOrUpdate(PreparedStatement pstmt) throws SQLException {
		ResultSet rs = pstmt.executeQuery();

		T res = this.getResult(rs);
		rs.close();

		return res;
	}

	/** SQL文を実行するテンプレートメソッドを実行し、その結果を返す */
	public T execute() {
		return this.executeTemplate();
	}
}
