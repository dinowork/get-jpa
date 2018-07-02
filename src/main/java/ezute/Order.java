/**
 * 
 */
package ezute;

import java.util.HashMap;
import java.util.Map;

/**
 * @author rpires
 *
 */
public class Order {

	private Map<SortOrder, String> values = new HashMap<>();
	
	public void add(SortOrder sortOrder, String propertieName) {
		values.put(sortOrder, propertieName);
	}

	public Map<SortOrder, String> getValues() {
		return values;
	}
	
}
