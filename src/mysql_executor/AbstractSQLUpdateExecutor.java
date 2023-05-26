package mysql_executor;

import java.sql.*;

public abstract class AbstractSQLUpdateExecutor extends AbstractSQLExecutor<Void> {
	@Override
	protected Void queryOrUpdate(PreparedStatement pstmt) throws SQLException {
		pstmt.executeUpdate();

		return null;
	}

	public void execute() {
		this.executeTemplate();
	}
}
