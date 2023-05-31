package usecase.delivery_member_conductor;

import java.util.*;
import database.data.location.trsp_hub.TrspHubData;
import database.data.delivery.*;
import database.executor.location.trsp_hub.GetTrspHubList;
import myutil.*;
import usecase.App;

public class DeliveryMemberConductorApp {
	private static final Console console = new Console();

	private final DeliveryMemberConductor conductor;

	public static void main(String[] args) {
		App.waitUntilSQLServerRunning();

		ArrayList<TrspHubData> trspHubList = new GetTrspHubList().execute();
		String[] trspHubStrs = new String[trspHubList.size()];
		int i = 0;
		for (TrspHubData trspHub : trspHubList) {
			trspHubStrs[i++] = i + ": " + trspHub.key.name;
		}
		int trspHubAnsIdx = console.select("管轄する配送拠点を選んでください", trspHubStrs) - 1;

		new DeliveryMemberConductorApp(new DeliveryMemberConductor(trspHubList.get(trspHubAnsIdx).key)).run();;
	}

	public DeliveryMemberConductorApp(DeliveryMemberConductor conductor) {
		this.conductor = conductor;
	}

	public void run() {
		console.print("処理を始めます");
		DeliveryInstruction di = this.conductor.checkNewDeliveryInstruction();
	}

}
