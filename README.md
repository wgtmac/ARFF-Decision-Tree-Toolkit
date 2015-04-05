Description
  Java implementation of decision tree algorithm C4.5 based on arff files.

Authors: Steelers(Team 1)
  Dai Wei(wdai1@andrew.cmu.edu)
  Gang Wu(gwu1@andrew.cmu.edu)
  Huimin Zhao(huiminz@andrew.cmu.edu)
  Ningxin Fan(ningxinf@andrew.cmu.edu)
  Tian Zheng(tzheng1@andrew.cmu.edu)

Project structure
  Attribute.java: Store type, value set of attributes.
  Data.java: Store one entry of data in arff file.
  DataSet.java: Parse arff files.
  DecisionTree.java: Generate decision tree, print tree, make decision for data.
  EntropyCalculator.java: Calculate entropy & conditional entropy for both discrete and continuous data.
  Main.java: Script file for inputing data and output result.

Usage
  Main.java "filename.arff" [fold for cross validation, default = 5, optional]
  example:
  1) Main.java trainProdIntro.binary.arff
     Console output: Cross Validation Result : 94.29%
  2) Main.java trainProdIntro.binary.arff 10
     Console output: Cross Validation Result : 100.00%

Video: https://youtu.be/sYrKQpYEpBo