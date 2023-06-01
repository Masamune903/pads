/**
 * @author CY21249 TAKAGI Masamune
 */

package usecase.user;

import java.util.ArrayList;
import database.data.delivery.*;
import database.data.location.*;
import database.data.location.receipt_location.*;
import database.data.location.receipt_location.register_location_register.*;
import database.data.model.*;
import database.data.product.*;
import database.data.user.*;
import database.executor.delivery.*;
import database.executor.location.receipt_location.*;
import database.executor.product.*;
import database.executor.user.*;
import module.route_manager.*;
import database.executor.purchase.*;

public class User {
	public final UserKey key;

	public String getName() {
		return this.getData().name;
	}

	public UserData getData() {
		return new GetUser(this.key).execute();
	}

	public User(UserKey key) {
		this.key = key;
	}

	public ProductData purchase(ModelKey model, int price, ReceiptLocationKey receiptLocation) throws Exception {
		UserData u = this.getData();
		if (u.money < price)
			throw new Exception("残高不足");

		ProductData product = new GetAvailableProductByModel(model).execute();
		if (product == null)
			throw new Exception("在庫不足");

		new PurchaseProduct(product.key, this.key, price, System.currentTimeMillis(), receiptLocation).execute();
		new ReduceUserMoney(this.key, price).execute();

		DeliveryPlanCreator dpc = new DeliveryPlanCreator(product.key, product.warehouse, receiptLocation);
		dpc.createDeliveryPlan();

		return product;
	}

	public ArrayList<ReceiptLocationData> fetchReceiptLocation() {
		return new GetReceiptLocationListByUser(this.key).execute();
	}

	public void registerReceiptLocation(ReceiptLocationKey recLoc) {
		new RegisterReceiptLocation(new ReceiptLocationRegisterKey(this.key, recLoc)).execute();
	}

	public ArrayList<ProductData> fetchPurchasedProductList() {
		return new GetProductListByUser(this.key).execute();
	}

	public ArrayList<ProductKey> fetchDeliveryNotFinishedProductList() {
		return new GetDeliveryNotFinishedProductListByUser(this.key).execute();
	}

	public DeliveryData fetchCurrentDeliveryOf(ProductKey product) {
		return new GetCurrentDeliveryOfProduct(product).execute();
	}

	public DeliveryData fetchLastDeliveryOf(ProductKey product) {
		return new GetLastDeliveryOfProduct(product).execute();
	}

	public ArrayList<DeliveryData> fetchDeliveryListOf(ProductKey product) {
		ProductData productData = new GetProduct(product).execute();

		ArrayList<DeliveryData> deliveryList = new ArrayList<>();
		LocationKey nextLocation = productData.warehouse;
		DeliveryData delivery;
		while ((delivery = new GetDelivery(new DeliveryKey(product, nextLocation)).execute()) != null) {
			deliveryList.add(delivery);
			nextLocation = delivery.toLocation;
		}

		return deliveryList;
	}

	@Override
	public String toString() {
		return "ユーザー [" + this.key.id + "] " + this.getName();
	}
}
