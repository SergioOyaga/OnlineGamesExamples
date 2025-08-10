# Sudoku
For this https://sudoku.com/ Sudoku problem, we use MIP to formulate and solve a linear problem.
In the [mathematical formulation](#mathematical-formulation) we can see how the problem sets, parameters, variables and constraints are defined.

<img src="https://github.com/SergioOyaga/OnlineGamesExamples/blob/main/src/out/Sudoku/Sudoku.gif"  title="Sudoku example" alt="Sudoku example"/>

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
            <li>$\textcolor{blue}{S} =\{0,2,...,8\}$ Square group numbers.</li>
         </ul>
      </td>
      <td rowspan="3">
        <ul>
           <li>$S_{r, c} \in \{1,2,...,9\}, \; \forall r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C} =$ Sudoku board.</li>
           <li>$DRCC_{r, c_i, c_j} \in \{0,1\}, \; \forall r \in \textcolor{blue}{R}, \; c_i, c_j \in \textcolor{blue}{C} =$ DiscriminatorRowColumColum.</li>
           <li>$DCRR_{c, r_i, r_j} \in \{0,1\}, \; \forall c \in \textcolor{blue}{C}, \; r_i, r_j \in \textcolor{blue}{R} =$ DiscriminatorColumRowRow.</li>
           <li>$DRCS_{r, c, s} \in \{0,1\}, \; \forall r \in \textcolor{blue}{R}, \; c \in \textcolor{blue}{C}\; s \in \textcolor{blue}{S} =$ DiscriminatorRowColumSquare.</li>
        </ul>
      </td>
   </tr>
   <tr>
        <th> Variables </th>
   </tr>
    <tr>
      <td>
        $\textcolor{magenta}{CV_{fv}} \in \{1,2,...,9\} \forall fv \in \textcolor{blue}{FV}$ CellValue by fixed value cells.
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
  - [1,9] for $S{r,c}$.
  - [0,1] for the discriminators.

The inequality can be expressed as:

  | Name                                                         | Inequality                 | Expression                                                 |
  |--------------------------------------------------------------|----------------------------|------------------------------------------------------------|
  | AllDifferentInRow                                            | $S_{r,c_i} \neq S_{r,c_j}$ | $1\leq S_{r,c_i}- S_{r,c_j} +9\cdot DRCC_{r,c_i,c_j}\leq8$ |
  | Same approach for AllDifferentInCol and AllDifferentInSquare |                            |

Let see why this approach works. If $DRCC_{r,c_i,c_j}=\{0,1\}$.

| Case                   | MaxDiffence                              | MinDifference                            | Implication                                                                                             |
|------------------------|------------------------------------------|------------------------------------------|---------------------------------------------------------------------------------------------------------|
| $S_{r,c_i}> S_{r,c_j}$ | $1\leq 8 +9\cdot DRCC_{r,c_i,c_j}\leq8$  | $1\leq 1 +9\cdot DRCC_{r,c_i,c_j}\leq8$  | $$\begin{cases}Max\to DRCC_{r,c_i,c_j}=0 \\ Min\to DRCC_{r,c_i,c_j}=0 \end{cases}$$                     |          
| $S_{r,c_i}< S_{r,c_j}$ | $1\leq -1 +9\cdot DRCC_{r,c_i,c_j}\leq8$ | $1\leq -8 +9\cdot DRCC_{r,c_i,c_j}\leq8$ | $$\begin{cases}Max\to DRCC_{r,c_i,c_j}=1 \\ Min\to DRCC_{r,c_i,c_j}=1 \end{cases}$$                     |
| $S_{r,c_i}= S_{r,c_j}$ | $1\leq 0 +9\cdot DRCC_{r,c_i,c_j}\leq8$  | $1\leq 0 +9\cdot DRCC_{r,c_i,c_j}\leq8$  | $$\begin{cases}Max\to DRCC_{r,c_i,c_j}=\varnothing \\ Min\to DRCC_{r,c_i,c_j}=\varnothing \end{cases}$$ |

## In this folder:
This folder contains one class and one package that defines the structures required for solving the problem.

1. [SudokuScraper](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Sudoku/SudokuScraper.java): Web Scrapper using Selenium and flow controller.
    - Scrapes the necessary information from the sudoku-web application.
      - Performs Web-scraping (Selenium).
      - Image processing (OpenCV).
      - OCR (Optical Character Recognition) with Tesseract (Tess4j).
    - Manages the flow of the program. 
    - Instantiates the SudokuMathModel.
    - Introduces the solution in the web app.
2. [SudokuMathModel](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Sudoku/MathModel/SudokuMathModel.java): Class that implements the required OptimizationLib interface and represents a Mathematical Modeling Optimization program.
    - Instantiates its own initializer.
    - Implements runnable. 
3. [SudokuMMInitializer](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Sudoku/MathModel/SudokuMMInitializer.java): Class that initializes the SudokuMathModel using problem specific information.
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
Loaded.
Accepting cookies...
Cookies accepted.
Sudoku loaded.
Screenshot the board...
Board screenshot.
Transforming image for OCR...
    Loading OpenCV...
    OpenCV loaded.
    Replacing conflictive colors...
    Conflictive colors replaced.
    Scaling...
    Scaled.
    Converting to Grayscale...
    Grayscale converted.
    Applying Gaussian noise...
    Gaussian noise applied.
Image transformed.
Doing OCR...
    Preparing Tesseract for OCR...
    Tesseract prepared.
    Recognizing characters...
++===+===+===++===+===+===++===+===+===++
||           || 8       1 ||           ||
|| 7         ||         9 ||     5     ||
||           || 2         || 4         ||
++===+===+===++===+===+===++===+===+===++
|| 9         ||           ||           ||
|| 6         ||     1     || 3   4     ||
||     5     ||         3 || 1         ||
++===+===+===++===+===+===++===+===+===++
||         2 ||           ||           ||
||           || 1         || 6         ||
|| 5   3     ||     6   4 ||         9 ||
++===+===+===++===+===+===++===+===+===++
    Characters recognized.
OCR done.
Building MathModel...
MathModel built.
Running MathModel...
presolving:
(round 1, fast)       23 del vars, 23 del conss, 0 add conss, 39 chg bounds, 0 chg sides, 0 chg coeffs, 0 upgd conss, 0 impls, 0 clqs
(round 2, fast)       247 del vars, 247 del conss, 0 add conss, 370 chg bounds, 621 chg sides, 499 chg coeffs, 0 upgd conss, 32 impls, 0 clqs
(round 3, fast)       281 del vars, 281 del conss, 0 add conss, 430 chg bounds, 964 chg sides, 842 chg coeffs, 0 upgd conss, 37 impls, 0 clqs
(round 4, fast)       293 del vars, 291 del conss, 0 add conss, 445 chg bounds, 1006 chg sides, 882 chg coeffs, 0 upgd conss, 38 impls, 0 clqs
(round 5, fast)       299 del vars, 295 del conss, 0 add conss, 450 chg bounds, 1017 chg sides, 893 chg coeffs, 0 upgd conss, 42 impls, 9 clqs
(round 6, fast)       299 del vars, 295 del conss, 0 add conss, 450 chg bounds, 1021 chg sides, 897 chg coeffs, 0 upgd conss, 42 impls, 9 clqs
(round 7, exhaustive) 299 del vars, 295 del conss, 0 add conss, 450 chg bounds, 1021 chg sides, 897 chg coeffs, 225 upgd conss, 42 impls, 9 clqs
(round 8, medium)     438 del vars, 295 del conss, 0 add conss, 450 chg bounds, 1021 chg sides, 897 chg coeffs, 225 upgd conss, 467 impls, 50 clqs
(round 9, medium)     444 del vars, 393 del conss, 0 add conss, 450 chg bounds, 1177 chg sides, 901 chg coeffs, 225 upgd conss, 467 impls, 50 clqs
(round 10, fast)       447 del vars, 408 del conss, 0 add conss, 450 chg bounds, 1186 chg sides, 904 chg coeffs, 225 upgd conss, 467 impls, 98 clqs
(round 11, medium)     447 del vars, 408 del conss, 0 add conss, 450 chg bounds, 1195 chg sides, 904 chg coeffs, 225 upgd conss, 467 impls, 102 clqs
(round 12, medium)     447 del vars, 409 del conss, 0 add conss, 450 chg bounds, 1196 chg sides, 905 chg coeffs, 225 upgd conss, 467 impls, 102 clqs
(round 13, exhaustive) 483 del vars, 409 del conss, 0 add conss, 457 chg bounds, 1196 chg sides, 905 chg coeffs, 225 upgd conss, 512 impls, 197 clqs
(round 14, fast)       483 del vars, 422 del conss, 0 add conss, 457 chg bounds, 1266 chg sides, 965 chg coeffs, 225 upgd conss, 513 impls, 199 clqs
(round 15, medium)     487 del vars, 422 del conss, 0 add conss, 457 chg bounds, 1266 chg sides, 965 chg coeffs, 225 upgd conss, 513 impls, 197 clqs
(round 16, fast)       487 del vars, 425 del conss, 0 add conss, 457 chg bounds, 1269 chg sides, 968 chg coeffs, 225 upgd conss, 513 impls, 210 clqs
(round 17, exhaustive) 487 del vars, 427 del conss, 0 add conss, 457 chg bounds, 1269 chg sides, 968 chg coeffs, 225 upgd conss, 513 impls, 210 clqs
(round 18, exhaustive) 487 del vars, 427 del conss, 0 add conss, 457 chg bounds, 1269 chg sides, 968 chg coeffs, 247 upgd conss, 513 impls, 210 clqs
(round 19, medium)     487 del vars, 428 del conss, 0 add conss, 457 chg bounds, 1271 chg sides, 969 chg coeffs, 247 upgd conss, 517 impls, 211 clqs
(round 20, exhaustive) 512 del vars, 428 del conss, 0 add conss, 461 chg bounds, 1271 chg sides, 969 chg coeffs, 247 upgd conss, 573 impls, 364 clqs
(round 21, fast)       512 del vars, 450 del conss, 0 add conss, 461 chg bounds, 1328 chg sides, 1028 chg coeffs, 247 upgd conss, 573 impls, 364 clqs
(round 22, medium)     512 del vars, 452 del conss, 0 add conss, 461 chg bounds, 1330 chg sides, 1028 chg coeffs, 247 upgd conss, 573 impls, 364 clqs
(round 23, exhaustive) 512 del vars, 454 del conss, 0 add conss, 461 chg bounds, 1330 chg sides, 1028 chg coeffs, 247 upgd conss, 573 impls, 364 clqs
(round 24, exhaustive) 512 del vars, 454 del conss, 0 add conss, 461 chg bounds, 1330 chg sides, 1028 chg coeffs, 248 upgd conss, 573 impls, 364 clqs
(round 25, exhaustive) 537 del vars, 454 del conss, 0 add conss, 463 chg bounds, 1330 chg sides, 1028 chg coeffs, 248 upgd conss, 666 impls, 790 clqs
(round 26, fast)       537 del vars, 458 del conss, 0 add conss, 463 chg bounds, 1334 chg sides, 1031 chg coeffs, 248 upgd conss, 666 impls, 790 clqs
(round 27, exhaustive) 537 del vars, 462 del conss, 0 add conss, 463 chg bounds, 1334 chg sides, 1031 chg coeffs, 248 upgd conss, 666 impls, 790 clqs
(round 28, exhaustive) 537 del vars, 462 del conss, 0 add conss, 463 chg bounds, 1334 chg sides, 1031 chg coeffs, 249 upgd conss, 666 impls, 790 clqs
(round 29, exhaustive) 563 del vars, 462 del conss, 0 add conss, 468 chg bounds, 1334 chg sides, 1031 chg coeffs, 249 upgd conss, 822 impls, 1238 clqs
(round 30, fast)       563 del vars, 473 del conss, 0 add conss, 468 chg bounds, 1378 chg sides, 1070 chg coeffs, 249 upgd conss, 824 impls, 1243 clqs
(round 31, medium)     565 del vars, 474 del conss, 0 add conss, 468 chg bounds, 1379 chg sides, 1070 chg coeffs, 249 upgd conss, 824 impls, 1238 clqs
(round 32, fast)       565 del vars, 475 del conss, 0 add conss, 468 chg bounds, 1380 chg sides, 1074 chg coeffs, 249 upgd conss, 824 impls, 1239 clqs
(round 33, exhaustive) 565 del vars, 478 del conss, 0 add conss, 468 chg bounds, 1380 chg sides, 1074 chg coeffs, 249 upgd conss, 824 impls, 1239 clqs
(round 34, exhaustive) 565 del vars, 478 del conss, 0 add conss, 468 chg bounds, 1380 chg sides, 1074 chg coeffs, 254 upgd conss, 824 impls, 1239 clqs
(round 35, exhaustive) 591 del vars, 478 del conss, 0 add conss, 469 chg bounds, 1380 chg sides, 1074 chg coeffs, 254 upgd conss, 900 impls, 1342 clqs
(round 36, fast)       591 del vars, 479 del conss, 0 add conss, 469 chg bounds, 1387 chg sides, 1080 chg coeffs, 254 upgd conss, 900 impls, 1342 clqs
(round 37, medium)     592 del vars, 479 del conss, 0 add conss, 469 chg bounds, 1387 chg sides, 1080 chg coeffs, 254 upgd conss, 900 impls, 1238 clqs
(round 38, fast)       601 del vars, 496 del conss, 0 add conss, 473 chg bounds, 1421 chg sides, 1111 chg coeffs, 254 upgd conss, 905 impls, 1250 clqs
(round 39, fast)       602 del vars, 497 del conss, 1 add conss, 473 chg bounds, 1421 chg sides, 1111 chg coeffs, 254 upgd conss, 905 impls, 1260 clqs
(round 40, exhaustive) 602 del vars, 502 del conss, 1 add conss, 473 chg bounds, 1421 chg sides, 1111 chg coeffs, 254 upgd conss, 905 impls, 1260 clqs
(round 41, exhaustive) 602 del vars, 502 del conss, 1 add conss, 473 chg bounds, 1421 chg sides, 1111 chg coeffs, 258 upgd conss, 905 impls, 1260 clqs
(round 42, medium)     602 del vars, 502 del conss, 1 add conss, 473 chg bounds, 1423 chg sides, 1111 chg coeffs, 258 upgd conss, 909 impls, 1270 clqs
(round 43, exhaustive) 627 del vars, 502 del conss, 1 add conss, 475 chg bounds, 1423 chg sides, 1111 chg coeffs, 258 upgd conss, 1215 impls, 1651 clqs
(round 44, fast)       627 del vars, 508 del conss, 1 add conss, 475 chg bounds, 1448 chg sides, 1125 chg coeffs, 258 upgd conss, 1217 impls, 1653 clqs
(round 45, medium)     630 del vars, 508 del conss, 1 add conss, 475 chg bounds, 1448 chg sides, 1125 chg coeffs, 258 upgd conss, 1256 impls, 1556 clqs
(round 46, medium)     630 del vars, 510 del conss, 1 add conss, 475 chg bounds, 1449 chg sides, 1125 chg coeffs, 258 upgd conss, 1256 impls, 1556 clqs
(round 47, exhaustive) 630 del vars, 513 del conss, 1 add conss, 475 chg bounds, 1449 chg sides, 1125 chg coeffs, 258 upgd conss, 1256 impls, 1556 clqs
(round 48, exhaustive) 630 del vars, 513 del conss, 1 add conss, 475 chg bounds, 1449 chg sides, 1125 chg coeffs, 269 upgd conss, 1256 impls, 1556 clqs
(round 49, exhaustive) 658 del vars, 513 del conss, 1 add conss, 478 chg bounds, 1449 chg sides, 1125 chg coeffs, 269 upgd conss, 1549 impls, 1584 clqs
(round 50, fast)       658 del vars, 526 del conss, 1 add conss, 478 chg bounds, 1475 chg sides, 1147 chg coeffs, 269 upgd conss, 1549 impls, 1584 clqs
(round 51, exhaustive) 658 del vars, 535 del conss, 1 add conss, 478 chg bounds, 1475 chg sides, 1147 chg coeffs, 269 upgd conss, 1549 impls, 1584 clqs
(round 52, exhaustive) 658 del vars, 535 del conss, 1 add conss, 478 chg bounds, 1475 chg sides, 1147 chg coeffs, 287 upgd conss, 1549 impls, 1584 clqs
(round 53, fast)       658 del vars, 536 del conss, 1 add conss, 478 chg bounds, 1475 chg sides, 1147 chg coeffs, 287 upgd conss, 1556 impls, 1645 clqs
(round 54, medium)     658 del vars, 543 del conss, 1 add conss, 478 chg bounds, 1478 chg sides, 1149 chg coeffs, 287 upgd conss, 1556 impls, 1645 clqs
(round 55, exhaustive) 716 del vars, 543 del conss, 1 add conss, 483 chg bounds, 1478 chg sides, 1149 chg coeffs, 287 upgd conss, 1585 impls, 1230 clqs
(round 56, fast)       717 del vars, 604 del conss, 1 add conss, 483 chg bounds, 1513 chg sides, 1178 chg coeffs, 287 upgd conss, 1587 impls, 1240 clqs
(round 57, medium)     720 del vars, 607 del conss, 1 add conss, 483 chg bounds, 1516 chg sides, 1178 chg coeffs, 287 upgd conss, 1588 impls, 1240 clqs
(round 58, fast)       720 del vars, 610 del conss, 1 add conss, 483 chg bounds, 1521 chg sides, 1187 chg coeffs, 287 upgd conss, 1589 impls, 1245 clqs
(round 59, exhaustive) 720 del vars, 611 del conss, 1 add conss, 483 chg bounds, 1521 chg sides, 1187 chg coeffs, 287 upgd conss, 1589 impls, 1245 clqs
(round 60, exhaustive) 720 del vars, 611 del conss, 1 add conss, 483 chg bounds, 1521 chg sides, 1187 chg coeffs, 304 upgd conss, 1589 impls, 1245 clqs
(round 61, exhaustive) 754 del vars, 611 del conss, 1 add conss, 488 chg bounds, 1521 chg sides, 1187 chg coeffs, 304 upgd conss, 1723 impls, 1007 clqs
(round 62, fast)       755 del vars, 641 del conss, 4 add conss, 488 chg bounds, 1555 chg sides, 1209 chg coeffs, 304 upgd conss, 1723 impls, 1019 clqs
(round 63, medium)     772 del vars, 643 del conss, 4 add conss, 488 chg bounds, 1561 chg sides, 1209 chg coeffs, 304 upgd conss, 1743 impls, 871 clqs
(round 64, fast)       772 del vars, 648 del conss, 5 add conss, 489 chg bounds, 1568 chg sides, 1213 chg coeffs, 304 upgd conss, 1743 impls, 874 clqs
(round 65, fast)       772 del vars, 648 del conss, 5 add conss, 489 chg bounds, 1570 chg sides, 1215 chg coeffs, 304 upgd conss, 1743 impls, 874 clqs
(round 66, medium)     772 del vars, 652 del conss, 5 add conss, 489 chg bounds, 1576 chg sides, 1216 chg coeffs, 304 upgd conss, 1743 impls, 874 clqs
(round 67, exhaustive) 772 del vars, 653 del conss, 5 add conss, 489 chg bounds, 1576 chg sides, 1216 chg coeffs, 304 upgd conss, 1743 impls, 874 clqs
(round 68, exhaustive) 772 del vars, 653 del conss, 5 add conss, 489 chg bounds, 1576 chg sides, 1216 chg coeffs, 321 upgd conss, 1743 impls, 874 clqs
(round 69, exhaustive) 829 del vars, 653 del conss, 5 add conss, 492 chg bounds, 1576 chg sides, 1216 chg coeffs, 321 upgd conss, 1925 impls, 778 clqs
(round 70, fast)       1067 del vars, 749 del conss, 5 add conss, 549 chg bounds, 1661 chg sides, 1274 chg coeffs, 321 upgd conss, 1932 impls, 0 clqs
(round 71, fast)       1258 del vars, 998 del conss, 5 add conss, 590 chg bounds, 1663 chg sides, 1274 chg coeffs, 321 upgd conss, 1932 impls, 0 clqs
   Deactivated symmetry handling methods, since SCIP was built without symmetry detector (SYM=none).
presolving (72 rounds: 72 fast, 46 medium, 30 exhaustive):
 1258 deleted vars, 1000 deleted constraints, 5 added constraints, 590 tightened bounds, 0 added holes, 1663 changed sides, 1274 changed coefficients
 1932 implications, 0 cliques
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