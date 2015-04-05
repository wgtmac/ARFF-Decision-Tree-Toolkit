<b>Description</b>
    Java implementation of decision tree algorithm C4.5 based on arff files.

<b>Authors: Steelers(Team 1)</b><br>
    Dai Wei(wdai1@andrew.cmu.edu)<br>
    Gang Wu(gwu1@andrew.cmu.edu)<br>
    Huimin Zhao(huiminz@andrew.cmu.edu)<br>
    Ningxin Fan(ningxinf@andrew.cmu.edu)<br>
    Tian Zheng(tzheng1@andrew.cmu.edu)<br>

<b>Project structure</b><br>
    Attribute.java: Store type, value set of attributes.<br>
    Data.java: Store one entry of data in arff file.<br>
    DataSet.java: Parse arff files.<br>
    DecisionTree.java: Generate decision tree, print tree, make decision for data.<br>
    EntropyCalculator.java: Calculate entropy & conditional entropy for both discrete and continuous data.<br>
    Main.java: Script file for inputing data and output result.<br>

<b>Usage</b><br>
    Main.java "filename.arff" [fold for cross validation, default = 5, optional]<br>
    example:<br>
    1) Main.java trainProdIntro.binary.arff<br>
      Console output: Cross Validation Result : 94.29%<br>
    2) Main.java trainProdIntro.binary.arff 10<br>
      Console output: Cross Validation Result : 100.00%<br>

<b>Video</b>: https://youtu.be/sYrKQpYEpBo<br>
