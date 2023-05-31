package database.executor.delivery;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.data.delivery.DeliveryData;
import database.data.delivery.DeliveryInstruction;
import database.data.location.trsp_hub.TrspHubKey;
import database.executor.*;

public class GetNotFinishedDeliveryInstructionListOfTrspHub extends AbstractGetListSQLQueryExecutor<DeliveryInstruction> {
	private final TrspHubKey trspHub;

	public GetNotFinishedDeliveryInstructionListOfTrspHub(TrspHubKey trspHub) {
		this.trspHub = trspHub;
	}

	@Override
	public String getSQLTemplate() {
		// fromLocation が trspHub である未配達の (member is null) 配達のうち、
		// 1つ前の配達(productが等しくto_locationがfrom_locationと一致)が完了済みであるもの

		/*
			SELECT *
				FROM delivery
				WHERE (product_code, product_model, from_location) IN (
					SELECT *
						FROM delivery
						WHERE from_location = "大阪運送拠点"
							AND delivery_member IS NULL)
		 */
		return "SELECT *"
			+ "	FROM delivery"
			+ "	WHERE from_location = ?"
			+ "		AND delivery_member IS NULL";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, this.trspHub.name);
	}

	@Override
	protected DeliveryInstruction createData(ResultSet resSet) throws SQLException {
		return DeliveryInstruction.from(DeliveryData.fromQueryResult(resSet));
	}
}
