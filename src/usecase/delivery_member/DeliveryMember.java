package usecase.delivery_member;

import java.util.Scanner;

public class DeliveryMember {
    private String code;

    public DeliveryMember(String code) {
        this.code = code;
    }

    public void app() {
        while (true) {
            // 配送拠点で運送指示を待つ
            DeliverInstruction instruction = waitForInstructions();

            // 運送するか尋ねる
            System.out.println("配送しますか？ (y/n)");
            Scanner sc = new Scanner(System.in);
            String res = sc.nextLine();
            if (res.equals("n"))
                continue;

            // 運送を開始する
            DeliverToNext dtn = new DeliverToNext(
                    this.code,
                    instruction.product,
                    instruction.fromLocation,
                    instruction.toLication);
            dtn.execute();

            // 運送が完了する

            // 配送拠点まで帰る

        }

    }

    private DeliverInstruction waitForInstructions() {
        System.out.println("指示を待っています…");
        while (true)
            ;

        return new DeliverInstruction(null, null);
    }
}
