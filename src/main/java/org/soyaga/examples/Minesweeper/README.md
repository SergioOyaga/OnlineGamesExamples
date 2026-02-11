# Minesweeper
For this https://minesweeper.online/ Minesweeper problem, we use a Constraint Programing SAT to solve the problem measuring the uncertainty using the callback feature.

<img src="https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/out/Minesweeper/Minesweeper.gif"  title="Minesweeper example" alt="Minesweeper example"/>

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
            <li>$\textcolor{blue}{R} =$ Rules.</li>
            <li>
                $\textcolor{blue}{C} =$ Cells.
                <ul>
                    <li>$\textcolor{blue}{C_{r}} =$ Cells by rule.</li>
                </ul>
            </li>
         </ul>
      </td>
      <td>
        <ul>
            <li>$\textcolor{magenta}{NB_{r}} \in \{1,  ..., 8\}$ Number of Bombs by rule.</li>
        </ul>
      </td>
   </tr>
</table>


<table>
   <tr>
      <th>Variables:</th>
   </tr>
   <tr>
      <td>
        <ul>
           <li>$CD_{c} \in \{0,1\}, \; \forall c \in \textcolor{blue}{C}$ (CellDecision) whether a bomb has to be placed in a specific cell.</li>
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
    <td><b>Value</b></td>
    <td>$$\sum_{c \in \textcolor{blue}{C_{r}}} CD_{c} == \textcolor{magenta}{NB_{r}},\; \forall r \in \textcolor{blue}{R}$$</td>
    <td> $|\sum_{r \in \textcolor{blue}{R}}|$ </td>
    <td> Value of the bombs by rule.</td>
  </tr>
</table>

### Uncertainty:
To measure the uncertainty we follow these steps:
1. Store all feasible solutions found by the SAT solver.
2. Compute how certain a bomb is in each cell, by adding the variable value of each solution and dividing it by the number of solutions found. E.j: 
   - Cell_0_0 = 0.0 implies it is completely safe, in al solutions found is always a 0
   - Cell_1_1 = 1.0 implies it is for sure a bomb, in al solutions found is always a 1
   - Cell_2_2 = 0.3 implies it is statistically safe, but there are solutions where it is a bomb, for example 3 solutions where it is a bomb and 7 solutions where it is not.
3. Introduce only certain values if available.
4. If not certain values are available, then we compute the average solution bomb. That is, how many bombs are being used in all the solutions by average.
5. We compute the risk of the cells that are not in the problem as remaining_bombs/remaining_cells.
6. We select the less probable to be bombs and click in a random one of those.

## In this folder:
This folder contains one class and a packages that define the structures required for solving the problem.

1. [MinesweeperScraper](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Minesweeper/MinesweeperScraper.java): Web Scrapper using Selenium and flow controller.
    - Scrapes the necessary information from the Minesweeper-web application.
    - Manages the flow of the program.
    - Instantiates the MinesweeperMM.
    - Introduces the solution in the web app.
2. [MinesweeperMathModel](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Minesweeper/MathModel/MinesweeperMathModel.java): Class that implements the required OptimizationLib interface and represents a Constraint Programming Mathematical Modeling Optimization program.
    - Instantiates all its components.
    - Includes AllSolutionsCallback, to measure uncertainty.
3. [MinesweeperMMInitializer](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Minesweeper/MathModel/Initializer/MinesweeperMMInitializer.java): Initializes the Mathematical Model:
    - Creates Variables.
    - Creates Constraints.
4. [Cell](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Minesweeper/Graph/Cell.java): Object that represents a minesweeper cell.


## Results
For the MathModel, the set of parameters used are in the MinesweeperMathModel file.

The output looks like:
`````
Sorry, but I don't have the outputs... My IP got shadowbanned and I don't have a VPN available right now...
So an advice, If you are going to use any of my optimization examples to cheat at games, please don't. That's not the intend of this. 
`````
As you see in the output example, the MathModel finds the solution strategy step by step pretty fast.

## Comment:
This problem is just a guide that the user can follow to build its own problems.

In this example, we didn't conduct an extensive optimization of the MathModel parameters.
Nevertheless, we were able to get in a pretty sort time the solutions for the scenario. :smirk_cat:
