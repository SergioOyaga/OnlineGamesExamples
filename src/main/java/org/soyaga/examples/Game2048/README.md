# Game2048
For this https://www.2048.org/ 2048 game, we use a GA to get a decision sequence represented as a genome. Then we compute how suitable is to choose each direction based on the individual experiences.
In the [GA-representation](#ga-representation) we can see the genome representation.

<img src="https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/out/Game2048/Game2048.gif"  title="Game2048 example" alt="Game2048 example"/>

## 2048
The problem is represented as a board and pieces.
The problem consists in joining numbers to reach to 2048. When you choose a direction, all numbers in the board try to go through that direction. If Two numbers with the same value collide, they merge adding their value. When a direction is selected, the numbers "compact" in that direction. Every time a direction is selected, a new number [2 or 4], with a ratio 9-1, appears in a random free square.

### Board
````mermaid
block-beta
    columns 1
    block:group1:1
        columns 4
        Node1["<br>&emsp; &emsp; <br> &emsp;"]  Node2["<br>&emsp; 2&emsp; <br> &emsp;"]  Node3["<br>&emsp; &emsp; <br> &emsp;"]Node4["<br> &emsp; &emsp; <br> &emsp;"]  
        Node5["<br> &emsp;2 &emsp; <br> &emsp;"] Node6["<br>&emsp; &emsp; <br> &emsp;"]  Node7["<br>&emsp; &emsp; <br> &emsp;"]  Node8["<br>&emsp;2 &emsp; <br> &emsp;"]
        Node9["<br> &emsp;8 &emsp; <br> &emsp;"] Node10["<br> &emsp;8 &emsp; <br> &emsp;"] Node11["<br>&emsp;8 &emsp; <br> &emsp;"] Node12["<br>&emsp;8 &emsp; <br> &emsp;"]    
        Node13["<br>&emsp;32 &emsp; <br> &emsp;"]  Node14["<br> &emsp;16 &emsp; <br> &emsp;"]  Node15["<br> &emsp;16 &emsp; <br> &emsp;"] Node16["<br> &emsp;&emsp;32&emsp;&emsp; <br> &emsp;"]
    end

    classDef red fill:#ff0000,stroke:#333,stroke-width:4px;
    classDef green fill:#00ff00,stroke:#333,stroke-width:4px;
    classDef blue fill:#0000ff,stroke:#333,stroke-width:4px;
    classDef white fill:#ffffff,stroke:#333,stroke-width:4px;
````

### GA Representation:
The genome looks like:
````mermaid
flowchart LR
  subgraph  ide1 [Genome]
    direction LR
        subgraph ide2[Decision1]
            direction LR
            P1[BoardState]
            M1[West]
        end
        subgraph ide3[Decision2]
            direction LR
            P2[BoardState]
            M2[North]
        end
        subgraph ide4[Decision3]
            direction LR
            P3[BoardState]
            M3[South]
        end
      ide2-->ide3-->ide4
      style ide1 fill:#0405
      style ide2 fill:#4005
      style ide3 fill:#4005
      style ide4 fill:#4005
  end
````
Using this representation, we can measure how good can be the decisions made by the individual. This value is a stochastic value du to the uncertainty on where the new values will appear and what these values were.

For the initial board, the new boards states can look like:

Going East
````mermaid
block-beta
    columns 1
    block:group1:1
        columns 4
        Node1["<br>&emsp; &emsp; <br> &emsp;"]  Node2["<br>&emsp; &emsp; <br> &emsp;"]  Node3["<br>&emsp; &emsp; <br> &emsp;"]Node4["<br> &emsp;2 &emsp; <br> &emsp;"]
        Node5["<br> &emsp; &emsp; <br> &emsp;"] Node6["<br>&emsp; &emsp; <br> &emsp;"]  Node7["<br>&emsp; &emsp; <br> &emsp;"]  Node8["<br>&emsp;4 &emsp; <br> &emsp;"]
        Node9["<br> &emsp; &emsp; <br> &emsp;"] Node10["<br> &emsp; &emsp; <br> &emsp;"] Node11["<br>&emsp;16 &emsp; <br> &emsp;"] Node12["<br>&emsp;16 &emsp; <br> &emsp;"]
        Node13["<br>&emsp; 2&emsp; <br> &emsp;"]  Node14["<br> &emsp;32 &emsp; <br> &emsp;"]  Node15["<br> &emsp;32 &emsp; <br> &emsp;"] Node16["<br> &emsp;&emsp;32&emsp;&emsp; <br> &emsp;"]
    end

    classDef red fill:#ff0000,stroke:#333,stroke-width:4px;
    classDef green fill:#00ff00,stroke:#333,stroke-width:4px;
    classDef blue fill:#0000ff,stroke:#333,stroke-width:4px;
    classDef white fill:#ffffff,stroke:#333,stroke-width:4px;
````

Going North
````mermaid
block-beta
    columns 1
    block:group1:1
        columns 4
        Node1["<br>&emsp;2 &emsp; <br> &emsp;"]  Node2["<br>&emsp; 32&emsp; <br> &emsp;"]  Node3["<br>&emsp; 16&emsp; <br> &emsp;"]Node4["<br> &emsp;2 &emsp; <br> &emsp;"]
        Node5["<br> &emsp; &emsp; <br> &emsp;"] Node6["<br>&emsp; &emsp; <br> &emsp;"]  Node7["<br>&emsp;32 &emsp; <br> &emsp;"]  Node8["<br>&emsp;4 &emsp; <br> &emsp;"]
        Node9["<br> &emsp; 4&emsp; <br> &emsp;"] Node10["<br> &emsp; &emsp; <br> &emsp;"] Node11["<br>&emsp; &emsp; <br> &emsp;"] Node12["<br>&emsp;16 &emsp; <br> &emsp;"]
        Node13["<br>&emsp; &emsp; <br> &emsp;"]  Node14["<br> &emsp; &emsp; <br> &emsp;"]  Node15["<br> &emsp; &emsp; <br> &emsp;"] Node16["<br> &emsp;&emsp;32&emsp;&emsp; <br> &emsp;"]
    end

    classDef red fill:#ff0000,stroke:#333,stroke-width:4px;
    classDef green fill:#00ff00,stroke:#333,stroke-width:4px;
    classDef blue fill:#0000ff,stroke:#333,stroke-width:4px;
    classDef white fill:#ffffff,stroke:#333,stroke-width:4px;
````

Going South
````mermaid
block-beta
    columns 1
    block:group1:1
        columns 4
        Node1["<br>&emsp; &emsp; <br> &emsp;"]  Node2["<br>&emsp; 2&emsp; <br> &emsp;"]  Node3["<br>&emsp; &emsp; <br> &emsp;"]Node4["<br> &emsp;2 &emsp; <br> &emsp;"]
        Node5["<br> &emsp; &emsp; <br> &emsp;"] Node6["<br>&emsp; &emsp; <br> &emsp;"]  Node7["<br>&emsp; &emsp; <br> &emsp;"]  Node8["<br>&emsp;4 &emsp; <br> &emsp;"]
        Node9["<br> &emsp; 2&emsp; <br> &emsp;"] Node10["<br> &emsp; &emsp; <br> &emsp;"] Node11["<br>&emsp; 16&emsp; <br> &emsp;"] Node12["<br>&emsp;16 &emsp; <br> &emsp;"]
        Node13["<br>&emsp; 4&emsp; <br> &emsp;"]  Node14["<br> &emsp;32 &emsp; <br> &emsp;"]  Node15["<br> &emsp;32 &emsp; <br> &emsp;"] Node16["<br> &emsp;&emsp;32&emsp;&emsp; <br> &emsp;"]
    end

    classDef red fill:#ff0000,stroke:#333,stroke-width:4px;
    classDef green fill:#00ff00,stroke:#333,stroke-width:4px;
    classDef blue fill:#0000ff,stroke:#333,stroke-width:4px;
    classDef white fill:#ffffff,stroke:#333,stroke-width:4px;
````

### Decide the Movement:
To actually decide the movement, we compute how good in average the individuals are in ach direction. Both, the final decision and the decisions made by the individuals are guided by some simple rules:
- Avoid north movements (we want to keep our highest numbers in the bottom).
- Try to keep the biggest number in the board in one corner.

At the end of the day, we will have a population that has built several fictional solutions to the problem. Then we compute which direction is best and decide our actual movement based on that information.

The cycle repeats for each new movement. At the beginning, the decisions are not much conditioned by the current state of the board, but after some turns, the board becomes relevant and the game rules come to play.


## In this folder:
This folder contains one class and a packages that define the structures required for solving the problem.

1. [Game2048Scraper](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Game2048/Game2048Scraper.java): Web Scrapper using Selenium and flow controller.
    - Scrapes the necessary information from game-2048-web application.
    - Manages the flow of the program.
    - Instantiates the Game2048GA.
    - Introduces the solution in the web app.
2. [Game2048GA](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Game2048/GA/Game2048GA.java): Class that implements the required OptimizationLib interface and represents a Genetic Algorithm Optimization program.
    - Instantiates all its components.
3. [Game2048ObjectiveFunction](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Game2048/GA/Evaluable/Game2048ObjectiveFunction.java): Class that evaluates how good a solution is. Based on:
   - Genome length. We want to be able to keep doing movements.
   - How close to a "snake" configuration is the solution.
4. [Game2048Initializer](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Game2048/GA/Initializer/Game2048Initializer.java): Class that initializes an individual.
5. [Board](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Game2048/Board/Board.java): Class that represent any board status. Contains methods to get information about the current board status and the building rules.

## Results
For the GA, the set of parameters used are in the Game2048GA file.

The output looks like:
`````
Loading 2048: ...
2048 loaded.
Refreshing in case it did not load...
Refreshed.
Accepting cookies...
Cookies accepted.
Getting tile-container...
Centering and clicking...
Centered and clicked.
Starting game loop...
Retrieving grid...
Grid retrieved.
Creating GA...
GA created.
Optimizing GA...
GA optimized.
Introducing solution...
Solution introduced.
Retrieving grid...
Grid retrieved.
Checking canContinue...
CanContinue checked.
Creating GA...
GA created.
Optimizing GA...
GA optimized.
Introducing solution...
Solution introduced.
Retrieving grid...
Grid retrieved.
Checking canContinue...
CanContinue checked.
`````
As you see in the output example, the GAs are created every time the actual board changes.

## Comment:
This problem is just a guide that the user can follow to build its own problems.

In this example, we didn't conduct an extensive optimization of the variables and constraints created. Nor the GA parameters.
Nevertheless, we were able to get in a pretty sort time the solutions for the scenario. :smirk_cat:
