package usecase.delivery_member;

import usecase.delivery_member.query.*;
import java.util.Date;

import myutil.*;

public class DeliveryMember {
    public final String code;
    private static Console console = new Console();

    public String getName() {
        return new GetDeliveryMember(this.code).execute().name;
    }

    public DeliveryMember(String code, String name) {
        this.code = code;
    }

    public DeliveryMember(DeliveryMemberData data) {
        this.code = data.code;
    }

    public void login() {
        ChangeState cs = new ChangeState(this.code, "wating");
        cs.execute();
    }

    public DeliveryInstruction getInstruction() {
        return new GetDeliveryInstruction(this.code).execute();
    }

    public DeliveryInstruction checkInstruction() {
        console.next("配送指示を確認します。");
        DeliveryInstruction instruction = this.getInstruction();

        if (instruction == null) 
            console.next("配送指示はありませんでした。");
        else 
            console.next("以下の配送指示があります:", instruction);

        return instruction;
    }

    public DeliveryInstruction waitForInstructions() {
        console.log(this, "は、配送指示を待っています…");

		DeliveryInstruction di = null;
        while (di == null) {
			di = this.getInstruction();
            MyUtil.sleep(null, 1000);
		}

		console.log(this, "が、以下の配送指示をうけました:", di);

        return di;
    }

    public void deliverToNext(String product, String fromLocation, String toLocation) {
        console.log(this, "が、", fromLocation, "から", toLocation, "まで" + product, "を配送します");
        
        AddDeliver ad = new AddDeliver(this.code, product, fromLocation, toLocation);
        ad.execute();

        ChangeState cs = new ChangeState(this.code, "delivering");
        cs.execute();
    }

    public void finishDelivery() {
        console.log(this, "の配送が終わりました。");

        SetEndTime set = new SetEndTime(new Date());
        set.execute();

        ChangeState cs = new ChangeState(this.code, "not_here");
        cs.execute();
    }

    public void backToTrspHub() {
        console.log(this, "は、拠点へ帰ります。");
    }

    public void arriveAtTrspHub() {
        console.log(this, "が、拠点へ帰ってきました。");
        ChangeState cs = new ChangeState(this.code, "wating");
        cs.execute();
    }

    public void logout() {
        console.log(this, "は、ログアウトしました。");
        ChangeState cs = new ChangeState(this.code, null);
        cs.execute();
    }

	@Override
	public String toString() {
		return "DeliveryMember { " + code + " }";
	}
}
