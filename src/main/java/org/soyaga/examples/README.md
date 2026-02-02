# OptimizationLib Online Games Examples
This repository contains various online games optimization problems solved using the OptimizationLib framework. The examples serve as templates for readers, enabling them to understand and develop their own optimization problems by following the structures used in these examples.

| n  | Package                                                                                                                           | Difficulty [1&rarr;5] | Comment                                                                                                                |
|----|-----------------------------------------------------------------------------------------------------------------------------------|-----------------------|------------------------------------------------------------------------------------------------------------------------|
| 1  | [LinkedInQueens](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/LinkedInQueens) | 1                     | LinkedIn Queens using Mathematical Modeling.                                                                           |
| 2  | [LinkedInTango](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/LinkedInTango)   | 1                     | LinkedIn Tango using Mathematical Modeling.                                                                            |
| 3  | [LinkedInZip](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/LinkedInZip)       | 3                     | LinkedIn Zip using Ant Colony Optimization and Mathematical Modeling                                                   |
| 4  | [LinkedInSudoku](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/LinkedInSudoku) | 1                     | LinkedIn Sudoku using Mathematical Modeling                                                                            |
| 5  | [Sudoku](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/Sudoku)                 | 2                     | Online sudoku game https://sudoku.com/ using Mathematical Modeling                                                     |
| 6  | [Maze](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/Maze)                     | 1                     | Online maze game https://www.mathsisfun.com/measure/mazes.html using Mathematical Modeling                             |
| 7  | [AllOut](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/AllOut)                 | 2                     | Online All-Out game https://www.mathsisfun.com/games/allout.html using Genetic Algorithm.                              |
| 8  | [MadVirus](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/MadVirus)             | 1                     | Online All-Out game https://www.mathsisfun.com/games/mad-virus.html using Genetic Algorithm.                           |
| 9  | [LongCat](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/LongCat)               | 1                     | Online Long-Cat game https://poki.com/en/g/longcat using Genetic Algorithm.                                            |
| 10 | [LongCat](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/JellyDoods)            | 1                     | Online Jelly-Doods game https://www.mathplayground.com/logic_jelly_doods.html using Genetic Algorithm.                 |
| 11 | [Game2048](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/Game2048)             | 3                     | Online 2048 game https://www.2048.org/ using Genetic Algorithm.                                                        |
| 12 | [NonoGrams](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/NonoGrams)           | 3                     | Online NonoGrams puzzle https://www.nonograms.org using Mathematical Modeling, Constrained Programming and SAT solver. |
| 13 | [RushHour](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/RushHour)             | 1                     | Online RushHour puzzle https://rushhourgame.thinkfun.com/ using Genetic Algorithm.                                     |

The difficulty level ranges from a minimum of (1) to a maximum of (5) in this repository.

## In This Folder
This folder contains 13 online games solved using the OptimizationLib:

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

10. [JellyDoods](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/JellyDoods):
   This problem involves merging all same color jellies on a board. You decide in which direction to move each jelly. The movement in that direction continues until you hit a wall, or other jelly. When you hit a jelly of the same color, the two jellies merge. You have a limited number of movements.

11. [Game2048](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/Game2048):
    The purpose of the game is to dictate a sequence of arrow movements on a 4x4 board with the purpose of reaching the 2048 value on the board. Each arrow movement:
    - triggers numbers on the board to move in that direction should free space be available.
    - merges two numbers of the same value into their sum when they are on consecutive positions aligned with the arrow's direction.
    - triggers new values of 2 or 4 to appear randomly on the board.

12. [NonoGrams](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/NonoGrams):
   This problem involves coloring a grid using column and row constraints. Each constraint identifies a coloring block in a row/column. It also determines the order in which the colors must appear. If two consecutive constraints are for the same color, then, they are separated in the row/colum.

13. [RushHour](https://github.com/SergioOyaga/OnlineGamesExamples/tree/master/src/main/java/org/soyaga/examples/RushHour):
    The goal of the game is to get only the red car out through the exit of the board by moving the other vehicles out of its way.

## Comment:
The examples in this repository illustrate the power and versatility of the OptimizationLib framework. Remember, these examples serve as templates that you can adapt for your specific optimization problems.

