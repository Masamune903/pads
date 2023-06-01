/**
 * @author CY21249 TAKAGI Masamune
 */

package database.data.location;

public class LocationKey {
	public final String name;

	public LocationKey(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null
			&& obj instanceof LocationKey
			&& this.name.equals(((LocationKey) obj).name);
	}
}
