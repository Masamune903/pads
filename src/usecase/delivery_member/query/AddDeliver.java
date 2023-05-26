package usecase.delivery_member.query;

import mysql_executor.AbstractSQLUpdateExecutor;

import java.sql.*;

public class AddDeliver extends AbstractSQLUpdateExecutor {
    private final String deliveryMember;
    private final String product;
    private final String fromLocation;
    private final String toLocation;
    
    public AddDeliver(String deliveryMember, String product, String fromLocation, String toLocation) {
        this.deliveryMember = deliveryMember;
        this.product = product;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
    }

    @Override
    public String getSQLTemplate() {
        return "INSERT delivery VALUES (?, ?, ?, NULL, ?, ?);";
    }

    @Override
    public void setQuery(PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, this.product);
        pstmt.setString(2, this.deliveryMember);
        pstmt.setDate(3, new Date(System.currentTimeMillis()));
        pstmt.setString(4, this.fromLocation);
        pstmt.setString(5, this.toLocation);
    }

}
