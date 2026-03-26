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
    <td>$$\displaystyle\sum_{\substack{r \in \textcolor{blue}{R}}}(ClRE_{cl,r} - ClRS_{cl,r}) - ClH_{cl} = 0 ,\; \forall cl \in \textcolor{blue}{Cl}$$</td>
    <td> $|\textcolor{blue}{Cl}|$ </td>
    <td> Color height computation.</td>
  </tr> 
  <tr>
    <td><b>ColorWidthComputation</b></td>
    <td>$$\displaystyle\sum_{\substack{c \in \textcolor{blue}{C}}}(ClCE_{cl,c} - ClCS_{cl,c}) - ClW_{cl} = 0 ,\; \forall cl \in \textcolor{blue}{Cl}$$</td>
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
