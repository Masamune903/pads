package usecase.delivery_member.query;

import mysql_executor.*;
import java.util.Date;

import java.sql.*;

public class SetEndTime extends AbstractSQLUpdateExecutor {
    private final Date endTime;
    
    public SetEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String getSQLTemplate() {
        return "UPDATE delivery "
            + "SET end_time = ? "
            + "WHERE end_time = NULL;";
    }

    @Override
    public void setQuery(PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, this.endTime.toString());
    }
}
