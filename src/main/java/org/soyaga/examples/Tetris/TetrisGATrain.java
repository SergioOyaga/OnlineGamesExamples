package org.soyaga.examples.Tetris;

import org.soyaga.examples.Tetris.GA.TetrisGA;

import java.io.*;

import static org.soyaga.examples.Tetris.Utils.saveObject;

public class TetrisGATrain {
    public static void main(String[] args) throws IOException {
        String filePath = "src/out/Tetris/player.dat";

        long startTime = System.nanoTime();
        TetrisGA ga = new TetrisGA("TrainTetrisGA", 50, 50);
        ga.optimize();

        Object [] results = ga.getResult();
        if(results[0].equals("GA_Optimal")) {
            saveObject(results[1], filePath);

            long  deltaSeconds = (System.nanoTime() - startTime) / 1_000_000_000L;
            System.out.println("DeltaTime= " + String.format("%02d:%02d:%02d",deltaSeconds/3600,(deltaSeconds%3600)/60,deltaSeconds%60));
        }
    }
}
