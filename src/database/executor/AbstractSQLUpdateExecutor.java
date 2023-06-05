/**
 * サブクラスで実装するSQLの更新を実行するクラス
 * 
 * @author CY21249 TAKAGI Masamune
 */

package database.executor;

import java.sql.*;

public abstract class AbstractSQLUpdateExecutor extends AbstractSQLExecutor<Void> {
	@Override
	protected Void queryOrUpdate(PreparedStatement pstmt) throws SQLException {
		pstmt.executeUpdate();

		return null;
	}

	/** SQL文を実行するテンプレートメソッドを実行する */
	public void execute() {
		this.executeTemplate();
	}
}
