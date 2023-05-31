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

	public T execute() {
		return this.executeTemplate();
	}
}
