import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DataSet {
	public HashMap<String, Attribute> attributeMap;
	public ArrayList<String> attributeList;
	public ArrayList<Data> data;
	public String relation;

	public DataSet() {
		attributeMap = new HashMap<String, Attribute>();
		attributeList = new ArrayList<String>();
		data = new ArrayList<Data>();
	}

	public void readDataFromFile(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String buffer = null;
			while ((buffer = reader.readLine()) != null
					&& !buffer.matches("@data")) {
				if (!buffer.isEmpty()) {
					if (buffer.startsWith("@relation")) {
						relation = buffer.split(" ")[1];
					} else if (buffer.startsWith("@attribute")) {
						String[] attr = buffer.split(" ");
						attributeList.add(attr[1]);
						attributeMap.put(attr[1], new Attribute(attr[1],
								attr[2]));
					}
				}
			}

			while ((buffer = reader.readLine()) != null) {
				data.add(new Data(attributeList, buffer));
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	public static void main(String[] args) {
		DataSet data = new DataSet();
		data.readDataFromFile("trainProdSelection.arff");
	}

}
