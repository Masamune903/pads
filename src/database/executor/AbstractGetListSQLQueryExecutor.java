package database.executor;

import java.sql.*;
import java.util.*;

public abstract class AbstractGetListSQLQueryExecutor<T> extends AbstractSQLQueryExecutor<ArrayList<T>> {
	protected abstract T createData(ResultSet resSet) throws SQLException;

	@Override
	public ArrayList<T> getResult(ResultSet resSet) throws SQLException {
		ArrayList<T> list = new ArrayList<>();

		while (resSet.next())
			list.add(this.createData(resSet));

		return list;
	}
}