# LinkedInPatches
For this LinkedIn Patches problem, we use a Constraint Programing SAT to solve a colored grid, constrained by number of cells for each color, shape and location.

<img src="https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/out/LinkedInPatches/LinkedInPatches.gif"  title="LinkedInPatches example" alt="LinkedInPatches example"/>


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
            <li>$\textcolor{blue}{R} =$ Rows.</li>
            <li>$\textcolor{blue}{C} =$ Columns.</li>
            <li>$\textcolor{blue}{Cl} =$ Colors.</li>
         </ul>
      </td>
      <td>
        <ul>
            <li>$\textcolor{magenta}{ClR_{cl}} \in \textcolor{blue}{R}$ ColorRow, Row where the color hint is present, this row has to be in the color rectangle.</li>
            <li>$\textcolor{magenta}{ClC_{cl}} \in \textcolor{blue}{C}$ ColorColumn, Column where the color hint is present, this column has to be in the color rectangle.</li>
            <li>$\textcolor{magenta}{ClN_{cl}} \in \mathbb{Z}:\{1,|\textcolor{blue}{R}| \ |\textcolor{blue}{C}|\}$ ColorNumber, Number of cells associated to each color.</li>
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
           <li>$ClB_{cl,r,c} \in \{0,1\}, \; \forall cl \in \textcolor{blue}{Cl}, \;r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C}=$ (ColorBoard) Board by color.</li>
           <li>$ClRR_{cl,r} \in \{0,1\}, \; \forall cl \in \textcolor{blue}{Cl}, \; r \in \textcolor{blue}{R}=$ (ColorRowRange) Row range bay color as boolean, determines lower and upper bound of the range.</li>
           <li>$ClCR_{cl,c} \in \{0,1\}, \; \forall cl \in \textcolor{blue}{Cl}, \; c \in \textcolor{blue}{c}=$ (ColorColumnRange) Column range bay color as boolean, determines lower and upper bound of the range.</li>
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
    <td><b>ColorSize</b></td>
    <td>$$\displaystyle\sum_{\substack{r \in \color{blue}{R} \\ c \in \color{blue}{C}}}ClB_{cl,r,c} = \textcolor{magenta}{ClN_{cl}}, \; \forall cl \in \textcolor{blue}{Cl}$$</td>
    <td> $|\textcolor{blue}{Cl}|$ </td>
    <td> Forces color size.</td>
  </tr>
  <tr>
    <td><b>ColorHintPosition</b></td>
    <td>$$ClB_{cl,\textcolor{magenta}{ClR_{cl}},\textcolor{magenta}{ClC_{cl}}} = 1 ,\; \forall cl \in \textcolor{blue}{Cl}$$</td>
    <td> $|\textcolor{blue}{Cl}|$ </td>
    <td> Forces color hint position.</td>
  </tr>
  <tr>
    <td><b>ColorRowRangeLimit</b></td>
    <td>$$1 \leq \displaystyle\sum_{\begin{matrix} r \in \color{blue}{R} \end{matrix}}ClRR_{cl,r} \leq 2 ,\; \forall cl \in \textcolor{blue}{Cl}$$</td>
    <td> $|\sum_{cl \in \textcolor{blue}{Cl}}|$ </td>
    <td> Forces Color Row range to be 1 or 2. 1: start and end the same position, 2: start and end separated.</td>
  </tr> 
  <tr>
    <td><b>ColorColRangeLimit</b></td>
    <td>$$1 \leq \displaystyle\sum_{\begin{matrix} c \in \color{blue}{C} \end{matrix}}ClCR_{cl,c} \leq 2 ,\; \forall cl \in \textcolor{blue}{Cl}$$</td>
    <td> $|\sum_{cl \in \textcolor{blue}{Cl}}|$ </td>
    <td> Forces Color Column range to be 1 or 2. 1: start and end the same position, 2: start and end separated.</td>
  </tr> 
  <tr>
    <td><b>ColorBoardRowLowerBoundRelation</b></td>
    <td>$$0 \leq \displaystyle\sum_{\begin{matrix} r_i \in \{ 0,r\}\end{matrix}}ClRR_{cl,r_i} - ClB_{cl,r,c} \leq 2 ,\; \forall cl \in \textcolor{blue}{Cl}, \; r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C}$$</td>
    <td> $|\textcolor{blue}{Cl}| \cdot |\textcolor{blue}{R}| \cdot |\textcolor{blue}{C}|$ </td>
    <td> Forces Board to be 0 when the range has not started.</td>
  </tr> 
  <tr>
    <td><b>ColorBoardRowUpperBoundRelation</b></td>
    <td>$$0 \leq \displaystyle\sum_{\begin{matrix} r_i \in \{ r,\textcolor{blue}{R}\}\end{matrix}}ClRR_{cl,r_i} - ClB_{cl,r,c} \leq 2 ,\; \forall cl \in \textcolor{blue}{Cl}, \; r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C}$$</td>
    <td> $|\textcolor{blue}{Cl}| \cdot |\textcolor{blue}{R}| \cdot |\textcolor{blue}{C}|$ </td>
    <td> Forces Board to be 0 when the range has ended.</td>
  </tr> 
  <tr>
    <td><b>ColorBoardColLowerBoundRelation</b></td>
    <td>$$0 \leq \displaystyle\sum_{\begin{matrix} c_i \in \{ 0,c\}\end{matrix}}ClCR_{cl,c_i} - ClB_{cl,r,c} \leq 2 ,\; \forall cl \in \textcolor{blue}{Cl}, \; r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C}$$</td>
    <td> $|\textcolor{blue}{Cl}| \cdot |\textcolor{blue}{R}| \cdot |\textcolor{blue}{C}|$ </td>
    <td> Forces Board to be 0 when the range has not started.</td>
  </tr> 
  <tr>
    <td><b>ColorBoardColUpperBoundRelation</b></td>
    <td>$$0 \leq \displaystyle\sum_{\begin{matrix} c_i \in \{ c,\textcolor{blue}{C}\}\end{matrix}}ClCR_{cl,c_i} - ClB_{cl,r,c} \leq 2 ,\; \forall cl \in \textcolor{blue}{Cl}, \; r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C}$$</td>
    <td> $|\textcolor{blue}{Cl}| \cdot |\textcolor{blue}{R}| \cdot |\textcolor{blue}{C}|$ </td>
    <td> Forces Board to be 0 when the range has ended.</td>
  </tr> 
</table>

## In this folder:
This folder contains one class and a packages that define the structures required for solving the problem.

1. [LinkedInPatchesScraper](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/LinkedInPatches/LinkedInPatchesScraper.java): Web Scrapper using Selenium and flow controller.
    - Scrapes the necessary information from the nonograms-web application.
    - Manages the flow of the program.
    - Instantiates the LinkedInPatchesMM.
    - Introduces the solution in the web app.
2. [LinkedInPatchesMathModel](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/LinkedInPatches/MathModel/LinkedInPatchesMathModel.java): Class that implements the required OptimizationLib interface and represents a Constraint Programming Mathematical Modeling Optimization program.
    - Instantiates all its components.
3. [LinkedInPatchesMMInitializer](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/LinkedInPatches/MathModel/Initializer/LinkedInPatchesMMInitializer.java): Initializes the Mathematical Model:
    - Creates Variables.
    - Creates constraints.


## Results
For the MathModel, the set of parameters used are in the LinkedInPatchesMathModel file.

The output looks like:
`````
TODO
`````
As you see in the output example, the MathModel finds the solution pretty fast.

## Comment:
This problem is just a guide that the user can follow to build its own problems.

In this example, we didn't conduct an extensive optimization of the MathModel parameters.
Nevertheless, we were able to get in a pretty sort time the solutions for the scenario. :smirk_cat:
