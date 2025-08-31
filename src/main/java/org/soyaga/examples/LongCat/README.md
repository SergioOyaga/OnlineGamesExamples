# LongCat
For this https://poki.com/en/g/longcat LongCat problem, we use an GA to make decisions on a board represented as a graph.
In the [GA-representation](#GA-representation) we can see the decisions.

<img src="https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/out/LongCat/LongCat.gif"  title="LongCat example" alt="LongCat example"/>

## GA Representation:
The graph looks like:
````mermaid
block-beta
    columns 5

    Node1["<br>&emsp;Cat &emsp;<br>&emsp;"] space Node2["<br>&emsp;Node2&emsp;<br>&emsp;"] space Node3["<br>&emsp;Node3&emsp;<br>&emsp;"]
    space W1[" "] space:3  
    Node4["<br>&emsp;Node4&emsp;<br>&emsp;"] space Node5["<br>&emsp;Node5&emsp;<br>&emsp;"] space Node6["<br>&emsp;Node6&emsp;<br>&emsp;"]
    space W2[" "] space W3[" "]  space
    Node7["<br>&emsp;Node7&emsp;<br>&emsp;"] space Node8["<br>&emsp;Node8&emsp;<br>&emsp;"] W4 [" "] Node9["<br>&emsp; Node9&emsp;<br>&emsp;"]

    Node1 --- Node2
    Node2 --- Node1
    
    Node1 --- Node4
    Node4 --- Node1
    
    Node2 --- Node3
    Node3 --- Node2
    
    Node4 --- Node5
    Node5 --- Node4
    
    Node4 --- Node7
    Node7 --- Node4
    
    Node5 --- Node6
    Node6 --- Node5
    
    
    Node6 --- Node9
    Node9 --- Node6
    
    Node7 --- Node8
    Node8 --- Node7
    
    
    style Node1 fill:#FFD700,color:#000000
    style W1 fill:#0000FF, stroke-width:0px
    style W2 fill:#0000FF,stroke-width:0px
    style W3 fill:#0000FF,stroke-width:0px
    style W4 fill:#0000FF,stroke-width:0px
    
    
````
The GA build solutions by moving through the Graph edges following the problem rules. 
A solution is an Array of decisions, that is a valid solution when:
1. By starting in the initial Node, all Nodes must be visited.

The solution looks like:
````mermaid
flowchart LR
  subgraph  ide1 [Solution]
    direction LR
        subgraph ide2[Decision1]
            one[South]
        end
        subgraph ide3[Decision2]
            two[West]
        end
        subgraph ide4[Decision3]
          three[North]
        end
        subgraph ide5[Decision4]
          four[West]
        end
        subgraph ide6[Decision5]
            five[South]
        end
      ide2-->ide3-->ide4-->ide5-->ide6
  end
````

## In this folder:
This folder contains one class and a packages that define the structures required for solving the problem.

1. [LongCatScraper](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/LongCat/LongCatScraper.java): Web Scrapper using Selenium and flow controller.
    - Scrapes the necessary information from LongCat-web application.
    - Manages the flow of the program.
    - Instantiates the LongCatGA.
    - Introduces the solution in the web app.
2. [LongCatGA](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/LongCat/GA/LongCatGA.java): Class that implements the required OptimizationLib interface and represents a Genetic Algorithm Optimization program.
    - Instantiates all its components.
3. [LongCatObjectiveFunction](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/LongCat/GA/Evaluable/LongCatObjectiveFunction.java): Class that evaluates the distance to find the solution.
4. [LongCatFeasibilityFunction](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/LongCat/GA/Evaluable/LongCatFeasibilityFunction.java): Class that evaluates whether the proposed solution is an actual solution.
5. [LongCatInitializer](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/LongCat/GA/Initializer/LongCatInitializer.java): Class that initializes an individual. The initialization is done following the game rules.
6. [TargetFeasibilityCriteriaPolicy](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/LongCat/GA/StoppingPolicy/TargetFeasibilityCriteriaPolicy.java): Stopping policy that checks only the feasibility to reach a target value.
7. [Node](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/LongCat/Graph/Node.java): Auxiliary class to store the node position.
8. [Graph](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/LongCat/Graph/Graph.java): Auxiliary class to store all nodes. Computes available nodes from a current node, and perform movement on the graph following game rules.


## Results
For the GA, the set of parameters used are in the LongCatGA file.

The output looks like:
`````
Loading https://poki.com/en/g/longcat: ...
Loaded.
Refreshing in case it did not load...
Refreshed.
Accepting cookies...
Cookies accepted.
Centering and clicking...
Centered and clicked.
Screenshot the Game...
Game screenshot.
Processing image...
    Applying patches...
    Patches applied.
    Cropping image...
    Image Crop.
    Computing size...
    Size computed.
    Replacing cat colors...
    Cat colors replaced.
Image processed.
Computing cell states...
Cell states computed.
Building GAModel...
GAModel built.
Running MathModel...
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
| Iteration | CurrentMin | CurrentMax | HistoricalMin | HistoricalMax | MeanFitness | StandardDev | P0    | P25   | P50   | P75   | P100  | StepGradient(u/iter) | TimeGradient(u/s) | ElapsedTime(s) |
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
| 0         | 5.0000     | 5.0000     | 5.0000        | 5.0000        | 5.0000      | 0.0000      | 5.000 | 5.000 | 5.000 | 5.000 | 5.000 |                      |                   | 0.0066         |
MathModel run.
Gathering result...
Result gathered.
Introducing solution...
Optimal solution introduced.
Continuing game...
Game continued.
Screenshot the Game...
Game screenshot.
Processing image...
    Applying patches...
    Patches applied.
    Cropping image...
    Image Crop.
    Computing size...
    Size computed.
    Replacing cat colors...
    Cat colors replaced.
Image processed.
Computing cell states...
Cell states computed.
Building GAModel...
GAModel built.
Running MathModel...
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
| Iteration | CurrentMin | CurrentMax | HistoricalMin | HistoricalMax | MeanFitness | StandardDev | P0    | P25   | P50   | P75   | P100  | StepGradient(u/iter) | TimeGradient(u/s) | ElapsedTime(s) |
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
| 0         | 7.0000     | 45.0000    | 7.0000        | 45.0000       | 18.4000     | 15.2000     | 7.000 | 7.000 | 7.000 | 26.00 | 45.00 |                      |                   | 0.0019         |
MathModel run.
Gathering result...
Result gathered.
Introducing solution...
Optimal solution introduced.
Continuing game...
Game continued.
Screenshot the Game...
Game screenshot.
Processing image...
    Applying patches...
    Patches applied.
    Cropping image...
    Image Crop.
    Computing size...
    Size computed.
    Replacing cat colors...
    Cat colors replaced.
Image processed.
Computing cell states...
Cell states computed.
Building GAModel...
GAModel built.
Running MathModel...
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
| Iteration | CurrentMin | CurrentMax | HistoricalMin | HistoricalMax | MeanFitness | StandardDev | P0    | P25   | P50   | P75   | P100  | StepGradient(u/iter) | TimeGradient(u/s) | ElapsedTime(s) |
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
| 0         | 5.0000     | 16.0000    | 5.0000        | 16.0000       | 11.7000     | 4.6915      | 5.000 | 6.250 | 15.00 | 15.75 | 16.00 |                      |                   | 0.0010         |
MathModel run.
Gathering result...
Result gathered.
Introducing solution...
Optimal solution introduced.
Continuing game...
Game continued.
`````
As you see in the output example, the GA finds the LongCat solution on the first iteration. The reason is that the initial levels of the game are pretty simple. However, we have tested the solver up to lovel 50, an any level solving time took more than a second.

Most of the time is spent waiting for the board to load on the web server. We need to wait until the level indicator disappears from the screen, around 4 seconds. For more difficult levels that pop-up overlays the board.

## Comment:
This problem is just a guide that the user can follow to build its own problems.

In this example, we didn't conduct an extensive optimization of the variables and constraints created. Nor the GA parameters.
Nevertheless, we were able to get in a pretty sort time the solutions for the scenario. :smirk_cat:
