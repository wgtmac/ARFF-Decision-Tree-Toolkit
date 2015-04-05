/**
 * Carnegie Mellon University
 * MSIT - eBusiness Task 11
 * @author Team 1
 */

import java.util.Collections;
import java.util.Random;


public class Main {
    
    /**
     * Cross validation for decision tree
     * @param DataSet dataSet: data including training and test set
     * @param int k: k fold
     * 
     * @return double: accuracy
     * */
    public static double crossValidation (DataSet dataSet, int k, boolean shuffle) {
        if (shuffle == true) {
            long seed = System.nanoTime();
            Collections.shuffle(dataSet.getData(), new Random(seed));
        }
        int[][] matrix  = new int[k][k];
        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < k; ++j) {
                matrix[i][j] = (i == j ? 0 : 1);
            }
        }
        
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
        
        if (args.length >= 1) {
            String filename = args[0];
            DataSet dataSet = new DataSet();
            dataSet.readDataFromFile (filename);
            
            int k = 10;
            if (args.length >= 2) {
                k = Integer.valueOf(args[1]);
            }
            
            System.out.println("Cross Validation Result : " + String.format("%.2f%%", crossValidation(dataSet, k, false) * 100));
        }
    }

}
