package usecase.user.query;

import mysql_executor.AbstractExecutor;

public class ShowModelListOfCategory extends AbstractExecutor {
    public void preQuery() { }

	public String getSQLTemplate() {
        return "SELECT model.name, model.price\n"
            + "FROM model\n"
            + "WHERE model.category_name=?"
            + "AND product.warehouse_name!=NULL\n"
            + "AND product.model_code=model.code";
    }

	public void setQuery(PreparedStatement pstmt) throws SQLException;

	public void showResult(ResultSet res);
}