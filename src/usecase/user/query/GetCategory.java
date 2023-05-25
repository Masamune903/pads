package usecase.user.query;

import mysql_executor.AbstractExecutor;
import java.sql.*;

public class GetCategory extends AbstractExecutor {
	public String getSQLTemplate() {
        return "SELECT category.name\n"
            + "FROM category";
    }

	public void setQuery(PreparedStatement pstmt) throws SQLException { };

	public void showResult(ResultSet res) { }
}
