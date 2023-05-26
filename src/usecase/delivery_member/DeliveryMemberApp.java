package usecase.delivery_member;

import java.util.ArrayList;

import myutil.*;
import usecase.delivery_member.query.*;

public class DeliveryMemberApp {
	private final DeliveryMember member;

	private static final Console console = new Console();

	public DeliveryMemberApp(DeliveryMember member) {
		this.member = member;
	}

	public static void main(String[] args) {
		ArrayList<DeliveryMemberData> memberDataList = new GetDeliveryMemberList().execute();

		console.log("配達員ID一覧です");
		for (DeliveryMemberData member : memberDataList) {
			console.log(" -", member.code);
		}

		DeliveryMemberData memberData;
		while (true) {
			String code = console.prompt("ログインする配達員IDを入力してください");

			memberData = new GetDeliveryMember(code).execute();
			if (memberData != null)
				break;
			
			console.error("配達員IDが見つかりません");
		}

		usecase.delivery_member.DeliveryMemberApp app = new usecase.delivery_member.DeliveryMemberApp(new DeliveryMember(memberData));
		app.run();

	}

	public void run() {
		Console console = new Console();

		// ログイン
		this.member.login();
		console.log(this.member + "がログインしました。操作を開始します。");

        while (true) {
			// 操作を聞く
			int cmd1 = 0;
			while (cmd1 == 0) {
				cmd1 = console.select("操作を選んでください",
					"1. 配送指示を確認する",
					"2. 配送指示を待つ",
					"3. ログアウト"
				);

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
			this.member.deliverToNext(
				instruction.product,
				instruction.fromLocation,
				instruction.toLication
			);

			MyUtil.sleep("配送中...", (int)(Math.random() * 5000 + 5000));

            // 配送が完了する
			this.member.finishDelivery();
			console.next("拠点へ帰ります");

            // 配送拠点まで帰る
			this.member.backToTrspHub();

			MyUtil.sleep("移動中...", 2000);
			this.member.arriveAtTrspHub();
        }

		this.member.logout();
    }
}
