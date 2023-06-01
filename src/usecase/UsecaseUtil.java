/**
 * @author CY21249 TAKAGI Masamune
 */

package usecase;

import myutil.*;

public class UsecaseUtil {
	private static final Console console = new Console();

	public static void waitUntilSQLServerRunning() {
		while (!MySQLUtil.checkServerConnection()) {
			console.print("SQLサーバーが起動していません。以下のコマンドで起動してください。");
			console.next("$ cd \"C:/Program Files/MySQL/MySQL 8.0.33\"; bin/mysqld;");
		}
	}
}
