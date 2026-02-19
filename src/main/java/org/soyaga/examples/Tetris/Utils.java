package org.soyaga.examples.Tetris;

import java.io.*;

public class Utils {
    public static void saveObject(Object obj, String filePath) {
        if (obj == null) {
            throw new IllegalArgumentException("Object to save cannot be null");
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(obj);
            System.out.println("Object saved successfully to " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving object: " + e.getMessage());
        }
    }

    // Load object from file
    public static Object loadObject(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("File not found: " + filePath);
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading object: " + e.getMessage());
            return null;
        }
    }
}
