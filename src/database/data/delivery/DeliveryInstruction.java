/**
 * @author CY21249 TAKAGI Masamune
 */

package database.data.delivery;

import database.data.delivery_member.DeliveryMemberKey;
import database.data.location.LocationKey;
import database.data.product.ProductKey;

public class DeliveryInstruction {
    public final DeliveryKey delivery;
    public final ProductKey product;
    public final LocationKey fromLocation;
    public final LocationKey toLocation;
    public final DeliveryMemberKey deliveryMember;

    protected DeliveryInstruction(DeliveryData delivery) {
        this.delivery = delivery.key;
        this.product = delivery.key.product;
        this.fromLocation = delivery.key.fromLocation;
        this.toLocation = delivery.toLocation;
        this.deliveryMember = delivery.deliveryMember;
    }

    public DeliveryInstruction(DeliveryData delivery, DeliveryMemberKey member) {
        this.delivery = delivery.key;
        this.product = delivery.key.product;
        this.fromLocation = delivery.key.fromLocation;
        this.toLocation = delivery.toLocation;
        this.deliveryMember = member;
    }

    public String toString() {
        return "配送指示「配送品: " + this.product.code + ", 配達員: " + this.deliveryMember.code + ", " + this.fromLocation.name + " -> " + this.toLocation.name + " 」";
    }

    public static DeliveryInstruction from(DeliveryData delivery) {
        return new DeliveryInstruction(delivery);
    }
}
