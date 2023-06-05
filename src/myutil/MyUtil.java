/**
 * ユーティリティクラス
 * 
 * @author CY21249 TAKAGI Masamune
 */

package myutil;

public class MyUtil {
	static final Console console = new Console();

	public static void sleep(double sec) {
		try {
			Thread.sleep((long) (sec * 1000));
		} catch (InterruptedException e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}

	public static void sleep(String msg, double sec) {
		if (msg != null)
			console.print(msg);

		sleep(sec);
	}
}
