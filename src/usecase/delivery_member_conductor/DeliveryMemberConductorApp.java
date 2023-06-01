/**
 * @author CY21249 TAKAGI Masamune
 */

package usecase.delivery_member_conductor;

import java.util.*;
import database.data.location.trsp_hub.TrspHubData;
import database.data.delivery.*;
import database.data.delivery_member.DeliveryMemberData;
import database.executor.delivery.GetAvailableDeliveryOfTrspHub;
import database.executor.location.trsp_hub.GetTrspHubList;
import myutil.*;
import usecase.UsecaseUtil;

public class DeliveryMemberConductorApp {
	private static final Console console = new Console();

	private final DeliveryMemberConductor conductor;

	public static void main(String[] args) {
		UsecaseUtil.waitUntilSQLServerRunning();

		ArrayList<TrspHubData> trspHubList = new GetTrspHubList().execute();
		String[] trspHubStrs = new String[trspHubList.size()];
		int i = 0;
		for (TrspHubData trspHub : trspHubList) {
			DeliveryData delivery = new GetAvailableDeliveryOfTrspHub(trspHub.key).execute();
			trspHubStrs[i++] = i + ": " + (delivery != null ? "*" : " ") + trspHub.key.name;
		}
		int trspHubAnsIdx = console.selectWithCancel("管轄する配送拠点を選んでください", "終了する", (Object[]) trspHubStrs) - 1;

		if (trspHubAnsIdx < 0)
			return;

		new DeliveryMemberConductorApp(new DeliveryMemberConductor(trspHubList.get(trspHubAnsIdx).key)).run();

		main(args);
	}

	public DeliveryMemberConductorApp(DeliveryMemberConductor conductor) {
		this.conductor = conductor;
	}

	public void run() {
		console.print(this.conductor, "の操作です。");
		int cmd = console.select("操作を選んでください",
			"1. 可能な配送を確認する",
			"2, 商品の搬入を待つ",
			"3. ログアウト");

		switch (cmd) {
			case 1:
				this.checkDeliveryInstruction();
				this.run();
				return;
			case 2:
				this.waitForProductImport();
				this.run();
				return;
			case 3:
				if (console.confirm("ログアウトしますか？"))
					return;

				this.run();
				return;
		}

	}

	public void checkDeliveryInstruction() {
		DeliveryData delivery = this.conductor.checkAvailableDelivery();

		if (delivery == null)
			return;

		if (!console.confirm("配送指示の作成に進みますか？"))
			return;

		this.selectDeliveryMember(delivery);
	}

	public void selectDeliveryMember(DeliveryData delivery) {
		console.print("以下の配送をする配達員の選択を始めます");
		console.print("\t", delivery);

		ArrayList<DeliveryMemberData> deliveryMemberList = this.conductor.showDeliveryMemberList();
		String[] deliveryMemberStrs = new String[deliveryMemberList.size()];
		int i = 0;
		for (DeliveryMemberData deliveryMember : deliveryMemberList) {
			deliveryMemberStrs[i++] = i + ": " + deliveryMember.key.code + " " + deliveryMember.name;
		}
		int selectMemberAns = console.selectWithCancel("配達員を選んでください", "戻る", (Object[]) deliveryMemberStrs) - 1;
		if (selectMemberAns < 0) {
			// 画面遷移: 戻る (最初)
			return;
		}

		DeliveryMemberData deliveryMember = deliveryMemberList.get(selectMemberAns);

		this.createDeliveryInstruction(delivery, deliveryMember);
	}

	public void createDeliveryInstruction(DeliveryData delivery, DeliveryMemberData deliveryMember) {
		DeliveryInstruction instruction = new DeliveryInstruction(delivery, deliveryMember.key);

		if (!console.confirm("以下の配送指示を作成します:", instruction)) {
			this.selectDeliveryMember(delivery);
			return;
		}

		this.conductor.createDeliveryInstruction(instruction);
	}

	public void waitForProductImport() {
		DeliveryData delivery = this.conductor.waitForProductImport();

		if (delivery == null)
			return;

		if (!console.confirm("配送指示の作成に進みますか？"))
			return;

		this.selectDeliveryMember(delivery);
	}
}
