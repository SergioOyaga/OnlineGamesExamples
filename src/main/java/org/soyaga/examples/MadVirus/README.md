# MadVirus
For this https://www.mathsisfun.com/games/mad-virus.html mad virus problem, we use an GA to solve a decision sequence represented as a genome.
In the [GA-representation](#ga-representation) we can see the genome representation.

<img src="https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/out/MAdVirus/MaadVirus.gif"  title="MadVirus example" alt="MadVirus example"/>

## Problem Graph
The problem is represented as a connected graph. the game is a grid of hexagons filling a rectangular shape. Some hexagons are missing leaving spaces.
The problem consists on, starting from a node (virus starting point), select the next color to use (infect). The mission is to infect all nodes in the grid before we run out of turns. 
````mermaid
block-beta
    columns 10
    Node1{{"<br>&emsp;(0,0) &emsp; <br> &emsp;"}} space Node2{{"<br>&emsp;(0,2) &emsp; <br> &emsp;"}} space Node3{{"<br>&emsp;(0,4) &emsp; <br> &emsp;"}} space Node4{{"<br> &emsp;(0,6) &emsp; <br> &emsp;"}} space Node5{{"<br> &emsp;(0,8) &emsp; <br> &emsp;"}} space
    space Node6{{"<br>&emsp;(1,0) &emsp; <br> &emsp;"}} space Node7{{"<br>&emsp;(1,2) &emsp; <br> &emsp;"}} space Node8{{"<br>&emsp;(1,4) &emsp; <br> &emsp;"}} space space space Node10{{"<br> &emsp;(1,8) &emsp; <br> &emsp;"}}
    Node11{{"<br>&emsp;START &emsp; <br> &emsp;"}} space space space Node13{{"<br>&emsp;(2,4) &emsp; <br> &emsp;"}} space Node14{{"<br> &emsp;(2,6) &emsp; <br> &emsp;"}} space Node15{{"<br> &emsp;(2,8) &emsp; <br> &emsp;"}} space


    classDef red fill:#ff0000,stroke:#333,stroke-width:4px;
    classDef green fill:#00ff00,stroke:#333,stroke-width:4px;
    classDef blue fill:#0000ff,stroke:#333,stroke-width:4px;
    classDef white fill:#ffffff,stroke:#333,stroke-width:4px;
    
    class Node1 red
    class Node2 blue
    class Node3 red
    class Node4 green
    class Node5 red
    class Node6 blue
    class Node7 red
    class Node8 green
    class Node9 red
    class Node10 blue
    class Node11 white
    class Node12 red
    class Node13 blue
    class Node14 green
    class Node15 blue

````
## GA Representation:
The genome looks like:
````mermaid
flowchart LR
  subgraph  ide1 [Genome]
    direction LR
        subgraph ide3[Decision1]
            direction LR
            Color1
        end
        subgraph ide4[DecisionX]
            direction LR
            ColorX
        end
        subgraph ide5[DecisionN]
            direction LR
            ColorN
        end
      ide3-->ide4-->ide5
    style ide1 fill:#0405
    style ide3 fill:#4005
    style ide4 fill:#4005
    style ide5 fill:#4005
  end
````
Using this representation, we decide which color should the virus select each turn.


## In this folder:
This folder contains one class and a packages that define the structures required for solving the problem.

1. [MadVirusScraper](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/MadVirus/MadVirusScraper.java): Web Scrapper using Selenium and flow controller.
    - Scrapes the necessary information from mad_virus-web application.
    - Manages the flow of the program.
    - Instantiates the MadVirusGA.
    - Introduces the solution in the web app.
2. [MadVirusGA](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/MadVirus/GA/MadVirusGA.java): Class that implements the required OptimizationLib interface and represents a Genetic Algorithm Optimization program.
    - Instantiates all its components.
    - Implements runnable.
3. [MadVirusObjectiveFunction](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/MadVirus/GA/Evaluable/MadVirusObjectiveFunction.java): Class that evaluates how good (short) the solution is.
4. [MadVirusFeasibilityFunction](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/MadVirus/GA/Evaluable/MadVirusFeasibilityFunction.java): Class that evaluates whether the solution sound is a real solution of the problem.
5. [MadVirusInitializer](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/MadVirus/GA/Initializer/MadVirusInitializer.java): Class that initializes an individual.
6. [RandomColorMutation](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/MadVirus/GA/Mutations/RandomColorMutation.java): Class that mutates a genome by randomly changing the color of a selection.
7. [TargetFeasibilityCriteriaPolicy](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/MadVirus/GA/StoppingPolicy/TargetFeasibilityCriteriaPolicy.java): Class that stops the optimization when the fitness reaches a certain value.
8. [Graph](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/MadVirus/Graph): Folder containing a graph representation of the problem.
   - Graph: Composed of nodes contains utility functions to retrieve the frontier nodes of a specific node. 
   - Node: contains information of which nodes are connected to it and what is its own color.

## Results
For the GA, the set of parameters used are in the MadVirusGA file.

The output looks like:
`````
Loading https://www.mathsisfun.com/games/mad-virus.html: ...
Loaded.
Accepting cookies...
Cookies accepted.
Pressing play...
Play pressed.
Screenshot the Game...
Game screenshot.
Splitting the image...
    Buttons Split.
    Turns Split.
    Board Split.
Image split.
Processing images...
    Processing buttons...
    Replacing conflictive colors...
    Buttons processed.
    Processing turns...
    Loading OpenCV...
    OpenCV loaded.
    Converting to Grayscale...
    Grayscale converted.
    Scaling...
    Scaled.
    Converting to Black and White...
    Black and White converted.
    Converting to BuffIm...
    BuffIm converted.
    Preparing Tesseract for OCR...
    Tesseract prepared.
    Recognizing characters...
    Characters recognized.
    Turns processed.
    Processing board...
    Cropping...
    Crop.
    Computing Graph...
    Computing centers...
    Centers computed.
    Graph computed.
    Board processed.
Images processed.
Building GAModel...
GAModel built.
Running MathModel...
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
| Iteration | CurrentMin | CurrentMax | HistoricalMin | HistoricalMax | MeanFitness | StandardDev | P0    | P25   | P50   | P75   | P100  | StepGradient(u/iter) | TimeGradient(u/s) | ElapsedTime(s) |
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
| 0         | 13.0000    | 31.0000    | 13.0000       | 31.0000       | 18.0000     | 4.7117      | 13.00 | 15.25 | 17.00 | 18.75 | 31.00 |                      |                   | 0.1016         |
MathModel run.
Gathering result...
Result gathered.
Introducing solution...
Optimal solution introduced.
Continuing game...
    Clicking...
    Clicked.
    Selecting starting point...
    Starting point selected.
Game continued.
Screenshot the Game...
Game screenshot.
Splitting the image...
    Buttons Split.
    Turns Split.
    Board Split.
Image split.
Processing images...
    Processing buttons...
    Replacing conflictive colors...
    Buttons processed.
    Processing turns...
    Loading OpenCV...
    OpenCV loaded.
    Converting to Grayscale...
    Grayscale converted.
    Scaling...
    Scaled.
    Converting to Black and White...
    Black and White converted.
    Converting to BuffIm...
    BuffIm converted.
    Preparing Tesseract for OCR...
    Tesseract prepared.
    Recognizing characters...
    Characters recognized.
    Turns processed.
    Processing board...
    Cropping...
    Crop.
    Computing Graph...
    Computing centers...
    Centers computed.
    Graph computed.
    Board processed.
Images processed.
Building GAModel...
GAModel built.
Running MathModel...
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
| Iteration | CurrentMin | CurrentMax | HistoricalMin | HistoricalMax | MeanFitness | StandardDev | P0    | P25   | P50   | P75   | P100  | StepGradient(u/iter) | TimeGradient(u/s) | ElapsedTime(s) |
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
| 0         | 95.0000    | 1945.0000  | 95.0000       | 1945.0000     | 465.0000    | 523.9847    | 95.00 | 150.0 | 270.0 | 537.5 | 1945. |                      |                   | 0.0408         |
| 1         | 23.0000    | 825.0000   | 23.0000       | 1945.0000     | 189.8000    | 219.8439    | 23.00 | 95.00 | 110.0 | 150.0 | 825.0 | 275.2000             | 50850.2449        | 0.0541         |
MathModel run.
Gathering result...
Result gathered.
Introducing solution...
Optimal solution introduced.
Continuing game...
    Clicking...
    Clicked.
    Selecting starting point...
    Starting point selected.
Game continued.
Screenshot the Game...
Game screenshot.
Splitting the image...
    Buttons Split.
    Turns Split.
    Board Split.
Image split.
Processing images...
    Processing buttons...
    Replacing conflictive colors...
    Buttons processed.
    Processing turns...
    Loading OpenCV...
    OpenCV loaded.
    Converting to Grayscale...
    Grayscale converted.
    Scaling...
    Scaled.
    Converting to Black and White...
    Black and White converted.
    Converting to BuffIm...
    BuffIm converted.
    Preparing Tesseract for OCR...
    Tesseract prepared.
    Recognizing characters...
    Characters recognized.
    Turns processed.
    Processing board...
    Cropping...
    Crop.
    Computing Graph...
    Computing centers...
    Centers computed.
    Graph computed.
    Board processed.
Images processed.
Building GAModel...
GAModel built.
Running MathModel...
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
| Iteration | CurrentMin | CurrentMax | HistoricalMin | HistoricalMax | MeanFitness | StandardDev | P0    | P25   | P50   | P75   | P100  | StepGradient(u/iter) | TimeGradient(u/s) | ElapsedTime(s) |
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
| 0         | 81.0000    | 1391.0000  | 81.0000       | 1391.0000     | 461.0000    | 397.1901    | 81.00 | 171.0 | 261.0 | 713.5 | 1391. |                      |                   | 0.0787         |
| 1         | 81.0000    | 291.0000   | 81.0000       | 1391.0000     | 178.0000    | 97.3704     | 81.00 | 81.00 | 171.0 | 276.0 | 291.0 | 283.0000             | 39877.4087        | 0.0710         |
| 2         | 51.0000    | 511.0000   | 51.0000       | 1391.0000     | 142.0000    | 138.8128    | 51.00 | 81.00 | 81.00 | 81.00 | 511.0 | 36.0000              | 4860.9039         | 0.0741         |
| 3         | 30.0000    | 481.0000   | 30.0000       | 1391.0000     | 138.9000    | 153.0284    | 30.00 | 51.00 | 81.00 | 81.00 | 481.0 | 3.1000               | 392.3966          | 0.0790         |
MathModel run.
Gathering result...
Result gathered.
Introducing solution...
Optimal solution introduced.
Continuing game...
    Clicking...
    Clicked.
    Selecting starting point...
    Starting point selected.
Game continued.
`````
As you see in the output example, the GA finds the mad_virus solution very fast, in the first iterations.

## Comment:
This problem is just a guide that the user can follow to build its own problems.

In this example, we didn't conduct an extensive optimization of the variables and constraints created. Nor the ACO parameters.
Nevertheless, we were able to get in a pretty sort time the solutions for the scenario. :smirk_cat:
