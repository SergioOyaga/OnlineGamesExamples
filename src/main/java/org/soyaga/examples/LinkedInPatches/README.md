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
            <li>$\textcolor{blue}{Cl} =$ Colors.
                <ul>
                    <li>$\textcolor{blue}{Cl_n} =$ colors with a number associated</li>
                    <li>$\textcolor{blue}{Cl_s} =$ colors with square shape</li>
                    <li>$\textcolor{blue}{Cl_vr} =$ colors with vertical-rectangle shape</li>
                    <li>$\textcolor{blue}{Cl_hr} =$ colors with horizontal-rectangle shape</li>
                    <li>$\textcolor{blue}{Cl_u} =$ colors with unknown shape</li>
                </ul>
            </li>
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
           <li>$ClRS_{cl,r} \in \{0,1\}, \; \forall cl \in \textcolor{blue}{Cl}, \; r \in \textcolor{blue}{R}=$ (ColorRowStart) Row range start by color as boolean.</li>
           <li>$ClRE_{cl,r} \in \{0,1\}, \; \forall cl \in \textcolor{blue}{Cl}, \; r \in \textcolor{blue}{R}=$ (ColorRowEnd) Row range end by color as boolean.</li>
           <li>$ClCS_{cl,c} \in \{0,1\}, \; \forall cl \in \textcolor{blue}{Cl}, \; c \in \textcolor{blue}{c}=$ (ColorColumnStart) Column range start by color as boolean.</li>
           <li>$ClCE_{cl,c} \in \{0,1\}, \; \forall cl \in \textcolor{blue}{Cl}, \; c \in \textcolor{blue}{c}=$ (ColorColumnEnd) Column range end by color as boolean.</li>
           <li>$ClH_{cl} \in , \textcolor{blue}{R}\; \forall cl \in \textcolor{blue}{Cl} =$ (ColorHeight) Shape height by color.</li>
           <li>$ClW_{cl} \in , \textcolor{blue}{C}\; \forall cl \in \textcolor{blue}{Cl} =$ (ColorWidth) Shape width by color.</li>
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
    <td><b>ColorHintPosition</b></td>
    <td>$$ClB_{cl,\textcolor{magenta}{ClR_{cl}},\textcolor{magenta}{ClC_{cl}}} = 1 ,\; \forall cl \in \textcolor{blue}{Cl}$$</td>
    <td> $|\textcolor{blue}{Cl}|$ </td>
    <td> Forces color hint position.</td>
  </tr>
  <tr>
    <td><b>BoardColorUnique</b></td>
    <td>$$\displaystyle\sum_{cl \in \color{blue}{Cl}}ClB_{cl,r,c} = 1 ,\; \forall r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C}$$</td>
    <td> $|\textcolor{blue}{R}| \cdot |\textcolor{blue}{C}|$ </td>
    <td> Forces color to assign one and only one color to each cell.</td>
  </tr>
  <tr>
    <td><b>ColorSize</b></td>
    <td>$$\displaystyle\sum_{\substack{r \in \color{blue}{R} \\ c \in \color{blue}{Cl_n}}}ClB_{cl,r,c} = \textcolor{magenta}{ClN_{cl}}, \; \forall cl \in \textcolor{blue}{Cl_n}$$</td>
    <td> $|\textcolor{blue}{Cl_n}|$ </td>
    <td> Forces color size.</td>
  </tr>
  <tr>
    <td><b>ColorRowStartUnique</b></td>
    <td>$$\displaystyle\sum_{\substack{ r \in \color{blue}{R} }}ClRS_{cl,r} = 1 ,\; \forall cl \in \textcolor{blue}{Cl}$$</td>
    <td> $|\sum_{cl \in \textcolor{blue}{Cl}}|$ </td>
    <td> Forces to only be one start.</td>
  </tr> 
  <tr>
    <td><b>ColorRowEndUnique</b></td>
    <td>$$\displaystyle\sum_{\substack{ r \in \color{blue}{R} }}ClRE_{cl,r} = 1 ,\; \forall cl \in \textcolor{blue}{Cl}$$</td>
    <td> $|\sum_{cl \in \textcolor{blue}{Cl}}|$ </td>
    <td> Forces to only be one end.</td>
  </tr> 
  <tr>
    <td><b>RowStartEndPrecedence</b></td>
    <td>$$0 \leq \displaystyle\sum_{ r \in \textcolor{blue}{R}} r \cdot (ClRE_{cl,r} - ClRS_{cl,r}) \leq |\textcolor{blue}{R}|,\; \forall cl \in \textcolor{blue}{Cl}$$</td>
    <td> $|\textcolor{blue}{Cl}|$ </td>
    <td> Row Start-End precedence.</td>
  </tr> 
  <tr>
    <td><b>ColorColStartUnique</b></td>
    <td>$$\displaystyle\sum_{\substack{ c \in \color{blue}{C} }}ClCS_{cl,c} = 1 ,\; \forall cl \in \textcolor{blue}{Cl}$$</td>
    <td> $|\sum_{cl \in \textcolor{blue}{Cl}}|$ </td>
    <td> Forces to only be one start.</td>
  </tr> 
  <tr>
    <td><b>ColorColEndUnique</b></td>
    <td>$$\displaystyle\sum_{\substack{ c \in \color{blue}{C} }}ClCE_{cl,c} = 1 ,\; \forall cl \in \textcolor{blue}{Cl}$$</td>
    <td> $|\sum_{cl \in \textcolor{blue}{Cl}}|$ </td>
    <td> Forces to only be one end.</td>
  </tr> 
  <tr>
    <td><b>ColStartEndPrecedence</b></td>
    <td>$$0 \leq \displaystyle\sum_{ c \in \textcolor{blue}{C}} c \cdot (ClCE_{cl,c} - ClCS_{cl,c}) \leq |\textcolor{blue}{C}|,\; \forall cl \in \textcolor{blue}{Cl}$$</td>
    <td> $|\textcolor{blue}{Cl}|$ </td>
    <td> Col Start-End precedence.</td>
  </tr> 
  <tr>
    <td><b>ColorBoardRangeRelation</b></td>
    <td>$$0 \leq \displaystyle(\sum_{\substack{r_i \in \{ 0,r\}}}ClRS_{cl,r_i} + \sum_{\substack{c_i \in \{ 0,c\}}}ClCS_{cl,c_i}) - (\sum_{\substack{r_i \in \{ 0,r-1\}}}ClRE_{cl,r_i} + \sum_{\substack{c_i \in \{ 0,c-1\}}}ClCE_{cl,c_i}) - 2 \cdot ClB_{cl,r,c} \leq 1 ,\; \forall cl \in \textcolor{blue}{Cl}, \; r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C}$$</td>
    <td> $|\textcolor{blue}{Cl}| \cdot |\textcolor{blue}{R}| \cdot |\textcolor{blue}{C}|$ </td>
    <td> Forces Board to be 1 when in the range. For r,c = 0 only left side applies.</td>
  </tr> 
  <tr>
    <td><b>ColorHeightComputation</b></td>
    <td>$$\displaystyle\sum_{\substack{r \in \textcolor{blue}{R}}}r\cdot(ClRE_{cl,r} - ClRS_{cl,r}) - ClH_{cl} = 0 ,\; \forall cl \in \textcolor{blue}{Cl}$$</td>
    <td> $|\textcolor{blue}{Cl}|$ </td>
    <td> Color height computation.</td>
  </tr> 
  <tr>
    <td><b>ColorWidthComputation</b></td>
    <td>$$\displaystyle\sum_{\substack{c \in \textcolor{blue}{C}}}c\cdot(ClCE_{cl,c} - ClCS_{cl,c}) - ClW_{cl} = 0 ,\; \forall cl \in \textcolor{blue}{Cl}$$</td>
    <td> $|\textcolor{blue}{Cl}|$ </td>
    <td> Color width computation.</td>
  </tr> 
  <tr>
    <td><b>ColorSquareRule</b></td>
    <td>$$ClW_{cl} - ClH_{cl} = 0 ,\; \forall cl \in \textcolor{blue}{Cl_s}$$</td>
    <td> $|\textcolor{blue}{Cl_s}|$ </td>
    <td> Color square rule.</td>
  </tr> 
  <tr>
    <td><b>ColorVerticalRectangleRule</b></td>
    <td>$$ClH_{cl} - ClW_{cl} \geq 0 ,\; \forall cl \in \textcolor{blue}{Cl_vr}$$</td>
    <td> $|\textcolor{blue}{Cl_s}|$ </td>
    <td> Color vertical rectangle rule.</td>
  </tr> 
  <tr>
    <td><b>ColorHorizontalRectangleRule</b></td>
    <td>$$ClW_{cl} - ClH_{cl} \geq 0 ,\; \forall cl \in \textcolor{blue}{Cl_hr}$$</td>
    <td> $|\textcolor{blue}{Cl_s}|$ </td>
    <td> Color horizontal rectangle rule.</td>
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
3. [LinkedInPatchesMMInitializer](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/LinkedInPatches/MathModel/LinkedInPatchesMMInitializer.java): Initializes the Mathematical Model:
    - Creates Variables.
    - Creates constraints.


## Results
For the MathModel, the set of parameters used are in the LinkedInPatchesMathModel file.

The output looks like:
`````
Loading https://www.linkedin.com/: ...
Loaded.
Refreshing in case it did not load...
Refreshed.
Logging-in...
Logged.
Loading game...
Game loaded.
Retrieving Grid...

Starting CP-SAT solver v9.7.2996
Parameters: max_time_in_seconds: 600 log_search_progress: true log_to_stdout: true
Setting number of workers to 16

Initial satisfaction model 'PatchesMMModelProto': (model_fingerprint: 0x8fe41a7eb4611679)
#Variables: 496
  - 480 Booleans in [0,1]
  - 16 in [0,5]
#kLinear1: 8
#kLinear3: 8
#kLinearN: 384 (#terms: 4'744)

Starting presolve at 0.00s
[ExtractEncodingFromLinear] #potential_supersets=60 #potential_subsets=0 #at_most_one_encodings=0 #exactly_one_encodings=0 #unique_terms=0 #multiple_terms=0 #literals=0 time=1.89e-05s
[Symmetry] Graph for symmetry has 1488 nodes and 5184 arcs.
[Symmetry] Symmetry computation done. time: 0.0003318 dtime: 0.00073083
[SAT presolve] num removable Booleans: 80 / 496
[SAT presolve] num trivial clauses: 0
[SAT presolve] [0s] clauses:32 literals:96 vars:24 one_side_vars:0 simple_definition:8 singleton_clauses:0
[SAT presolve] [2.17e-05s] clauses:24 literals:56 vars:24 one_side_vars:0 simple_definition:24 singleton_clauses:0
[SAT presolve] [4.18e-05s] clauses:24 literals:56 vars:24 one_side_vars:0 simple_definition:24 singleton_clauses:0
[Probing] implications and bool_or (work_done=6867).
[DetectDuplicateConstraints] #duplicates=33 #without_enforcements=0 time=0.0003219s
[DetectDominatedLinearConstraints] #relevant_constraints=239 #work_done=29092 #num_inclusions=692 #num_redundant=1 time=0.0018213s
[ProcessSetPPC] #relevant_constraints=318 #num_inclusions=118 work=8825 time=0.0010789s
[FindBigHorizontalLinearOverlap] #blocks=0 #saved_nz=0 #linears=202 #work_done=2100/1e+09 time=6.94e-05s
[FindBigVerticalLinearOverlap] #blocks=27 #nz_reduction=802 #work_done=31087 time=0.000547s
[MergeClauses] #num_collisions=4 #num_merges=4 #num_saved_literals=8 work=87/100000000 time=2.26e-05s
[Symmetry] Graph for symmetry has 1024 nodes and 1330 arcs.
[Symmetry] Symmetry computation done. time: 0.0001353 dtime: 0.00020856
[SAT presolve] num removable Booleans: 365 / 524
[SAT presolve] num trivial clauses: 0
[SAT presolve] [0s] clauses:239 literals:652 vars:112 one_side_vars:17 simple_definition:60 singleton_clauses:0
[SAT presolve] [3.2e-05s] clauses:171 literals:376 vars:109 one_side_vars:22 simple_definition:57 singleton_clauses:8
[SAT presolve] [7.47e-05s] clauses:171 literals:376 vars:109 one_side_vars:22 simple_definition:57 singleton_clauses:8
[Probing] implications and bool_or (work_done=184).
[MaxClique] Merged 136(278 literals) into 130(263 literals) at_most_ones. time=0.0001213s
[DetectDuplicateConstraints] #duplicates=0 #without_enforcements=0 time=3.9e-06s
[DetectDominatedLinearConstraints] #relevant_constraints=0 #work_done=0 #num_inclusions=0 #num_redundant=0 time=1e-06s
[ProcessSetPPC] #relevant_constraints=0 #num_inclusions=0 work=0 time=2.1e-06s
[FindBigHorizontalLinearOverlap] #blocks=0 #saved_nz=0 #linears=0 #work_done=0/1e+09 time=1.2e-06s
[FindBigVerticalLinearOverlap] #blocks=0 #nz_reduction=0 #work_done=0 time=9e-06s
[MergeClauses] #num_collisions=0 #num_merges=0 #num_saved_literals=0 work=0/100000000 time=1.6e-06s
[Symmetry] Graph for symmetry has 524 nodes and 0 arcs.
[Symmetry] Symmetry computation done. time: 1.48e-05 dtime: 3.144e-05
[DetectDuplicateConstraints] #duplicates=0 #without_enforcements=0 time=3e-06s
[DetectDominatedLinearConstraints] #relevant_constraints=0 #work_done=0 #num_inclusions=0 #num_redundant=0 time=9e-07s
[ProcessSetPPC] #relevant_constraints=0 #num_inclusions=0 work=0 time=1.8e-06s
[FindBigHorizontalLinearOverlap] #blocks=0 #saved_nz=0 #linears=0 #work_done=0/1e+09 time=1e-06s
[FindBigVerticalLinearOverlap] #blocks=0 #nz_reduction=0 #work_done=0 time=6.3e-06s
[MergeClauses] #num_collisions=0 #num_merges=0 #num_saved_literals=0 work=0/100000000 time=1.5e-06s

Presolve summary:
  - 20 affine relations were detected.
  - rule 'TODO dual: only one blocking constraint?' was applied 17 times.
  - rule 'TODO linear2: contains a Boolean.' was applied 82 times.
  - rule 'affine: new relation' was applied 20 times.
  - rule 'at_most_one: empty or all false' was applied 32 times.
  - rule 'at_most_one: removed literals' was applied 45 times.
  - rule 'at_most_one: satisfied' was applied 102 times.
  - rule 'at_most_one: size one' was applied 10 times.
  - rule 'at_most_one: transformed into max clique.' was applied 1 time.
  - rule 'bool_and: always false' was applied 1 time.
  - rule 'bool_and: x => x' was applied 3 times.
  - rule 'bool_or: always true' was applied 84 times.
  - rule 'bool_or: fixed literals' was applied 3 times.
  - rule 'bool_or: implications' was applied 76 times.
  - rule 'bool_or: only one literal' was applied 8 times.
  - rule 'bool_or: removed enforcement literal' was applied 10 times.
  - rule 'deductions: 18 stored' was applied 1 time.
  - rule 'duplicate: merged rhs of linear constraint' was applied 13 times.
  - rule 'duplicate: removed constraint' was applied 33 times.
  - rule 'enforcement: can never be true' was applied 3 times.
  - rule 'enforcement: false literal' was applied 10 times.
  - rule 'enforcement: true literal' was applied 18 times.
  - rule 'exactly_one: removed literals' was applied 108 times.
  - rule 'exactly_one: satisfied' was applied 55 times.
  - rule 'exactly_one: singleton' was applied 2 times.
  - rule 'exactly_one: size one' was applied 3 times.
  - rule 'exactly_one: size two' was applied 4 times.
  - rule 'exactly_one: x and not(x)' was applied 21 times.
  - rule 'linear + amo: advanced infeasible linear constraint' was applied 9 times.
  - rule 'linear + amo: detect hidden AMO' was applied 1 time.
  - rule 'linear + amo: extracted enforcement literal' was applied 50 times.
  - rule 'linear + amo: fixed literal' was applied 41 times.
  - rule 'linear + amo: trivial linear constraint' was applied 2 times.
  - rule 'linear inclusion: redundant containing constraint' was applied 1 time.
  - rule 'linear matrix: common vertical rectangle' was applied 27 times.
  - rule 'linear1: is boolean implication' was applied 13 times.
  - rule 'linear1: transformed to implication' was applied 1 time.
  - rule 'linear1: without enforcement' was applied 21 times.
  - rule 'linear2: contains a Boolean.' was applied 20 times.
  - rule 'linear2: implied ax + by = cte has only one solution' was applied 1 time.
  - rule 'linear: always true' was applied 114 times.
  - rule 'linear: divide by GCD' was applied 15 times.
  - rule 'linear: empty' was applied 36 times.
  - rule 'linear: enforcement literal in expression' was applied 1 time.
  - rule 'linear: fixed or dup variables' was applied 698 times.
  - rule 'linear: infeasible' was applied 8 times.
  - rule 'linear: negative at most one' was applied 14 times.
  - rule 'linear: negative clause' was applied 21 times.
  - rule 'linear: negative equal one' was applied 1 time.
  - rule 'linear: positive at most one' was applied 13 times.
  - rule 'linear: positive clause' was applied 9 times.
  - rule 'linear: positive equal one' was applied 68 times.
  - rule 'linear: reduced variable domains' was applied 76 times.
  - rule 'linear: reduced variable domains in derived constraint' was applied 33 times.
  - rule 'linear: remapped using affine relations' was applied 135 times.
  - rule 'linear: simplified rhs' was applied 12 times.
  - rule 'linear: singleton column' was applied 16 times.
  - rule 'linear: small Boolean expression' was applied 78 times.
  - rule 'linear: variable substitution 2' was applied 1 time.
  - rule 'presolve: 485 unused variables removed.' was applied 1 time.
  - rule 'presolve: iteration' was applied 3 times.
  - rule 'probing: bool_or reduced to implication' was applied 9 times.
  - rule 'setppc: bool_or in at_most_one.' was applied 3 times.
  - rule 'setppc: exactly_one included in linear' was applied 80 times.
  - rule 'setppc: fixed variables' was applied 3 times.
  - rule 'setppc: reduced linear coefficients' was applied 75 times.
  - rule 'setppc: removed dominated constraints' was applied 3 times.
  - rule 'setppc: removed infeasible linear constraint' was applied 2 times.
  - rule 'setppc: removed trivial linear constraint' was applied 3 times.
  - rule 'variables with 2 values: create encoding literal' was applied 1 time.
  - rule 'variables with 2 values: new affine relation' was applied 1 time.
  - rule 'variables: detect fully reified value encoding' was applied 1 time.
  - rule 'variables: detect half reified value encoding' was applied 8 times.

Presolved satisfaction model 'PatchesMMModelProto': (model_fingerprint: 0xa5b85c5e198ed849)
#Variables: 0


Preloading model.
[Symmetry] Graph for symmetry has 0 nodes and 0 arcs.
#Model   0.02s var:0/0 constraints:0/0

Starting search at 0.02s with 16 workers.
6 full problem subsolvers: [default_lp, less_encoding, max_lp, no_lp, quick_restart, quick_restart_no_lp]
8 first solution subsolvers: [jump, jump_decay_perturb, jump_decay_rnd_on_rst, jump_no_rst, random(2), random_quick_restart(2)]
2 incomplete subsolvers: [feasibility_pump, rins/rens]
2 helper subsolvers: [neighborhood_helper, synchronization_agent]
#1       0.02s no_lp fixed_bools:0/0
#2       0.02s default_lp fixed_bools:0/0
#3       0.02s max_lp fixed_bools:0/0
#4       0.02s less_encoding fixed_bools:0/0
#5       0.02s jump(batch:1 #lin_moves:0/0 #weight_updates:1)
#6       0.02s jump_decay_perturb(batch:1 #lin_moves:0/0 #weight_updates:1)
#7       0.02s jump_no_rst(batch:1 #lin_moves:0/0 #weight_updates:1)
#8       0.02s jump_decay_rnd_on_rst(batch:1 #lin_moves:0/0 #weight_updates:1)

Task timing                        n [     min,      max]      avg      dev     time         n [     min,      max]      avg      dev    dtime
  'synchronization_agent':         0 [  0.00ns,   0.00ns]   0.00ns   0.00ns   0.00ns         0 [  0.00ns,   0.00ns]   0.00ns   0.00ns   0.00ns
    'neighborhood_helper':         0 [  0.00ns,   0.00ns]   0.00ns   0.00ns   0.00ns         0 [  0.00ns,   0.00ns]   0.00ns   0.00ns   0.00ns
             'default_lp':         1 [304.30us, 304.30us] 304.30us   0.00ns 304.30us         0 [  0.00ns,   0.00ns]   0.00ns   0.00ns   0.00ns
          'less_encoding':         1 [396.80us, 396.80us] 396.80us   0.00ns 396.80us         0 [  0.00ns,   0.00ns]   0.00ns   0.00ns   0.00ns
                  'no_lp':         1 [200.00us, 200.00us] 200.00us   0.00ns 200.00us         0 [  0.00ns,   0.00ns]   0.00ns   0.00ns   0.00ns
                 'max_lp':         1 [206.90us, 206.90us] 206.90us   0.00ns 206.90us         0 [  0.00ns,   0.00ns]   0.00ns   0.00ns   0.00ns
          'quick_restart':         1 [ 33.70us,  33.70us]  33.70us   0.00ns  33.70us         0 [  0.00ns,   0.00ns]   0.00ns   0.00ns   0.00ns
    'quick_restart_no_lp':         1 [ 26.30us,  26.30us]  26.30us   0.00ns  26.30us         0 [  0.00ns,   0.00ns]   0.00ns   0.00ns   0.00ns
       'feasibility_pump':         1 [  3.40us,   3.40us]   3.40us   0.00ns   3.40us         0 [  0.00ns,   0.00ns]   0.00ns   0.00ns   0.00ns
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
  'feasible solutions':      8        0        0        8
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
walltime: 0.0322135
usertime: 0.0322136
deterministic_time: 0.00235217
gap_integral: 0
solution_fingerprint: 0xad22056893c1b877

CP solution introduced.
`````
As you see in the output example, the MathModel finds the solution pretty fast.

## Comment:
This problem is just a guide that the user can follow to build its own problems.

In this example, we didn't conduct an extensive optimization of the MathModel parameters.
Nevertheless, we were able to get in a pretty sort time the solutions for the scenario. :smirk_cat:
