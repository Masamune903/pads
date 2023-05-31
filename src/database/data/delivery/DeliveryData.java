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
}
