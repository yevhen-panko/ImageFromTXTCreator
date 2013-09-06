package com.teamdev.imagecreator;

public class Point2D {
    public int xCoordinate;
    public int yCoordinate;
    public int depth;
    public int rColor;
    public int gColor;
    public int bColor;
    public int indexOfCluster;

    public Point2D(int xCoordinate, int yCoordinate, int depth,
                   int rColor, int gColor, int bColor, int indexOfCluster) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.depth = depth;
        this.rColor = rColor;
        this.gColor = gColor;
        this.bColor = bColor;
        this.indexOfCluster = indexOfCluster;
    }
}
