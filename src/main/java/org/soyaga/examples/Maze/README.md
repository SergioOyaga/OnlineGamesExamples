# Maze
For this https://www.mathsisfun.com/measure/mazes.html maze problem, we use an ACO to solve a maze represented as a graph.
In the [ACO-representation](#aco-representation) we can see the graph design.

<img src="https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/out/Maze/Maze.gif"  title="Maze example" alt="Maze example"/>

## ACO Representation:
The graph looks like:
````mermaid
block-beta
    columns 5

    Node1(("&emsp;Start &emsp;")) space Node2(("&emsp;Node2&emsp;")) space Node3(("&emsp;Node3&emsp;"))
    space W1[" "]:4  
    Node4(("&emsp;Node4&emsp;")) space Node5(("&emsp;Node5&emsp;")) space Node6(("&emsp;Node6&emsp;"))
    space W2[" "]:3  space
    Node7(("&emsp;Node7&emsp;")) space Node8(("&emsp;Node8&emsp;")) W3 [" "] Node9(("&emsp; End&emsp;&emsp;"))

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
    style Node9 fill:#0000FF
    style W1 stroke-width:0px
    style W2 stroke-width:0px
    style W3 stroke-width:0px
    
    
````
The Ants build solutions by moving through the Graph edges. A solution is an Array of Nodes, that is a valid solution when:
1. Start and end Nodes are the start and finish points.

The solution looks like:
````mermaid
flowchart LR
  subgraph  ide1 [Solution]
    direction LR
        subgraph ide3[Node1]
            direction LR
            Row1
            Col1
        end
        subgraph ide4[NodeX]
            direction LR
            RowX
            ColX
        end
        subgraph ide5[NodeN]
            direction LR
            RowN
            ColN
        end
      ide3-->ide4-->ide5
    style ide1 fill:#0405
    style ide3 fill:#4005
    style ide4 fill:#4005
    style ide5 fill:#4005
  end
````

## In this folder:
This folder contains one class and a packages that define the structures required for solving the problem.

1. [MazeScraper](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Maze/MazeScraper.java): Web Scrapper using Selenium and flow controller.
    - Scrapes the necessary information from LinkedIn-web application.
    - Manages the flow of the program.
    - Instantiates the ZipMathModel and ZipACO.
    - Introduces the solution in the web app.
2. [MazeACO](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Maze/ACO/MazeACO.java): Class that implements the required OptimizationLib interface and represents a Ant Colony Optimization program.
    - Instantiates all its components.
    - Implements runnable.
3. [MazeObjectiveFunction](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Maze/ACO/Evaluable/MazeObjectiveFunction.java): Class that evaluates the distance to find the solution.
4. [MazeNode](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Maze/ACO/Elements/MazeNode.java): Auxiliary class to store the node position and compute node to node distance.


## Results
For the ACO, the set of parameters used are in the MazeACO file.

The output looks like:
`````
Loading https://www.mathsisfun.com/measure/mazes.html: ...
Loaded.
Accepting cookies...
Cookies accepted.
Switching to Iframe...
Iframe switched.
Retrieving Maze...
    Selecting difficulty...
    Difficulty selected.
    Clicking ?...
    ? clicked .
Maze retrieved
Screenshot the Maze...
Maze screenshot.
Transforming image...
    Cropping image...
Image transformed.
Computing World...
World computed.
Building ACO...
ACO built.
Running ACO...
---------------------------------------------------------------------------------------------------------------------------------
| Iteration | CurrentMin | CurrentMax | HistoricalMin | MeanFitness | StandardDevFitness | MeanPheromone | StandardDevPheromone |
---------------------------------------------------------------------------------------------------------------------------------
| 1         | 0.00       | 29.15      | 0.00          | 11.81       | 4.30               | 0.83          | 0.20                 |
ACO run.
Gathering result...
Result gathered.
Optimizing the result...
Result optimized.
Introducing solution...
Optimal solution introduced.
    Clicking ?...
    ? clicked .
Restarting.
Restarted.
`````
As you see in the output example, the ACO finds the maze solution on the first iteration.

## Comment:
This problem is just a guide that the user can follow to build its own problems.

In this example, we didn't conduct an extensive optimization of the variables and constraints created. Nor the ACO parameters.
Nevertheless, we were able to get in a pretty sort time the solutions for the scenario. :smirk_cat:
