/**
 * Carnegie Mellon University
 * MSIT - eBusiness Task 11
 * @author Gang Wu, Tian Zheng
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class DecisionTree {
    private static class Node {
        String attribute;      // attribute used to divide dataset
        String value;          // used only for real number
        String decision;       // final decision
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
        
        public boolean isLeaf () {
            return children == null || children.isEmpty();
        }
    }
        
    public Node buildTree(DataSet dataSet) {
        return buildSubTree(dataSet, dataSet.getAttributeList(), null, 0);
    }
    
    
    private Node makeLeaf (DataSet dataSet, String value) {
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
    
    @SuppressWarnings("unchecked")
    private Node buildSubTree (DataSet dataSet, ArrayList<String> attributeList, String value, int depth) {
        HashSet<String> set = new HashSet<String>();
        for (Data d : dataSet.getData()) {
            set.add(d.getData(dataSet.getObjective()));
        }
        
        if (set.size() <= 1 || dataSet.size() <= 1 || depth >= 5) {
            return makeLeaf (dataSet, value);
        }
        
        EntropyCalculator calculator = new EntropyCalculator(dataSet);

        // calculate entropy for objective in the entire dataset
        double entropyObj = calculator.getEntropy(dataSet.getObjective());
        
        String bestAttri = "";
        double maxGainRatio = 0.0;
        String bestVar = "";
        
        // calculate conditional entropy for each attribute
        for (String attri : attributeList) {
            
            double condEntropy;
            double varEntropy;
            double gainRatio;
            
            String tmpBestVar = "";
            
            if (dataSet.isAttriReal(attri)) {            // continuous
                dataSet.sort(attri);
                
                // partition & find best partition
                double maxGain = 0.0;
                double tmpBestCondEntropy = 0.0;
                
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
                    }
                    
                    if (attriChanged && objChanged) {
                        condEntropy = calculator.getContinuousCondEntropy(attri, currAttri, dataSet.getObjective());    
                        
                        if ((entropyObj - condEntropy) > maxGain) {
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
                
                if (!tmpBestVar.equals("")) {
                    varEntropy = calculator.getContinuousEntropy(attri, tmpBestVar);
                    gainRatio = (entropyObj - tmpBestCondEntropy) / varEntropy - 0.0;// Math.log( (count - 1)/dataList.size() ) / Math.log(2.0);
                } else {
                    gainRatio = 0.0;
                }

            } else {                    // discrete
                condEntropy = calculator.getCondEntropy(attri, dataSet.getObjective());
                varEntropy = calculator.getEntropy(attri);
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
        if (maxGainRatio == 0.0) {
            return makeLeaf (dataSet, value);
        }
        
        Node head = new Node(bestAttri, value, null);
        if (bestAttri != null && !bestAttri.isEmpty() && dataSet.isAttriReal(bestAttri)) {
            DataSet lSubset = dataSet.getSubSet(bestAttri, bestVar,  true, dataSet.getAttributeList());
            head.children.put("left", buildSubTree(lSubset, dataSet.getAttributeList(), bestVar, depth + 1));
            
            DataSet rSubset = dataSet.getSubSet(bestAttri, bestVar, false, dataSet.getAttributeList());
            head.children.put("right", buildSubTree(rSubset, dataSet.getAttributeList(), bestVar, depth + 1));
        } else {
            ArrayList<String> subAttriList = (ArrayList<String>) dataSet.getAttributeList().clone();
            subAttriList.remove(bestAttri);
            for (String attriValue : dataSet.getAttributeMap().get(bestAttri).getValueSet()) {
                DataSet subset = dataSet.getSubSet(bestAttri, attriValue, true, subAttriList);
                if (!subset.getData().isEmpty()) {
                    head.children.put(attriValue, buildSubTree(subset,subAttriList, attriValue, depth + 1));
                }
            }
        }
        
        return head;
    }
    
    public static void print (Node root) {
        printHelper(root, 0);
    }
    
    private static void printHelper (Node root, int depth) {
        if (root != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < depth; ++i) {
                sb.append("\t");
            }
            String tab = sb.toString();

            for (String key : root.children.keySet()) {
                Node child = root.children.get(key);

                if ((key.equals("left") || key.equals("right"))) {
                    System.out.print(tab + root.attribute + (key.equals("left") ? " <= " : " > ") + child.value);
                } else {
                    System.out.print(tab + root.attribute + " " + key);
                }
                if (child.isLeaf()) {
                    System.out.println(" [Decision : " + child.decision + "]");
                } else {
                    System.out.println();
                    printHelper(child, depth + 1);
                }
            }
        }
    }
    
    public static String classification (Node root, Data data) {
        while (root != null && !root.isLeaf()) {
            for (String key : root.children.keySet()) {
                if (key.equals("left")) {
                    if (Double.parseDouble(data.getData(root.attribute)) <=Double.parseDouble(root.children.get(key).value)) {
                        root = root.children.get(key);
                        break;
                    }
                } else if (key.equals("right")) {
                    if (Double.parseDouble(data.getData(root.attribute)) > Double.parseDouble(root.children.get(key).value)) {
                        root = root.children.get(key);
                        break;
                    }
                } else {
                    if (key.equals(data.getData(root.attribute))) {
                        root = root.children.get(key);
                        break;
                    }
                }
            }
        }
        return root == null ? "" : root.decision;
    }
    
    public static boolean validation (Node root, Data data, String target) {
        while (root != null && !root.isLeaf()) {
            boolean keyIsInTree = false;
            for (String key : root.children.keySet()) {
                if (key.equals("left")) {
                    if (Double.parseDouble(data.getData(root.attribute)) <=Double.parseDouble(root.children.get(key).value)) {
                        root = root.children.get(key);
                        keyIsInTree = true;
                        break;
                    }
                } else if (key.equals("right")) {
                    if (Double.parseDouble(data.getData(root.attribute)) > Double.parseDouble(root.children.get(key).value)) {
                        root = root.children.get(key);
                        keyIsInTree = true;
                        break;
                    }
                } else {
                    if (key.equals(data.getData(root.attribute))) {
                        root = root.children.get(key);
                        keyIsInTree = true;
                        break;
                    }
                }
            }
            
            /**
             * Here is an edge case: when training data doesn't a value of that attribute,
             * but a test data comes with that missing value, it will get a wrong decision.
             * This is the fault of training data, not mine. :) 
             * */
            if (!keyIsInTree) {
                root = null;
            }
        }
        //System.out.println("[Prediction: " + root.decision + " , Actual: " + data.getData(target));
        //System.out.println(data + " Prediction: " + (root == null ? "Undecided": root.decision) + "\n");
        return root == null ? false : root.decision.equals(data.getData(target));
    }
    
    public static double accuracy (DataSet dataSet, Node root) {
        double total = 0, correct = 0;
        for (Data data : dataSet.getData()) {
            if (validation(root, data, dataSet.getObjective())){
                correct++;
            }
            total++;
        }
        return correct / total;
    }

    public static void main(String[] args) {
        /**********************************************************
         * task11(a)
         * ********************************************************/
        DataSet dataSetA = new DataSet();
        dataSetA.readDataFromFile ("trainProdSelection.arff");
        
        // build tree
        DecisionTree dtA = new DecisionTree();
        Node rootA = dtA.buildTree(dataSetA);
        
        // print tree
        DecisionTree.print(rootA);
        
        // use entire train set to test accuracy (not cross validation)
        System.out.println("Task11(a)'s accuracy : " + String.format("%.2f%%", accuracy(dataSetA, rootA) * 100));
        
        // load test data
        DataSet testDataSetA = new DataSet();
        testDataSetA.readDataFromFile ("testProdSelection.arff");
        
        // show prediction
        accuracy(testDataSetA, rootA);

        /**********************************************************
         * task11(b)
         * ********************************************************/
        DataSet dataSetB = new DataSet();
        dataSetB.readDataFromFile ("trainProdIntro.binary.arff"); 
        
        // build tree
        DecisionTree dtB = new DecisionTree();
        Node rootB = dtB.buildTree(dataSetB);
        
        // print tree
        DecisionTree.print(rootB);

        // use entire train set to test accuracy (not cross validation)
        System.out.println("Task11(b)'s accuracy : " + String.format("%.2f%%", accuracy(dataSetB, rootB) * 100));
        
        // load test data
        DataSet testDataSetB = new DataSet();
        testDataSetB.readDataFromFile ("testProdIntro.binary.arff");
        
        // show prediction
        accuracy(testDataSetB, rootB);
    }

}
