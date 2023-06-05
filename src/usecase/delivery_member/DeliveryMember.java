/**
 * @author CY21249 TAKAGI Masamune
 */

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

		if (instruction == null) {
			console.next("配送指示はありませんでした。");
			return null;
		}

		console.next("以下の配送指示があります:", instruction);

		return instruction;
	}

	public DeliveryInstruction waitForInstruction() {
		console.print(this, "は、配送指示を待っています…");

		while (true) {
			DeliveryInstruction di = this.fetchInstruction();

			if (di != null) {
				console.print(this, "が、以下の配送指示をうけました:", di);
				return di;
			}

			MyUtil.sleep(null, 1);
		}
	}

	public void deliverToNext(DeliveryInstruction instruction) {
		console.print(this, "が、", instruction.delivery.fromLocation.name, "から", instruction.toLocation.name, "まで" + instruction.product.code, "を配送します");

		new SetStartTime(instruction.delivery, System.currentTimeMillis()).execute();

		new ChangeState(this.key, "delivering").execute();
	}

	public void finishDelivery(DeliveryKey delivery) {
		new SetEndTime(delivery, System.currentTimeMillis()).execute();

		new ChangeState(this.key, "not_here").execute();

		console.next(this, "の配送が終わりました。");
	}

	public void backToTrspHub() {
		console.print(this, "は、拠点へ帰ります。");
	}

	public void arriveAtTrspHub() {
		new ChangeState(this.key, "wating").execute();
		console.next(this, "が、拠点へ帰ってきました。");
	}

	public void logout() {
		new ChangeState(this.key, null).execute();
		console.print(this, "は、ログアウトしました。");
	}

	@Override
	public String toString() {
		return "DeliveryMember { " + this.key.code + " }";
	}
}
