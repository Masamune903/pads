/**
 * @author CY21249 TAKAGI Masamune
 */

package database.data.delivery;

import java.sql.*;

import database.data.delivery_member.DeliveryMemberKey;
import database.data.location.LocationKey;

public class DeliveryData {
	public final DeliveryKey key;
	public final LocationKey toLocation;
	public final DeliveryMemberKey deliveryMember; // nullable
	public final Timestamp startTime; // nullable
	public final Timestamp endTime; // nullable

	protected DeliveryData(DeliveryKey key, LocationKey toLocation, DeliveryMemberKey deliveryMember, Timestamp startTime, Timestamp endTime) {
		this.key = key;
		this.toLocation = toLocation;
		this.deliveryMember = deliveryMember;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	protected DeliveryData(String productCode, String productModel, String fromLocation, String toLocation, String deliveryMember, Timestamp startTime, Timestamp endTime) {
		this(
			new DeliveryKey(productCode, productModel, fromLocation),
			new LocationKey(toLocation),
			new DeliveryMemberKey(deliveryMember),
			startTime,
			endTime);
	}

	public static DeliveryData fromQueryResult(ResultSet resSet) throws SQLException {
		return new DeliveryData(
			DeliveryKey.fromQueryResult(resSet),
			new LocationKey(resSet.getString("to_location")),
			new DeliveryMemberKey(resSet.getString("delivery_member")),
			resSet.getTimestamp("start_time"),
			resSet.getTimestamp("end_time"));
	}

	@Override
	public String toString() {
		return "配送 { 商品: [" + this.key.product.model.code + "] " + this.key.product.code + ", " + this.key.fromLocation.name + " -> " + this.toLocation.name
			+ ((this.deliveryMember != null)
				? ", 配達員: " + this.deliveryMember.code + ", "
				: "")
			+ ((this.startTime != null)
				? "運送開始時刻: " + this.startTime + ", "
				: "")
			+ ((this.endTime != null)
				? "運送完了時刻: " + this.endTime
				: "");
	}
}
