package database.executor.delivery;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.data.delivery.DeliveryData;
import database.data.delivery.DeliveryInstruction;
import database.data.location.trsp_hub.TrspHubKey;
import database.executor.AbstractSQLQueryExecutor;

public class GetNewDeliveryInstructionOfTrspHub extends AbstractSQLQueryExecutor<DeliveryInstruction> {
	private final TrspHubKey trspHub;

	public GetNewDeliveryInstructionOfTrspHub(TrspHubKey trspHub) {
		this.trspHub = trspHub;
	}

	@Override
	public DeliveryInstruction getResult(ResultSet resSet) throws SQLException {
		if (!resSet.next())
			return null;
		return DeliveryInstruction.from(DeliveryData.fromQueryResult(resSet));
	}

	@Override
	public String getSQLTemplate() {
		// fromLocation が trspHub である未配達の (member is null) 配達のうち、
		// その直前の配達 () が完了しているもの

		/*
			SELECT *
				FROM delivery
				WHERE from_location = "大阪運送拠点"
					AND (product_code, product_model, from_location) IN (
						SELECT product_code, product_model, from_location
							FROM delivery AS d
							WHERE d.delivery_member IS NULL
							GROUP BY d.product_code, d.product_model
							ORDER BY d.order_index
							LIMIT 1);
		 */

		return "SELECT *"
			+ "	FROM delivery"
			+ "	WHERE from_location = ?"
			+ "		AND delivery_member IS NULL"
			+ "	GROUP BY product_code, product_model"
			+ "	ORDER BY order_index";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, this.trspHub.name);
	}


}
