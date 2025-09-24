# NonoGrams
For this https://www.nonograms.org NonoGrams problem, we use a Constraint Programing SAT to solve a colored grid, constrained by rows and columns.

<img src="https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/out/NonoGrams/NonoGrams.gif"  title="NonoGrams example" alt="NonoGrams example"/>


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
            <li>$\textcolor{blue}{RP_{r}} =$ Row Pieces by row.</li>
            <li>$\textcolor{blue}{CP_{c}} =$ Col Pieces by col.</li>
            <li>$\textcolor{blue}{R} =$ Rows.</li>
            <li>$\textcolor{blue}{C} =$ Cols.</li>
            <li>$\textcolor{blue}{Cl} =$ Colors.</li>
            <li>$\textcolor{blue}{Pr} =$ Priorities.</li>
            <li>$\textcolor{blue}{RPPr_{r,pr}} =$ Row Pieces by row by priority.</li>
            <li>$\textcolor{blue}{CPPr_{c,pr}} =$ Col Pieces by col by priority.</li>
         </ul>
      </td>
      <td>
        <ul>
            <li>$\textcolor{magenta}{PCl_{p}} \in \{1,  ..., N_{Colors}\}$ Colors by piece represented as an integer.</li>
            <li>$\textcolor{magenta}{PS_{p}} \in \mathbb{Z}$ Size by piece.</li>
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
           <li>$RPS_{r,p} \in \{0,\textcolor{blue}{C}\}, \; \forall r \in \textcolor{blue}{R}, \; p \in \textcolor{blue}{RP_{r}}=$ (RowPieceStart) Column number where the row-piece starts.</li>
           <li>$RPE_{r,p} \in \{0,\textcolor{blue}{C}\}, \; \forall r \in \textcolor{blue}{R}, \; p \in \textcolor{blue}{RP_{r}}=$ (RowPieceEnd) Column number where the row-piece ends.</li>
           <li>$CPS_{c,p} \in \{0,\textcolor{blue}{R}\}, \; \forall c \in \textcolor{blue}{C}, \; p \in \textcolor{blue}{CP_{c}}=$ (ColPieceStart) Row number where the column-piece starts.</li>
           <li>$CPE_{c,p} \in \{0,\textcolor{blue}{R}\}, \; \forall c \in \textcolor{blue}{C}, \; p \in \textcolor{blue}{CP_{c}}=$ (ColPieceEnd) Row number where the column-piece ends.</li>
           <li>$RPX_{r,p, c} \in \mathbb{Z}:\{0,1\}, \; \forall r \in \textcolor{blue}{R}, \; p \in \textcolor{blue}{RP_{r}}, \; c \in \textcolor{blue}{C} =$ (RowPieceX) Assignation of a row-piece to columns. 1 assigned, 0 not assigned.</li>
           <li>$CPX_{c,p, r} \in \mathbb{Z}:\{0,1\}, \; \forall c \in \textcolor{blue}{C}, \; p \in \textcolor{blue}{CP_{c}}, \; r \in \textcolor{blue}{R} =$ (ColPieceX) Assignation of a column-piece to rows. 1 assigned, 0 not assigned.</li>
           <li>$B_{r, c} \in \mathbb{Z}:\{0,\textcolor{blue}{Cl}\}, \; \forall r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C} =$ (Board) Board result of assigning pieces.</li>
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
    <td><b>RowPieceSize</b></td>
    <td>$$RPE_{r,p} - RPS_{r,p} == \textcolor{magenta}{PS_{p}}-1 ,\; \forall r \in \textcolor{blue}{R}, \; p \in \textcolor{blue}{RP_{r}}$$</td>
    <td> $|\sum_{r \in \textcolor{blue}{R}}\textcolor{blue}{RP_{r}}|$ </td>
    <td> Forces rowPiece size.</td>
  </tr>
  <tr>
    <td><b>ColPieceSize</b></td>
    <td>$$CPE_{c,p} - CPS_{c,p} == \textcolor{magenta}{PS_{p}}-1 ,\; \forall c \in \textcolor{blue}{C}, \; p \in \textcolor{blue}{CP_{c}}$$</td>
    <td> $|\sum_{c \in \textcolor{blue}{C}}\textcolor{blue}{CP_{c}}|$ </td>
    <td> Forces colPiece size.</td>
  </tr> 
  <tr>
    <td><b>RowPieceStartXLink</b></td>
    <td>$$RPS_{r,p}-Big \cdot (1-RPX_{r,p, c}) \leq c,\; \forall r \in \textcolor{blue}{R}, \; p \in \textcolor{blue}{RP_{r}}, \; c \in \textcolor{blue}{C}$$</td>
    <td> $|\sum_{r \in \textcolor{blue}{R}}\textcolor{blue}{RP_{r}} \cdot \textcolor{blue}{C}|$ </td>
    <td> Relates RowPiece start with assignation.</td>
  </tr>
  <tr>
    <td><b>RowPieceEndXLink</b></td>
    <td>$$RPE_{r,p}-Big \cdot (1-RPX_{r,p, c}) > c,\; \forall r \in \textcolor{blue}{R}, \; p \in \textcolor{blue}{RP_{r}}, \; c \in \textcolor{blue}{C}$$</td>
    <td> $|\sum_{r \in \textcolor{blue}{R}}\textcolor{blue}{RP_{r}} \cdot \textcolor{blue}{C}|$ </td>
    <td> Relates RowPiece end with assignation.</td>
  </tr>
  <tr>
    <td><b>ColPieceStartXLink</b></td>
    <td>$$CPS_{c,p}-Big \cdot (1-CPX_{c,p, r}) \leq r,\; \forall c \in \textcolor{blue}{C}, \; p \in \textcolor{blue}{CP_{c}}, \; r \in \textcolor{blue}{R}$$</td>
    <td> $|\sum_{c \in \textcolor{blue}{C}}\textcolor{blue}{CP_{r}} \cdot \textcolor{blue}{R}|$ </td>
    <td> Relates ColPiece start with assignation.</td>
  </tr>
  <tr>
    <td><b>ColPieceEndXLink</b></td>
    <td>$$CPE_{c,p}-Big \cdot (1-CPX_{c,p, r}) > r,\; \forall c \in \textcolor{blue}{C}, \; p \in \textcolor{blue}{CP_{c}}, \; r \in \textcolor{blue}{R}$$</td>
    <td> $|\sum_{c \in \textcolor{blue}{C}}\textcolor{blue}{CP_{r}} \cdot \textcolor{blue}{R}|$ </td>
    <td> Relates ColPiece end with assignation.</td>
  </tr>
  <tr>
    <td><b>ForceRowAssignationSize</b></td>
    <td>$$\sum_{c \in \textcolor{blue}{C}} RPX_{r, p, c}== \textcolor{magenta}{PS_{p}},\; \forall r \in \textcolor{blue}{R}, \; p \in \textcolor{blue}{RP_{r}}$$</td>
    <td> $|\sum_{r \in \textcolor{blue}{R}}\textcolor{blue}{RP_{r}}|$ </td>
    <td> RowPiece assignation size.</td>
  </tr>
  <tr>
    <td><b>ForceColAssignationSize</b></td>
    <td>$$\sum_{r \in \textcolor{blue}{R}} CPX_{c, p, r}== \textcolor{magenta}{PS_{p}},\; \forall c \in \textcolor{blue}{C}, \; p \in \textcolor{blue}{CP_{c}}$$</td>
    <td> $|\sum_{c \in \textcolor{blue}{C}}\textcolor{blue}{CP_{r}}|$ </td>
    <td> ColPiece assignation size.</td>
  </tr>
  <tr>
    <td><b>RowToBoard</b></td>
    <td>$$B_{r, c} - \sum_{p \in \textcolor{blue}{RP_{r}}} \textcolor{magenta}{PCl_{p}} \cdot CPX_{r, p, c}== 0,\; \forall r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C}$$</td>
    <td> $|\textcolor{blue}{R} \cdot \textcolor{blue}{C}|$ </td>
    <td> Relation to board from row pieces.</td>
  </tr>
  <tr>
    <td><b>ColToBoard</b></td>
    <td>$$B_{r, c} - \sum_{p \in \textcolor{blue}{CP_{c}}} \textcolor{magenta}{PCl_{p}} \cdot RPX_{c, p, r}== 0,\; \forall r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C}$$</td>
    <td> $|\textcolor{blue}{R} \cdot \textcolor{blue}{C}|$ </td>
    <td> Relation to board from column pieces.</td>
  </tr>
  <tr>
    <td><b>RowPieceOrder</b></td>
    <td>$$RPE_{r,p1} - RPS_{r,p2}> 0,\; \forall p1 \in \textcolor{blue}{RPPr_{r,pr}},\; r \in \textcolor{blue}{R}, \; pr \in \textcolor{blue}{PR-1} \mid p2= \textcolor{blue}{RPPr_{r,pr+1}}$$</td>
    <td> $|\textcolor{blue}{R} \cdot \textcolor{blue}{PR-1}|$ </td>
    <td> Priority row piece relation.</td>
  </tr>
  <tr>
    <td><b>ColPieceOrder</b></td>
    <td>$$CPE_{c,p1} - CPS_{c,p2}> 0,\; \forall p1 \in \textcolor{blue}{CPPr_{c,pr}},\; c \in \textcolor{blue}{C}, \; pr \in \textcolor{blue}{PR-1} \mid p2= \textcolor{blue}{CPPr_{c,pr+1}}$$</td>
    <td> $|\textcolor{blue}{C} \cdot \textcolor{blue}{PR-1}|$ </td>
    <td> Priority col piece relation.</td>
  </tr>
</table>

## In this folder:
This folder contains one class and a packages that define the structures required for solving the problem.

1. [NonoGramsScraper](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/NonoGrams/NonoGramsScraper.java): Web Scrapper using Selenium and flow controller.
    - Scrapes the necessary information from the nonograms-web application.
    - Manages the flow of the program.
    - Instantiates the NonoGramsMM.
    - Introduces the solution in the web app.
2. [NonoGramsMathModel](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/NonoGrams/MathModel/NonoGramsMathModel.java): Class that implements the required OptimizationLib interface and represents a Constraint Programming Mathematical Modeling Optimization program.
    - Instantiates all its components.
    - Implements callable.
3. [NonoGramsInitializer](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/NonoGrams/MathModel/Initializer/NonoGramsInitializer.java): Initializes the Mathematical Model:
    - Creates Variables.
    - Creates constraints.


## Results
For the MathModel, the set of parameters used are in the NonoGramsMathModel file.

MM parameters are in NonoGramsMathModel.

The output looks like:
`````
`````
As you see in the output example, the MathModel finds the solution pretty fast.

## Comment:
This problem is just a guide that the user can follow to build its own problems.

In this example, we didn't conduct an extensive optimization of the MathModel parameters.
Nevertheless, we were able to get in a pretty sort time the solutions for the scenario. :smirk_cat:
