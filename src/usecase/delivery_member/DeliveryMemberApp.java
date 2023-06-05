/**
 * @author CY21249 TAKAGI Masamune
 */

package usecase.delivery_member;

import java.util.*;

import database.data.delivery.DeliveryInstruction;
import database.data.delivery_member.*;
import database.executor.delivery.GetDeliveryInstruction;
import database.executor.delivery_member.*;
import myutil.*;
import usecase.UsecaseUtil;

public class DeliveryMemberApp {
	private final DeliveryMember member;

	private static final Console console = new Console();

	public DeliveryMemberApp(DeliveryMember member) {
		this.member = member;
	}

	public static void main(String[] args) {
		UsecaseUtil.waitUntilSQLServerRunning();

		ArrayList<DeliveryMemberData> memberDataList = new GetDeliveryMemberList().execute();

		console.print("配達員ID一覧です");
		for (DeliveryMemberData member : memberDataList) {
			DeliveryInstruction instruction = new GetDeliveryInstruction(member.key).execute();
			console.print(" -", (instruction != null ? "*" : " "), member.key.code);
		}

		DeliveryMemberData memberData;
		while (true) {
			String code = console.prompt("ログインする配達員IDを入力してください");

			if (code.equals("exit") || code.equals("0"))
				return;

			memberData = new GetDeliveryMember(new DeliveryMemberKey(code)).execute();
			if (memberData != null)
				break;

			console.error("配達員IDが見つかりません");
		}

		DeliveryMemberApp app = new DeliveryMemberApp(new DeliveryMember(memberData.key));
		app.run();

		main(args);
	}

	public void run() {
		// ログイン
		this.member.login();
		console.print(this.member.key.code + " " + this.member.getName() + "がログインしました。操作を開始します。");

		this.selectSection();
	}

	public void selectSection() {
		// 操作を聞く
		int cmd = console.select("操作を選んでください",
			"1. 配送指示を確認する",
			"2. 配送指示を待つ",
			"3. ログアウト");

		switch (cmd) {
			case 1:
				this.checkInstructionSection();
				break;
			case 2:
				this.waitForInstructionSection();
				break;
			case 3:
				if (console.confirm("ログアウトしますか？")) {
					this.member.logout();
					return;
				}
				break;
		}

		this.selectSection();
	}

	public void checkInstructionSection() {
		// 配送指示を確認する
		DeliveryInstruction instruction = this.member.checkInstruction();

		if (instruction == null)
			return;

		// 配送するか尋ねる
		boolean doDeliveryAns = console.confirm("配送を開始しますか？");
		if (!doDeliveryAns)
			return;

		this.deliverySection(instruction);
	}

	public void waitForInstructionSection() {
		// 配送拠点で配送指示を待つ
		DeliveryInstruction instruction = this.member.waitForInstruction();

		if (instruction == null)
			return;

		// 配送するか尋ねる
		boolean doDeliveryAns = console.confirm("配送を開始しますか？");
		if (!doDeliveryAns)
			return;

		this.deliverySection(instruction);
	}

	public void deliverySection(DeliveryInstruction instruction) {
		// 配送を開始する
		this.member.deliverToNext(instruction);

		console.waitProgress("配送中...", (int) (Math.random() * 8 + 5));

		// 配送が完了する
		this.member.finishDelivery(instruction.delivery);
		console.next("拠点へ帰ります");

		// 配送拠点まで帰る
		this.member.backToTrspHub();

		console.waitProgress("移動中...", 2);
		this.member.arriveAtTrspHub();
	}
}
