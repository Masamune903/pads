package data;

public class ModelDataWithProductCount {
	public final String code;
	public final String name;
	public final String category;
	public final int price;
	public final String manufacturer;
	public final int productCount;

	public ModelDataWithProductCount(String code, String name, String category, int price, String manufacturer, int productCount) {
		this.code = code;
		this.name = name;
		this.category = category;
		this.price = price;
		this.manufacturer = manufacturer;
		this.productCount = productCount;
	}

}
