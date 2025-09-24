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
    <td>$$RPE_{r,p}+Big \cdot (1-RPX_{r,p, c}) \geq c,\; \forall r \in \textcolor{blue}{R}, \; p \in \textcolor{blue}{RP_{r}}, \; c \in \textcolor{blue}{C}$$</td>
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
    <td>$$CPE_{c,p}+Big \cdot (1-CPX_{c,p, r}) \geq r,\; \forall c \in \textcolor{blue}{C}, \; p \in \textcolor{blue}{CP_{c}}, \; r \in \textcolor{blue}{R}$$</td>
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
    <td>$$RPS_{r,p2} - RPE_{r,p1} > 
\begin{cases} 
0, \; if\textcolor{magenta}{PCl_{p1}} \neq \textcolor{magenta}{PCl_{p2}} \\ 
1, \; if\textcolor{magenta}{PCl_{p1}} = \textcolor{magenta}{PCl_{p2}}  
\end{cases}
,\; \forall p1 \in \textcolor{blue}{RPPr_{r,pr}},\; r \in \textcolor{blue}{R}, \; pr \in \textcolor{blue}{PR-1} \mid p2= \textcolor{blue}{RPPr_{r,pr+1}}$$</td>
    <td> $|\textcolor{blue}{R} \cdot \textcolor{blue}{PR-1}|$ </td>
    <td> Priority row piece relation.</td>
  </tr>
  <tr>
    <td><b>ColPieceOrder</b></td>
    <td>$$CPS_{c,p2} - CPE_{c,p1} > 
\begin{cases} 
0, \; if\textcolor{magenta}{PCl_{p1}} \neq \textcolor{magenta}{PCl_{p2}} \\ 
1, \; if\textcolor{magenta}{PCl_{p1}} = \textcolor{magenta}{PCl_{p2}}  
\end{cases}
,\; \forall p1 \in \textcolor{blue}{CPPr_{c,pr}},\; c \in \textcolor{blue}{C}, \; pr \in \textcolor{blue}{PR-1} \mid p2= \textcolor{blue}{CPPr_{c,pr+1}}$$</td>
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
3. [NonoGramsMMInitializer](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/NonoGrams/MathModel/Initializer/NonoGramsMMInitializer.java): Initializes the Mathematical Model:
    - Creates Variables.
    - Creates constraints.


## Results
For the MathModel, the set of parameters used are in the NonoGramsMathModel file.

The output looks like:
`````
Loading nonogram: ...
Nonograms loaded.
Refreshing in case it did not load...
Refreshed.
Selecting random nonogram...
Nonogram selected.
Scaling image...
Image scaled.
Retrieving size...
Size retrieved.
Computing colors...
Colors computed.
Retrieving board
    Retrieving cells...
    Cells retrieved.
    Retrieving row constraints...
    Row constraints retrieved.
    Retrieving col constraints...
    Col constraints retrieved.
Hiding details...
Details hidden.
Creating MathModel SAT...
MathModel SAT created.
Computing CP...

Starting CP-SAT solver v9.7.2996
Parameters: max_time_in_seconds: 600 log_search_progress: true log_to_stdout: true
Setting number of workers to 16

Initial satisfaction model 'NonoSATModelProto': (model_fingerprint: 0x682c22af160bfee5)
#Variables: 74'732
  - 69'532 Booleans in [0,1]
  - 2'300 in [0,5]
  - 1'484 in [0,45]
  - 1'416 in [0,49]
#kLinear2: 142'060
#kLinearN: 5'858 (#terms: 143'280)

Starting presolve at 0.07s
[ExtractEncodingFromLinear] #potential_supersets=1301 #potential_subsets=3216 #at_most_one_encodings=0 #exactly_one_encodings=0 #unique_terms=0 #multiple_terms=0 #literals=0 time=0.00378079s
[Symmetry] Graph for symmetry has 171916 nodes and 220840 arcs.
[Symmetry] Symmetry computation done. time: 0.0174033 dtime: 0.0339457
[Probing] implications and bool_or (work_done=106446).
[DetectDuplicateConstraints] #duplicates=0 #without_enforcements=0 time=0.00503266s
[DetectDominatedLinearConstraints] #relevant_constraints=0 #work_done=0 #num_inclusions=0 #num_redundant=0 time=0.00177322s
[ProcessSetPPC] #relevant_constraints=0 #num_inclusions=0 work=0 time=0.0036982s
[FindBigHorizontalLinearOverlap] #blocks=0 #saved_nz=0 #linears=0 #work_done=0/1e+09 time=0.0019026s
[FindBigVerticalLinearOverlap] #blocks=0 #nz_reduction=0 #work_done=0 time=0.00129176s
[MergeClauses] #num_collisions=0 #num_merges=0 #num_saved_literals=0 work=0/100000000 time=0.00195732s
[Symmetry] Graph for symmetry has 74744 nodes and 0 arcs.
[Symmetry] Symmetry computation done. time: 0.00266986 dtime: 0.00448464
[DetectDuplicateConstraints] #duplicates=0 #without_enforcements=0 time=0.00497433s
[DetectDominatedLinearConstraints] #relevant_constraints=0 #work_done=0 #num_inclusions=0 #num_redundant=0 time=0.00161296s
[ProcessSetPPC] #relevant_constraints=0 #num_inclusions=0 work=0 time=0.00362048s
[FindBigHorizontalLinearOverlap] #blocks=0 #saved_nz=0 #linears=0 #work_done=0/1e+09 time=0.00166238s
[FindBigVerticalLinearOverlap] #blocks=0 #nz_reduction=0 #work_done=0 time=0.00127416s
[MergeClauses] #num_collisions=0 #num_merges=0 #num_saved_literals=0 work=0/100000000 time=0.00191399s

Presolve summary:
  - 1896 affine relations were detected.
  - rule 'TODO variables: only used in linear1.' was applied 4 times.
  - rule 'affine: new relation' was applied 1896 times.
  - rule 'at_most_one: empty or all false' was applied 1946 times.
  - rule 'at_most_one: removed literals' was applied 1946 times.
  - rule 'at_most_one: satisfied' was applied 1018 times.
  - rule 'bool_or: only one literal' was applied 34048 times.
  - rule 'deductions: 57578 stored' was applied 1 time.
  - rule 'enforcement: false literal' was applied 60298 times.
  - rule 'enforcement: true literal' was applied 3615 times.
  - rule 'exactly_one: removed literals' was applied 5997 times.
  - rule 'exactly_one: satisfied' was applied 1301 times.
  - rule 'exactly_one: size two' was applied 45 times.
  - rule 'linear1: without enforcement' was applied 2429 times.
  - rule 'linear2: contains a Boolean.' was applied 122149 times.
  - rule 'linear: always true' was applied 44614 times.
  - rule 'linear: divide by GCD' was applied 13 times.
  - rule 'linear: extracted at most one (max).' was applied 1513 times.
  - rule 'linear: extracted at most one (min).' was applied 1451 times.
  - rule 'linear: fixed or dup variables' was applied 22253 times.
  - rule 'linear: infeasible' was applied 34048 times.
  - rule 'linear: negative equal one' was applied 99 times.
  - rule 'linear: only two odd Booleans in equality' was applied 1 time.
  - rule 'linear: positive equal one' was applied 1247 times.
  - rule 'linear: reduced variable domains' was applied 18101 times.
  - rule 'linear: remapped using affine relations' was applied 68801 times.
  - rule 'linear: simplified rhs' was applied 476594 times.
  - rule 'linear: variable substitution 1' was applied 445 times.
  - rule 'presolve: 72403 unused variables removed.' was applied 1 time.
  - rule 'presolve: iteration' was applied 2 times.
  - rule 'variables: canonicalize affine domain' was applied 12 times.
  - rule 'variables: detect half reified value encoding' was applied 31779 times.

Presolved satisfaction model 'NonoSATModelProto': (model_fingerprint: 0xa5b85c5e198ed849)
#Variables: 0


Preloading model.
[Symmetry] Graph for symmetry has 0 nodes and 0 arcs.
#Model   3.06s var:0/0 constraints:0/0

Starting search at 3.06s with 16 workers.
6 full problem subsolvers: [default_lp, less_encoding, max_lp, no_lp, quick_restart, quick_restart_no_lp]
8 first solution subsolvers: [jump, jump_decay_perturb, jump_decay_rnd_on_rst, jump_no_rst, random(2), random_quick_restart(2)]
2 incomplete subsolvers: [feasibility_pump, rins/rens]
2 helper subsolvers: [neighborhood_helper, synchronization_agent]
#1       3.06s default_lp fixed_bools:0/0
#2       3.06s jump(batch:1 #lin_moves:0/0 #weight_updates:1)
#3       3.06s jump_decay_perturb(batch:1 #lin_moves:0/0 #weight_updates:1)
#4       3.06s jump_decay_rnd_on_rst(batch:1 #lin_moves:0/0 #weight_updates:1)
#5       3.06s jump_no_rst(batch:1 #lin_moves:0/0 #weight_updates:1)

Task timing                        n [     min,      max]      avg      dev     time         n [     min,      max]      avg      dev    dtime
  'synchronization_agent':         0 [  0.00ns,   0.00ns]   0.00ns   0.00ns   0.00ns         0 [  0.00ns,   0.00ns]   0.00ns   0.00ns   0.00ns
    'neighborhood_helper':         0 [  0.00ns,   0.00ns]   0.00ns   0.00ns   0.00ns         0 [  0.00ns,   0.00ns]   0.00ns   0.00ns   0.00ns
             'default_lp':         1 [168.67us, 168.67us] 168.67us   0.00ns 168.67us         0 [  0.00ns,   0.00ns]   0.00ns   0.00ns   0.00ns
          'less_encoding':         1 [ 48.97us,  48.97us]  48.97us   0.00ns  48.97us         0 [  0.00ns,   0.00ns]   0.00ns   0.00ns   0.00ns
                  'no_lp':         1 [ 20.32us,  20.32us]  20.32us   0.00ns  20.32us         0 [  0.00ns,   0.00ns]   0.00ns   0.00ns   0.00ns
                 'max_lp':         1 [ 30.96us,  30.96us]  30.96us   0.00ns  30.96us         0 [  0.00ns,   0.00ns]   0.00ns   0.00ns   0.00ns
          'quick_restart':         1 [ 15.44us,  15.44us]  15.44us   0.00ns  15.44us         0 [  0.00ns,   0.00ns]   0.00ns   0.00ns   0.00ns
    'quick_restart_no_lp':         1 [ 18.26us,  18.26us]  18.26us   0.00ns  18.26us         0 [  0.00ns,   0.00ns]   0.00ns   0.00ns   0.00ns
       'feasibility_pump':         1 [  3.11us,   3.11us]   3.11us   0.00ns   3.11us         0 [  0.00ns,   0.00ns]   0.00ns   0.00ns   0.00ns
              'rins/rens':         0 [  0.00ns,   0.00ns]   0.00ns   0.00ns   0.00ns         0 [  0.00ns,   0.00ns]   0.00ns   0.00ns   0.00ns

Search stats              Bools  Conflicts  Branches  Restarts  BoolPropag  IntegerPropag
           'default_lp':      0          0         0         0           0              0
        'less_encoding':      0          0         0         0           0              0
                'no_lp':      0          0         0         0           0              0
               'max_lp':      0          0         0         0           0              0
        'quick_restart':      0          0         0         0           0              0
  'quick_restart_no_lp':      0          0         0         0           0              0

LNS stats       Improv/Calls  Closed  Difficulty  TimeLimit
  'rins/rens':           0/0      0%        0.50       0.10

Solution repositories    Added  Queried  Ignored  Synchro
  'feasible solutions':      5        0        0        5
        'lp solutions':      0        0        0        0
                'pump':      0        0

CpSolverResponse summary:
status: OPTIMAL
objective: NA
best_bound: NA
integers: 0
booleans: 0
conflicts: 0
branches: 0
propagations: 0
integer_propagations: 0
restarts: 0
lp_iterations: 0
walltime: 3.07626
usertime: 3.07626
deterministic_time: 0.70942
gap_integral: 0
solution_fingerprint: 0xd9fbce807a16246e

CP computed.
Introducing solution...
    Selecting color...
    Color selected.
    Clicking cells...
    Cells clicked.
    Selecting color...
    Color selected.
    Clicking cells...
    Cells clicked.
    Selecting color...
    Color selected.
    Clicking cells...
    Cells clicked.
    Selecting color...
    Color selected.
    Clicking cells...
    Cells clicked.
    Selecting color...
    Color selected.
    Clicking cells...
    Cells clicked.
Solution introduced.
Clicking OK...
OK clicked.
Unhiding back details...
Details Unhidden.
Scaling image...
Image scaled.
Game loop finalized.
`````
As you see in the output example, the MathModel finds the solution pretty fast.

## Comment:
This problem is just a guide that the user can follow to build its own problems.

In this example, we didn't conduct an extensive optimization of the MathModel parameters.
Nevertheless, we were able to get in a pretty sort time the solutions for the scenario. :smirk_cat:
