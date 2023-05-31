package usecase.delivery_member.query;

import mysql_executor.AbstractExecutor;
import java.util.Date;

import java.sql.*;

public class SetEndTimeQuery extends AbstractExecutor {
    private Date endTime;
    
    public SetEndTimeQuery(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String getSQLTemplate() {
        return "UPDATE delivery SET endTime = " + new Date().toString() + " WHERE endTime = NULL ";

    public void setQuery(PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, this.endTime);
    }
    
    public void showResult(ResultSet rs) { }


}
