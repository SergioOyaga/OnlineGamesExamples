# Sudoku
For this LinkedIn Sudoku problem, we use MIP to formulate and solve a linear problem.
In the [mathematical formulation](#mathematical-formulation) we can see how the problem sets, parameters, variables and constraints are defined.

<img src="https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/out/LinkedInSudoku/LinkedInSudoku.gif"  title="Sudoku example" alt="Sudoku example"/>

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
            <li>$\textcolor{blue}{R} =$ Row numbers in the board.</li>
            <li>$\textcolor{blue}{R_{fv}} =$ Row associated with a fixed value.</li>
            <li>$\textcolor{blue}{C} =$ Column numbers in the board.</li>
            <li>$\textcolor{blue}{C_{fv}} =$ Column associated with a fixed value.</li>
            <li>$\textcolor{blue}{FV} =$ FixedValues cells in the board.</li>
            <li>$\textcolor{blue}{S} =\{0,2,...,(\textcolor{magenta}{Max}-1)\}$ Square group numbers.</li>
         </ul>
      </td>
      <td rowspan="3">
        <ul>
           <li>$S_{r, c} \in \{1,2,...,\textcolor{magenta}{Max}\}, \; \forall r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C} =$ Sudoku board.</li>
           <li>$DRCC_{r, c_i, c_j} \in \{0,1\}, \; \forall r \in \textcolor{blue}{R}, \; c_i, c_j \in \textcolor{blue}{C} =$ DiscriminatorRowColumColum.</li>
           <li>$DCRR_{c, r_i, r_j} \in \{0,1\}, \; \forall c \in \textcolor{blue}{C}, \; r_i, r_j \in \textcolor{blue}{R} =$ DiscriminatorColumRowRow.</li>
           <li>$DRCS_{r, c, s} \in \{0,1\}, \; \forall r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C}\; s \in \textcolor{blue}{S} =$ DiscriminatorRowColumSquare.</li>
        </ul>
      </td>
   </tr>
   <tr>
        <th> Parameters </th>
   </tr>
    <tr>
      <td>
        <ul>
           <li> $\textcolor{magenta}{Max} = \max(\mid\textcolor{blue}{R}\mid, \mid\textcolor{blue}{C}\mid)$ Maximum value in the sudoku.</li>
            <li>$\textcolor{magenta}{CV_{fv}} \in \{1,2,...,\textcolor{magenta}{Max}\} \forall fv \in \textcolor{blue}{FV}$ CellValue by fixed value cells.</li>
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
    <td><b>AllDifferentInRow</b></td>
    <td>$S_{r,c_i} \neq S{r,c_j},\; \forall r \in \textcolor{blue}{R},\; \forall c_i \in \textcolor{blue}{C},\; \forall c_j \in \textcolor{blue}{C} \| c_j>c_i$</td>
    <td> $|\textcolor{blue}{R} \cdot \textcolor{blue}{C} \cdot \frac{\textcolor{blue}{C} \cdot(\textcolor{blue}{C} -1))}{2}|$ </td>
    <td> Each row element has to be different with the rest of the row elements.</td>
  </tr>
  <tr>
    <td><b>AllDifferentInCol</b></td>
    <td>$S_{r_i,c} \neq S{r_j,c},\; \forall r_i \in \textcolor{blue}{R},\; \forall r_j \in \textcolor{blue}{R},\; \forall c \in \textcolor{blue}{C} \| r_j>r_i$</td>
    <td> $|\textcolor{blue}{C} \cdot \textcolor{blue}{R} \cdot \frac{\textcolor{blue}{R} \cdot(\textcolor{blue}{R} -1))}{2}|$ </td>
    <td> Each colum element has to be different with the rest of the column elements.</td>
  </tr>
  <tr>
    <td><b>AllDifferentInSquare</b></td>
    <td>$S_{r,c} \neq S{r_s,c_s},\; \forall r \in \textcolor{blue}{R},\; \forall c \in \textcolor{blue}{C},\; \forall s \in \textcolor{blue}{S}$</td>
    <td> $|\textcolor{blue}{R} \cdot \textcolor{blue}{C} \cdot \frac{\textcolor{blue}{S} \cdot(\textcolor{blue}{S} -1))}{2}|$ </td>
    <td> Each element has to be different with the rest of the square elements.</td>
  </tr>
  <tr>
    <td><b>ForceAssignations</b></td>
    <td>$S_{r_{fv},c_{fv}} == \textcolor{magenta}{CV_{fv}},\; \forall fv \in \textcolor{blue}{FV}$</td>
    <td> $|\textcolor{blue}{FV}|$ </td>
    <td> Force assignation of fixed cell values.</td>
  </tr>
</table>

### Implementation of the inequality
Due to the problem nature:
- All variables are integers with a known lower and upper bound:
  - [1,$\textcolor{magenta}{Max}$] for $S{r,c}$.
  - [0,1] for the discriminators.

The inequality can be expressed as:

  | Name                                                         | Inequality                 | Expression                                                                                                   |
  |--------------------------------------------------------------|----------------------------|--------------------------------------------------------------------------------------------------------------|
  | AllDifferentInRow                                            | $S_{r,c_i} \neq S_{r,c_j}$ | $1\leq S_{r,c_i}- S_{r,c_j} +\textcolor{magenta}{Max}\cdot DRCC_{r,c_i,c_j}\leq(\textcolor{magenta}{Max}-1)$ |
  | Same approach for AllDifferentInCol and AllDifferentInSquare |                            |

Let see why this approach works. If $DRCC_{r,c_i,c_j}=\{0,1\}$.

| Case                   | MaxDifference                                                                                                        | MinDifference                                                                                                         | Implication                                                                       |
|------------------------|----------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------|
| $S_{r,c_i}> S_{r,c_j}$ | $1\leq (\textcolor{magenta}{Max}-1) +\textcolor{magenta}{Max}\cdot DRCC_{r,c_i,c_j}\leq(\textcolor{magenta}{Max}-1)$ | $1\leq 1 +\textcolor{magenta}{Max}\cdot DRCC_{r,c_i,c_j}\leq(\textcolor{magenta}{Max}-1)$                             | $Max\to DRCC_{r,c_i,c_j}=0$ <br/>$Min\to DRCC_{r,c_i,c_j}=0$                      |          
| $S_{r,c_i}< S_{r,c_j}$ | $1\leq -1 +\textcolor{magenta}{Max}\cdot DRCC_{r,c_i,c_j}\leq(\textcolor{magenta}{Max}-1)$                           | $1\leq -(\textcolor{magenta}{Max}-1) +\textcolor{magenta}{Max}\cdot DRCC_{r,c_i,c_j}\leq(\textcolor{magenta}{Max}-1)$ | $Max\to DRCC_{r,c_i,c_j}=1$ <br/>$Min\to DRCC_{r,c_i,c_j}=1$                      |
| $S_{r,c_i}= S_{r,c_j}$ | $1\leq 0 +\textcolor{magenta}{Max}\cdot DRCC_{r,c_i,c_j}\leq(\textcolor{magenta}{Max}-1)$                            | $1\leq 0 +\textcolor{magenta}{Max}\cdot DRCC_{r,c_i,c_j}\leq(\textcolor{magenta}{Max}-1)$                             | $Max\to DRCC_{r,c_i,c_j}=\varnothing$ <br/>$Min\to DRCC_{r,c_i,c_j}=\varnothing $ |


## In this folder:
This folder contains one class and one package that defines the structures required for solving the problem.

1. [LinkedInSudokuScraper](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/LinkedInSudoku/LinkedInSudokuScraper.java): Web Scrapper using Selenium and flow controller.
    - Scrapes the necessary information from the LinkedIn-web application.
    - Manages the flow of the program. 
    - Instantiates the SudokuMathModel.
    - Introduces the solution in the web app.
2. [SudokuMathModel](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/LinkedInSudoku/MathModel/SudokuMathModel.java): Class that implements the required OptimizationLib interface and represents a Mathematical Modeling Optimization program.
    - Instantiates its own initializer.
    - Implements runnable. 
3. [SudokuMMInitializer](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/LinkedInSudoku/MathModel/SudokuMMInitializer.java): Class that initializes the SudokuMathModel using problem specific information.
    - Create variables.
    - Create constraints.

## Results
We solve the problem using:
```` java
    @Override
    public void initializeModelRequest() {
        this.getProtoModelRequest()
                .setEnableInternalSolverOutput(true)
                .setSolverTimeLimitSeconds(60)
                .setSolverType(MPModelRequest.SolverType.SCIP_MIXED_INTEGER_PROGRAMMING);
    }
````
As solver SCIP, a max time of 1 minute and asking the solver to plot the solving info, which looks like:
`````
Loading sudoku: ...
Loaded.
Grid not found.
Checkpoint not detected.
Tutorial not found.
Building MathModel...
MathModel built.
Running MathModel...
presolving:
(round 1, fast)       12 del vars, 12 del conss, 0 add conss, 21 chg bounds, 0 chg sides, 0 chg coeffs, 0 upgd conss, 0 impls, 0 clqs
(round 2, fast)       88 del vars, 88 del conss, 0 add conss, 136 chg bounds, 145 chg sides, 117 chg coeffs, 0 upgd conss, 9 impls, 0 clqs
(round 3, fast)       125 del vars, 118 del conss, 0 add conss, 186 chg bounds, 222 chg sides, 188 chg coeffs, 0 upgd conss, 26 impls, 2 clqs
(round 4, fast)       161 del vars, 141 del conss, 0 add conss, 227 chg bounds, 253 chg sides, 209 chg coeffs, 0 upgd conss, 37 impls, 21 clqs
(round 5, fast)       202 del vars, 175 del conss, 0 add conss, 266 chg bounds, 274 chg sides, 222 chg coeffs, 0 upgd conss, 43 impls, 11 clqs
(round 6, fast)       252 del vars, 222 del conss, 0 add conss, 312 chg bounds, 310 chg sides, 241 chg coeffs, 0 upgd conss, 45 impls, 6 clqs
(round 7, fast)       299 del vars, 262 del conss, 0 add conss, 360 chg bounds, 333 chg sides, 246 chg coeffs, 0 upgd conss, 46 impls, 0 clqs
(round 8, fast)       316 del vars, 278 del conss, 0 add conss, 377 chg bounds, 344 chg sides, 248 chg coeffs, 0 upgd conss, 47 impls, 0 clqs
   Deactivated symmetry handling methods, since SCIP was built without symmetry detector (SYM=none).
presolving (9 rounds: 9 fast, 1 medium, 1 exhaustive):
 320 deleted vars, 282 deleted constraints, 0 added constraints, 381 tightened bounds, 0 added holes, 348 changed sides, 248 changed coefficients
 47 implications, 0 cliques
transformed 1/1 original solutions to the transformed problem space
Presolving Time: 0.00

SCIP Status        : problem is solved [optimal solution found]
Solving Time (sec) : 0.00
Solving Nodes      : 0
Primal Bound       : +0.00000000000000e+00 (1 solutions)
Dual Bound         : +0.00000000000000e+00
Gap                : 0.00 %
MathModel run.
Gathering result...
Result gathered.
Introducing solution...
Optimal solution introduced.
`````

## Comment:
This problem is solved using the power of Mixed Integer Linear Programming. It is just a guide that the user can follow to build its own problems.

In this example, we didn't conduct an extensive optimization of the variables and constraints created. 
Nevertheless, we were able to get in a pretty sort time the solutions for the scenario. :smirk_cat: