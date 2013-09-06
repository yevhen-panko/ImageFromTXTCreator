package com.teamdev.imagecreator.utils;

import com.teamdev.imagecreator.Point3D;

import java.io.*;
import java.util.StringTokenizer;

public class FileParser {
    public int maxXCoordinate = Integer.MIN_VALUE;
    public int minXCoordinate = Integer.MAX_VALUE;
    public int maxYCoordinate = Integer.MIN_VALUE;
    public int minYCoordinate = Integer.MAX_VALUE;
    public int maxZCoordinate = Integer.MIN_VALUE;
    public int minZCoordinate = Integer.MAX_VALUE;
    public int countOfLines = 0;
    public int centerX;
    public int centerY;
    public int centerZ;

    public Point3D[] parseFileAndFill3DContainer(File file) {
        resetMaxAndMinCoordinates();
        try {
            this.countOfLines = new FileUtils().countLinesInFile(file);
            Point3D[] point3Ds = new Point3D[this.countOfLines];
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            int i = 0;
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                Point3D point3D = this.createPointFromString(str);
                point3Ds[i] = point3D;
                i++;
            }
            bufferedReader.close();
            return point3Ds;

        } catch (IOException e) {
            e.printStackTrace();
            return new Point3D[0];
        }
    }

    private void resetMaxAndMinCoordinates() {
        maxXCoordinate = Integer.MIN_VALUE;
        minXCoordinate = Integer.MAX_VALUE;
        maxYCoordinate = Integer.MIN_VALUE;
        minYCoordinate = Integer.MAX_VALUE;
        maxZCoordinate = Integer.MIN_VALUE;
        minZCoordinate = Integer.MAX_VALUE;
    }

    public Point3D createPointFromString(String str) {
        Point3D point3D = new Point3D();
        int count = 0;
        StringTokenizer tokenizer = new StringTokenizer(str, ",");
        while (tokenizer.hasMoreTokens()) {
            double number = Double.parseDouble(tokenizer.nextToken().trim());
            int ratio = 1;
            switch (count) {
                //X coordinate
                case 0:
                    if (number * ratio > maxXCoordinate) {
                        maxXCoordinate = (int) (number * ratio);
                    }
                    if (number * ratio < minXCoordinate) {
                        minXCoordinate = (int) (number * ratio);
                    }
                    point3D.setXCoordinate((int) (number * ratio));
                    break;
                //Y coordinate
                case 1:
                    if (number * ratio > maxYCoordinate) {
                        maxYCoordinate = (int) (number * ratio);
                    }
                    if (number * ratio < minYCoordinate) {
                        minYCoordinate = (int) (number * ratio);
                    }
                    point3D.setYCoordinate((int) (number * ratio));
                    break;
                //Z coordinate
                case 2:
                    if (number * ratio > maxZCoordinate) {
                        maxZCoordinate = (int) (number * ratio);
                    }
                    if (number * ratio < minZCoordinate) {
                        minZCoordinate = (int) (number * ratio);
                    }
                    point3D.setZCoordinate((int) (number * ratio));
                    break;
                //R color component
                case 3:
                    point3D.setRColor((int) number);
                    break;
                //G color component
                case 4:
                    point3D.setGColor((int) number);
                    break;
                //B color component
                case 5:
                    point3D.setBColor((int) number);
                    break;
                //Index of cluster
                case 6:
                    point3D.setIndexOfCluster((int) number);
                    if (number == -2){
                        centerX = point3D.xCoordinate;
                        centerY = point3D.yCoordinate;
                        centerZ = point3D.zCoordinate;
                    }
                    count = -1;
                    break;
            }
            count++;
        }
        return point3D;
    }

    public int findAverageOfXCoordinate() {
        return  centerX;
//        return (maxXCoordinate + minXCoordinate) / 2;
    }

    public int findAverageOfYCoordinate() {
        return centerY;
//        return (maxYCoordinate + minYCoordinate) / 2;
    }

    public int findAverageOfZCoordinate() {
        return centerZ;
//        return (maxZCoordinate + minZCoordinate) / 2;
    }

}
