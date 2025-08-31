# OptimizationLib Online Games Examples
This repository contains various online games optimization problems solved using the OptimizationLib framework. The examples serve as templates for readers, enabling them to understand and develop their own optimization problems by following the structures used in these examples.

| n | Package                                                                                                                           | Difficulty [1&rarr;5] | Comment                                                                                      |
|---|-----------------------------------------------------------------------------------------------------------------------------------|-----------------------|----------------------------------------------------------------------------------------------|
| 1 | [LinkedInQueens](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/LinkedInQueens) | 1                     | LinkedIn Queens using Mathematical Modeling.                                                 |
| 2 | [LinkedInTango](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/LinkedInTango)   | 1                     | LinkedIn Tango using Mathematical Modeling.                                                  |
| 3 | [LinkedInZip](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/LinkedInZip)       | 3                     | LinkedIn Zip using Ant Colony Optimization and Mathematical Modeling                         |
| 4 | [LinkedInSudoku](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/LinkedInSudoku) | 1                     | LinkedIn Sudoku using Mathematical Modeling                                                  |
| 5 | [Sudoku](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/Sudoku)                 | 2                     | Online sudoku game https://sudoku.com/ using Mathematical Modeling                           |
| 6 | [Maze](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/Maze)                     | 1                     | Online maze game https://www.mathsisfun.com/measure/mazes.html using Mathematical Modeling   |
| 7 | [AllOut](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/AllOut)                 | 2                     | Online All-Out game https://www.mathsisfun.com/games/allout.html using Genetic Algorithm.    |
| 8 | [MadVirus](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/MadVirus)             | 1                     | Online All-Out game https://www.mathsisfun.com/games/mad-virus.html using Genetic Algorithm. |
| 9 | [LongCat](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/LongCat)               | 1                     | Online Long-Cat game https://poki.com/en/g/longcat using Genetic Algorithm.                  |

The difficulty level ranges from a minimum of (1) to a maximum of (5) in this repository.

## In This Folder
This folder contains 8 online game solved using the OptimizationLib:

1. [LinkedInQueens](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/LinkedInQueens):
   This problem involves placing N queens on an NxN chessboard such that each row, column and color only contains one Queen, and two queens cannot touch each other, not even diagonally.

2. [LinkedInTango](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/LinkedInTango):
   This problem consists of filling an NxN grid so that each cell contains either a :sunny: or a :first_quarter_moon_with_face:. No more than 2 :sunny: or :first_quarter_moon_with_face: may be next to each other either vertically or horizontally. Each row and column must contain the same number of :sunny: and :first_quarter_moon_with_face:. There are some constraints between cells. Cells separated with = must be the same type, and cells separated by X must be the opposite type.

3. [LinkedInZip](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/LinkedInZip):
   This problem consists of connecting X numbered cells of an NxM grid following the number order, and filling every cell with the path followed. Some barriers separate some cells.

4. [LinkedInSudoku](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/LinkedInSudoku):
This problem involves placing numbers 1&rarr;M in a MxM grid, in such a way  that all numbers are present in each row, each column and each of the M NxM/N predefined blocks.

5. [Sudoku](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/Sudoku):
   This problem involves placing numbers 1&rarr;9 in a 9x9 grid, in such a way  that all numbers are present in each row, each column and each of the 9 3x3 predefined blocks.

6. [Maze](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/Maze):
   This problem involves searching the path that connects two points in a maze.

7. [AllOut](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/AllOut):
   This problem involves turning off all the lights on a board grid. When you switch a light all surrounding (North, East, South & West) lights also switch.

8. [MadVirus](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/MadVirus):
   This problem involves infecting all the grid cells on a board before we run out of turns. A virus starts on a cell. It can infect surrounding cells by selecting a color to infect. When you choose a color, all cells of that color that are in contact with the virus are infected and become the virus.

9. [LongCat](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/LongCat):
   This problem involves visiting all nodes on a board. You start from a node and decide in which direction to move. The movement in that direction continues until you hit a wall, the board end, or your previous path.

## Comment:
The examples in this repository illustrate the power and versatility of the OptimizationLib framework. Remember, these examples serve as templates that you can adapt for your specific optimization problems.

