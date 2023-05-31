package usecase.delivery_member;

/** 
 * @author Yuri Morimoto
 */
public class DeliverInstruction {
    public final String product;
    public final String fromLocation;
    public final String toLication;

    public DeliverInstruction(String product, String fromLocation, String toLication) {
        this.product = product;
        this.fromLocation = fromLocation;
        this.toLication = toLication;
    }
}
