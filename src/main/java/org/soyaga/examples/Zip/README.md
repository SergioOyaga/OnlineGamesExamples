# Zip
For this LinkedIn Zip problem, we use two approaches that compete:
-  **MIP** formulating and solving a linear problem.
-  **ACO** designing a graph representation of the problem.

In the [mathematical formulation](#mathematical-formulation) we can see how the problem sets, parameters, variables and constraints are defined.

In the [ACO-representation](#aco-representation) we can see the graph design.

<img src="https://github.com/SergioOyaga/OnlineGamesExamples/blob/main/src/out/Zip/Zip.gif"  title="Zip example" alt="Zip example"/>

## Mathematical formulation:
We have to represent the problem using lineal mathematical expressions.

<table>
   <tr>
      <th>Sets</th>
      <th>Parameters:</th>
   </tr>
   <tr>
      <td>
         <ul>
            <li>$\textcolor{blue}{C} =$ Columns in the board.</li>
            <li>$\textcolor{blue}{R} =$ Row number in the board.</li>
            <li>$\textcolor{blue}{PC} =$ Priority cells in a sorted set.</li>
         </ul>
      </td>
      <td>
        <ul>
           <li>
                $\textcolor{magenta}{CR_{r,c, i}} \in \{0,1\} \vee \{ (r, c) \mid r \in \textcolor{blue}{R}, c \in \textcolor{blue}{C}, i \in [0,3]\}, \; = \textbf{(CellRelation)}$ Cell relation with surrounding cells North, East, South and West.
           </li>
        </ul>
      </td>
   </tr>
</table>

### Variables:
<table>
   <tr>
      <th>Name:</th>
      <th>Variable:</th>
      <th>Description:</th>
   </tr>
   <tr>
      <td>
           Grid Cell Value
      </td>
      <td>
           $GCV_{r, c} \in [1,\textcolor{blue}{R} \cdot \textcolor{blue}{C}], \; \forall r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C} $
      </td>
      <td>
        Grid Cell Value. Integer that indicates the order in which the solution is built.
      </td>
   </tr>
   <tr>
      <td>
           Auxiliary
      </td>
      <td>
           $AUX_{r, c, i} \in \mathbb{Z}:\{0,1\}, \;  \forall r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C}, \; \vee i \in [0,3] $
      </td>
      <td>
        Auxiliary Variable for the OR disjunction implementation.
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
    <td><b>StartNode</b></td>
    <td>$$GCV_{r,c} ==1,\; \vee (r,c) \in \textcolor{blue}{PC_{0}}$$</td>
    <td> $1$ </td>
    <td> The problem fixes the start node.</td>
  </tr>
  <tr>
    <td><b>EndNode</b></td>
    <td>$$GCV_{r,c} ==\textcolor{blue}{R} \cdot \textcolor{blue}{C},\; \vee (r,c) \in \textcolor{blue}{PC_{-1}}$$</td>
    <td> $1$ </td>
    <td> The problem fixes the end node.</td>
  </tr>
  <tr>
    <td><b>AllCellsUsed</b></td>
    <td>$$\sum_{r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C}} GCV_{r,c} = \frac{(\textcolor{blue}{R} \cdot \textcolor{blue}{C} +1) \cdot \textcolor{blue}{R} \cdot \textcolor{blue}{C}}{2}$$</td>
    <td> $1$ </td>
    <td> All cells must be used.</td>
  </tr>
  <tr>
    <td><b>Priority</b></td>
    <td>$$GCV_{r_i,c_i} < GCV_{r_j,c_j},\; \vee (r_i,c_i), (r_j,c_j) \in \textcolor{blue}{PC} \mid i = j-1 $$</td>
    <td> $|\textcolor{blue}{PC}-1|$ </td>
    <td> Respect the priority assignation.</td>
  </tr>
  <tr>
    <td><b>AuxUnique</b></td>
    <td>$$\sum_{i=[0,3]} AUX_{r,c,i}==1,\; \forall r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C}$$</td>
    <td> $|\textcolor{blue}{R} \cdot \textcolor{blue}{C}|$ </td>
    <td> Only one can be selected. In this constraint, we take into account barriers in the graph and borders.</td>
  </tr>
  <tr>
    <td><b>PathContinuity</b></td>
    <td>
        <ul>
            <li>$$\ GCV_{r-1,c} - GCV_{r,c} + \textcolor{magenta}{Big} \cdot AUX_{r,c,0}<= 1+\textcolor{magenta}{Big},\; \forall r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C}$$</li>
            <li>$$\ GCV_{r-1,c} - GCV_{r,c} - \textcolor{magenta}{Big} \cdot AUX_{r,c,0}<= 1-\textcolor{magenta}{Big},\; \forall r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C}$$</li>
            <li>$$\ GCV_{r+1,c} - GCV_{r,c} + \textcolor{magenta}{Big} \cdot AUX_{r,c,1}<= 1+\textcolor{magenta}{Big},\; \forall r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C}$$</li>
            <li>$$\ GCV_{r+1,c} - GCV_{r,c} - \textcolor{magenta}{Big} \cdot AUX_{r,c,1}<= 1-\textcolor{magenta}{Big},\; \forall r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C}$$</li>
            <li>$$\ GCV_{r,c-1} - GCV_{r,c} + \textcolor{magenta}{Big} \cdot AUX_{r,c,2}<= 1+\textcolor{magenta}{Big},\; \forall r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C}$$</li>
            <li>$$\ GCV_{r,c-1} - GCV_{r,c} - \textcolor{magenta}{Big} \cdot AUX_{r,c,2}<= 1-\textcolor{magenta}{Big},\; \forall r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C}$$</li>
            <li>$$\ GCV_{r,c+1} - GCV_{r,c} + \textcolor{magenta}{Big} \cdot AUX_{r,c,3}<= 1+\textcolor{magenta}{Big},\; \forall r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C}$$</li>
            <li>$$\ GCV_{r,c+1} - GCV_{r,c} - \textcolor{magenta}{Big} \cdot AUX_{r,c,3}<= 1-\textcolor{magenta}{Big},\; \forall r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C}$$</li>
        </ul>
    </td>
    <td> 
        <ul>
            <li> $|\textcolor{blue}{R} \cdot \textcolor{blue}{C}|-1$ </li>
            <li> $|\textcolor{blue}{R} \cdot \textcolor{blue}{C}|-1$ </li>
            <li> $|\textcolor{blue}{R} \cdot \textcolor{blue}{C}|-1$ </li>
            <li> $|\textcolor{blue}{R} \cdot \textcolor{blue}{C}|-1$ </li>
            <li> $|\textcolor{blue}{R} \cdot \textcolor{blue}{C}|-1$ </li>
            <li> $|\textcolor{blue}{R} \cdot \textcolor{blue}{C}|-1$ </li>
            <li> $|\textcolor{blue}{R} \cdot \textcolor{blue}{C}|-1$ </li>
            <li> $|\textcolor{blue}{R} \cdot \textcolor{blue}{C}|-1$ </li>
        </ul>
    </td>
    <td> The numbers must be connected. In this constraint, we take into account barriers in the graph and borders.</td>
  </tr>
</table>

## ACO Representation:
The graph looks like:
````mermaid
block-beta
    columns 3

    Node1 Node2 Node3
    Node4 Node5 Node6
    Node7 Node8 Node9

    Node1<--->Node2
    Node1<--->Node4
    Node2<--->Node3
    Node2<--->Node5
    Node3<--->Node6
    Node4<--->Node5
    Node4<--->Node7
    Node5<--->Node6
    Node5<--->Node8
    Node6<--->Node9
````
The Ants build solutions by moving through the Graph edges. A solution is an Array of Nodes, that is a valid solution when:
1. Start and end Nodes are the first and last priority nodes.
2. The priority Nodes are selected in order.
3. All Nodes of the Graph have been visited only once.

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
This folder contains one class and two packages that define the structures required for solving the problem.

1. [LinkedInZipScraper](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Zip/LinkedInSipScraper.java): Web Scrapper using Selenium and flow controller.
    - Scrapes the necessary information from LinkedIn-web application.
    - Manages the flow of the program.
    - Instantiates the ZipMathModel and ZipACO.
    - Introduces the solution in the web app.
2. [TangoMathModel](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Tango/MathModel/TangoMathModel.java): Class that implements the required OptimizationLib interface and represents a Mathematical Modeling Optimization program.
    - Instantiates its own initializer.
    - Implements runnable.
3. [TangoMMInitializer](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Tango/MathModel/TangoMMInitializer.java): Class that initializes the TangoMathModel using problem specific information.
    - Create variables.
    - Create constraints.
4. [ZipZCO](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Tango/ACO/ZipZCO.java): Class that implements the required OptimizationLib interface and represents an Ant Colony Optimization program.
   - Defines its parameters.
5. [ZipFeasibilityFunction](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Tango/ACO/Evaluable/ZipFeasibilityFunction.java): Class that Represents the feasibility function of the problem.
6. [MaxSolFitnessProportionalGlobalBestAddPheromonePolicy](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Tango/ACO/UpdatePheromonePolicy/AddPheromonePolicy/MaxSolFitnessProportionalGlobalBestAddPheromonePolicy.java): Class implements a custom AddPheromonePolicy.


## Results
For the Math model, we solve the problem using:
```` java
    @Override
    public void initializeModelRequest() {
        this.getProtoModelRequest()
                .setEnableInternalSolverOutput(true)
                .setSolverTimeLimitSeconds(60)
                .setSolverType(MPModelRequest.SolverType.SCIP_MIXED_INTEGER_PROGRAMMING);
    }
````
As solver SCIP, a max time of 1 minutes and asking the solver to plot the solving info

For the ACO, the set of parameters used are in the LinkedInZipScraper and the ZipACO files.

The output looks like:
`````
---------------------------------------------------------------------------------------------------------------------------------
| Iteration | CurrentMin | CurrentMax | HistoricalMin | MeanFitness | StandardDevFitness | MeanPheromone | StandardDevPheromone |
---------------------------------------------------------------------------------------------------------------------------------
| 20        | 63.00      | 808.00     | 39.00         | 339.61      | 142.52             | 0.26          | 0.20                 |
presolving:
(round 1, fast)       79 del vars, 24 del conss, 0 add conss, 17 chg bounds, 594 chg sides, 195 chg coeffs, 0 upgd conss, 0 impls, 46 clqs
(round 2, fast)       80 del vars, 28 del conss, 0 add conss, 19 chg bounds, 685 chg sides, 291 chg coeffs, 0 upgd conss, 0 impls, 46 clqs
(round 3, fast)       80 del vars, 29 del conss, 0 add conss, 19 chg bounds, 688 chg sides, 296 chg coeffs, 0 upgd conss, 1 impls, 46 clqs
(round 4, exhaustive) 80 del vars, 29 del conss, 0 add conss, 19 chg bounds, 688 chg sides, 296 chg coeffs, 68 upgd conss, 1 impls, 46 clqs
(round 5, fast)       83 del vars, 30 del conss, 0 add conss, 30 chg bounds, 768 chg sides, 378 chg coeffs, 68 upgd conss, 11 impls, 47 clqs
   (0.0s) probing cycle finished: starting next cycle
(round 6, exhaustive) 89 del vars, 30 del conss, 0 add conss, 31 chg bounds, 768 chg sides, 378 chg coeffs, 68 upgd conss, 204 impls, 54 clqs
(round 7, fast)       90 del vars, 43 del conss, 0 add conss, 31 chg bounds, 770 chg sides, 382 chg coeffs, 68 upgd conss, 205 impls, 53 clqs
   (0.0s) probing: 51/170 (30.0%) - 6 fixings, 0 aggregations, 203 implications, 1 bound changes
   (0.0s) probing aborted: 50/50 successive totally useless probings
   Deactivated symmetry handling methods, since SCIP was built without symmetry detector (SYM=none).
presolving (8 rounds: 8 fast, 3 medium, 3 exhaustive):
 90 deleted vars, 43 deleted constraints, 0 added constraints, 31 tightened bounds, 0 added holes, 770 changed sides, 382 changed coefficients
 205 implications, 53 cliques
presolved problem has 230 variables (169 bin, 61 int, 0 impl, 0 cont) and 427 constraints
     22 constraints of type <varbound>
     44 constraints of type <setppc>
    361 constraints of type <linear>
transformed objective value is always integral (scale: 1)
Presolving Time: 0.00

 time | node  | left  |LP iter|LP it/n|mem/heur|mdpt |vars |cons |rows |cuts |sepa|confs|strbr|  dualbound   | primalbound  |  gap   | compl. 
  0.0s|     1 |     0 |   139 |     - |  4016k |   0 | 230 | 443 | 427 |   0 |  0 |  15 |   0 | 0.000000e+00 |      --      |    Inf | unknown
| 40        | 134.00     | 901.00     | 39.00         | 384.70      | 143.88             | 0.22          | 0.16                 |
| 60        | 138.00     | 816.00     | 39.00         | 382.31      | 146.79             | 0.22          | 0.17                 |
  0.0s|     1 |     0 |   316 |     - |  4480k |   0 | 230 | 611 | 467 |  40 |  1 |  16 |   0 | 0.000000e+00 |      --      |    Inf | unknown
  0.0s|     1 |     0 |   328 |     - |  5324k |   0 | 230 | 611 | 477 |  50 |  2 |  17 |   0 | 0.000000e+00 |      --      |    Inf | unknown
  0.0s|     1 |     0 |   364 |     - |  6436k |   0 | 230 | 614 | 487 |  60 |  3 |  20 |   0 | 0.000000e+00 |      --      |    Inf | unknown
  0.0s|     1 |     0 |   389 |     - |  7126k |   0 | 230 | 614 | 494 |  67 |  4 |  20 |   0 | 0.000000e+00 |      --      |    Inf | unknown
| 80        | 80.00      | 932.00     | 39.00         | 401.33      | 169.06             | 0.22          | 0.16                 |
  0.0s|     1 |     0 |   431 |     - |  8168k |   0 | 230 | 615 | 525 |  98 |  5 |  21 |   0 | 0.000000e+00 |      --      |    Inf | unknown
| 100       | 115.00     | 766.00     | 39.00         | 343.47      | 145.66             | 0.23          | 0.17                 |
  0.0s|     1 |     0 |   460 |     - |  8938k |   0 | 230 | 615 | 544 | 117 |  6 |  21 |   0 | 0.000000e+00 |      --      |    Inf | unknown
| 120       | 107.00     | 848.00     | 39.00         | 403.17      | 145.17             | 0.21          | 0.16                 |
  0.0s|     1 |     0 |   501 |     - |    10M |   0 | 230 | 615 | 551 | 124 |  7 |  22 |   0 | 0.000000e+00 |      --      |    Inf | unknown
| 140       | 109.00     | 867.00     | 39.00         | 350.16      | 145.47             | 0.23          | 0.18                 |
  0.0s|     1 |     0 |   580 |     - |    10M |   0 | 230 | 616 | 550 | 126 |  8 |  23 |   0 | 0.000000e+00 |      --      |    Inf | unknown
| 160       | 144.00     | 808.00     | 37.00         | 384.95      | 153.10             | 0.22          | 0.19                 |
  0.0s|     1 |     0 |   607 |     - |    12M |   0 | 230 | 616 | 558 | 134 |  9 |  23 |   0 | 0.000000e+00 |      --      |    Inf | unknown
| 180       | 92.00      | 679.00     | 37.00         | 307.47      | 134.60             | 0.25          | 0.24                 |
  0.0s|     1 |     0 |   613 |     - |    12M |   0 | 230 | 617 | 564 | 140 | 10 |  24 |   0 | 0.000000e+00 |      --      |    Inf | unknown
  0.0s|     1 |     0 |   626 |     - |    12M |   0 | 230 | 621 | 570 | 146 | 11 |  28 |   0 | 0.000000e+00 |      --      |    Inf | unknown
| 200       | 153.00     | 704.00     | 35.00         | 330.91      | 126.00             | 0.24          | 0.19                 |
| 220       | 61.00      | 719.00     | 35.00         | 367.59      | 147.96             | 0.24          | 0.18                 |
| 240       | 17.00      | 596.00     | 16.00         | 223.19      | 148.65             | 0.44          | 0.40                 |
| 260       | 16.00      | 734.00     | 7.00          | 247.30      | 176.16             | 0.46          | 0.41                 |
  0.0s|     1 |     0 |   727 |     - |    12M |   0 | 230 | 627 | 570 | 146 | 11 |  36 |  14 | 0.000000e+00 |      --      |    Inf | unknown
  0.0s|     1 |     0 |   729 |     - |    12M |   0 | 230 | 627 | 572 | 148 | 12 |  36 |  14 | 0.000000e+00 |      --      |    Inf | unknown
| 280       | 10.00      | 905.00     | 7.00          | 187.53      | 192.59             | 0.47          | 0.42                 |
| 300       | 14.00      | 678.00     | 7.00          | 195.67      | 153.72             | 0.47          | 0.42                 |
  1.0s|     1 |     0 |   735 |     - |    12M |   0 | 230 | 629 | 572 | 148 | 13 |  41 |  29 | 0.000000e+00 |      --      |    Inf | unknown
| 320       | 7.00       | 788.00     | 6.00          | 225.53      | 191.82             | 0.49          | 0.41                 |
 time | node  | left  |LP iter|LP it/n|mem/heur|mdpt |vars |cons |rows |cuts |sepa|confs|strbr|  dualbound   | primalbound  |  gap   | compl. 
  1.0s|     1 |     0 |   747 |     - |    12M |   0 | 230 | 630 | 581 | 157 | 14 |  42 |  29 | 0.000000e+00 |      --      |    Inf | unknown
  1.0s|     1 |     0 |   755 |     - |    12M |   0 | 230 | 630 | 501 | 158 | 15 |  43 |  29 | 0.000000e+00 |      --      |    Inf | unknown
| 340       | 7.00       | 814.00     | 6.00          | 248.23      | 184.29             | 0.42          | 0.41                 |
| 360       | 6.00       | 608.00     | 6.00          | 198.92      | 157.96             | 0.46          | 0.42                 |
  1.0s|     1 |     0 |   759 |     - |    12M |   0 | 230 | 631 | 501 | 158 | 15 |  45 |  44 | 0.000000e+00 |      --      |    Inf | unknown
  1.0s|     1 |     0 |   772 |     - |    12M |   0 | 230 | 631 | 512 | 169 | 16 |  45 |  44 | 0.000000e+00 |      --      |    Inf | unknown
  1.0s|     1 |     0 |   773 |     - |    12M |   0 | 230 | 632 | 514 | 171 | 17 |  46 |  44 | 0.000000e+00 |      --      |    Inf | unknown
| 380       | 6.00       | 643.00     | 6.00          | 169.47      | 145.09             | 0.50          | 0.43                 |
  1.0s|     1 |     0 |   775 |     - |    12M |   0 | 230 | 633 | 483 | 171 | 17 |  50 |  49 | 0.000000e+00 |      --      |    Inf | unknown
  1.0s|     1 |     0 |   900 |     - |    12M |   0 | 230 | 633 | 482 | 174 | 18 |  50 |  49 | 0.000000e+00 |      --      |    Inf | unknown
| 400       | 19.00      | 980.00     | 6.00          | 218.25      | 186.64             | 0.47          | 0.42                 |
  1.0s|     1 |     0 |   920 |     - |    12M |   0 | 230 | 635 | 493 | 185 | 19 |  52 |  49 | 0.000000e+00 |      --      |    Inf | unknown
  1.0s|     1 |     0 |   922 |     - |    12M |   0 | 230 | 635 | 495 | 187 | 20 |  52 |  49 | 0.000000e+00 |      --      |    Inf | unknown
| 420       | 12.00      | 648.00     | 6.00          | 209.13      | 166.04             | 0.46          | 0.42                 |
  1.0s|     1 |     0 |   922 |     - |    12M |   0 | 230 | 635 | 495 | 187 | 20 |  52 |  60 | 0.000000e+00 |      --      |    Inf | unknown
  1.0s|     1 |     0 |   939 |     - |    12M |   0 | 230 | 635 | 520 | 212 | 21 |  52 |  60 | 0.000000e+00 |      --      |    Inf | unknown
  1.0s|     1 |     0 |   942 |     - |    12M |   0 | 230 | 636 | 525 | 217 | 22 |  53 |  60 | 0.000000e+00 |      --      |    Inf | unknown
| 440       | 8.00       | 784.00     | 3.00          | 182.55      | 178.46             | 0.50          | 0.43                 |
| 460       | 6.00       | 650.00     | 2.00          | 191.23      | 143.99             | 0.50          | 0.43                 |
  1.0s|     1 |     2 |   942 |     - |    12M |   0 | 230 | 638 | 525 | 217 | 22 |  55 |  71 | 0.000000e+00 |      --      |    Inf | unknown
| 480       | 6.00       | 496.00     | 2.00          | 167.75      | 123.81             | 0.50          | 0.42                 |
| 500       | 4.00       | 596.00     | 2.00          | 183.02      | 124.44             | 0.50          | 0.42                 |
| 520       | 8.00       | 618.00     | 2.00          | 176.80      | 131.47             | 0.49          | 0.43                 |
...
...
...
| 780       | 6.00       | 629.00     | 2.00          | 172.66      | 147.66             | 0.49          | 0.43                 |
| 800       | 5.00       | 560.00     | 2.00          | 192.28      | 135.07             | 0.49          | 0.42                 |
| 820       | 6.00       | 487.00     | 2.00          | 159.33      | 131.39             | 0.50          | 0.43                 |
| 840       | 8.00       | 507.00     | 2.00          | 167.56      | 133.52             | 0.51          | 0.43                 |
  1.0s|   100 |    63 |  3567 |  27.6 |    13M |  29 | 230 | 717 | 470 | 384 |  1 | 150 |  71 | 0.000000e+00 |      --      |    Inf |   7.56%
  2.0s|   200 |    88 |  6127 |  26.6 |    14M |  33 | 230 | 814 | 516 | 760 |  1 | 252 |  71 | 0.000000e+00 |      --      |    Inf |  52.30%
 time | node  | left  |LP iter|LP it/n|mem/heur|mdpt |vars |cons |rows |cuts |sepa|confs|strbr|  dualbound   | primalbound  |  gap   | compl. 
  2.0s|   300 |   135 |  7229 |  21.4 |    15M |  44 | 230 | 858 | 521 | 958 |  1 | 300 |  71 | 0.000000e+00 |      --      |    Inf |  52.32%
  2.0s|   400 |   152 |  8479 |  19.2 |    15M |  44 | 230 | 956 | 523 |1182 |  0 | 400 |  71 | 0.000000e+00 |      --      |    Inf |  52.32%
...
...
...
`````
As you see in the output example, the two models are competing at the same time to achieve the solution. For example, the solution is found by the ACO first. However, in other scenarios, the mathematical model might be much faster to find the solution. 

## Comment:
This problem is just a guide that the user can follow to build its own problems.

In this example, we didn't conduct an extensive optimization of the variables and constraints created. Nor the ACO parameters.
Nevertheless, we were able to get in a pretty sort time the solutions for the scenario. :smirk_cat:
