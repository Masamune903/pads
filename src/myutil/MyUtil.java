package myutil;

public class MyUtil {
	static final Console console = new Console();

	public static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}

	public static void sleep(String msg, long ms) {
		console.log(msg);

		sleep(ms);
	}
}
