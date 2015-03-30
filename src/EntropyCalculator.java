import java.util.ArrayList;
import java.util.HashSet;


public class EntropyCalculator {

	private String target;
	private DataSet dataSet;
	private HashSet<String> valueSet;
	
	//temp
	public HashSet<String> getValueSet() {
		return valueSet;
	}

	public void setValueSet(HashSet<String> valueSet) {
		this.valueSet = valueSet;
	}

	public EntropyCalculator(DataSet dataSet, String target) {
		this.dataSet = dataSet;
		this.target = target;
		valueSet = dataSet.getAttributeMap().get(target).getValueSet();
		if (isContinuous(target)) {
			discretize(target);
		}
	}
		
	public double getTargetEntropy() {
		double sumEntropy = 0;

		for (String value : valueSet) {
			sumEntropy += getValueEntropy(target, value);
		}
				
		return sumEntropy;
	}
	


	public double getAttributeEntropy(String attr) {
		if (isContinuous(attr)) {
			discretize(attr);
		}
		
		double sumEntropy = 0;
		HashSet<String> attrValueSet = dataSet.getAttributeMap()
									   .get(attr).getValueSet();		
		for (String attrValue : attrValueSet) {
			sumEntropy += getAttrValueGain(attr, attrValue);
		}
		
		return sumEntropy;
	}
	
	// Get possibility * entropy for one value while target is found
	private double getAttrValueGain(String attr, String value) {
		double sumEntropy = 0;
		double possibility = getPossibility(attr, value);
		for (String targetValue : valueSet) {
			double entropy = getAttrValueEntropy(attr, value, targetValue);
			sumEntropy += entropy;
		}
		return possibility * sumEntropy;
	}
	
	// Get entropy for one value while target is found
	private double getAttrValueEntropy(String attr, String value, String targetValue) {
		int valueCount = 0;
		ArrayList<Data> dataList = dataSet.getData();
		for (Data data : dataSet.getData()) {
			if (data.getData().get(attr).equals(value) 
			    && data.getData().get(target).equals(targetValue)) valueCount++;		
		}
		double possibility = (double) valueCount / dataList.size();
		if (possibility == 0) return 0;
		else return -(possibility * getLog(possibility));
	}
	
	// Get entropy for one value
	private double getValueEntropy(String attr, String value) {
		double possibility = getPossibility(attr, value);
		return -(possibility * getLog(possibility));
	}
	
	// Get possibility for one value
	private double getPossibility(String attr, String value) {
		int valueCount = 0;
		ArrayList<Data> dataList = dataSet.getData();
		for (Data data : dataSet.getData()) {
			if (data.getData().get(attr).equals(value)) valueCount++;		
		}
		return (double) valueCount / dataList.size();
	}
	
	// To get log base 2 value
	private double getLog(double x) {
		return Math.log(x) / Math.log(2);
	}
	
	// To check whether attribute is continuous
	private boolean isContinuous(String attr) {
		
		return false;
	}
	
	// To discretize continuous attribute
	private void discretize(String attr) {
		
	}
	
	public static void main(String[] args) {
		DataSet data = new DataSet();
		data.readDataFromFile("trainProdSelection.arff");
		EntropyCalculator calc = new EntropyCalculator(data, "label");
		
		System.out.println(calc.getAttributeEntropy("Type"));
	}

}
