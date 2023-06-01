/**
 * @author CY21202 MIHARA Yutaro
 */

package database.executor.delivery;

import java.sql.*;

import database.executor.*;

import database.data.delivery.DeliveryKey;
import database.data.delivery_member.DeliveryMemberKey;

/** 配送指示が出たとき */
public class SetDeliveryMember extends AbstractSQLUpdateExecutor {
	private final DeliveryKey key;
	private final DeliveryMemberKey deliveryMember;

	public SetDeliveryMember(DeliveryKey key, DeliveryMemberKey deliveryMember) {
		this.key = key;
		this.deliveryMember = deliveryMember;
	}

	@Override
	public String getSQLTemplate() {
		return "UPDATE delivery"
			+ "	SET delivery_member = ?"
			+ "	WHERE product_code = ? AND product_model = ? AND from_location = ?;";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, this.deliveryMember.code);
		pstmt.setString(2, this.key.product.code);
		pstmt.setString(3, this.key.product.model.code);
		pstmt.setString(4, this.key.fromLocation.name);
	}
}
