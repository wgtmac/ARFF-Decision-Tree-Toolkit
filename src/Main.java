
public class Main {
	
    public static double crossValidation (DataSet dataSet) {
    	int[][] matrix ={ 
    			{ 0, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
    			{ 1, 0, 1, 1, 1, 1, 1, 1, 1, 1 },
    			{ 1, 1, 0, 1, 1, 1, 1, 1, 1, 1 },
    			{ 1, 1, 1, 0, 1, 1, 1, 1, 1, 1 },
    			{ 1, 1, 1, 1, 0, 1, 1, 1, 1, 1 },
    			{ 1, 1, 1, 1, 1, 0, 1, 1, 1, 1 },
    			{ 1, 1, 1, 1, 1, 1, 0, 1, 1, 1 },
    			{ 1, 1, 1, 1, 1, 1, 1, 0, 1, 1 },
    			{ 1, 1, 1, 1, 1, 1, 1, 1, 0, 1 },
    			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 },  
    		};
    	
    	double accumulated_accuracy = 0.0;
    	for (int i = 0; i < matrix.length; ++i) {
    		DataSet trainSet = dataSet.getTrainSet(matrix[i]);
    		DataSet testSet = dataSet.getTestSet(matrix[i]);
            DecisionTree dt = new DecisionTree();
            accumulated_accuracy += DecisionTree.accuracy(testSet, dt.buildTree(trainSet));
    	}
    	
    	return accumulated_accuracy / matrix.length;
    }

	public static void main(String[] args) {
        DataSet dataSetA = new DataSet();
        dataSetA.readDataFromFile ("trainProdSelection.arff");
        System.out.println("Task11(a)'s Cross Validation Result : " + String.format("%.2f%%", crossValidation(dataSetA) * 100));
        
        DataSet dataSetB = new DataSet();
        dataSetB.readDataFromFile ("trainProdIntro.binary.arff"); 
        System.out.println("Task11(b)'s Cross Validation Result : " + String.format("%.2f%%", crossValidation(dataSetB) * 100));
	}

}
