# AllOut
For this https://www.mathsisfun.com/games/allout.html AllOut problem, we use a GA and MM to solve a grid represented as an ArrayGenome.
In the [GA-representation](#ga-representation) we can see the genome design.

<img src="https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/out/AllOut/AllOut.gif"  title="AllOut example" alt="AllOut example"/>

## GA Representation:
The genome looks like:
````mermaid
block-beta
    columns 3
    block: Chromosome1:3
    Gen1("&emsp;Gen1 &emsp;") Gen2("&emsp;Gen2 &emsp;") Gen3("&emsp;Gen3 &emsp;")
    end
    block: Chromosome2:3
    Gen4("&emsp;Gen4 &emsp;") Gen5("&emsp;Gen5 &emsp;") Gen6("&emsp;Gen6 &emsp;")
    end
    block: Chromosome3:3
    Gen7("&emsp;Gen7 &emsp;") Gen8("&emsp;Gen8 &emsp;") Gen9("&emsp;Gen9 &emsp;")
    end

    
````
The Genome represents a M*M matrix of booleans. When it is True, it means we have to press that cell to obtain the result.

## Mathematical formulation:
We have to represent the problem using lineal mathematical expressions.
<table>
   <tr>
      <th>Sets</th>
      <th>Variables:</th>
   </tr>
   <tr>
      <td>
         <ul>
            <li>$\textcolor{blue}{BIS_{r,c}} =$ Board Initial State by row anc column.</li>
            <li>$\textcolor{blue}{R}= $ Rows.</li>
            <li>$\textcolor{blue}{C}= $ Cols.</li>
         </ul>
      </td>
      <td>
        <ul>
           <li>$BA_{r, c} \in \mathbb{Z}:\{0,1\}, \; \forall r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C} =$ (BoardAction) one implies we must switch and zero no switch.</li>
           <li>$BS_{r, c} \in \mathbb{Z}:\{0,10\}, \; \forall r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C} =$ (BoardState) state of the final board after we apply the actions. Even implies off and add on.</li>
           <li>$Aux_{r, c} \in \mathbb{Z}:\{0,5\}, \; \forall r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C} =$ (Auxiliary) forces using constraints to $BS_{r, c}$ to be even.</li>
        </ul>
      </td>
   </tr>
</table>

### Constraints:
<table>
  <tr>
    <th>Name</th>
    <th>Expression</th>
    <th>Nº of constraints</th>
    <th>Description</th>
  </tr>
  <tr>
    <td><b>ForceEvenState</b></td>
    <td>$$BS_{r, c} - 2 \cdot Aux_{r, c}==0,\; \forall r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C}$$</td>
    <td> $|\textcolor{blue}{C} \cdot \textcolor{blue}{R}|$ </td>
    <td> Forces state variable to be even.</td>
  </tr>
  <tr>
    <td><b>RelateActionWithState</b></td>
    <td>$$BA_{r-1, c}+BA_{r, c-1}+BA_{r+1, c}+BA_{r, c+1}-BA_{r, c}==\textcolor{blue}{BIS_{r,c}},\; \forall r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C}$$</td>
    <td> $|\textcolor{blue}{C} \cdot \textcolor{blue}{R}|$ </td>
    <td> Apply actions to the initial board state.</td>
  </tr>
</table>

## In this folder:
This folder contains one class and a packages that define the structures required for solving the problem.

1. [AllOutScraper](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/AllOut/AllOutScraper.java): Web Scrapper using Selenium and flow controller.
    - Scrapes the necessary information from the allout-web application.
    - Manages the flow of the program.
    - Instantiates the AllOutGA.
    - Introduces the solution in the web app.
2. [AllOutGA](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/AllOut/GA/AllOutGA.java): Class that implements the required OptimizationLib interface and represents a Genetic Algorithm Optimization program.
    - Instantiates all its components.
    - Implements callable.
3. [AllOutFeasibilityFunction](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/AllOut/GA/Evaluable/AllOutFeasibilityFunction.java): Class that evaluates whether the solution has been found or not.
4. [AllOutObjectiveFunction](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/AllOut/GA/Evaluable/AllOutObjectiveFunction.java): Class that evaluates the number of movements done. We want to find the solution with less possible movements.
5. [AllOutInitializer](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/AllOut/GA/Initializer/AllOutInitializer.java): Initializes an individual.
6. [TargetFeasibilityCriteriaPolicy](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/AllOut/GA/StoppingPolicy/TargetFeasibilityCriteriaPolicy.java): Evaluates if the solution has been found, or we need to keep looking.
7. [AllOutMathModel](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/AllOut/MathModel/AllOutMathModel.java): Class that implements the required OptimizationLib interface and represents a Mathematical Modeling Optimization program.
    - Instantiates all its components.
    - Implements callable.
8. [AllOutMMInitializer](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/AllOut/MathModel/AllOutMMInitializer.java): Initializes the Mathematical Model:
   - Creates Variables.
   - Creates constraints.


## Results
For the GA, the set of parameters used are in the AllOutGA file.
MM parameters ar in AllOutMathModel.

The output looks like:
`````
Loading https://www.mathsisfun.com/games/allout.html: ...
Loaded.
Accepting cookies...
Cookies accepted.
Switching to Iframe...
Iframe switched.
Screenshot the board...
Board screenshot.
Transforming image...
    Loading OpenCV...
    OpenCV loaded.
    Cropping...
    Cropped
    Converting to Grayscale...
    Grayscale converted.
    Converting to Black&White...
    Black&White Converting.
Image transformed.
Computing grid...
Grid computed.
Mapping buttons...
Buttons mapped.
Building MM...
MM built.
Building GA...
GA built.
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
| Iteration | CurrentMin | CurrentMax | HistoricalMin | HistoricalMax | MeanFitness | StandardDev | P0    | P25   | P50   | P75   | P100  | StepGradient(u/iter) | TimeGradient(u/s) | ElapsedTime(s) |
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
| 0         | 33.0000    | 67.0000    | 33.0000       | 67.0000       | 48.8800     | 8.7101      | 33.00 | 42.00 | 47.00 | 54.00 | 67.00 |                      |                   | 0.0091         |
presolving:
(round 1, fast)       25 del vars, 25 del conss, 0 add conss, 25 chg bounds, 0 chg sides, 0 chg coeffs, 0 upgd conss, 0 impls, 0 clqs
(round 2, exhaustive) 25 del vars, 25 del conss, 0 add conss, 25 chg bounds, 0 chg sides, 0 chg coeffs, 25 upgd conss, 0 impls, 0 clqs
   (0.0s) probing cycle finished: starting next cycle
   Deactivated symmetry handling methods, since SCIP was built without symmetry detector (SYM=none).
presolving (3 rounds: 3 fast, 2 medium, 2 exhaustive):
 25 deleted vars, 25 deleted constraints, 0 added constraints, 25 tightened bounds, 0 added holes, 0 changed sides, 0 changed coefficients
 19 implications, 8 cliques
presolved problem has 50 variables (33 bin, 17 int, 0 impl, 0 cont) and 25 constraints
     25 constraints of type <xor>
transformed objective value is always integral (scale: 1)
Presolving Time: 0.00

 time | node  | left  |LP iter|LP it/n|mem/heur|mdpt |vars |cons |rows |cuts |sepa|confs|strbr|  dualbound   | primalbound  |  gap   | compl. 
p 0.0s|     1 |     0 |     0 |     - | vbounds|   0 |  50 |  27 |  41 |   0 |  0 |   2 |   0 | 0.000000e+00 | 0.000000e+00 |   0.00%| unknown

SCIP Status        : problem is solved [optimal solution found]
Solving Time (sec) : 0.00
Solving Nodes      : 1
Primal Bound       : +0.00000000000000e+00 (1 solutions)
Dual Bound         : +0.00000000000000e+00
Gap                : 0.00 %
GA optimal introduced.
Clicking next...
Next clicked .
Screenshot the board...
Board screenshot.
Transforming image...
    Loading OpenCV...
    OpenCV loaded.
    Cropping...
    Cropped
    Converting to Grayscale...
    Grayscale converted.
    Converting to Black&White...
    Black&White Converting.
Image transformed.
Computing grid...
Grid computed.
Mapping buttons...
Buttons mapped.
Building MM...
MM built.
Building GA...
GA built.
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
| Iteration | CurrentMin | CurrentMax | HistoricalMin | HistoricalMax | MeanFitness | StandardDev | P0    | P25   | P50   | P75   | P100  | StepGradient(u/iter) | TimeGradient(u/s) | ElapsedTime(s) |
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
| 0         | 38.0000    | 69.0000    | 38.0000       | 69.0000       | 50.5600     | 9.0026      | 38.00 | 43.00 | 48.00 | 58.00 | 69.00 |                      |                   | 0.0010         |
presolving:
(round 1, fast)       25 del vars, 25 del conss, 0 add conss, 25 chg bounds, 0 chg sides, 0 chg coeffs, 0 upgd conss, 0 impls, 0 clqs
(round 2, exhaustive) 25 del vars, 25 del conss, 0 add conss, 25 chg bounds, 0 chg sides, 0 chg coeffs, 25 upgd conss, 0 impls, 0 clqs
   (0.0s) probing cycle finished: starting next cycle
   Deactivated symmetry handling methods, since SCIP was built without symmetry detector (SYM=none).
presolving (3 rounds: 3 fast, 2 medium, 2 exhaustive):
 25 deleted vars, 25 deleted constraints, 0 added constraints, 25 tightened bounds, 0 added holes, 0 changed sides, 0 changed coefficients
 27 implications, 8 cliques
presolved problem has 50 variables (32 bin, 18 int, 0 impl, 0 cont) and 25 constraints
     25 constraints of type <xor>
transformed objective value is always integral (scale: 1)
Presolving Time: 0.00

 time | node  | left  |LP iter|LP it/n|mem/heur|mdpt |vars |cons |rows |cuts |sepa|confs|strbr|  dualbound   | primalbound  |  gap   | compl. 
t 0.0s|     1 |     0 |     0 |     - |  trysol|   0 |  50 |  38 |  41 |   0 |  0 |  13 |   0 | 0.000000e+00 | 0.000000e+00 |   0.00%| unknown

SCIP Status        : problem is solved [optimal solution found]
Solving Time (sec) : 0.00
Solving Nodes      : 1
Primal Bound       : +0.00000000000000e+00 (1 solutions)
Dual Bound         : +0.00000000000000e+00
Gap                : 0.00 %
MM optimal introduced.
Clicking next...
Next clicked .
`````
As you see in the output example, the GA finds the solution of the first problem faster, but then the MM takes the lead.

## Comment:
This problem is just a guide that the user can follow to build its own problems.

In this example, we didn't conduct an extensive optimization of the GA parameters.
Nevertheless, we were able to get in a pretty sort time the solutions for the scenario. :smirk_cat:
