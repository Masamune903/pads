package usecase.delivery_member_conductor;

import myutil.*;

import database.data.delivery.DeliveryInstruction;
import database.data.location.trsp_hub.TrspHubKey;
import database.executor.delivery.GetNewDeliveryInstructionOfTrspHub;

/** 各拠点で運送指示を出す人 */
public class DeliveryMemberConductor {
	static final Console console = new Console();

	private final TrspHubKey trspHub;

	public DeliveryMemberConductor(TrspHubKey trspHub) {
		this.trspHub = trspHub;
	}

	protected DeliveryInstruction fetchNewDeliveryInstruction() {
		return new GetNewDeliveryInstructionOfTrspHub(this.trspHub).execute();
	}

	/** 商品の搬入があるか確認する */
	public DeliveryInstruction checkNewDeliveryInstruction() {
		console.next("商品の搬入による待機中の搬送予定の有無を確認します");

		DeliveryInstruction di = this.fetchNewDeliveryInstruction();
		if (di != null) {
			console.print("以下の待機中の搬送予定があります:");
			console.next("\t" + di);
			return di;
		} else {
			console.next("待機中の搬送予定はありません");
			return null;
		}
	}

	public void assignMember() {

	}
}
