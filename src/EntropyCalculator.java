import java.util.ArrayList;
import java.util.HashSet;

public class EntropyCalculator {

	private DataSet dataSet;

	public EntropyCalculator(DataSet dataSet) {
		this.dataSet = dataSet;
	}
		
	/**
	 * Discrete stand alone Entropy, target or normal
	 * @param attr the desired attribute
	 * @return entropy of the attribute
	 */
	public double getEntropy(String attr) {
		double sumEntropy = 0;
		HashSet<String> valueSet = dataSet.getAttributeMap().get(attr).getValueSet();
		for (String value : valueSet) {
			sumEntropy += getValueEntropy(attr, value);
		}
				
		return sumEntropy;
	}
	

	/**
	 * Discrete conditional entropy
	 * @param attr the desired attribute
	 * @param target the target attribute
	 * @return attribute entropy at target
	 */
	public double getCondEntropy(String attr, String target) {
		
		double sumEntropy = 0;
		HashSet<String> attrValueSet = dataSet.getAttributeMap()
									   .get(attr).getValueSet();		
		for (String attrValue : attrValueSet) {
			sumEntropy += getCondProduct(attr, attrValue, target);
		}
		
		return sumEntropy;
	}
	
	/**
	 * Continuous stand alone Entropy, target or normal
	 * @param attr the desired attribute
	 * @param cut the cut point for continuous value
	 * @return entropy of the attribute
	 */
	public double getContinuousEntropy(String attr, String cut) {
		double totalEntries = dataSet.getData().size();
		double leftSum = 0;
		double rightSum = 0;
		
		for (Data data : dataSet.getData()) {
			String value = data.getData().get(attr);
			if (Double.valueOf(value) <= Double.valueOf(cut)) {
				leftSum++;
			} else {
				rightSum++;
			}
		}	
		double leftPossibility = leftSum / totalEntries;
		double rightPossibility = rightSum / totalEntries;
		return -(leftPossibility * getLog(leftPossibility)) 
				- (rightPossibility * getLog(rightPossibility));
	}
	
	/**
	 * Continuous conditional entropy
	 * @param attr the desired attribute
	 * @param cut the cut point for continuous value
	 * @return entropy of the attribute
	 */
	public double getContinuousCondEntropy(String attr, String cut, String target) {
		HashSet<String> valueSet = dataSet.getAttributeMap().get(target).getValueSet();
		double totalEntries = dataSet.getData().size();
		
		double leftTotalSum = 0;
		double rightTotalSum = 0;		
		for (Data data : dataSet.getData()) {
			String value = data.getData().get(attr);
			if (Double.valueOf(value) <= Double.valueOf(cut)) {
				leftTotalSum++;
			} else {
				rightTotalSum++;
			}
		}	

		double leftEntropy = 0;
		double rightEntropy = 0;
		for (String targetValue : valueSet) {
			double[] entropy = getContinuousValueEntropy(attr, cut, target, targetValue);
			leftEntropy += entropy[0];
			rightEntropy += entropy[1];
		}

		double leftTotalPoss = leftTotalSum / totalEntries;
		double rightTotalPoss = rightTotalSum / totalEntries;		
		return leftTotalPoss * leftEntropy + rightTotalPoss * rightEntropy;
	}
	
	// Get entropy for one value while target is found
	private double[] getContinuousValueEntropy(String attr, 
			String cut, String target, String targetValue) {
		double[] entropy = new double[2];
		int leftCount = 0;
		int rightCount = 0;
		for (Data data : dataSet.getData()) {
			if (Double.valueOf(data.getData().get(attr)) <= Double.valueOf(cut)
			    && data.getData().get(target).equals(targetValue)) leftCount++;
			if (Double.valueOf(data.getData().get(attr)) > Double.valueOf(cut)
				    && data.getData().get(target).equals(targetValue)) rightCount++;
		}
		double leftPossibility = (double) leftCount / dataSet.getData().size();
		if (leftPossibility == 0) entropy[0] = 0;
		else entropy[0] = -(leftPossibility * getLog(leftPossibility));
		double rightPossibility = (double) rightCount / dataSet.getData().size();
		if (rightPossibility == 0) entropy[1] = 0;
		else entropy[1] = -(rightPossibility * getLog(rightPossibility));
		
		return entropy;
	}
	
	
	// Get possibility * entropy for one value while target is found
	private double getCondProduct(String attr, String value, String target) {
		double sumEntropy = 0;
		double possibility = getPossibility(attr, value);
		HashSet<String> valueSet = dataSet.getAttributeMap().get(target).getValueSet();
		for (String targetValue : valueSet) {
			double entropy = getCondValueEntropy(attr, value, target, targetValue);
			sumEntropy += entropy;
		}
		return possibility * sumEntropy;
	}
	
	// Get entropy for one value while target is found
	private double getCondValueEntropy(String attr, 
			String value, String target, String targetValue) {
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
	
	public static void main(String[] args) {
		DataSet data = new DataSet();
		data.readDataFromFile("trainProdSelection.arff");
		EntropyCalculator calc = new EntropyCalculator(data);
		
		System.out.println(calc.getEntropy("label"));
		System.out.println(calc.getCondEntropy("label", "Type"));
		System.out.println(calc.getContinuousEntropy("salary", "20.8"));
		System.out.println(calc.getContinuousCondEntropy("salary", "20.8", "Type"));
	}

}
