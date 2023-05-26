package usecase.delivery_member.query;

import java.sql.*;
import mysql_executor.AbstractSQLUpdateExecutor;

public class ChangeState extends AbstractSQLUpdateExecutor {
	private final String deliveryMember;
    private final String state; // null | "wating" | "delivering" | "not_here"
    
    public ChangeState(String deliveryMember, String state) {
        if (state != null) {
            switch (state) {
                case "wating":
                case "delivering":
                case "not_here":
                    break;
                default:
                    throw new RuntimeException(state + "is invalid");
            }
        }

		this.deliveryMember = deliveryMember;
        this.state = state;
    }

    @Override
    public String getSQLTemplate() {
        return "UPDATE delivery_member SET state = ? WHERE code = ?;";
    }

    @Override
    public void setQuery(PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, this.state);
        pstmt.setString(2, this.deliveryMember);
    }
}
