package module.route_manager;

import database.data.location.LocationKey;
import database.data.location.trsp_hub.TrspHubData;
import database.data.location.trsp_hub.TrspHubKey;
import database.data.product.ProductKey;
import database.executor.delivery.AddDelivery;
import database.executor.delivery.GetNextDeriveryTrspHub;

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
		LocationKey curr = this.originLocation;

		int order = 1;
		while (true) {
			TrspHubData nextData = new GetNextDeriveryTrspHub(curr, this.targetLocation).execute();
			if (nextData == null)
				break;

			TrspHubKey next = nextData.key;

			new AddDelivery(this.product, curr, next, order++).execute();

			curr = next;
		}

		new AddDelivery(this.product, curr, this.targetLocation, order).execute();
	}
}
