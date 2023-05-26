package usecase.delivery_member;

public class DeliveryInstruction {
    public final String product;
    public final String fromLocation;
    public final String toLication;

    public DeliveryInstruction(String product, String fromLocation, String toLication) {
        this.product = product;
        this.fromLocation = fromLocation;
        this.toLication = toLication;
    }

    @Override
    public String toString() {
        return "DeliveryInstruction { product: " + this.product + ", from: " + this.fromLocation + ", to: " + this.toLication + " }";
    }
}
