package usecase.delivery_member.query;

import mysql_executor.AbstractExecutor;
import java.util.Date;

import java.sql.*;

public class AddDeliverQuery extends AbstractExecutor {
    private String deliveryMember;
    private String product;
    private String fromLocation;
    private String toLocation;
    
    public AddDeliverQuery(String deliveryMember, String product, String fromLocation, String toLocation) {
        this.deliveryMember = deliveryMember;
        this.product = product;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
    }

    @Override
    public String getSQLTemplate() {
        return "INSERT delivery VALUES (?, ?, " + new Date().toString() + ", NULL, ?, ?)";
    }

    public void setQuery(PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, this.product);
        pstmt.setString(0, deliveryMember);
        pstmt.setString(0, fromLocation);
        pstmt.setString(0, toLocation);
    }
    
    public void showResult(ResultSet rs) {

    }


}
