import java.util.ArrayList;
import java.util.HashMap;

/**
 * Data class contains attributes for an entry/record
 * It stores data in a HashMap:
 * */

public class Data {
	// key: attribute name
	// value: attribute value, stored in string
	private HashMap<String, String> data;

	public HashMap<String, String> getData() {
		return data;
	}
	
	public Data (ArrayList<String> attributeList, String content) {
		data = new HashMap<String, String>();
		String[] values = content.split(",");
		for (int i = 0; i < attributeList.size(); ++i) {
			data.put(attributeList.get(i), values[i]);
		}
	}

	@Override
	public String toString() {
		return "Data [data=" + data + "]";
	}

	public static void main(String[] args) {
		@SuppressWarnings("serial")
		ArrayList<String> attributeList = new ArrayList<String> ()
				{{add("A"); add("B"); add("C");}};
		String content = "Fund,Student,0.64";
		Data d = new Data(attributeList, content);
		System.out.println(d);
	}

}
