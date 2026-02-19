package org.soyaga.examples.Tetris;

import org.soyaga.examples.Tetris.Board.Board;
import org.soyaga.examples.Tetris.Board.Pieces.*;
import org.soyaga.examples.Tetris.Player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static org.soyaga.examples.Tetris.Utils.loadObject;

public class TestPlayer {
    public static void main(String[] args) throws IOException {
        String filePath = "src/out/Tetris/";

        Player tanhP1 = (Player) loadObject(filePath+"player_Tanh_1.dat");
        Player tanhP2 = (Player) loadObject(filePath+"player_Tanh_2.dat");
        Player linP3 = (Player) loadObject(filePath+"player_Linear_1.dat");
        Player linP4 = (Player) loadObject(filePath+"player_Linearsoft_1.dat");
        Board bP1 = new Board();
        Board bP2 = new Board();
        Board bP3 = new Board();
        Board bP4 = new Board();
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
            Piece selectedPiece = pieces.get(random.nextInt(7));
            tanhP1.movePiece(bP1,selectedPiece);
            tanhP2.movePiece(bP2,selectedPiece);
            linP3.movePiece(bP3,selectedPiece);
            linP4.movePiece(bP4,selectedPiece);
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
            if(bP4.isActive()){
                //System.out.println("P4, Iteration: "+ iter + ", Score: " + bP4.getScore());
                //System.out.println(bP4);
                //System.out.println(" ");
            }
            else {
                System.out.println("P4 Game Lost!!");
            }
            if(!bP1.isActive() && !bP2.isActive() && !bP3.isActive() && !bP4.isActive()){
                break;
            }
            System.out.println(" ");
        }

    }
}
