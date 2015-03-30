import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class DataSet {
    private HashMap<String, Attribute> attributeMap;    // attri name -> attribute info. 
    private ArrayList<String> attributeList;                        // list of attributes
    private ArrayList<Data> dataList;                                        // list of all data
    private String relation;
    private String objective;
    
    public HashMap<String, Attribute> getAttributeMap() { return attributeMap; }
    public ArrayList<String> getAttributeList() { return attributeList; }
    public ArrayList<Data> getData() { return dataList; }
    public String getRelation() { return relation; }
    public String getObjective() { return objective; }
    
    public void setObjective(String v) {  objective = v; }
    
    public DataSet() {
        attributeMap = new HashMap<String, Attribute>();
        attributeList = new ArrayList<String>();
        dataList = new ArrayList<Data>();
    }
    
    public int size() {
    	return dataList.size();
    }
    
    public DataSet(String rel, HashMap<String, Attribute> map, ArrayList<String> attriList, String obj) {
    	relation = rel;
        attributeMap = map;
        attributeList = attriList;
        dataList = new ArrayList<Data>();
        objective = obj;
    }

    /**
     * @param String attribute: name of attribute
     * @param String value: decision point, for class, it represents a category; for real number, it halves the range
     * @param boolean left: only used in real number, represents <= value OR > value
     * 
     * @return DataSet: the targeted sub dataset
     * */
    public DataSet getSubSet (String attribute, String value, boolean left, ArrayList<String> attriList) {
    	DataSet subset = new DataSet(this.relation, this.attributeMap, attriList, this.getObjective());
    	if (attributeMap.get(attribute).isRealNum()) {
    		for (Data data : dataList) {
    			if (left && Double.parseDouble(data.getData(attribute)) <= Double.parseDouble(value)) {
    				subset.add(data);
    			}
    			else if (!left && Double.parseDouble(data.getData(attribute)) > Double.parseDouble(value)) {
    				subset.add(data);
    			}
    		}
    	} else {
    		for (Data data : dataList) {
    			if (data.getData(attribute).equals(value)) {
    				subset.add(data);
    			}
    		}
    	}
    	return subset;
    }
    
    public void add (Data d) {
    	dataList.add(d);
    }
    
    public boolean isAttriReal (String attri) {
    	return attributeMap.get(attri).isRealNum();
    }

    public void readDataFromFile(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String buffer = null;
            while ((buffer = reader.readLine()) != null && !buffer.matches("@data")) {
                if (!buffer.isEmpty()) {
                    if (buffer.startsWith("@relation")) {
                        relation = buffer.split(" ")[1];
                    } else if (buffer.startsWith("@attribute")) {
                        String[] attr = buffer.split(" ");
                        attributeList.add(attr[1]);
                        attributeMap.put(attr[1], new Attribute(attr[1], attr[2]));
                    }
                }
            }
            while ((buffer = reader.readLine()) != null) {
                dataList.add(new Data(attributeList, buffer));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {    reader.close();} 
                catch (IOException e1) {}
            }
        }
        
        this.objective = attributeList.get(attributeList.size() - 1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nRelation : " + relation + "\n");
        for (String key : attributeMap.keySet()) {
            sb.append(attributeMap.get(key) + "\n");
        }
        for (Data dt : dataList) {
            sb.append(dt + "\n");
        }
        return "DataSet [" +sb.toString() + "]";
    }
    
    public void sort (String attri) {
    	quick_sort(attri, 0, dataList.size() - 1);
    }
    
    private void quick_sort (String attri, int low, int high) {
    	if (low >= high) return;
    	int i = low, j = high + 1;
    	while (true) {
    		while (Double.parseDouble(dataList.get(low).getData().get(attri)) > Double.parseDouble(dataList.get(++i).getData().get(attri))) {
    			if (i == high) break;
    		}
    		while (Double.parseDouble(dataList.get(low).getData().get(attri)) < Double.parseDouble(dataList.get(--j).getData().get(attri))) {
    			if (j == low) break;
    		}
    		if (i >= j) break;
    		Data tmp = dataList.get(i);
    		dataList.set(i, dataList.get(j));
    		dataList.set(j, tmp);
    	}
		Data tmp = dataList.get(low);
		dataList.set(low, dataList.get(j));
		dataList.set(j, tmp);
		quick_sort (attri, low, j - 1);
		quick_sort (attri, j + 1, high);
    }
    
    public static void main(String[] args) {
        DataSet data = new DataSet();
        data.readDataFromFile("trainProdSelection.arff");
        data.sort("salary");
        System.out.println(data);
        //System.out.println(data.getSubSet("eCredit", "5", true, data.getAttributeList()));
    }

}
