package usecase.delivery_member.query;

import java.sql.*;

import mysql_executor.*;
import usecase.delivery_member.DeliveryMemberData;

public class GetDeliveryMemberList extends AbstractGetListSQLQueryExecutor<DeliveryMemberData> {
		@Override
		public String getSQLTemplate() {
			return "SELECT * FROM delivery_member;";
		}
	
		@Override
		public void setQuery(PreparedStatement pstmt) throws SQLException {	}

		@Override
		protected DeliveryMemberData createData(ResultSet resSet) throws SQLException {
			return new DeliveryMemberData(
				resSet.getString("code"),
				resSet.getString("name")
			);
		}
	}
	