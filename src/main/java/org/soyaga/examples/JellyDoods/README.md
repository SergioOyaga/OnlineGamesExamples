# JellyDoods
For this https://www.mathplayground.com/logic_jelly_doods.html jelly-doods problem, we use a GA to solve a decision sequence represented as a genome.
In the [GA-representation](#ga-representation) we can see the genome representation.

<img src="https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/out/JellyDoods/JellyDoods.gif"  title="JellyDoods example" alt="JellyDoods example"/>

## Problem Graph
The problem is represented as a board and pieces.
The problem consists in joining all jellies of the same color in one before we run out of turns. When you move a jelly, it does it all the way in that direction until it hits other piece. If that piece is other jelly of the same color, the two jellies merge into one.
### Board
````mermaid
block-beta
    columns 1
    block:group1:1
        columns 4
        Node1["<br>&emsp;(0,0) &emsp; <br> &emsp;"]  Node2["<br>&emsp;(0,1) &emsp; <br> &emsp;"]  Node3["<br>&emsp;(0,2) &emsp; <br> &emsp;"]Node4["<br> &emsp;(0,3) &emsp; <br> &emsp;"]  
        Node5["<br> &emsp;(1,0) &emsp; <br> &emsp;"] Node6["<br>&emsp;(1,1) &emsp; <br> &emsp;"]  Node7["<br>&emsp;(1,2) &emsp; <br> &emsp;"]  Node8["<br>&emsp;(1,3) &emsp; <br> &emsp;"]
        Node9["<br> &emsp;(2,0) &emsp; <br> &emsp;"] Node10["<br> &emsp;(2,1) &emsp; <br> &emsp;"] Node11["<br>&emsp;(2,2) &emsp; <br> &emsp;"] Node12["<br>&emsp;(2,3) &emsp; <br> &emsp;"]    
        Node13["<br>&emsp;(3,0) &emsp; <br> &emsp;"]  Node14["<br> &emsp;(3,1) &emsp; <br> &emsp;"]  Node15["<br> &emsp;(3,2) &emsp; <br> &emsp;"] Node16["<br> &emsp;(3,3) &emsp; <br> &emsp;"]
    end

    classDef red fill:#ff0000,stroke:#333,stroke-width:4px;
    classDef green fill:#00ff00,stroke:#333,stroke-width:4px;
    classDef blue fill:#0000ff,stroke:#333,stroke-width:4px;
    classDef white fill:#ffffff,stroke:#333,stroke-width:4px;
````
The board will keep track of what piece is present in each cell.

### Pieces
````mermaid
block-beta

    columns 1
    block:group1:1
        columns 4
        Node1["<br> P1 <br> &emsp;"]:2   space:2 
        space Node6["<br> P2 <br> &emsp;"]  Node7["<br> Wall<br> &emsp;"] space
        space Node10["<br>Wall <br> &emsp;"] Node11["<br> P3<br> &emsp;"] space    
        space  Node15["<br> P4<br> &emsp;"]:2 space
    end

    classDef red fill:#ff0000,stroke:#333,stroke-width:4px;
    classDef green fill:#00ff00,stroke:#333,stroke-width:4px;
    classDef blue fill:#0000ff,stroke:#333,stroke-width:4px;
    classDef grey fill:#888888,stroke:#333,stroke-width:4px;
    
    class Node1 red
    class Node6 blue
    class Node7 grey
    class Node10 grey
    class Node11 red
    class Node15 blue
```` 
When two pieces of the same type (color) collide, the one that is moving will incorporate the one that is still. After the fusion wi will have that the moving piece incorporated all cells from the collided piece. The collided thus disappeared from the board.

## GA Representation:
The genome looks like:
````mermaid
flowchart LR
  subgraph  ide1 [Genome]
    direction LR
        subgraph ide2[Decision1]
            direction LR
            P1[P3]
            M1[West]
        end
        subgraph ide3[Decision2]
            direction LR
            P2[P3]
            M2[North]
        end
        subgraph ide4[Decision3]
            direction LR
            P3[P3]
            M3[West]
        end
      subgraph ide5[Decision4]
          direction LR
          P4[P2]
          M4[West]
      end
      subgraph ide6[Decision5]
          direction LR
          P5[P2]
          M5[South]
      end
      subgraph ide7[Decision6]
          direction LR
          P6[P2]
          M6[East]
      end
      ide2-->ide3-->ide4-->ide5-->ide6-->ide7
      style ide1 fill:#0405
      style ide2 fill:#4005
      style ide3 fill:#4005
      style ide4 fill:#4005
      style ide5 fill:#4005
      style ide6 fill:#4005
      style ide7 fill:#4005
  end
````
Using this representation, we decide which piece we move in which direction.

````mermaid
block-beta
    columns 1
    block:group1:1
        columns 4
        Node1["<br>P1<br>&nbsp;"]:2 space left<[" "]>(left)
        left2<[" "]>(left) Node6["<br>P2<br>&nbsp;"]  Node7["<br>Wall<br>&nbsp;"] up<[" "]>(up)
        down<[" "]>(down) Node10["<br>Wall<br>&nbsp;"] Node11["<br>P3<br>&nbsp;"] right<[" "]>(right)
        right2<[" "]>(right)  Node15["<br>P4<br>&nbsp;"]:2 space
    end

    classDef red fill:#ff0000,stroke:#333,stroke-width:4px;
    classDef green fill:#00ff00,stroke:#333,stroke-width:4px;
    classDef blue fill:#0000ff,stroke:#333,stroke-width:4px;
    classDef grey fill:#888888,stroke:#333,stroke-width:4px;
    
    class Node1 red
    class Node6 blue
    class Node7 grey
    class Node10 grey
    class Node11 red
    class Node15 blue
```` 
Which leaves the  desired final state:
````mermaid
block-beta
    columns 1
    block:group1:1
        columns 4
        Node1["<br>P3<br>&nbsp;"]:3 space 
        space:2  Node7["<br>W<br>&nbsp;"] space
       space Node10["<br>W<br>&nbsp;"] space:2
        Node15["<br>P2<br>&nbsp;"]:3 space
    end

    classDef red fill:#ff0000,stroke:#333,stroke-width:4px;
    classDef green fill:#00ff00,stroke:#333,stroke-width:4px;
    classDef blue fill:#0000ff,stroke:#333,stroke-width:4px;
    classDef grey fill:#888888,stroke:#333,stroke-width:4px;
    
    class Node1 red
    class Node6 blue
    class Node7 grey
    class Node10 grey
    class Node11 red
    class Node15 blue
```` 

## In this folder:
This folder contains one class and a packages that define the structures required for solving the problem.

1. [JellyDoodsScraper](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/JellyDoods/JellyDoodsScraper.java): Web Scrapper using Selenium and flow controller.
    - Scrapes the necessary information from jelly-doods-web application.
    - Manages the flow of the program.
    - Instantiates the JellyDoodsGA.
    - Introduces the solution in the web app.
2. [JellyDoodsGA](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/JellyDoods/GA/JellyDoodsGA.java): Class that implements the required OptimizationLib interface and represents a Genetic Algorithm Optimization program.
    - Instantiates all its components.
3. [JellyDoodsObjectiveFunction](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/JellyDoods/GA/Evaluable/JellyDoodsObjectiveFunction.java): Class that evaluates how good (short) the solution is.
4. [JellyDoodsFeasibilityFunction](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/JellyDoods/GA/Evaluable/JellyDoodsFeasibilityFunction.java): Class that evaluates whether the solution found is a real solution to the problem.
5. [JellyDoodsInitializer](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/JellyDoods/GA/Initializer/JellyDoodsInitializer.java): Class that initializes an individual.
6. [RandomDirectionMutation](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/JellyDoods/GA/Mutations/RandomDirectionMutation.java): Class that mutates a genome by randomly changing the direction of a moving piece.
7. [TargetFeasibilityCriteriaPolicy](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/JellyDoods/GA/StoppingPolicy/TargetFeasibilityCriteriaPolicy.java): Class that stops the optimization when the fitness reaches a certain value.
8. [Board](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/JellyDoods/Board): Folder containing a board representation of the problem.
   - Board: Composed of Cells and Pieces contains utility functions to retrieve move pieces around, and gather the movement possibilities of each piece.
   - Cell: contains information of which Piece is contained in each board position.
   - Piece: movable or fixed element on the board. Can be merged with other pieces in some circumstances.

## Results
For the GA, the set of parameters used are in the JellyDoodsGA file.

The output looks like:
`````
Loading https://www.mathplayground.com/logic_jelly_doods.html: ...
Loaded.
Refreshing in case it did not load...
Refreshed.
Clicking play...
Play clicked.
Switching to iFrame...
iFrame switched.
Centering and clicking...
Centered and clicked.
Retrieving levels...
Levels retrieved.
Getting level info...
Level info get.
Building GAModel...
GAModel built.
Running MathModel...
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
| Iteration | CurrentMin | CurrentMax | HistoricalMin | HistoricalMax | MeanFitness | StandardDev | P0    | P25   | P50   | P75   | P100  | StepGradient(u/iter) | TimeGradient(u/s) | ElapsedTime(s) |
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
| 0         | 2.0000     | 22.0000    | 2.0000        | 22.0000       | 18.0000     | 6.6332      | 2.000 | 14.50 | 22.00 | 22.00 | 22.00 |                      |                   | 0.0150         |
MathModel run.
Gathering result...
Result gathered.
Introducing solution...
Optimal solution introduced.
Continuing game...
Game continued.
Clicking...
Clicked.
Getting level info...
Level info get.
Building GAModel...
GAModel built.
Running MathModel...
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
| Iteration | CurrentMin | CurrentMax | HistoricalMin | HistoricalMax | MeanFitness | StandardDev | P0    | P25   | P50   | P75   | P100  | StepGradient(u/iter) | TimeGradient(u/s) | ElapsedTime(s) |
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
| 0         | 30.0000    | 40.0000    | 30.0000       | 40.0000       | 37.0000     | 4.5826      | 30.00 | 32.50 | 40.00 | 40.00 | 40.00 |                      |                   | 0.0045         |
| 1         | 30.0000    | 40.0000    | 30.0000       | 40.0000       | 32.0000     | 4.0000      | 30.00 | 30.00 | 30.00 | 30.00 | 40.00 | 5.0000               | 3430.7436         | 0.0146         |
| 2         | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 31.0000     | 5.3852      | 20.00 | 30.00 | 30.00 | 30.00 | 40.00 | 1.0000               | 2843.2515         | 0.0035         |
| 3         | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 30.0000     | 8.9443      | 20.00 | 20.00 | 30.00 | 40.00 | 40.00 | 1.0000               | 3329.2273         | 0.0030         |
| 4         | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 28.0000     | 8.7178      | 20.00 | 20.00 | 25.00 | 37.50 | 40.00 | 2.0000               | 6670.2241         | 0.0030         |
| 5         | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 29.0000     | 8.3066      | 20.00 | 20.00 | 30.00 | 37.50 | 40.00 | -1.0000              | -2841.7164        | 0.0035         |
| 6         | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 28.0000     | 8.7178      | 20.00 | 20.00 | 25.00 | 37.50 | 40.00 | 1.0000               | 2494.0144         | 0.0040         |
| 7         | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 31.0000     | 8.3066      | 20.00 | 22.50 | 30.00 | 40.00 | 40.00 | -3.0000              | -9997.0009        | 0.0030         |
| 8         | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 29.0000     | 8.3066      | 20.00 | 20.00 | 30.00 | 37.50 | 40.00 | 2.0000               | 10016.5273        | 0.0020         |
| 9         | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 31.0000     | 8.3066      | 20.00 | 22.50 | 30.00 | 40.00 | 40.00 | -2.0000              | -6216.5859        | 0.0032         |
| 10        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 29.0000     | 9.4340      | 20.00 | 20.00 | 25.00 | 40.00 | 40.00 | 2.0000               | 6260.7607         | 0.0032         |
| 11        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 26.0000     | 6.6332      | 20.00 | 20.00 | 25.00 | 30.00 | 40.00 | 3.0000               | 15000.0000        | 0.0020         |
| 12        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 28.0000     | 7.4833      | 20.00 | 20.00 | 30.00 | 30.00 | 40.00 | -2.0000              | -7920.4784        | 0.0025         |
| 13        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 27.0000     | 7.8102      | 20.00 | 20.00 | 25.00 | 30.00 | 40.00 | 1.0000               | 3340.9061         | 0.0030         |
| 14        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 31.0000     | 9.4340      | 20.00 | 20.00 | 35.00 | 40.00 | 40.00 | -4.0000              | -19947.1401       | 0.0020         |
| 15        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 27.0000     | 9.0000      | 20.00 | 20.00 | 20.00 | 37.50 | 40.00 | 4.0000               | 10014.7718        | 0.0040         |
| 16        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 28.0000     | 9.7980      | 20.00 | 20.00 | 20.00 | 40.00 | 40.00 | -1.0000              | -3970.6174        | 0.0025         |
| 17        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 25.0000     | 6.7082      | 20.00 | 20.00 | 20.00 | 30.00 | 40.00 | 3.0000               | 15002.2503        | 0.0020         |
| 18        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 27.0000     | 7.8102      | 20.00 | 20.00 | 25.00 | 30.00 | 40.00 | -2.0000              | -9985.0225        | 0.0020         |
| 19        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 28.0000     | 8.7178      | 20.00 | 20.00 | 25.00 | 37.50 | 40.00 | -1.0000              | -5005.5061        | 0.0020         |
| 20        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 27.0000     | 7.8102      | 20.00 | 20.00 | 25.00 | 30.00 | 40.00 | 1.0000               | 6618.1337         | 0.0015         |
| 21        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 28.0000     | 8.7178      | 20.00 | 20.00 | 25.00 | 37.50 | 40.00 | -1.0000              | -3318.8411        | 0.0030         |
| 22        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 28.0000     | 8.7178      | 20.00 | 20.00 | 25.00 | 37.50 | 40.00 | 0.0000               | 0.0000            | 0.0025         |
| 23        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 29.0000     | 9.4340      | 20.00 | 20.00 | 25.00 | 40.00 | 40.00 | -1.0000              | -3336.3360        | 0.0030         |
| 24        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 28.0000     | 9.7980      | 20.00 | 20.00 | 20.00 | 40.00 | 40.00 | 1.0000               | 3965.8933         | 0.0025         |
| 25        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 27.0000     | 9.0000      | 20.00 | 20.00 | 20.00 | 37.50 | 40.00 | 1.0000               | 4987.2824         | 0.0020         |
| 26        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 28.0000     | 8.7178      | 20.00 | 20.00 | 25.00 | 37.50 | 40.00 | -1.0000              | -9978.0483        | 0.0010         |
| 27        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 28.0000     | 7.4833      | 20.00 | 20.00 | 30.00 | 30.00 | 40.00 | 0.0000               | 0.0000            | 0.0020         |
| 28        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 28.0000     | 8.7178      | 20.00 | 20.00 | 25.00 | 37.50 | 40.00 | 0.0000               | 0.0000            | 0.0020         |
| 29        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 29.0000     | 8.3066      | 20.00 | 20.00 | 30.00 | 37.50 | 40.00 | -1.0000              | -9986.0196        | 0.0010         |
| 30        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 26.0000     | 8.0000      | 20.00 | 20.00 | 20.00 | 30.00 | 40.00 | 3.0000               | 9810.3336         | 0.0031         |
| 31        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 29.0000     | 8.3066      | 20.00 | 20.00 | 30.00 | 37.50 | 40.00 | -3.0000              | -14773.2309       | 0.0020         |
| 32        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 26.0000     | 6.6332      | 20.00 | 20.00 | 25.00 | 30.00 | 40.00 | 3.0000               | 15005.2518        | 0.0020         |
| 33        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 27.0000     | 9.0000      | 20.00 | 20.00 | 20.00 | 37.50 | 40.00 | -1.0000              | -10022.0485       | 0.0010         |
| 34        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 26.0000     | 6.6332      | 20.00 | 20.00 | 25.00 | 30.00 | 40.00 | 1.0000               | 3332.2226         | 0.0030         |
| 35        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 24.0000     | 6.6332      | 20.00 | 20.00 | 20.00 | 27.50 | 40.00 | 2.0000               | Infinity          | 0.0000         |
| 36        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 29.0000     | 9.4340      | 20.00 | 20.00 | 25.00 | 40.00 | 40.00 | -5.0000              | -20730.5444       | 0.0024         |
| 37        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 29.0000     | 8.3066      | 20.00 | 20.00 | 30.00 | 37.50 | 40.00 | 0.0000               | 0.0000            | 0.0020         |
| 38        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 29.0000     | 8.3066      | 20.00 | 20.00 | 30.00 | 37.50 | 40.00 | 0.0000               | 0.0000            | 0.0025         |
| 39        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 28.0000     | 9.7980      | 20.00 | 20.00 | 20.00 | 40.00 | 40.00 | 1.0000               | 3340.0134         | 0.0030         |
| 40        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 28.0000     | 7.4833      | 20.00 | 20.00 | 30.00 | 30.00 | 40.00 | 0.0000               | 0.0000            | 0.0021         |
| 41        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 27.0000     | 7.8102      | 20.00 | 20.00 | 25.00 | 30.00 | 40.00 | 1.0000               | 4984.7964         | 0.0020         |
| 42        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 26.0000     | 6.6332      | 20.00 | 20.00 | 25.00 | 30.00 | 40.00 | 1.0000               | 4999.0002         | 0.0020         |
| 43        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 27.0000     | 7.8102      | 20.00 | 20.00 | 25.00 | 30.00 | 40.00 | -1.0000              | -10015.0225       | 0.0010         |
| 44        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 27.0000     | 7.8102      | 20.00 | 20.00 | 25.00 | 30.00 | 40.00 | 0.0000               | 0.0000            | 0.0020         |
| 45        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 26.0000     | 6.6332      | 20.00 | 20.00 | 25.00 | 30.00 | 40.00 | 1.0000               | 4922.4711         | 0.0020         |
| 46        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 30.0000     | 7.7460      | 20.00 | 22.50 | 30.00 | 37.50 | 40.00 | -4.0000              | -19985.0112       | 0.0020         |
| 47        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 26.0000     | 6.6332      | 20.00 | 20.00 | 25.00 | 30.00 | 40.00 | 4.0000               | 19981.0180        | 0.0020         |
| 48        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 26.0000     | 8.0000      | 20.00 | 20.00 | 20.00 | 30.00 | 40.00 | 0.0000               | 0.0000            | 0.0020         |
| 49        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 29.0000     | 9.4340      | 20.00 | 20.00 | 25.00 | 40.00 | 40.00 | -3.0000              | -14963.3398       | 0.0020         |
| 50        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 30.0000     | 8.9443      | 20.00 | 20.00 | 30.00 | 40.00 | 40.00 | -1.0000              | -10054.2932       | 0.0010         |
| 51        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 27.0000     | 7.8102      | 20.00 | 20.00 | 25.00 | 30.00 | 40.00 | 3.0000               | 14791.4407        | 0.0020         |
| 52        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 30.0000     | 6.3246      | 20.00 | 30.00 | 30.00 | 30.00 | 40.00 | -3.0000              | -29946.0970       | 0.0020         |
| 53        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 30.0000     | 8.9443      | 20.00 | 20.00 | 30.00 | 40.00 | 40.00 | 0.0000               | 0.0000            | 0.0020         |
| 54        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 25.0000     | 8.0623      | 20.00 | 20.00 | 20.00 | 27.50 | 40.00 | 5.0000               | 16185.9441        | 0.0021         |
| 55        | 20.0000    | 40.0000    | 20.0000       | 40.0000       | 28.0000     | 8.7178      | 20.00 | 20.00 | 25.00 | 37.50 | 40.00 | -3.0000              | -15054.9506       | 0.0020         |
| 56        | 9.0000     | 40.0000    | 9.0000        | 40.0000       | 25.9000     | 9.3429      | 9.000 | 20.00 | 25.00 | 30.00 | 40.00 | 2.1000               | 10149.3403        | 0.0021         |
MathModel run.
Gathering result...
Result gathered.
Introducing solution...
Optimal solution introduced.
Continuing game...
Game continued.
`````
As you see in the output example, the GA finds the jelly-doods solution very fast.

## Comment:
This problem is just a guide that the user can follow to build its own problems.

In this example, we didn't conduct an extensive optimization of the variables and constraints created. Nor the GA parameters.
Nevertheless, we were able to get in a pretty sort time the solutions for the scenario. :smirk_cat:
