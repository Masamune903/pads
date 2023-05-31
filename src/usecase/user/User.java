package usecase.user;

import java.util.ArrayList;

import database.data.location.receipt_location.*;
import database.data.location.receipt_location.register_location_register.*;
import database.data.model.*;
import database.data.product.*;
import database.data.user.*;
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

	public void purchase(ModelKey model, int price, ReceiptLocationKey receiptLocation) throws Exception {
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

	}

	@Override
	public String toString() {
		return "User { id: " + this.key.id + ", name: " + this.getName() + " }";
	}

	public ArrayList<ReceiptLocationData> getReceiptLocation() {
		return new GetReceiptLocationListByUser(this.key).execute();
	}

	public void registerReceiptLocation(ReceiptLocationKey recLoc) {
		new RegisterReceiptLocation(new ReceiptLocationRegisterKey(this.key, recLoc)).execute();
	}

}
