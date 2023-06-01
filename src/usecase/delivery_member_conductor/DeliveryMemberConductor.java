/**
 * @author CY21249 TAKAGI Masamune
 */

package usecase.delivery_member_conductor;

import myutil.*;
import java.util.ArrayList;
import database.data.delivery.*;
import database.data.delivery_member.*;
import database.data.location.trsp_hub.*;
import database.executor.delivery.*;
import database.executor.delivery_member.*;

/** 各拠点で運送指示を出す人 */
public class DeliveryMemberConductor {
	static final Console console = new Console();

	private final TrspHubKey trspHub;

	public DeliveryMemberConductor(TrspHubKey trspHub) {
		this.trspHub = trspHub;
	}

	public ArrayList<DeliveryMemberData> showDeliveryMemberList() {
		return new GetDeliveryMemberListByBelongs(this.trspHub).execute();
	}

	protected DeliveryData fetchAvailableDelivery() {
		return new GetAvailableDeliveryOfTrspHub(this.trspHub).execute();
	}

	/** 商品の搬入があるか確認する */
	public DeliveryData checkAvailableDelivery() {
		console.next("商品の搬入による配送可能な配送を確認します");

		DeliveryData delivery = this.fetchAvailableDelivery();
		if (delivery != null) {
			console.print("以下の配送が可能です:");
			console.next("\t" + delivery);
			return delivery;
		} else {
			console.next("待機中の搬送予定はありません");
			return null;
		}
	}

	public void createDeliveryInstruction(DeliveryInstruction instruction) {
		new SetDeliveryMember(instruction.delivery, instruction.deliveryMember).execute();

		console.next("配送指示を出しました。");
	}

	public DeliveryData waitForProductImport() {
		if (!console.confirm("商品の搬入を待機します"))
			return null;

		console.print("商品の搬入を待っています…");
		while (true) {
			DeliveryData delivery = this.fetchAvailableDelivery();

			if (delivery != null) {
				console.next("以下の配達が可能になりました: " + delivery);
				return delivery;
			}

			MyUtil.sleep(1);
		}
	}

	@Override
	public String toString() {
		return trspHub.name + "の配達員管理人";
	}


}
