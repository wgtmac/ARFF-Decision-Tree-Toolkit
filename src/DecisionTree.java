import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class DecisionTree {
	private static class Node {
		String attribute;		// attribute used to divide dataset
		String value;		   // used only for real number
		String decision;		// final decision
		// @key
		// for class: attribute name
		// for real number: "left" (<= value), "right" (> value) 
		HashMap<String, Node>children;	
		
		public Node (String attr, String v, String dec) {
			attribute = attr;
			value = v;
			decision = dec;
			children = new HashMap<String, Node>();
		}
	}
		
	public Node buildTree(DataSet dataSet) {
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private Node buildSubTree (DataSet dataSet, ArrayList<String> attributeList, String value, int depth) {
		HashSet<String> set = new HashSet<String>();
		for (Data d : dataSet.getData()) {
			set.add(d.getData(dataSet.getObjective()));
		}
		
		if (set.size() <= 1 || dataSet.size() <= 5 || depth >= 10) {
			// maximum voting
			String bestDecision = "";
			int max = 0;
			String obj = dataSet.getObjective();
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			for (String attri : dataSet.getAttributeMap().get(dataSet.getObjective()).getValueSet()) {
				map.put(attri, 0);
			}
			for (Data d : dataSet.getData()) {
				map.put(d.getData(obj), map.get(d.getData(obj)) + 1);
			}
			for (String key : map.keySet()) {
				if (map.get(key) > max) {
					max = map.get(key);
					bestDecision = key;
				}
			}
			
			return new Node("", value, bestDecision);
		}

		// calculate entropy for objective in the entire dataset
		double entropyObj = 26.88;		// calcEntropy(dataSet, dataSet.getObjective());
		
		String bestAttri = "";
		double maxGainRatio = 0.0;
		String bestVar = "";
		
		// calculate conditional entropy for each attribute
		for (String attri : attributeList) {
			
			double condEntropy;
			double varEntropy;
			double gainRatio;
			
			String tmpBestVar = "";
			
			if (dataSet.isAttriReal(attri)) {			// continuous
				// sort attri
				
				// partition & find best partition
				double maxGain = 0.0;
				double tmpBestCondEntropy = 0.0;
				int count = 1;
				
				ArrayList<Data> dataList = dataSet.getData();
				boolean objChanged = false;
				boolean attriChanged = false;
				String prevObj = dataList.get(0).getData(dataSet.getObjective());
				String prevAttri = dataList.get(0).getData(attri);

				for (int i = 1; i < dataList.size(); ++i) {
					String currObj = dataList.get(i).getData(dataSet.getObjective());
					String currAttri = dataList.get(i).getData(attri);
					
					if (!prevObj.equals(currObj)) {
						objChanged = true;
					}
					if (Double.parseDouble(currAttri) != Double.parseDouble(prevAttri)) {
						attriChanged = true;
						count++;
					}
					
					if (attriChanged && objChanged) {
						condEntropy = 1.1;		// calcEntropy(dataSet, attri, currAttri, dataSet.getObjective());
						if ((entropyObj - condEntropy)> maxGain) {
							maxGain = (entropyObj - condEntropy);
							tmpBestVar = currAttri;
							tmpBestCondEntropy = condEntropy;
						}
						objChanged = false;
						attriChanged = false;
					}
					prevAttri = currAttri;
					prevObj = currObj;
				}
				varEntropy = 3.3;			// calcEntropy(dataSet, attri, tmpBestVar);
				gainRatio = (entropyObj - tmpBestCondEntropy) / varEntropy 
										- Math.log( (count - 1)/dataList.size() ) / Math.log(2.0);
			} else {					// discrete
				condEntropy = 2.2;		// calcEntropy(dataSet, attri, dataSet.getObjective());
				varEntropy = 4.4;			// calcEntropy(dataSet, attri);
				gainRatio = (entropyObj - condEntropy) / varEntropy;
			}
			
			if (maxGainRatio < gainRatio) {
				maxGainRatio = gainRatio;
				bestAttri = attri;
				bestVar = tmpBestVar;
			}
		}
		
		/**
		 * Create node
		 * */
		Node head = new Node(bestAttri, value, null);
		if (dataSet.isAttriReal(bestAttri)) {
			DataSet lSubset = dataSet.getSubSet(bestAttri, bestVar,  true, dataSet.getAttributeList());
			head.children.put("left", buildSubTree(lSubset, dataSet.getAttributeList(), "left", depth + 1));
			DataSet rSubset = dataSet.getSubSet(bestAttri, bestVar, false, dataSet.getAttributeList());
			head.children.put("right", buildSubTree(rSubset, dataSet.getAttributeList(), "right", depth + 1));
		} else {
			ArrayList<String> subAttriList = (ArrayList<String>) dataSet.getAttributeList().clone();
			subAttriList.remove(bestAttri);
			for (String attriValue : dataSet.getAttributeMap().get(bestAttri).getValueSet()) {
				DataSet subset = dataSet.getSubSet(bestAttri, attriValue, true, subAttriList);
				head.children.put(attriValue, buildSubTree(subset,subAttriList, attriValue, depth + 1));
			}
		}
		
		return head;
	}

	public static void main(String[] args) {
		
	}

}
