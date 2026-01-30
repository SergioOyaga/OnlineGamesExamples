# RushHour
For this https://rushhourgame.thinkfun.com/ rush hour game, we use a GA to get a decision sequence represented as a genome. Then we compute whether the proposed genome provides a solution.
In the [GA-representation](#ga-representation) we can see the genome representation.

<img src="https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/out/RushHour/RushHour.gif"  title="RushHour example" alt="RushHour example"/>

## RushHour
The board is a 6×6 grid. Cars and trucks are both one square wide, but cars are two squares long and trucks are three squares long. Vehicles can only be moved along a straight line on the grid; rotation is forbidden.

The goal of the game is to get only the red car out through the exit of the board by moving the other vehicles out of its way. However, the cars and trucks obstruct the path of both the red car and each other, which makes the puzzle even more difficult.

### Board

````mermaid
block-beta
  columns 7

  %% --- 6x6 board (left) ---
  block:BOARD[" "]:6
    columns 6

    c11["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c12["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c13["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c14["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c15["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c16["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]

    c21["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c22["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c23["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c24["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c25["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c26["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]

    c31["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c32["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c33["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c34["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c35["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c36["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]

    c41["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c42["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c43["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c44["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c45["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c46["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]

    c51["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c52["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c53["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c54["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c55["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c56["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]

    c61["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c62["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c63["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c64["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c65["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    c66["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
  end

  %% --- right-side "exit" column (outside the board) ---
  %% Make this column narrow by using smaller labels.
  block:OUT[" "]:1
    columns 1
    o1["&nbsp;<br/>&nbsp;"]
    o2["&nbsp;<br/>&nbsp;"]
    o3["&nbsp;<br/>&nbsp;"]
    o4["&nbsp;<br/>&nbsp;"]
    o5["&nbsp;<br/>&nbsp;"]
    o6["&nbsp;<br/>&nbsp;"]
  end

  %% --- Frame styling ---
  style BOARD fill:#5f647e,stroke:#3f4357,stroke-width:18px
  style OUT fill:transparent,stroke:transparent,stroke-width:0px

  %% --- Default empty-cell styling (apply to ALL board cells first) ---
  style c11 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c12 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c13 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c14 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c15 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c16 fill:#6e7392,stroke:#52566f,stroke-width:4px

  style c21 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c22 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c23 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c24 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c25 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c26 fill:#6e7392,stroke:#52566f,stroke-width:4px

  style c31 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c32 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c33 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c34 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c35 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c36 fill:#6e7392,stroke:#52566f,stroke-width:4px

  style c41 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c42 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c43 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c44 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c45 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c46 fill:#6e7392,stroke:#52566f,stroke-width:4px

  style c51 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c52 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c53 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c54 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c55 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c56 fill:#6e7392,stroke:#52566f,stroke-width:4px

  style c61 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c62 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c63 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c64 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c65 fill:#6e7392,stroke:#52566f,stroke-width:4px
  style c66 fill:#6e7392,stroke:#52566f,stroke-width:4px

  %% --- Vehicles (override specific cells; stroke removed to “merge” cells) ---
  %% Red (row 3, col 1-2)
  style c31 fill:#9c1b23,stroke:transparent,stroke-width:0px
  style c32 fill:#9c1b23,stroke:transparent,stroke-width:0px

  %% Cyan vertical (row 2-3, col 3)
  style c23 fill:#6dbfd2,stroke:transparent,stroke-width:0px
  style c33 fill:#6dbfd2,stroke:transparent,stroke-width:0px

  %% Yellow truck vertical (row 2-4, col 5)
  style c25 fill:#d9d86a,stroke:transparent,stroke-width:0px
  style c35 fill:#d9d86a,stroke:transparent,stroke-width:0px
  style c45 fill:#d9d86a,stroke:transparent,stroke-width:0px

  %% Orange vertical (row 3-4, col 6)
  style c36 fill:#a65a1f,stroke:transparent,stroke-width:0px
  style c46 fill:#a65a1f,stroke:transparent,stroke-width:0px

  %% Lavender horizontal truck (row 4, col 1-3)
  style c41 fill:#9a7ad8,stroke:transparent,stroke-width:0px
  style c42 fill:#9a7ad8,stroke:transparent,stroke-width:0px
  style c43 fill:#9a7ad8,stroke:transparent,stroke-width:0px

  %% Bottom-left vertical cars (rows 5-6)
  style c51 fill:#5b8fea,stroke:transparent,stroke-width:0px
  style c61 fill:#5b8fea,stroke:transparent,stroke-width:0px

  style c52 fill:#c99ad8,stroke:transparent,stroke-width:0px
  style c62 fill:#c99ad8,stroke:transparent,stroke-width:0px

  style c53 fill:#5630d1,stroke:transparent,stroke-width:0px
  style c63 fill:#5630d1,stroke:transparent,stroke-width:0px

  %% Teal horizontal (row 5, col 5-6)
  style c55 fill:#1e7f89,stroke:transparent,stroke-width:0px
  style c56 fill:#1e7f89,stroke:transparent,stroke-width:0px

  %% Silver horizontal (row 6, col 5-6)
  style c65 fill:#8a8d96,stroke:transparent,stroke-width:0px
  style c66 fill:#8a8d96,stroke:transparent,stroke-width:0px

  %% --- Right-side white exit rectangle (row 3 of OUT column) ---
  %% (Make other OUT cells invisible)
  style o1 fill:transparent,stroke:transparent,stroke-width:0px
  style o2 fill:transparent,stroke:transparent,stroke-width:0px
  style o3 fill:#ffffff,stroke:#ffffff,stroke-width:0px
  style o4 fill:transparent,stroke:transparent,stroke-width:0px
  style o5 fill:transparent,stroke:transparent,stroke-width:0px
  style o6 fill:transparent,stroke:transparent,stroke-width:0px
````

### GA Representation:
The genome looks like:

````mermaid
flowchart LR
  subgraph  ide1 [Genome]
    direction LR
        subgraph ide2[Movement1]
            direction LR
            P1[CAR-TURQUOISE]
            M1[North]
            D1["&nbsp;1&nbsp;"]
        end
        subgraph ide3[Movement2]
            direction LR
            P2[TRUCK-YELLOW]
            M2[North]
            D2["&nbsp;1&nbsp;"]
        end
        subgraph ide4[Movement3]
            direction LR
            P3[TRUCK-PURPLE]
            M3[EAST]
            D3["&nbsp;2&nbsp;"]
        end
      subgraph ide5[MovementX]
          direction LR
          P4[***]
          M4[***]
          D4[***]
      end
      subgraph ide6[MovementLast]
          direction LR
          P5[CAR-RED]
          M5[East]
          D3["&nbsp;5&nbsp;"]
      end
      ide2-->ide3-->ide4-->ide5-->ide6
      style ide1 fill:#0405
      style ide2 fill:#4005
      style ide3 fill:#4005
      style ide4 fill:#4005
      style ide5 fill:#4005
      style ide6 fill:#4005
  end
````
Using this representation, we can try different movement combinations to find out which one solves the puzzle.

For the initial board, the new boards states can look like:

CAR-TURQUOISE to North 1 square:
````mermaid
block-beta
    columns 7

%% --- 6x6 board (left) ---
    block:BOARD[" "]:6
        columns 6

        c11["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c12["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c13["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c14["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c15["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c16["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]

        c21["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c22["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c23["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c24["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c25["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c26["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]

        c31["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c32["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c33["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c34["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c35["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c36["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]

        c41["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c42["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c43["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c44["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c45["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c46["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]

        c51["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c52["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c53["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c54["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c55["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c56["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]

        c61["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c62["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c63["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c64["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c65["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c66["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    end

%% --- right-side "exit" column (outside the board) ---
%% Make this column narrow by using smaller labels.
    block:OUT[" "]:1
        columns 1
        o1["&nbsp;<br/>&nbsp;"]
        o2["&nbsp;<br/>&nbsp;"]
        o3["&nbsp;<br/>&nbsp;"]
        o4["&nbsp;<br/>&nbsp;"]
        o5["&nbsp;<br/>&nbsp;"]
        o6["&nbsp;<br/>&nbsp;"]
    end

%% --- Frame styling ---
    style BOARD fill:#5f647e,stroke:#3f4357,stroke-width:18px
    style OUT fill:transparent,stroke:transparent,stroke-width:0px

%% --- Default empty-cell styling (apply to ALL board cells first) ---
    style c11 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c12 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c13 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c14 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c15 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c16 fill:#6e7392,stroke:#52566f,stroke-width:4px

    style c21 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c22 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c23 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c24 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c25 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c26 fill:#6e7392,stroke:#52566f,stroke-width:4px

    style c31 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c32 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c33 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c34 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c35 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c36 fill:#6e7392,stroke:#52566f,stroke-width:4px

    style c41 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c42 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c43 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c44 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c45 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c46 fill:#6e7392,stroke:#52566f,stroke-width:4px

    style c51 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c52 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c53 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c54 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c55 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c56 fill:#6e7392,stroke:#52566f,stroke-width:4px

    style c61 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c62 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c63 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c64 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c65 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c66 fill:#6e7392,stroke:#52566f,stroke-width:4px

%% --- Vehicles (override specific cells; stroke removed to “merge” cells) ---
%% Red (row 3, col 1-2)
    style c31 fill:#9c1b23,stroke:transparent,stroke-width:0px
    style c32 fill:#9c1b23,stroke:transparent,stroke-width:0px

%% Cyan vertical (row 2-3, col 3)
    style c13 fill:#6dbfd2,stroke:transparent,stroke-width:0px
    style c23 fill:#6dbfd2,stroke:transparent,stroke-width:0px

%% Yellow truck vertical (row 2-4, col 5)
    style c25 fill:#d9d86a,stroke:transparent,stroke-width:0px
    style c35 fill:#d9d86a,stroke:transparent,stroke-width:0px
    style c45 fill:#d9d86a,stroke:transparent,stroke-width:0px

%% Orange vertical (row 3-4, col 6)
    style c36 fill:#a65a1f,stroke:transparent,stroke-width:0px
    style c46 fill:#a65a1f,stroke:transparent,stroke-width:0px

%% Lavender horizontal truck (row 4, col 1-3)
    style c41 fill:#9a7ad8,stroke:transparent,stroke-width:0px
    style c42 fill:#9a7ad8,stroke:transparent,stroke-width:0px
    style c43 fill:#9a7ad8,stroke:transparent,stroke-width:0px

%% Bottom-left vertical cars (rows 5-6)
    style c51 fill:#5b8fea,stroke:transparent,stroke-width:0px
    style c61 fill:#5b8fea,stroke:transparent,stroke-width:0px

    style c52 fill:#c99ad8,stroke:transparent,stroke-width:0px
    style c62 fill:#c99ad8,stroke:transparent,stroke-width:0px

    style c53 fill:#5630d1,stroke:transparent,stroke-width:0px
    style c63 fill:#5630d1,stroke:transparent,stroke-width:0px

%% Teal horizontal (row 5, col 5-6)
    style c55 fill:#1e7f89,stroke:transparent,stroke-width:0px
    style c56 fill:#1e7f89,stroke:transparent,stroke-width:0px

%% Silver horizontal (row 6, col 5-6)
    style c65 fill:#8a8d96,stroke:transparent,stroke-width:0px
    style c66 fill:#8a8d96,stroke:transparent,stroke-width:0px

%% --- Right-side white exit rectangle (row 3 of OUT column) ---
%% (Make other OUT cells invisible)
    style o1 fill:transparent,stroke:transparent,stroke-width:0px
    style o2 fill:transparent,stroke:transparent,stroke-width:0px
    style o3 fill:#ffffff,stroke:#ffffff,stroke-width:0px
    style o4 fill:transparent,stroke:transparent,stroke-width:0px
    style o5 fill:transparent,stroke:transparent,stroke-width:0px
    style o6 fill:transparent,stroke:transparent,stroke-width:0px
````

TRUCK-YELLOW to North 1 square:
````mermaid
block-beta
    columns 7

%% --- 6x6 board (left) ---
    block:BOARD[" "]:6
        columns 6

        c11["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c12["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c13["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c14["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c15["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c16["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]

        c21["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c22["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c23["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c24["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c25["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c26["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]

        c31["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c32["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c33["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c34["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c35["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c36["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]

        c41["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c42["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c43["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c44["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c45["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c46["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]

        c51["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c52["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c53["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c54["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c55["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c56["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]

        c61["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c62["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c63["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c64["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c65["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c66["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    end

%% --- right-side "exit" column (outside the board) ---
%% Make this column narrow by using smaller labels.
    block:OUT[" "]:1
        columns 1
        o1["&nbsp;<br/>&nbsp;"]
        o2["&nbsp;<br/>&nbsp;"]
        o3["&nbsp;<br/>&nbsp;"]
        o4["&nbsp;<br/>&nbsp;"]
        o5["&nbsp;<br/>&nbsp;"]
        o6["&nbsp;<br/>&nbsp;"]
    end

%% --- Frame styling ---
    style BOARD fill:#5f647e,stroke:#3f4357,stroke-width:18px
    style OUT fill:transparent,stroke:transparent,stroke-width:0px

%% --- Default empty-cell styling (apply to ALL board cells first) ---
    style c11 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c12 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c13 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c14 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c15 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c16 fill:#6e7392,stroke:#52566f,stroke-width:4px

    style c21 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c22 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c23 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c24 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c25 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c26 fill:#6e7392,stroke:#52566f,stroke-width:4px

    style c31 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c32 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c33 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c34 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c35 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c36 fill:#6e7392,stroke:#52566f,stroke-width:4px

    style c41 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c42 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c43 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c44 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c45 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c46 fill:#6e7392,stroke:#52566f,stroke-width:4px

    style c51 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c52 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c53 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c54 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c55 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c56 fill:#6e7392,stroke:#52566f,stroke-width:4px

    style c61 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c62 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c63 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c64 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c65 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c66 fill:#6e7392,stroke:#52566f,stroke-width:4px

%% --- Vehicles (override specific cells; stroke removed to “merge” cells) ---
%% Red (row 3, col 1-2)
    style c31 fill:#9c1b23,stroke:transparent,stroke-width:0px
    style c32 fill:#9c1b23,stroke:transparent,stroke-width:0px

%% Cyan vertical (row 2-3, col 3)
    style c13 fill:#6dbfd2,stroke:transparent,stroke-width:0px
    style c23 fill:#6dbfd2,stroke:transparent,stroke-width:0px

%% Yellow truck vertical (row 2-4, col 5)
    style c15 fill:#d9d86a,stroke:transparent,stroke-width:0px
    style c25 fill:#d9d86a,stroke:transparent,stroke-width:0px
    style c35 fill:#d9d86a,stroke:transparent,stroke-width:0px

%% Orange vertical (row 3-4, col 6)
    style c36 fill:#a65a1f,stroke:transparent,stroke-width:0px
    style c46 fill:#a65a1f,stroke:transparent,stroke-width:0px

%% Lavender horizontal truck (row 4, col 1-3)
    style c41 fill:#9a7ad8,stroke:transparent,stroke-width:0px
    style c42 fill:#9a7ad8,stroke:transparent,stroke-width:0px
    style c43 fill:#9a7ad8,stroke:transparent,stroke-width:0px

%% Bottom-left vertical cars (rows 5-6)
    style c51 fill:#5b8fea,stroke:transparent,stroke-width:0px
    style c61 fill:#5b8fea,stroke:transparent,stroke-width:0px

    style c52 fill:#c99ad8,stroke:transparent,stroke-width:0px
    style c62 fill:#c99ad8,stroke:transparent,stroke-width:0px

    style c53 fill:#5630d1,stroke:transparent,stroke-width:0px
    style c63 fill:#5630d1,stroke:transparent,stroke-width:0px

%% Teal horizontal (row 5, col 5-6)
    style c55 fill:#1e7f89,stroke:transparent,stroke-width:0px
    style c56 fill:#1e7f89,stroke:transparent,stroke-width:0px

%% Silver horizontal (row 6, col 5-6)
    style c65 fill:#8a8d96,stroke:transparent,stroke-width:0px
    style c66 fill:#8a8d96,stroke:transparent,stroke-width:0px

%% --- Right-side white exit rectangle (row 3 of OUT column) ---
%% (Make other OUT cells invisible)
    style o1 fill:transparent,stroke:transparent,stroke-width:0px
    style o2 fill:transparent,stroke:transparent,stroke-width:0px
    style o3 fill:#ffffff,stroke:#ffffff,stroke-width:0px
    style o4 fill:transparent,stroke:transparent,stroke-width:0px
    style o5 fill:transparent,stroke:transparent,stroke-width:0px
    style o6 fill:transparent,stroke:transparent,stroke-width:0px
````

TRUCK-PURPLE to East 2 squares:
````mermaid
block-beta
    columns 7

%% --- 6x6 board (left) ---
    block:BOARD[" "]:6
        columns 6

        c11["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c12["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c13["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c14["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c15["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c16["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]

        c21["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c22["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c23["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c24["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c25["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c26["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]

        c31["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c32["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c33["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c34["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c35["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c36["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]

        c41["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c42["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c43["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c44["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c45["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c46["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]

        c51["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c52["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c53["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c54["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c55["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c56["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]

        c61["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c62["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c63["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c64["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c65["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c66["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    end

%% --- right-side "exit" column (outside the board) ---
%% Make this column narrow by using smaller labels.
    block:OUT[" "]:1
        columns 1
        o1["&nbsp;<br/>&nbsp;"]
        o2["&nbsp;<br/>&nbsp;"]
        o3["&nbsp;<br/>&nbsp;"]
        o4["&nbsp;<br/>&nbsp;"]
        o5["&nbsp;<br/>&nbsp;"]
        o6["&nbsp;<br/>&nbsp;"]
    end

%% --- Frame styling ---
    style BOARD fill:#5f647e,stroke:#3f4357,stroke-width:18px
    style OUT fill:transparent,stroke:transparent,stroke-width:0px

%% --- Default empty-cell styling (apply to ALL board cells first) ---
    style c11 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c12 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c13 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c14 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c15 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c16 fill:#6e7392,stroke:#52566f,stroke-width:4px

    style c21 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c22 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c23 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c24 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c25 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c26 fill:#6e7392,stroke:#52566f,stroke-width:4px

    style c31 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c32 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c33 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c34 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c35 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c36 fill:#6e7392,stroke:#52566f,stroke-width:4px

    style c41 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c42 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c43 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c44 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c45 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c46 fill:#6e7392,stroke:#52566f,stroke-width:4px

    style c51 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c52 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c53 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c54 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c55 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c56 fill:#6e7392,stroke:#52566f,stroke-width:4px

    style c61 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c62 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c63 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c64 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c65 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c66 fill:#6e7392,stroke:#52566f,stroke-width:4px

%% --- Vehicles (override specific cells; stroke removed to “merge” cells) ---
%% Red (row 3, col 1-2)
    style c31 fill:#9c1b23,stroke:transparent,stroke-width:0px
    style c32 fill:#9c1b23,stroke:transparent,stroke-width:0px

%% Cyan vertical (row 2-3, col 3)
    style c13 fill:#6dbfd2,stroke:transparent,stroke-width:0px
    style c23 fill:#6dbfd2,stroke:transparent,stroke-width:0px

%% Yellow truck vertical (row 2-4, col 5)
    style c15 fill:#d9d86a,stroke:transparent,stroke-width:0px
    style c25 fill:#d9d86a,stroke:transparent,stroke-width:0px
    style c35 fill:#d9d86a,stroke:transparent,stroke-width:0px

%% Orange vertical (row 3-4, col 6)
    style c36 fill:#a65a1f,stroke:transparent,stroke-width:0px
    style c46 fill:#a65a1f,stroke:transparent,stroke-width:0px

%% Lavender horizontal truck (row 4, col 1-3)
    style c43 fill:#9a7ad8,stroke:transparent,stroke-width:0px
    style c44 fill:#9a7ad8,stroke:transparent,stroke-width:0px
    style c45 fill:#9a7ad8,stroke:transparent,stroke-width:0px

%% Bottom-left vertical cars (rows 5-6)
    style c51 fill:#5b8fea,stroke:transparent,stroke-width:0px
    style c61 fill:#5b8fea,stroke:transparent,stroke-width:0px

    style c52 fill:#c99ad8,stroke:transparent,stroke-width:0px
    style c62 fill:#c99ad8,stroke:transparent,stroke-width:0px

    style c53 fill:#5630d1,stroke:transparent,stroke-width:0px
    style c63 fill:#5630d1,stroke:transparent,stroke-width:0px

%% Teal horizontal (row 5, col 5-6)
    style c55 fill:#1e7f89,stroke:transparent,stroke-width:0px
    style c56 fill:#1e7f89,stroke:transparent,stroke-width:0px

%% Silver horizontal (row 6, col 5-6)
    style c65 fill:#8a8d96,stroke:transparent,stroke-width:0px
    style c66 fill:#8a8d96,stroke:transparent,stroke-width:0px

%% --- Right-side white exit rectangle (row 3 of OUT column) ---
%% (Make other OUT cells invisible)
    style o1 fill:transparent,stroke:transparent,stroke-width:0px
    style o2 fill:transparent,stroke:transparent,stroke-width:0px
    style o3 fill:#ffffff,stroke:#ffffff,stroke-width:0px
    style o4 fill:transparent,stroke:transparent,stroke-width:0px
    style o5 fill:transparent,stroke:transparent,stroke-width:0px
    style o6 fill:transparent,stroke:transparent,stroke-width:0px
````


Last move CAR-READ to East 5 squares:
````mermaid
block-beta
    columns 7

%% --- 6x6 board (left) ---
    block:BOARD[" "]:6
        columns 6

        c11["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c12["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c13["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c14["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c15["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c16["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]

        c21["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c22["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c23["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c24["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c25["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c26["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]

        c31["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c32["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c33["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c34["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c35["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c36["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]

        c41["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c42["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c43["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c44["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c45["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c46["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]

        c51["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c52["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c53["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c54["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c55["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c56["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]

        c61["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c62["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c63["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c64["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c65["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
        c66["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]
    end

%% --- right-side "exit" column (outside the board) ---
%% Make this column narrow by using smaller labels.
    block:OUT[" "]:1
        columns 1
        o1["&nbsp;<br/>&nbsp;"]
        o2["&nbsp;<br/>&nbsp;"]
        o3["&nbsp;<br/>&nbsp;"]
        o4["&nbsp;<br/>&nbsp;"]
        o5["&nbsp;<br/>&nbsp;"]
        o6["&nbsp;<br/>&nbsp;"]
    end

%% --- Frame styling ---
    style BOARD fill:#5f647e,stroke:#3f4357,stroke-width:18px
    style OUT fill:transparent,stroke:transparent,stroke-width:0px

%% --- Default empty-cell styling (apply to ALL board cells first) ---
    style c11 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c12 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c13 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c14 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c15 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c16 fill:#6e7392,stroke:#52566f,stroke-width:4px

    style c21 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c22 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c23 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c24 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c25 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c26 fill:#6e7392,stroke:#52566f,stroke-width:4px

    style c31 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c32 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c33 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c34 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c35 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c36 fill:#6e7392,stroke:#52566f,stroke-width:4px

    style c41 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c42 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c43 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c44 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c45 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c46 fill:#6e7392,stroke:#52566f,stroke-width:4px

    style c51 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c52 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c53 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c54 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c55 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c56 fill:#6e7392,stroke:#52566f,stroke-width:4px

    style c61 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c62 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c63 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c64 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c65 fill:#6e7392,stroke:#52566f,stroke-width:4px
    style c66 fill:#6e7392,stroke:#52566f,stroke-width:4px

%% --- Vehicles (override specific cells; stroke removed to “merge” cells) ---
%% Red (row 3, col 1-2)
    style c36 fill:#9c1b23,stroke:transparent,stroke-width:0px

%% Cyan vertical (row 2-3, col 3)
    style c13 fill:#6dbfd2,stroke:transparent,stroke-width:0px
    style c23 fill:#6dbfd2,stroke:transparent,stroke-width:0px

%% Yellow truck vertical (row 2-4, col 5)
    style c45 fill:#d9d86a,stroke:transparent,stroke-width:0px
    style c55 fill:#d9d86a,stroke:transparent,stroke-width:0px
    style c65 fill:#d9d86a,stroke:transparent,stroke-width:0px

%% Orange vertical (row 3-4, col 6)
    style c56 fill:#a65a1f,stroke:transparent,stroke-width:0px
    style c66 fill:#a65a1f,stroke:transparent,stroke-width:0px

%% Lavender horizontal truck (row 4, col 1-3)
    style c41 fill:#9a7ad8,stroke:transparent,stroke-width:0px
    style c42 fill:#9a7ad8,stroke:transparent,stroke-width:0px
    style c43 fill:#9a7ad8,stroke:transparent,stroke-width:0px

%% Bottom-left vertical cars (rows 5-6)
    style c11 fill:#5b8fea,stroke:transparent,stroke-width:0px
    style c21 fill:#5b8fea,stroke:transparent,stroke-width:0px

    style c12 fill:#c99ad8,stroke:transparent,stroke-width:0px
    style c22 fill:#c99ad8,stroke:transparent,stroke-width:0px

    style c53 fill:#5630d1,stroke:transparent,stroke-width:0px
    style c63 fill:#5630d1,stroke:transparent,stroke-width:0px

%% Teal horizontal (row 5, col 5-6)
    style c51 fill:#1e7f89,stroke:transparent,stroke-width:0px
    style c52 fill:#1e7f89,stroke:transparent,stroke-width:0px

%% Silver horizontal (row 6, col 5-6)
    style c61 fill:#8a8d96,stroke:transparent,stroke-width:0px
    style c62 fill:#8a8d96,stroke:transparent,stroke-width:0px

%% --- Right-side white exit rectangle (row 3 of OUT column) ---
%% (Make other OUT cells invisible)
    style o1 fill:transparent,stroke:transparent,stroke-width:0px
    style o2 fill:transparent,stroke:transparent,stroke-width:0px
    style o3 fill:#9c1b23,stroke:#transparent,stroke-width:0px
    style o4 fill:transparent,stroke:transparent,stroke-width:0px
    style o5 fill:transparent,stroke:transparent,stroke-width:0px
    style o6 fill:transparent,stroke:transparent,stroke-width:0px
````

### Decide the Movement:
The approach for these type of problems is a lucky brute force technique. These technique consists of simulating the movements of the vehicles in the grid, by randomly selecting the vehicle and the movement to perform. The problem is constrained by blockages, few vehicles can move and the distance of the moves are also constrained by other vehicles. As consequence, the number of possible movements is not that high. Eventually a lucky shot will find a solution.


## In this folder:
This folder contains one class and packages that define the structures required for solving the problem.

1. [RushHourScraper](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/RushHour/RushHourScraper.java): Web Scrapper using Selenium and Robot, and flow controller.
    - Scrapes the necessary information from rush hour web application.
    - Manages the flow of the program.
    - Instantiates the RushHourGA.
    - Introduces the solution in the web app.
2. [RushHourGA](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/RushHour/GA/RushHourGA.java): Class that implements the required OptimizationLib interface and represents a Genetic Algorithm Optimization program.
    - Instantiates all its components.
3. [RushHourObjectiveFunction](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/RushHour/GA/Evaluable/RushHourObjectiveFunction.java): Class that evaluates how good a solution is. Based on:
   - How close to exiting with the red car is the solution.
4. [RushHourInitializer](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/RushHour/GA/Initializer/RushHourInitializer.java): Class that initializes an individual.
5. [Board](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/RushHour/Board/Board.java): Class that represent any board status. Contains methods to get information about the current board status and the building rules.

## Results
For the GA, the set of parameters used are in the RushHourGA file.

The output looks like:
`````
Loading https://rushhourgame.thinkfun.com/: ...
Loaded.
Refreshing in case it did not load...
Refreshed.
Retrieving board...
Board retrieved.
Getting vehicle elements...
Vehicle elements got.
Getting level info...
Level info got.
Building GAModel...
GAModel built.
Running GAModel...
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
| Iteration | CurrentMin | CurrentMax | HistoricalMin | HistoricalMax | MeanFitness | StandardDev | P0    | P25   | P50   | P75   | P100  | StepGradient(u/iter) | TimeGradient(u/s) | ElapsedTime(s) |
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
| 0         | 2.0000     | 4.0000     | 2.0000        | 4.0000        | 2.9790      | 0.8663      | 2.000 | 2.000 | 3.000 | 4.000 | 4.000 |                      |                   | 0.4258         |
| 1         | 2.0000     | 4.0000     | 2.0000        | 4.0000        | 3.0510      | 0.8674      | 2.000 | 2.000 | 3.000 | 4.000 | 4.000 | -0.0720              | -221.3591         | 0.3253         |
| 2         | 2.0000     | 4.0000     | 2.0000        | 4.0000        | 3.0240      | 0.8680      | 2.000 | 2.000 | 3.000 | 4.000 | 4.000 | 0.0270               | 101.3786          | 0.2663         |
| 3         | 2.0000     | 4.0000     | 2.0000        | 4.0000        | 3.0320      | 0.8585      | 2.000 | 2.000 | 3.000 | 4.000 | 4.000 | -0.0080              | -28.6073          | 0.2801         |
| 4         | 2.0000     | 4.0000     | 2.0000        | 4.0000        | 3.0400      | 0.8651      | 2.000 | 2.000 | 3.000 | 4.000 | 4.000 | -0.0080              | -28.3632          | 0.2816         |
| 5         | 2.0000     | 4.0000     | 2.0000        | 4.0000        | 2.9930      | 0.8526      | 2.000 | 2.000 | 3.000 | 4.000 | 4.000 | 0.0470               | 187.9550          | 0.2501         |
| 6         | 2.0000     | 4.0000     | 2.0000        | 4.0000        | 3.0230      | 0.8732      | 2.000 | 2.000 | 3.000 | 4.000 | 4.000 | -0.0300              | -126.8509         | 0.2365         |
| 7         | 2.0000     | 4.0000     | 2.0000        | 4.0000        | 3.0200      | 0.8635      | 2.000 | 2.000 | 3.000 | 4.000 | 4.000 | 0.0030               | 12.9823           | 0.2311         |
| 8         | 2.0000     | 4.0000     | 2.0000        | 4.0000        | 3.0210      | 0.8571      | 2.000 | 2.000 | 3.000 | 4.000 | 4.000 | -0.0010              | -3.9403           | 0.2538         |
| 9         | 2.0000     | 4.0000     | 2.0000        | 4.0000        | 3.0030      | 0.8735      | 2.000 | 2.000 | 3.000 | 4.000 | 4.000 | 0.0180               | 65.3925           | 0.2753         |
| 10        | 2.0000     | 4.0000     | 2.0000        | 4.0000        | 3.0300      | 0.8690      | 2.000 | 2.000 | 3.000 | 4.000 | 4.000 | -0.0270              | -118.6984         | 0.2275         |
| 11        | 2.0000     | 4.0000     | 2.0000        | 4.0000        | 3.0510      | 0.8720      | 2.000 | 2.000 | 3.000 | 4.000 | 4.000 | -0.0210              | -94.8803          | 0.2213         |
| 12        | 2.0000     | 4.0000     | 2.0000        | 4.0000        | 3.0220      | 0.8657      | 2.000 | 2.000 | 3.000 | 4.000 | 4.000 | 0.0290               | 121.3732          | 0.2389         |
| 13        | 2.0000     | 4.0000     | 2.0000        | 4.0000        | 3.0030      | 0.8758      | 2.000 | 2.000 | 3.000 | 4.000 | 4.000 | 0.0190               | 76.0772           | 0.2497         |
| 14        | 2.0000     | 4.0000     | 2.0000        | 4.0000        | 2.9790      | 0.8629      | 2.000 | 2.000 | 3.000 | 4.000 | 4.000 | 0.0240               | 101.3992          | 0.2367         |
| 15        | 0.0000     | 4.0000     | 0.0000        | 4.0000        | 3.0140      | 0.8785      | 0.000 | 2.000 | 3.000 | 4.000 | 4.000 | -0.0350              | -132.7446         | 0.2637         |
GAModel run.
Gathering result...
Result gathered.
Introducing solution...
Optimal solution introduced.
Continuing game...
Game continued.
`````

## Comment:
This problem is just a guide that the user can follow to build its own problems.

In this example, we didn't conduct an extensive optimization of the variables and constraints created. Nor the GA parameters.
Nevertheless, we were able to get in a pretty sort time the solutions for the scenario. :smirk_cat:
