package database.executor.delivery_member;

import java.sql.*;

import database.data.location.trsp_hub.TrspHubKey;

public class GetDeliveryMemberByBelongsList extends GetDeliveryMemberList {
	private final TrspHubKey trspHub;

	public GetDeliveryMemberByBelongsList(TrspHubKey trspHub) {
		this.trspHub = trspHub;
	}

	@Override
	public String getSQLTemplate() {
		return "SELECT * FROM delivery_member WHERE belongs_to = ?;";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, this.trspHub.name);
	}
}
