package usecase.delivery_member.query;

import java.sql.*;

public class GetDeliveryMemberByBelongsList extends GetDeliveryMemberList {
		private final String trspHub;
	
		public GetDeliveryMemberByBelongsList(String trspHub) {
			this.trspHub = trspHub;
		}
	
		@Override
		public String getSQLTemplate() {
			return "SELECT * FROM delivery_member WHERE belongs_to = ?;";
		}
	
		@Override
		public void setQuery(PreparedStatement pstmt) throws SQLException {
			pstmt.setString(1, this.trspHub);
		}
	}
	