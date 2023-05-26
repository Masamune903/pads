package usecase.user;

import java.util.ArrayList;

import data.*;
import myutil.*;
import usecase.user.query.*;

public class User {
	public final int id;

	public String getName() {
		return this.getData().name;
	}

	public UserData getData() {
		return new GetUser(this.id).execute();
	}

	public User(int id) {
		this.id = id;
	}

	public User(UserData data) {
		this(data.id);
	}

	public void purchase(String model, int price, String receiptLocation) throws Exception {
		UserData u = this.getData();
		if (u.money < price)
			throw new Exception("残高不足");

		ProductData product = new GetAvailableProductByModel(model).execute();
		if (product == null)
			throw new Exception("在庫不足");

		new PurchaseProduct(this.id, price, System.currentTimeMillis(), receiptLocation).execute();
		new ReduceUserMoney(this.id, price).execute();
	}

	@Override
	public String toString() {
		return "User { id: " + id + ", name: " + this.getName() + " }";
	}

	public ArrayList<ReceiptLocationData> getReceiptLocation() {
		return new GetReceiptLocationByUser(this.id).execute();
	}

	public void registerReceiptLocation(ReceiptLocationData recLoc) {
		new RegisterReceiptLocation(this.id, recLoc.name).execute();
	}
	
}
