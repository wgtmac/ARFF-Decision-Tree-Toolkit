import java.util.ArrayList;
import java.util.HashMap;


public class Data {
	public Data (ArrayList<String> attributeList, String content) {
		data = new HashMap<String, String>();
		String[] values = content.split(",");
		for (int i = 0; i < attributeList.size(); ++i) {
			data.put(attributeList.get(i), values[i]);
		}
	}
	
	public HashMap<String, String> data;

	public static void main(String[] args) {
		ArrayList<String> attributeList = new ArrayList<String> () {{add("A"); add("B"); add("C");}};
		String content = "Fund,Student,0.64";
		Data d = new Data(attributeList, content);
		for (String str : d.data.keySet()) {
			System.out.println(str + " " + d.data.get(str));
		}
	}

}
