package usecase.delivery_member;

import java.util.*;

import database.data.delivery.DeliveryInstruction;
import database.data.delivery_member.*;
import database.executor.delivery_member.*;
import myutil.*;
import usecase.App;

public class DeliveryMemberApp {
	private final DeliveryMember member;

	private static final Console console = new Console();

	public DeliveryMemberApp(DeliveryMember member) {
		this.member = member;
	}

	public static void main(String[] args) {
		App.waitUntilSQLServerRunning();

		ArrayList<DeliveryMemberData> memberDataList = new GetDeliveryMemberList().execute();

		console.print("配達員ID一覧です");
		for (DeliveryMemberData member : memberDataList) {
			console.print(" -", member.key.code);
		}

		DeliveryMemberData memberData;
		while (true) {
			String code = console.prompt("ログインする配達員IDを入力してください");

			memberData = new GetDeliveryMember(new DeliveryMemberKey(code)).execute();
			if (memberData != null)
				break;

			console.error("配達員IDが見つかりません");
		}

		DeliveryMemberApp app = new DeliveryMemberApp(new DeliveryMember(memberData.key));
		app.run();

	}

	public void run() {
		// ログイン
		this.member.login();
		console.print(this.member + "がログインしました。操作を開始します。");

		while (true) {
			// 操作を聞く
			int cmd1 = 0;
			while (cmd1 == 0) {
				cmd1 = console.select("操作を選んでください",
					"1. 配送指示を確認する",
					"2. 配送指示を待つ",
					"3. ログアウト");

				if (cmd1 == 3) {
					if (console.confirm("ログアウトしますか？"))
						break;
					cmd1 = 0;
				}
			}

			if (cmd1 == 3)
				break;

			DeliveryInstruction instruction;
			if (cmd1 == 1) {
				instruction = this.member.checkInstruction();
				if (instruction == null)
					continue;
			} else {
				// 配送拠点で配送指示を待つ
				instruction = this.member.waitForInstructions();
			}


			// 配送するか尋ねる
			boolean cmd2 = console.confirm("配送を開始しますか？");
			if (!cmd2)
				continue;

			// 配送を開始する
			this.member.deliverToNext(instruction.delivery);

			console.waitProgress("配送中...", (int) (Math.random() * 5 + 5));

			// 配送が完了する
			this.member.finishDelivery(instruction.delivery);
			console.next("拠点へ帰ります");

			// 配送拠点まで帰る
			this.member.backToTrspHub();

			console.waitProgress("移動中...", 2);
			this.member.arriveAtTrspHub();
		}

		this.member.logout();
	}
}
