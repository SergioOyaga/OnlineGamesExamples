# Tetris
For this https://play.tetris.com/ decision problem, we use a GA to train a decision-maker player.
In the [GA-representation](#ga-representation) we can see the genome representation.

<img src="https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/out/Tetris/Tetris.gif"  title="Tetris example" alt="Tetris example"/>

## Train Phase
The approach is to train a player how to make good decisions based on the board properties and the piece we have to play.

The idea is simple, we create a so called BoardEvaluationFunction which will be the brain the player uses to evaluate how good-bad a board is. This evaluation is made using some weights and parameters which are learnt via our GA.
A player will consider all possible combinations on how can we place a piece in order to decide which scenario is best.

As consequence, the train is as follows:
1. A set of individuals with random weights are created.
2. Those individuals are evaluated multiple times against the Tetris game.
3. We cross and mutate the individuals as usual.
4. We reevaluate the new population.

Doing this we will end with a set of weights that evaluates the board correctly ant make the right decisions.

### GA Representation:
The genome looks like:
````mermaid
flowchart LR
  subgraph  ide1 [Genome Map]
        subgraph ide2[" "]
            direction LR
            VN1[Weight1Name]
            V1[Wight1Value]
        end
        subgraph ide3[" "]
            direction LR
            VN2[...]
            V2[...]
        end
        subgraph ide4[" "]
            direction LR
            VN3[WeighNtName]
            V3[WightNValue]
        end
        
      style ide1 fill:#0405
      style ide2 fill:#4005
      style ide3 fill:#4005
      style ide4 fill:#4005
  end
````
This representation is a simple map of the weight name and value. We can then load those values into the players BoardEvaluationFunction and evaluate how good are to play Tetris.

## Test
We also have a test script that allow us to run different players to evaluate which one plays better.

## Scraper
In this phase we just load the best player we have, and play the Tetris online.

The important remarks here are:
- The player logic is superfast, in milliseconds we have the selected move.
- The web page design is not that friendly, and canvas based, so the timings events and such are hard to manage.
- The key limitation is the detection rate of keyboard events from the JS canvas. in order to move a piece delays between key inputs have to be made, between movements the new piece appearing time is variable... A lot of things that limit the online gameplay to 100 cleared rows, after that the speed of the dropping pieces is to fast for the canvas to detect the keyboard inputs. 

As a conclusion, the solver is able to play pretty fast, and can keep playing for thousands of steps. The issue is the JS canvas.

## In this folder:
This folder contains one class and a packages that define the structures required for solving the problem.

1. [TetrisScraper](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Tetris/TetrisScraper.java): Web Scrapper using Selenium and flow controller.
    - Scrapes the necessary information from tetris-web application.
    - Manages the flow of the program.
    - Instantiates the Player.
    - Introduces the decision in the web app.
2. [TetrisGATrain](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Tetris/TetrisGATrain.java): Trainer of players.
    - Instantiate the GA.
    - Write the best player.
3. [TestPlayer](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Tetris/TestPlayer.java): TAllow to evaluate players to decide which one plays better.
4. [TetrisGA](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Tetris/GA/TetrisGA.java): Class that implements the required OptimizationLib interface and represents a Genetic Algorithm Optimization program.
    - Instantiates all its components.
5. [TetrisCrossoverPolicy](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Tetris/GA/CrossoverPolicy/TetrisCrossoverPolicy.java): Class that implements a policy where the ParentCross function is randomly selected.
5. [ParentCross](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Tetris/GA/CrossoverPolicy/ParentCross/): Folder that contains two ParentCross classes. One that averages the parent values for the child, and other that randomly selects the chromosomes from the parents.
7. [TetrisInitializer](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Tetris/GA/Initializer/TetrisInitializer.java): Class that initializes an individual.
8. [TetrisBrownianMovement](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Tetris/GA/Mutations/TetrisBrownianMovement.java): Class that mutates a genome applying a brownian movement to random genes of the genome.
9. [Player](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Tetris/Player/Player.java): Player class that is able to evaluate all possible moves in a board. Implements Serializable.
10. [Movement](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Tetris/Player/Movement.java): The decision a player makes over a board.
11. [BoardEvaluationFunction](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Tetris/Player/BoardEvaluationFunction/BoardEvaluationFunction.java): The "brain" of the player, evaluates a board. This class is implemented by 4 classes:
    - Linear $Eval = $:

      $W_{MH1} \cdot MeanHeight +$
      $W_{SH1} \cdot StdHeight +$
      $W_{MaH1} \cdot MaxHeight +$
      $W_{SmH1} \cdot SmoothHeight +$

      $W_{HN1} \cdot HoleNumber +$
      $W_{HMD1} \cdot HoleMeanDepth +$
      $W_{HSD1} \cdot HoleStdDepth +$
      $W_{CL1} \cdot ClearedLines +$
    - LinearSoft $Eval = $:

      $W_{MH1} \cdot \tanh(MeanHeight) +$
      $W_{SH1} \cdot \tanh(StdHeight) +$
      $W_{MaH1} \cdot \tanh(MaxHeight) +$
      $W_{SmH1} \cdot \tanh(SmoothHeight) +$

      $W_{HN1} \cdot \tanh(HoleNumber) +$
      $W_{HMD1} \cdot \tanh(HoleMeanDepth) +$
      $W_{HSD1} \cdot \tanh(HoleStdDepth) +$
      $W_{CL1} \cdot \tanh(ClearedLines) +$
    - Tanh $Eval = $: 
    
      $W_{MH1} \cdot \tanh(W_{MH2}\cdot MeanHeight) +$
      $W_{SH1} \cdot \tanh(W_{SH2}\cdot StdHeight) +$
    
      $W_{MaH1} \cdot \tanh(W_{MaH2}\cdot MaxHeight) +$
      $W_{SmH1} \cdot \tanh(W_{SmH2}\cdot SmoothHeight) +$

      $W_{HN1} \cdot \tanh(W_{HN2}\cdot HoleNumber) +$
      $W_{HMD1} \cdot \tanh(W_{HMD2}\cdot HoleMeanDepth) +$

      $W_{HSD1} \cdot \tanh(W_{HSD2}\cdot HoleStdDepth) +$
      $W_{CL1} \cdot \tanh(W_{CL2}\cdot ClearedLines) +$

12. . [Board](https://github.com/SergioOyaga/OnlineGamesExamples/blob/master/src/main/java/org/soyaga/examples/Tetris/Board): Folder containing a board representation of the game.
    - Board: Composed of a boolean grid, board parameters and functions to move pieces in it.
    - Piece: each one of the Tetrominoes

## Results
For the GA, the set of parameters used are in the TetrisGA file.

The output looks like:
`````
Loading https://play.tetris.com/: ...
Loaded.
Refreshing in case it did not load...
Refreshed.
Switching to iframe..
Switched
Loading Player..
Player loaded
Creating Board...
Board Created
Selecting Config...
Searching add...
Closing add...
Config selected
Clicking play...
Play clicked.
Retrieving first piece...
First piece retrieved
Solving first move..                           
First move solved                              
                                           
                              
                              
                              
                              
                         #  # 
                         #  # 

Retrieving next piece...
Next piece retrieved
Solving next move...
Next move solved
Introducing move...
ITeration: 1, OPiece --> Movement[orientation=N, gap=4]                             
                              
                              
                              
                              
                              
                      #  #  # 
                #  #  #  #  # 

Move introduced
Retrieving next piece...
Next piece retrieved
Solving next move...
Next move solved
Introducing move...
ITeration: 2, LPiece --> Movement[orientation=N, gap=2]                             
                              
                              
                              
                              
                              
          #           #  #  # 
       #  #  #  #  #  #  #  # 

Move introduced
Retrieving next piece...
Next piece retrieved
Solving next move...
Next move solved
Introducing move...
ITeration: 3, TPiece --> Movement[orientation=N, gap=-1]                             
                              
                            # 
                            # 
                            # 
                            # 
          #           #  #  # 
       #  #  #  #  #  #  #  # 

Move introduced
Retrieving next piece...
Next piece retrieved
Solving next move...
Next move solved
Introducing move...
ITeration: 4, IPiece --> Movement[orientation=E, gap=4]                             
                              
                              
                            # 
                            # 
                            # 
    #                       # 
    #     #           #  #  # 

Move introduced
Retrieving next piece...
Next piece retrieved
Solving next move...
Next move solved
Introducing move...
ITeration: 5, JPiece --> Movement[orientation=W, gap=-3]                             
                              
                              
                            # 
                            # 
          #                 # 
    #  #  #                 # 
    #  #  #           #  #  # 

Move introduced
Retrieving next piece...
Next piece retrieved
Solving next move...
Next move solved
Introducing move...
ITeration: 6, ZPiece --> Movement[orientation=E, gap=-2]                             
                              
                              
                            # 
       #  #                 # 
    #  #  #                 # 
    #  #  #                 # 
    #  #  #           #  #  # 

Move introduced
Retrieving next piece...
Next piece retrieved
Solving next move...
Next move solved
Introducing move...
ITeration: 7, SPiece --> Movement[orientation=N, gap=-2]                             
                              
       #                      
    #  #                    # 
    #  #  #                 # 
    #  #  #                 # 
    #  #  #                 # 
    #  #  #           #  #  # 

Move introduced
Retrieving next piece...
Next piece retrieved
Solving next move...
Next move solved
Introducing move...
ITeration: 8, ZPiece --> Movement[orientation=E, gap=-3]                             
                              
       #                      
    #  #                    # 
    #  #  #                 # 
    #  #  #                 # 
    #  #  #  #              # 
    #  #  #  #  #  #  #  #  # 

Move introduced
Retrieving next piece...
Next piece retrieved
Solving next move...
Next move solved
Introducing move...
ITeration: 9, JPiece --> Movement[orientation=N, gap=1]                             
                              
       #                      
    #  #                    # 
    #  #  #     #           # 
    #  #  #  #  #           # 
    #  #  #  #  #           # 
    #  #  #  #  #  #  #  #  # 

Move introduced
Retrieving next piece...
Next piece retrieved
Solving next move...
Next move solved
Introducing move...
ITeration: 10, TPiece --> Movement[orientation=W, gap=1]                             
                              
                              
       #                      
    #  #                    # 
 #  #  #  #     #           # 
 #  #  #  #  #  #           # 
 #  #  #  #  #  #           # 

Move introduced
Retrieving next piece...
Next piece retrieved
Solving next move...
Next move solved
Introducing move...
ITeration: 11, IPiece --> Movement[orientation=E, gap=-5]
                                                          
                              
                              
       #  #                   
    #  #  #  #              # 
 #  #  #  #  #  #           # 
 #  #  #  #  #  #           # 
 #  #  #  #  #  #           # 

Move introduced
Retrieving next piece...
Next piece retrieved
Solving next move...
Next move solved
Introducing move...
ITeration: 12, SPiece --> Movement[orientation=E, gap=-1]
                                                         
                              
                              
       #  #                   
    #  #  #  #              # 
 #  #  #  #  #  #           # 
 #  #  #  #  #  #  #  #     # 
 #  #  #  #  #  #  #  #     # 

Move introduced
Retrieving next piece...
Next piece retrieved
Solving next move...
Next move solved
Introducing move...
ITeration: 13, OPiece --> Movement[orientation=N, gap=2]
                                                          
                              
                              
                              
                              
       #  #                   
    #  #  #  #              # 
 #  #  #  #  #  #     #  #  # 

Move introduced
Retrieving next piece...
Next piece retrieved
Solving next move...
Next move solved
Introducing move...
ITeration: 14, LPiece --> Movement[orientation=W, gap=4]
                                                         
                              
                              
                              
                              
                              
       #  #                   
    #  #  #  #     #  #  #  # 

Move introduced
Retrieving next piece...
Next piece retrieved
Solving next move...
Next move solved
Introducing move...
ITeration: 15, LPiece --> Movement[orientation=S, gap=3]                             
                              
                              
                              
                              
                              
       #  #  #  #  #          
    #  #  #  #  #  #  #  #  # 

Move introduced
Retrieving next piece...
Next piece retrieved
Solving next move...
Next move solved
Introducing move...
ITeration: 16, TPiece --> Movement[orientation=S, gap=1]                             
                              
                              
                              
                              
                              
 #  #                         
 #     #  #  #  #  #          

Move introduced
Retrieving next piece...
Next piece retrieved
Solving next move...
Next move solved
Introducing move...
ITeration: 17, JPiece --> Movement[orientation=E, gap=-4]                             
                              
                              
                              
                              
                              
 #  #                    #  # 
 #     #  #  #  #  #  #  #    

Move introduced
`````
As you see in the output example, the GA found a decent tetris player, and the scrapper is able to play using it.

## Comment:
This problem is just a guide that the user can follow to build its own problems.

In this example, we didn't conduct an extensive optimization of the variables and constraints created. Nor the GA parameters. Nor the scraper timings and events.
Nevertheless, we were able to get in a pretty sort time the player for the Tetris game. :smirk_cat:
