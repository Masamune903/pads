package usecase.delivery_member;

import mysql_executor.AbstractExecutor;
import usecase.delivery_member.query.AddDeliverQuery;

public class DeliverToNext {
    private String member;
    private String product;
    private String fromLocation;
    private String toLocation;

    public DeliverToNext(String member, String product, String fromLocation, String toLocation) {
        this.member = member;
        this.product = product;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
    }

    void execute() {
        AddDeliverQuery adQuery = new AddDeliverQuery(member, product, fromLocation, toLocation);
        adQuery.execute();
    }
}
