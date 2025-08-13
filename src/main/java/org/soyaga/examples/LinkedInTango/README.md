# Tango
For this LinkedIn Tango problem, we use MIP to formulate and solve a linear problem.
In the [mathematical formulation](#mathematical-formulation) we can see how the problem sets, parameters, variables and 
constraints are defined.

<img src="https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/out/LinkedInTango/LinkedInTango.gif"  title="Tango example" alt="Tango example"/>

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
            <li>$\textcolor{blue}{FC} =$ FixedCells in the board.
            </li>
         </ul>
      </td>
      <td>
        <ul>
           <li>
                $\textcolor{magenta}{FCV_{fc}} \in \{0,1\} \vee \{ (r, c) \mid r \in \textcolor{blue}{R}, c \in \textcolor{blue}{C}\}, \; = \textbf{(Fixed Cell)}$ Cells type.
           </li>
           <li>
                $\textcolor{magenta}{CR_{r,c, i}} \in \{0,1\} \vee \{ (r, c) \mid r \in \textcolor{blue}{R}, c \in \textcolor{blue}{C}, i \in N\}, \; = \textbf{(Fixed Cell)}$ Cell relation with other.
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
           Grid Cell Type
      </td>
      <td>
           $GCT_{r, c} \in \mathbb{Z}:\{0,1\}, \; r \in \textcolor{blue}{R}, \; \forall c \in \textcolor{blue}{C} $
      </td>
      <td>
        Grid Cell Type. Binary variable where 1 implies "sun" :sunny: and 0 "moon" :first_quarter_moon_with_face:
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
    <td><b>NotThreeConsecutiveByRow</b></td>
    <td>$$0 < GCT_{r,c} + GCT_{r,c+1} + GCT_{r,c+2}<3,\; \forall c \in \textcolor{blue}{C}-2$$</td>
    <td> $|\textcolor{blue}{C}-2|$ </td>
    <td> At most two ones every three consecutive columns, for each row.</td>
  </tr>
  <tr>
    <td><b>NotThreeConsecutiveByCol</b></td>
    <td>$$0 < GCT_{r,c} + GCT_{r+1,c} + GCT_{r+2,c}<3,\; \forall r \in \textcolor{blue}{R}-2$$</td>
    <td> $|\textcolor{blue}{R}-2|$ </td>
    <td> At most two ones every three consecutive rows, for each column.</td>
  </tr>
  <tr>
    <td><b>SameZeroesAndOnesByRow</b></td>
    <td>$$\sum_{c \in \textcolor{blue}{C}} GCT_{r,c}==|\textcolor{blue}{C}/2|,\; \forall r \in \textcolor{blue}{R}$$</td>
    <td> $|\textcolor{blue}{R}|$ </td>
    <td> Each row must contain as many ones as zeros.</td>
  </tr>
  <tr>
    <td><b>SameZeroesAndOnesByCol</b></td>
    <td>$$\sum_{r \in \textcolor{blue}{R}} GCT_{r,c}==|\textcolor{blue}{R}/2|,\; \forall c \in \textcolor{blue}{C}$$</td>
    <td> $|\textcolor{blue}{C}|$ </td>
    <td> Each column must contain as many ones as zeros.</td>
  </tr>
  <tr>
    <td><b>FixedCells</b></td>
    <td>$$\ GCT_{r,c} == \textcolor{magenta}{FCV_{r,c}},\; \vee c \in \textcolor{blue}{C}, \; \vee r \in \textcolor{blue}{R}$$</td>
    <td> $|\textcolor{blue}{C} \cdot \textcolor{blue}{R}|$ </td>
    <td> Follow cell assignation.</td>
  </tr>
  <tr>
    <td><b>FixedRelations</b></td>
    <td>$$\ GCT_{r,c} == \textcolor{magenta}{CR_{r,c,1}} \wedge \textcolor{magenta}{CR_{r,c,2}} \wedge \cdot\cdot\cdot \wedge\textcolor{magenta}{CR_{r,c,n}},\; \forall c \in \textcolor{blue}{C}, \; \forall r \in \textcolor{blue}{R}$$</td>
    <td> $4 \cdot |\textcolor{blue}{C} \cdot \textcolor{blue}{R}|$ </td>
    <td> Follow the fixed relations between cells.</td>
  </tr>
</table>

## In this folder:
This folder contains one class and one package that defines the structures required for solving the problem.

1. [LinkedInTangoScraper](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/LinkedInTango/LinkedInTangoScraper.java): Web Scrapper using Selenium and flow controller.
    - Scrapes the necessary information from LinkedIn-web application.
    - Manages the flow of the program.
    - Instantiates the TangoMathModel.
    - Introduces the solution in the web app.
2. [TangoMathModel](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/LinkedInTango/MathModel/TangoMathModel.java): Class that implements the required OptimizationLib interface and represents a Mathematical Modeling Optimization program.
    - Instantiates its own initializer.
    - Implements runnable.
3. [TangoMMInitializer](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/LinkedInTango/MathModel/TangoMMInitializer.java): Class that initializes the TangoMathModel using problem specific information.
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
As solver SCIP, a max time of 10minutes and asking the solver to plot the solving info, which looks like:
`````
presolving:
(round 1, fast)       30 del vars, 67 del conss, 0 add conss, 12 chg bounds, 3 chg sides, 0 chg coeffs, 0 upgd conss, 0 impls, 1 clqs
(round 2, fast)       36 del vars, 94 del conss, 0 add conss, 18 chg bounds, 4 chg sides, 0 chg coeffs, 0 upgd conss, 0 impls, 0 clqs
   Deactivated symmetry handling methods, since SCIP was built without symmetry detector (SYM=none).
presolving (3 rounds: 3 fast, 1 medium, 1 exhaustive):
 36 deleted vars, 96 deleted constraints, 0 added constraints, 18 tightened bounds, 0 added holes, 4 changed sides, 0 changed coefficients
 0 implications, 0 cliques
transformed 1/1 original solutions to the transformed problem space
Presolving Time: 0.00

SCIP Status        : problem is solved [optimal solution found]
Solving Time (sec) : 0.00
Solving Nodes      : 0
Primal Bound       : +0.00000000000000e+00 (1 solutions)
Dual Bound         : +0.00000000000000e+00
Gap                : 0.00 %
`````

## Comment:
This problem is solved using the power of Mixed Integer Linear Programming. It is just a guide that the user can follow to build its own problems.

In this example, we didn't conduct an extensive optimization of the variables and constraints created. 
Nevertheless, we were able to get in a pretty sort time the solutions for the scenario. :smirk_cat: