/**
 * 指定した拠点について、商品が搬入されており配送が可能な配送を取得するSQLQuery
 * 
 * @author CY21249 TAKAGI Masamune
 */

package database.executor.delivery;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.data.delivery.DeliveryData;
import database.data.location.trsp_hub.TrspHubKey;
import database.executor.*;

public class GetAvailableDeliveryOfTrspHub extends AbstractSQLQueryExecutor<DeliveryData> {
	private final TrspHubKey trspHub;

	public GetAvailableDeliveryOfTrspHub(TrspHubKey trspHub) {
		this.trspHub = trspHub;
	}

	@Override
	public String getSQLTemplate() {
		// fromLocation が trspHub である未配達の (member is null) 配達のうち、
		// 1つ前の配達(productが等しくto_locationがfrom_locationと一致)が完了済みであるもの

		return "SELECT *"
			+ "	FROM delivery"
			+ "	WHERE from_location = ?"
			+ "		AND delivery_member IS NULL"
			+ "		AND ((from_location) IN ("
			+ "			SELECT prev.to_location"
			+ "				FROM delivery AS prev"
			+ "				/* 同じ商品の前の配達 (同じ商品を運んでおり、to_location が from_location と一致している) が完了済みである */"
			+ "				WHERE (prev.product_code, prev.product_model) IN ((delivery.product_code, delivery.product_model))"
			+ "					AND prev.to_location = delivery.from_location"
			+ "					AND prev.end_time IS NOT NULL"
			+ "			) OR (0) IN ("
			+ "				SELECT COUNT(*)"
			+ "					FROM delivery AS prev"
			+ "					/* 同じ商品の前の配達 (同じ商品を運んでおり、to_location が from_location と一致している) がない*/"
			+ "					WHERE (prev.product_code, prev.product_model) IN ((delivery.product_code, delivery.product_model))"
			+ "						AND prev.to_location = delivery.from_location"
			+ "			));";
	}

	@Override
	public void setQuery(PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, this.trspHub.name);
	}

	@Override
	public DeliveryData getResult(ResultSet resSet) throws SQLException {
		if (!resSet.next())
			return null;
		return DeliveryData.fromQueryResult(resSet);
	}
}


/*
	SELECT *
		FROM delivery
		WHERE from_location = "大阪運送拠点"
			AND delivery_member IS NULL
			AND ((from_location) IN (
				SELECT prev.to_location
					FROM delivery AS prev
					-- 同じ商品の前の拠点 (同じ商品を運んでおり、to_location が from_location と一致している) かつ配達完了済みである
					WHERE (prev.product_code, prev.product_model) IN ((delivery.product_code, delivery.product_model))
						AND prev.to_location = delivery.from_location
						AND prev.end_time IS NOT NULL
			) OR (0) IN (
				SELECT COUNT(*)
					FROM delivery AS prev
					-- 同じ商品の前の拠点 (同じ商品を運んでおり、to_location が from_location と一致している)
					WHERE (prev.product_code, prev.product_model) IN ((delivery.product_code, delivery.product_model))
						AND prev.to_location = delivery.from_location
			));

	SELECT prev.to_location
		FROM delivery AS prev
		-- 同じ商品の前の拠点 (同じ商品を運んでおり、to_location が from_location と一致している) かつ配達完了済みである
		WHERE prev.to_location = "大阪運送拠点"
			AND prev.end_time IS NOT NULL;

	SELECT COUNT(*)
		FROM delivery AS prev
		-- 同じ商品の前の拠点 (同じ商品を運んでおり、to_location が from_location と一致している)
		WHERE (prev.product_code, prev.product_model) IN (("P011", "P"), ("P021", "P"), ("P031", "P"))
			AND prev.to_location = "大阪運送拠点";
 */
