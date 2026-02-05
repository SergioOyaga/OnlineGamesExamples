# SlitherLink
For this https://www.puzzle-loop.com maze problem, we use an ACO to solve a path represented as a graph.
In the [ACO-representation](#aco-representation) we can see the graph design.

<img src="https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/out/SlitherLink/SlitherLink.gif"  title="SlitherLink example" alt="SlitherLink example"/>

## ACO Representation:
The graph looks like:
````mermaid
block-beta
    columns 7

    Node1(("&emsp;")) space Node2(("&emsp;")) space Node3(("&emsp;")) space Node4(("&emsp;"))
    space N1["3"] space:3 N2["1"] space
    Node5(("&emsp;")) space Node6(("&emsp;")) space Node7(("&emsp;")) space Node8(("&emsp;"))
    space:3 N3["3"] space:3
    Node9(("&emsp;")) space Node10(("&emsp;")) space Node11(("&emsp;")) space Node12(("&emsp;"))
    space N4["0"] space:3 N5["&ensp;0&ensp;"] space
    Node13(("&emsp;")) space Node14(("&emsp;")) space Node15(("&emsp;")) space Node16(("&emsp;"))

    Node1 --- Node2
    Node2 --- Node3
    Node3 --- Node4
    
    Node1 --- Node5
    Node2 --- Node6
    Node3 --- Node7
    Node4 --- Node8

    Node5 --- Node6
    Node6 --- Node7
    Node7 --- Node8

    Node5 --- Node9
    Node6 --- Node10
    Node7 --- Node11
    Node8 --- Node12

    Node10 --- Node11
    Node14 --- Node15
    
````
The Ants build solutions by moving through the Graph edges. A solution is an Array of Edges, that is a valid solution when:
1. Start and end Nodes are the same.
2. The number of edges utilized surrounding a value matches the actual value.

The solution looks like:
````mermaid
flowchart LR
  subgraph  ide1 [Solution]
    direction LR
        subgraph ide3[Edge1]
            direction LR
            N1["NodeOrigin1"]
            N2["NodeDest2"]
        end
        subgraph ide4[EdgeX]
            direction LR
            N3["NodeOriginX"]
            N4["NodeDestY"]
        end
        subgraph ide5[EdgeN]
            direction LR
            N5["NodeOriginN"]
            N6["NodeDest1"]
        end
      ide3-->ide4-->ide5
    style ide1 fill:#0405
    style ide3 fill:#4005
    style ide4 fill:#4005
    style ide5 fill:#4005
  end
````

## In this folder:
This folder contains one class and a package that define the structures required for solving the problem.

1. [SlitherLinkScraper](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/SlitherLink/SlitherLinkScraper.java): Web Scrapper using Selenium and flow controller.
    - Scrapes the necessary information from slither link web application.
    - Manages the flow of the program.
    - Instantiates the SlitherLinkACO.
    - Introduces the solution in the web app.
2. [SlitherLinkACO](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/SlitherLink/ACO/SlitherLinkACO.java): Class that implements the required OptimizationLib interface and represents a Ant Colony Optimization program.
    - Instantiates all its components.
    - Implements runnable.
3. [DistanceObjectiveFunction](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/SlitherLink/ACO/Evaluable/DistanceObjectiveFunction.java): Class that evaluates how much of the grid it has been covered.
4. [Constraints](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/SlitherLink/ACO/Evaluable/Constraints/): Package that contains the constraints.
   - StartEndConstraint: Constraint for the route to be circular.
   - SlitherLinkConstraint: Constraint for each of the numbers in the grid.
5. [SlitherLinkAnt](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/SlitherLink/ACO/Ant/SlitherLinkAnt.java): Class that implements a memory-back home ant that does not add any link when the ant gets stuck.
6. [TargetFeasibilityCriteriaPolicy](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/SlitherLink/ACO/StoppingPolicy/TargetFeasibilityCriteriaPolicy.java): Stopping policy base solely on feasibility.
7. [ImageStat](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/SlitherLink/ACO/Stats/ImageStat.java): Sat tha plots the pheromone container.


## Results
For the ACO, the set of parameters used are in the SlitherLinkACO file.

The output looks like:
`````
Loading https://www.puzzle-loop.com/: ...
Loaded.
Refreshing in case it did not load...
Refreshed.
Scrolling...
Scrolled.
Retrieving grid size...
Grid size retrieved.
Retrieving grid...
Grid retrieved.
Computing World...
World computed.
Creating stat image...
Stat image created.
Building ACO Model...
ACO Model built.
Running ACO Model...
----------------------------------------------------------------------------------------------------------------------------------------------
| Iteration | CurrentMin | CurrentMax | HistoricalMin | MeanFitness | StandardDevFitness | MeanPheromone | StandardDevPheromone | ImageSaved |
----------------------------------------------------------------------------------------------------------------------------------------------
| 1         | 15010.00   | 128030.00  | 15010.00      | 60913.94    | 46238.80           | 0.99          | 0.00                 | true       |
| 2         | 11012.00   | 129032.00  | 11012.00      | 53722.06    | 43541.52           | 0.99          | 0.01                 | true       |
| 3         | 13022.00   | 128030.00  | 11012.00      | 67665.67    | 46873.61           | 0.98          | 0.01                 | true       |
| 4         | 17014.00   | 127025.00  | 11012.00      | 72024.64    | 46665.29           | 0.97          | 0.02                 | true       |
| 5         | 12012.00   | 129031.00  | 11012.00      | 51610.72    | 41807.12           | 0.96          | 0.02                 | true       |
| 6         | 11018.00   | 128032.00  | 11012.00      | 67332.00    | 47639.67           | 0.96          | 0.02                 | true       |
| 7         | 17020.00   | 128029.00  | 11012.00      | 82468.25    | 46325.88           | 0.95          | 0.03                 | true       |
| 8         | 14012.00   | 128030.00  | 11012.00      | 59722.14    | 45255.71           | 0.94          | 0.03                 | true       |
| 9         | 11014.00   | 129032.00  | 11012.00      | 55692.64    | 45128.76           | 0.93          | 0.03                 | true       |
| 10        | 21022.00   | 127030.00  | 11012.00      | 59832.33    | 43125.02           | 0.93          | 0.04                 | true       |
| 11        | 6006.00    | 129032.00  | 6006.00       | 68663.97    | 47425.12           | 0.93          | 0.04                 | true       |
| 12        | 12010.00   | 129030.00  | 6006.00       | 57276.97    | 43930.78           | 0.93          | 0.05                 | true       |
| 13        | 12014.00   | 129032.00  | 6006.00       | 75831.89    | 49070.34           | 0.93          | 0.06                 | true       |
| 14        | 23024.00   | 129032.00  | 6006.00       | 68638.36    | 45822.64           | 0.92          | 0.06                 | true       |
| 15        | 5004.00    | 127031.00  | 5004.00       | 56856.67    | 46446.70           | 0.92          | 0.06                 | true       |
...
...
| 130       | 12018.00   | 128029.00  | 5004.00       | 58998.72    | 45538.55           | 0.49          | 0.32                 | true       |
| 131       | 4006.00    | 127025.00  | 4006.00       | 49886.31    | 41954.84           | 0.50          | 0.32                 | true       |
...
...
| 146       | 11010.00   | 129032.00  | 4006.00       | 70526.78    | 48109.30           | 0.53          | 0.35                 | true       |
| 147       | 4004.00    | 129032.00  | 4004.00       | 66718.53    | 46713.66           | 0.53          | 0.36                 | true       |
...
...
| 230       | 11008.00   | 129031.00  | 4004.00       | 56609.42    | 45522.64           | 0.45          | 0.41                 | true       |
| 231       | 3008.00    | 128031.00  | 3008.00       | 43218.14    | 41202.84           | 0.46          | 0.42                 | true       |
...
...
| 260       | 6012.00    | 129029.00  | 3008.00       | 63108.00    | 45929.86           | 0.41          | 0.40                 | true       |
| 261       | 3006.00    | 129028.00  | 3006.00       | 65024.14    | 50291.21           | 0.41          | 0.40                 | true       |
| 262       | 5010.00    | 128029.00  | 3006.00       | 50522.28    | 43606.50           | 0.41          | 0.41                 | true       |
| 263       | 3006.00    | 129030.00  | 3006.00       | 62579.25    | 47306.08           | 0.41          | 0.41                 | true       |
| 264       | 5010.00    | 127028.00  | 3006.00       | 55744.58    | 47219.78           | 0.41          | 0.41                 | true       |
| 265       | 3006.00    | 125028.00  | 3006.00       | 59023.61    | 45284.51           | 0.41          | 0.41                 | true       |
| 266       | 1002.00    | 126029.00  | 1002.00       | 62218.39    | 47120.75           | 0.44          | 0.43                 | true       |
...
...
| 330       | 6008.00    | 128028.00  | 1002.00       | 58386.14    | 45121.49           | 0.40          | 0.44                 | true       |
| 331       | 2.00       | 128029.00  | 2.00          | 27746.89    | 23842.84           | 0.40          | 0.44                 | true       |
ACO Model run.
Gathering result...
Result gathered.
Introducing solution...
Optimal solution introduced.
Continuing game...
Game continued.
`````
As you see in the output example, the ACO finds the slither link solution pretty fast.

## Comment:
This problem is just a guide that the user can follow to build its own problems.

In this example, we didn't conduct an extensive optimization of the variables and constraints created. Nor the ACO parameters.
Nevertheless, we were able to get in a pretty sort time the solutions for the scenario. :smirk_cat:
