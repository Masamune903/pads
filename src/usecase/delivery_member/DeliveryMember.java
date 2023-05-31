package usecase.delivery_member;

import myutil.*;
import database.data.delivery.*;
import database.data.delivery_member.*;
import database.executor.delivery.*;
import database.executor.delivery_member.*;

public class DeliveryMember {
    public final DeliveryMemberKey key;
    private static Console console = new Console();

    public DeliveryMemberData getData() {
        return new GetDeliveryMember(this.key).execute();
    }

    public String getName() {
        return this.getData().name;
    }

    public DeliveryMember(DeliveryMemberKey key) {
        this.key = key;
    }

    public DeliveryMember(String code) {
        this(new DeliveryMemberKey(code));
    }

    public void login() {
        new ChangeState(this.key, "wating").execute();
    }

    public DeliveryInstruction fetchInstruction() {
        return new GetDeliveryInstruction(this.key).execute();
    }

    public DeliveryInstruction checkInstruction() {
        console.next("配送指示を確認します。");
        DeliveryInstruction instruction = this.fetchInstruction();

        if (instruction == null)
            console.next("配送指示はありませんでした。");
        else
            console.next("以下の配送指示があります:", instruction);

        return instruction;
    }

    public DeliveryInstruction waitForInstructions() {
        console.print(this, "は、配送指示を待っています…");

        DeliveryInstruction di = null;
        while (di == null) {
            di = this.fetchInstruction();
            MyUtil.sleep(null, 1);
        }

        console.print(this, "が、以下の配送指示をうけました:", di);

        return di;
    }

    public void deliverToNext(DeliveryKey delivery) {
        DeliveryData deliveryData = new GetDelivery(delivery).execute();
        console.print(this, "が、", deliveryData.key.fromLocation.name, "から", deliveryData.toLocation.name, "まで" + deliveryData.key.product.code, "を配送します");

        new SetStartTime(delivery, System.currentTimeMillis()).execute();

        new ChangeState(this.key, "delivering").execute();
    }

    public void finishDelivery(DeliveryKey delivery) {
        console.next(this, "の配送が終わりました。");

        new SetEndTime(delivery, System.currentTimeMillis()).execute();

        new ChangeState(this.key, "not_here").execute();
    }

    public void backToTrspHub() {
        console.print(this, "は、拠点へ帰ります。");
    }

    public void arriveAtTrspHub() {
        console.next(this, "が、拠点へ帰ってきました。");
        new ChangeState(this.key, "wating").execute();
    }

    public void logout() {
        console.print(this, "は、ログアウトしました。");
        new ChangeState(this.key, null).execute();
    }

    @Override
    public String toString() {
        return "DeliveryMember { " + this.key.code + " }";
    }
}
