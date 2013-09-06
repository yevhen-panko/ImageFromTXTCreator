package com.teamdev.imagecreator.utils;

import java.io.*;

public class FileCreator {

    public void addCubeToTheFile(File file){
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write("\r\n");
            for (int i=0; i<201; i++){
                float zCoordinate = -1 + i/100f;
                float yCoordinate = -1 + i/100f;
                float xCoordinate = -1 + i/100f;
                fileWriter.write(xCoordinate + ", -1, -1, 0, 0, 0, 50\r\n");
                fileWriter.write(xCoordinate + ", -1, 1, 0, 0, 0, 50\r\n");
                fileWriter.write(xCoordinate + ", 1, -1, 0, 0, 0, 50\r\n");
                fileWriter.write(xCoordinate + ", 1, 1, 0, 0, 0, 50\r\n");
                fileWriter.write("-1, " + yCoordinate + ", -1, 0, 0, 0, 50\r\n");
                fileWriter.write("1, " + yCoordinate + ", -1, 0, 0, 0, 50\r\n");
                fileWriter.write("-1, " + yCoordinate + ", 1, 0, 0, 0, 50\r\n");
                fileWriter.write("1, " + yCoordinate + ", 1, 0, 0, 0, 50\r\n");
                fileWriter.write("-1, -1, " + zCoordinate + ", 0, 0, 0, 50\r\n");
                fileWriter.write("-1, 1, " + zCoordinate + ", 0, 0, 0, 50\r\n");
                fileWriter.write("1, -1, " + zCoordinate + ", 0, 0, 0, 50\r\n");
                fileWriter.write("1, 1, " + zCoordinate + ", 0, 0, 0, 50\r\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
