package mysql_executor;

import java.sql.*;

public abstract class AbstractExecutor {
	public void preQuery() { }

	public abstract String getSQLTemplate();

	public abstract void setQuery(PreparedStatement pstmt) throws SQLException;

	public abstract void showResult(ResultSet res);

	public void execute() {
		try {
			this.preQuery();

			Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost/courses?useSSL=false&characterEncoding=utf8&useServerPrepStmts=true", 
				"root", ""
			);		 

			PreparedStatement st=conn.prepareStatement(this.getSQLTemplate());
			
			// SQLに値を埋め込む. その他、個別に必要な処理もここで行う
			this.setQuery(st);

			// SQLを実行して結果を得る.
			ResultSet res=st.executeQuery();

			// 結果を用いた個別処理
			this.showResult(res);

			// 終了処理
			res.close();
			st.close();
			conn.close();
		} catch (SQLException se) {
			System.out.println("SQL Error 1: " + se.toString() + " " + se.getErrorCode() + " " + se.getSQLState());
			se.printStackTrace();
		} catch (Exception e) {
			System.out.println("Error: " + e.toString() + e.getMessage());
			e.printStackTrace();
		}
	}
}
