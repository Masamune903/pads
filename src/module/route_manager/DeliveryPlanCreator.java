/**
 * 配送ルートを作成するオブジェクト
 * 
 * @author CY21249 TAKAGI Masamune
 */

package module.route_manager;

import database.data.location.*;
import database.data.location.trsp_hub.*;
import database.data.product.*;
import database.executor.delivery.*;

public class DeliveryPlanCreator {
	private final ProductKey product;
	private final LocationKey originLocation;
	private final LocationKey targetLocation;

	public DeliveryPlanCreator(ProductKey product, LocationKey originLocation, LocationKey targetLocation) {
		this.product = product;
		this.originLocation = originLocation;
		this.targetLocation = targetLocation;
	}

	public void createDeliveryPlan() {
		// 初期値を配送の出発点に
		LocationKey curr = this.originLocation;

		while (true) {
			// 次の配送拠点を取得
			TrspHubData next = new GetNextDeriveryTrspHub(curr, this.targetLocation).execute();
			if (next == null)
				break;

			// 運送予定に追加
			new AddDelivery(this.product, curr, next.key).execute();

			curr = next.key;
		}

		// 目的地までの運送を追加
		new AddDelivery(this.product, curr, this.targetLocation).execute();
	}
}
