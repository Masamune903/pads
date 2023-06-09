/**
 * 標準入出力のユーティリティ
 * 
 * @author CY21249 TAKAGI Masamune
 */

package myutil;

import java.util.Scanner;

public class Console {
	public void print(Object... msgs) {
		String output = "";
		if (msgs != null) {
			for (Object msg : msgs) {
				if (msg != null)
					output += (msg + " ");
			}
		}
		if (output.length() > 0)
			System.out.println(output);
	}

	public void error(Object... msgs) {
		System.out.print("Error: ");
		this.next(msgs);
	}

	public String prompt(Object... msgs) {
		return this.prompt(msgs, "> ");
	}

	private String prompt(Object[] msgs, String inputMark) {
		this.print(msgs);

		Scanner sc = new Scanner(System.in);
		System.out.print(inputMark);
		String input = sc.nextLine();
		System.out.println();

		return input;
	}

	public boolean confirm(Object... msgs) {
		String input;
		while (true) {
			input = this.prompt(msgs, "y/n > ");

			switch (input) {
				case "":
				case "y":
				case "Y":
				case "1":
					return true;
				case "n":
				case "N":
				case "2":
					return false;
			}

			this.print("Please answer y/n");
		}
	}

	public void next(Object... msgs) {
		this.prompt(msgs, "( Enter )");
	}

	public int select(Object msg, Object... options) {
		return this.selectWithCancel(msg, null, options);
	}

	public int selectWithCancel(Object msg, String cancelOption, Object... options) {
		boolean hasCancel = cancelOption != null;
		while (true) {
			this.print(msg);

			for (Object option : options) {
				String optionStr = option + "";
				String[] optionStrs = optionStr.split("\n");
				for (String optionStrElm : optionStrs)
					this.print("\t" + optionStrElm);
			}

			if (hasCancel)
				this.print("\t", cancelOption);

			String input = (hasCancel)
				? this.prompt(null, "0-" + options.length + " > ")
				: this.prompt(null, "1-" + options.length + " > ");

			if (cancelOption == null && options.length == 0)
				throw new RuntimeException("選択肢の数が0です。");

			try {
				int res = Integer.parseInt(input);
				if ((hasCancel ? 0 : 1) <= res && res <= options.length)
					return res;
				this.error((hasCancel ? 0 : 1) + "から" + options.length + "の数字を入力してください");
			} catch (NumberFormatException e) {
				this.error("整数を入力してください");
			}
		}
	}

	public void waitProgress(String msg, double sec) {
		if (msg != null)
			this.print(msg);

		int max = 10;

		for (int i = 0; i < max; i++) {
			System.out.print("*");
			MyUtil.sleep(null, sec / max);
		}

		System.out.println();
	}
}
