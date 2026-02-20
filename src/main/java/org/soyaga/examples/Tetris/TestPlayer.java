package org.soyaga.examples.Tetris;

import org.soyaga.examples.Tetris.Board.Board;
import org.soyaga.examples.Tetris.Board.Pieces.*;
import org.soyaga.examples.Tetris.Player.BoardEvaluationFunction.BoardEvaluationFunction;
import org.soyaga.examples.Tetris.Player.Players.NoobPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static org.soyaga.examples.Tetris.Utils.loadObject;

public class TestPlayer {
    public static void main(String[] args) throws IOException {
        String filePath = "src/out/Tetris/";

        BoardEvaluationFunction tanhBEF = (BoardEvaluationFunction) loadObject(filePath+"Player_Brain_Tanh.dat");
        BoardEvaluationFunction linBEF = (BoardEvaluationFunction) loadObject(filePath+"Player_Brain_Lin.dat");
        BoardEvaluationFunction linSoftBEF = (BoardEvaluationFunction) loadObject(filePath+"Player_Brain_LinSoft.dat");
        NoobPlayer tanhP = new NoobPlayer(tanhBEF);
        NoobPlayer linP = new NoobPlayer(linBEF);
        NoobPlayer linSoftP = new NoobPlayer(linSoftBEF);
        Board bP1 = new Board();
        Board bP2 = new Board();
        Board bP3 = new Board();
        ArrayList<Piece> pieces = new ArrayList<>(){{
           add(new IPiece());
           add(new JPiece());
           add(new LPiece());
           add(new OPiece());
           add(new SPiece());
           add(new ZPiece());
           add(new TPiece());
        }};
        Random random = new Random();
        for(int iter=0;iter<2000; iter++){
            ArrayList<Piece> selectedPiece = new ArrayList<>(){{add(pieces.get(random.nextInt(7)));}};
            tanhP.movePiece(bP1,selectedPiece);
            linP.movePiece(bP2,selectedPiece);
            linSoftP.movePiece(bP3,selectedPiece);
            System.out.println("Iteration: "+ iter);
            if(bP1.isActive()){
                //System.out.println("P1, Iteration: "+ iter + ", Score: " + bP1.getScore());
                //System.out.println(bP1);
                //System.out.println(" ");
            }
            else {
                System.out.println("P1 Game Lost!!");
            }
            if(bP2.isActive()){
                //System.out.println("P2, Iteration: "+ iter + ", Score: " + bP2.getScore());
                //System.out.println(bP2);
                //System.out.println(" ");
            }
            else {
                System.out.println("P2 Game Lost!!");
            }
            if(bP3.isActive()){
                //System.out.println("P3, Iteration: "+ iter + ", Score: " + bP3.getScore());
                //System.out.println(bP3);
                //System.out.println(" ");
            }
            else {
                System.out.println("P3 Game Lost!!");
            }
            if(!bP1.isActive() && !bP2.isActive() && !bP3.isActive()){
                break;
            }
            System.out.println(" ");
        }

    }
}
