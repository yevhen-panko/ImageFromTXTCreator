package com.teamdev.imagecreator.utils;

import com.teamdev.imagecreator.Point2D;
import com.teamdev.imagecreator.Point3D;
import com.teamdev.imagecreator.customizedelements.DrawPanel;
import com.teamdev.imagecreator.threads.PointsConverterThread;
import com.teamdev.imagecreator.threads.RendererThread;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class DataTransferObject {
    public BufferedImage image;
    public JFrame mainFrame;
    public DrawPanel drawPanel;
    public File file;
    public int centerX;
    public int centerY;
    public int averageOfXCoordinate;
    public int averageOfYCoordinate;
    public int averageOfZCoordinate;
    public int xAxisStartPositionInPoint3DArray;
    public int yAxisStartPositionInPoint3DArray;
    public int zAxisStartPositionInPoint3DArray;
    public int maxXCoordinate = Integer.MIN_VALUE;
    public int minXCoordinate = Integer.MAX_VALUE;
    public int maxYCoordinate = Integer.MIN_VALUE;
    public int minYCoordinate = Integer.MAX_VALUE;
    public Point3D maxXPoint;
    public Point3D minXPoint;
    public boolean findDistance = true;
    public double alpha = 0;
    public double betta = 0;
    public int scale = 1;
    public int multiplier = 2;
    public float distance = 750f;
    public boolean originalColor = true;
    public boolean onePixelPoint = false;
    public Point3D[] point3Ds;
    public Point2D[] point2Ds;
    public ArrayList<Long> times = new ArrayList<>();
    public int[][][] zBuffer;
    public int number_of_processors = Integer.parseInt(System.getenv("NUMBER_OF_PROCESSORS"));
    public PointsConverterThread[] converterThreads = new PointsConverterThread[number_of_processors];
    public RendererThread[] rendererThreads = new RendererThread[number_of_processors];

    //Create color list for clusters. Max count of clusters is 50.
    public int[][] colorList = {
            {255, 0, 0}, {0, 255, 0}, {0, 0, 255}, {0, 0, 128}, {100, 149, 237},
            {72, 61, 139}, {106, 90, 205}, {0, 0, 205}, {65, 105, 225}, {0, 191, 255},
            {70, 130, 180}, {176, 196, 222}, {175, 238, 238}, {0, 206, 209}, {0, 255, 255},
            {127, 255, 212}, {0, 100, 0}, {85, 107, 47}, {143, 188, 143}, {60, 179, 113},
            {152, 251, 152}, {0, 255, 127}, {124, 252, 0}, {0, 255, 0}, {50, 205, 50},
            {34, 139, 34}, {189, 183, 107}, {238, 232, 170}, {255, 255, 0}, {255, 215, 0},
            {238, 221, 130}, {184, 134, 11}, {188, 143, 143}, {205, 92, 92}, {139, 69, 19},
            {160, 82, 45}, {205, 133, 63}, {245, 245, 220}, {210, 180, 140}, {210, 105, 30},
            {178, 34, 34}, {233, 150, 122}, {255, 165, 0}, {255, 140, 0}, {255, 170, 80},
            {255, 0, 0}, {255, 105, 180}, {255, 20, 147}, {255, 192, 203}, {155, 48, 255},
            {0, 0, 0}
    };

    public DataTransferObject() {
    }

    public void fillThreads() {
        for (int i = 0; i < number_of_processors; i++) {
            converterThreads[i] = new PointsConverterThread();
            rendererThreads[i] = new RendererThread();
        }
    }

    public void clearTimes() {
        this.times.clear();
    }

    public long getTime(){
        long allTime = 0;
        if (this.times.size() > 0){
            for (Long time: this.times){
                allTime += time;
            }
            return (allTime - this.times.get(0))/this.times.size();
        } else return 0;
    }
}
