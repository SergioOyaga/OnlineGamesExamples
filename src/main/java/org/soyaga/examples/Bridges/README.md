# Bridges
For this https://www.puzzle-bridges.com/ Bridges problem, we use a Constraint Programing SAT to formulate the problem, using the callback feature to exit the solving process under certain condition.

<img src="https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/out/Bridges/Bridges.gif"  title="Bridges example" alt="Bridges example"/>

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
            <li>
                $\textcolor{blue}{B} =$ Bridges.
                <ul>
                    <li>$\textcolor{blue}{B_{vr}} =$ Bridges by value rule.</li>
                    <li>$\textcolor{blue}{VB} =$ Vertical Bridges.</li>
                    <li>
                        $\textcolor{blue}{HB} =$ Horizontal Bridges.
                        <ul>
                            <li>$\textcolor{blue}{HB_{vb}} =$ Horizontal Bridges by Vertical Bridges that cross.</li>
                        </ul>
                    </li>
                </ul>
            </li>
            <li>
                $\textcolor{blue}{VR} =$ ValueRule.
            </li>
         </ul>
      </td>
      <td>
        <ul>
            <li>$\textcolor{magenta}{V_{vr}} \in \{1,  ..., 8\}$ Number of bridges by value rule.</li>
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
           <li>$BU_{b} \in \{0,1\}, \; \forall b \in \textcolor{blue}{B}$ (BridgeUtilized) whether a has being utilized.</li>
           <li>$BN_{b} \in \{0,2\}, \; \forall b \in \textcolor{blue}{B}$ (BridgeNumber) How many conexions a bridge has.</li>
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
    <td><b>RelateBNandBU</b></td>
    <td>$$0 \leq 2 \cdot BN_{b} - BU_{b} \leq 1,\; \forall b \in \textcolor{blue}{B}$$</td>
    <td> $|\sum_{b \in \textcolor{blue}{B}}|$ </td>
    <td> Relates boolean decision with number decision..</td>
  </tr>
  <tr>
    <td><b>Value</b></td>
    <td>$$\sum_{b \in \textcolor{blue}{B_{vr}}} BN_{b} == \textcolor{magenta}{V_{vr}},\; \forall vr \in \textcolor{blue}{VR}$$</td>
    <td> $|\sum_{vr \in \textcolor{blue}{VR}}|$ </td>
    <td> Value of bridges utilized constraint.</td>
  </tr>
  <tr>
    <td><b>Crossing</b></td>
    <td>$$0 \leq BU_{vb} + BU_{hb} \leq 1,\; \forall vb \in \textcolor{blue}{VB}, hb \in \textcolor{blue}{HB_{vb}}$$</td>
    <td> $|\sum_{vb \in \textcolor{blue}{VB}} \cdot \sum_{hb \in \textcolor{blue}{HB_{vb}}}|$ </td>
    <td> Make sure crossing  bridges are not selected simultaneously. </td>
  </tr>
</table>

### Callback:
The reader might notice that in any point we are enforcing a unique connected solution. The relevance of this example relais on this fact. 

Modelling this rule can be tedious and might overcomplicate the mathematical model.

On the other hand, this problem is one of those problems where we won't expect many different solution to appear, and naturally the solution will tend to include this "implicit" rule.

As consequence, we can simply model the pretty simple model already defined and evaluate whether the achieved solution complains with the unique connected solution rule. To do so, we rely on the callback feature. measuring this connectivity for each solution found. The solver will stop searching for feasible solutions as soon as the connectivity is unique. That simple logic is done not mathematically but programmatically in the callback logic.

## In this folder:
This folder contains one class and a packages that define the structures required for solving the problem.

1. [BridgesScraper](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Bridges/BridgesScraper.java): Web Scrapper using Selenium and flow controller.
    - Scrapes the necessary information from the Bridges-web application.
    - Manages the flow of the program.
    - Instantiates the BridgesMM.
    - Introduces the solution in the web app.
2. [BridgesMathModel](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Bridges/MathModel/BridgesMathModel.java): Class that implements the required OptimizationLib interface and represents a Constraint Programming Mathematical Modeling Optimization program.
    - Instantiates all its components.
    - Includes ConnectedSolutionsCallback, to exit when the right solution is found.
3. [BridgesMMInitializer](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Bridges/MathModel/Initializer/BridgesMMInitializer.java): Initializes the Mathematical Model:
    - Creates Variables.
    - Creates Constraints.
4. [ConnectedSolutionCallBack](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Bridges/MathModel/Initializer/CallBack/ConnectedSolutionCallBack.java): Callback that stops the search once the solution is fully connected.
5. [Bridge](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Bridges/Graph/Bridge.Bridge): Object that represents a Bridges bridge.
6. [Rule](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Bridges/Graph/Rule.Bridge): Object that represents a numbered rule of connected bridges.


## Results
For the MathModel, the set of parameters used are in the BridgesMathModel file.

The output looks like:
`````
Loading https://www.puzzle-bridges.com/: ...
Loaded.
Refreshing in case it did not load...
Refreshed.
Accepting cookies...
Cookies accepted.
Scrolling...
Not scrolled.
Retrieving grid properties...
Grid properties retrieved.
Building CP Model...
CP Model built.
Running CP Model...

Starting CP-SAT solver v9.7.2996
Parameters: max_time_in_seconds: 120 log_search_progress: true enumerate_all_solutions: true log_to_stdout: true
Setting number of workers to 1

Initial satisfaction model 'BridgesSATModelProto': (model_fingerprint: 0x80c2392a4d20b3c5)
#Variables: 650
  - 325 Booleans in [0,1]
  - 325 in [0,2]
#kLinear2: 586
#kLinear3: 76
#kLinearN: 100 (#terms: 400)

Starting presolve at 0.00s
[ExtractEncodingFromLinear] #potential_supersets=0 #potential_subsets=0 #at_most_one_encodings=0 #exactly_one_encodings=0 #unique_terms=0 #multiple_terms=0 #literals=0 time=6.9e-06s
[Probing] implications and bool_or (work_done=76).
[DetectDuplicateConstraints] #duplicates=0 #without_enforcements=0 time=9.7e-06s
[DetectDominatedLinearConstraints] #relevant_constraints=0 #work_done=0 #num_inclusions=0 #num_redundant=0 time=2.1e-06s
[ProcessSetPPC] #relevant_constraints=0 #num_inclusions=0 work=0 time=4.9e-06s
[FindBigHorizontalLinearOverlap] #blocks=0 #saved_nz=0 #linears=0 #work_done=0/1e+09 time=6e-06s
[FindBigVerticalLinearOverlap] #blocks=0 #nz_reduction=0 #work_done=260 time=1.04e-05s
[MergeClauses] #num_collisions=0 #num_merges=0 #num_saved_literals=0 work=0/100000000 time=6e-06s
[Probing] implications and bool_or (work_done=76).
[DetectDuplicateConstraints] #duplicates=0 #without_enforcements=0 time=4.2e-06s
[DetectDominatedLinearConstraints] #relevant_constraints=0 #work_done=0 #num_inclusions=0 #num_redundant=0 time=1.3e-06s
[ProcessSetPPC] #relevant_constraints=0 #num_inclusions=0 work=0 time=2.6e-06s
[FindBigHorizontalLinearOverlap] #blocks=0 #saved_nz=0 #linears=0 #work_done=0/1e+09 time=1.3e-06s
[FindBigVerticalLinearOverlap] #blocks=0 #nz_reduction=0 #work_done=260 time=9.6e-06s
[MergeClauses] #num_collisions=0 #num_merges=0 #num_saved_literals=0 work=0/100000000 time=2.2e-06s
125 affine relations still in the model.

Presolve summary:
  - 167 affine relations were detected.
  - rule 'TODO linear2: contains a Boolean.' was applied 500 times.
  - rule 'affine: new relation' was applied 167 times.
  - rule 'bool_and: always false' was applied 117 times.
  - rule 'bool_and: non-reified.' was applied 113 times.
  - rule 'bool_or: implications' was applied 250 times.
  - rule 'bool_or: only one literal' was applied 284 times.
  - rule 'deductions: 600 stored' was applied 1 time.
  - rule 'enforcement: false literal' was applied 195 times.
  - rule 'enforcement: true literal' was applied 186 times.
  - rule 'exactly_one: removed literals' was applied 37 times.
  - rule 'exactly_one: satisfied' was applied 25 times.
  - rule 'exactly_one: size one' was applied 3 times.
  - rule 'exactly_one: size two' was applied 29 times.
  - rule 'linear1: is boolean implication' was applied 198 times.
  - rule 'linear1: transformed to implication' was applied 4 times.
  - rule 'linear2: contains a Boolean.' was applied 325 times.
  - rule 'linear: always true' was applied 179 times.
  - rule 'linear: empty' was applied 97 times.
  - rule 'linear: enforcement literal in expression' was applied 4 times.
  - rule 'linear: fixed or dup variables' was applied 461 times.
  - rule 'linear: infeasible' was applied 167 times.
  - rule 'linear: negative clause' was applied 250 times.
  - rule 'linear: negative equal one' was applied 20 times.
  - rule 'linear: positive equal one' was applied 37 times.
  - rule 'linear: reduced variable domains' was applied 197 times.
  - rule 'linear: remapped using affine relations' was applied 358 times.
  - rule 'linear: simplified rhs' was applied 99 times.
  - rule 'presolve: 0 unused variables removed.' was applied 1 time.
  - rule 'presolve: iteration' was applied 2 times.
  - rule 'variables with 2 values: create encoding literal' was applied 42 times.
  - rule 'variables with 2 values: new affine relation' was applied 45 times.
  - rule 'variables with 2 values: register other encoding' was applied 3 times.
  - rule 'variables: canonicalize domain' was applied 13 times.
  - rule 'variables: detect fully reified value encoding' was applied 221 times.
  - rule 'variables: detect half reified value encoding' was applied 446 times.

Presolved satisfaction model 'BridgesSATModelProto': (model_fingerprint: 0x51111bec00b17567)
#Variables: 705
  - 38 Booleans in [0,1]
  - 25 in [0,2]
  - 62 in [1,2]
  - 580 constants in {0,1,2} 
#kLinear2: 125

Preloading model.
[Symmetry] Graph for symmetry has 866 nodes and 286 arcs.
[Symmetry] Symmetry computation done. time: 0.0002706 dtime: 0.00018077
[Symmetry] #generators: 54, average support size: 3.88889
[Symmetry] Found orbitope of size 3 x 3

Starting to load the model at 0.01s
[Probing] deterministic_time: 0 (limit: 1) wall_time: 4e-07 (0/0)
[LinearRelaxationBeforeCliqueExpansion] #linear:125 #at_most_ones:0
[FinalLinearRelaxation] #linear:125 #cut_generators:0
Initial num_bool: 562

Starting sequential search at 0.01s
#1       0.01s  fixed_bools:0/562
SolutionFound
#Done    0.01s 

LP statistics:
  final dimension: 0 rows, 4 columns, 0 entries with magnitude in [0.000000e+00, 0.000000e+00]
  total number of simplex iterations: 0
  total num cut propagation: 0
  total num eq cut propagation: 0
  total num cut overflow: 0
  total num bad cuts: 0
  total num adjust: 0
  total num scaling issues: 0
  num solves: 
    - #OPTIMAL: 4
  managed constraints: 1
  shortened constraints: 3
  num simplifications: 1
  total cuts added: 0 (out of 0 calls)
LP statistics:
...
...
CpSolverResponse summary:
status: OPTIMAL
objective: NA
best_bound: NA
integers: 251
booleans: 562
conflicts: 0
branches: 0
propagations: 562
integer_propagations: 94
restarts: 0
lp_iterations: 0
walltime: 0.0149847
usertime: 0.0149848
deterministic_time: 9.472e-05
gap_integral: 0
solution_fingerprint: 0x5e7ac77dbbcaec15

CP Model run.
Scrolling...
Scrolled.
`````
As you see in the output example, the MathModel finds the solution pretty fast.

## Comment:
This problem is just a guide that the user can follow to build its own problems.

In this example, we didn't conduct an extensive optimization of the MathModel parameters.
Nevertheless, we were able to get in a pretty sort time the solutions for the scenario. :smirk_cat:
