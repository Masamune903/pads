/**
 * 「受け取り場所を登録する」
 * 
 * 指定したユーザーの受け取り場所の登録情報を登録するSQLUpdate
 * 
 * ユースケース：「受け取り場所を登録する」
 * 
 * @author CY21248 SASAHARA Hayato
 */

package database.executor.location.receipt_location;

import java.sql.*;
import database.data.location.receipt_location.receipt_location_register.*;
import database.executor.*;

public class RegisterReceiptLocation extends AbstractSQLUpdateExecutor {
	private final ReceiptLocationRegisterKey key;

	public RegisterReceiptLocation(ReceiptLocationRegisterKey key) {
		this.key = key;
	}

	@Override
	public String getSQLTemplate() {
		/* INSERT receipt_location_register VALUES ({{user}}, {{location}}); */
		return "INSERT receipt_location_register VALUES (?, ?);";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setInt(1, this.key.user.id);
		pstmt.setString(2, this.key.location.name);
	}
}
