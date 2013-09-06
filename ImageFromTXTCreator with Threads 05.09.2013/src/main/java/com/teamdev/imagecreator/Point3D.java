package com.teamdev.imagecreator;

import com.teamdev.imagecreator.utils.DataTransferObject;

public class Point3D {
    public int xCoordinate;
    public int yCoordinate;
    public int zCoordinate;
    private int rColor;
    private int gColor;
    private int bColor;
    private int indexOfCluster;
    public int count;
    Vector3D vectorR = null;
    Vector3D vectorXc0 = null;
    Vector3D vectorYc0 = null;
    Vector3D vectorZc0 = null;
    Vector3D vectorC;
    final Vector3D vectorZ = new Vector3D(0f, 0f, 1f);
    final int hArgument = 200;

    public Point3D() {
    }

    public Point3D(int xCoordinate, int yCoordinate, int zCoordinate, int rColor,
                   int gColor, int bColor, int indexOfCluster) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.zCoordinate = zCoordinate;
        this.rColor = rColor;
        this.gColor = gColor;
        this.bColor = bColor;
        this.indexOfCluster = indexOfCluster;
    }

    public Point2D convert3DTo2D(DataTransferObject dto) {
        Vector3D vectorInput = new Vector3D(xCoordinate, yCoordinate, zCoordinate);
        if (count == 0) {
            vectorC = new Vector3D(dto.averageOfXCoordinate, dto.averageOfYCoordinate, dto.averageOfZCoordinate);
            float newDistance = (float) (dto.distance * Math.cos(dto.betta));
            Vector3D vectorRO = new Vector3D((float) (newDistance * Math.cos(dto.alpha)),
                    (float) (newDistance * Math.sin(dto.alpha)), (float) (dto.distance * Math.sin(dto.betta)));
            vectorR = vectorRO.addition(vectorC);
            Vector3D vectorXc = vectorZ.multiply(vectorRO);
            Vector3D vectorYc = vectorRO.multiply(-1);
            Vector3D vectorZc = vectorXc.multiply(vectorYc);
            vectorXc0 = vectorXc.multiply(1 / vectorXc.length());
            vectorYc0 = vectorYc.multiply(1 / vectorYc.length());
            vectorZc0 = vectorZc.multiply(1 / vectorZc.length());
            count++;
        }

        float resultXCoordinate = vectorInput.subtraction(vectorR).scalarMultiply(vectorXc0);
        float resultYCoordinate = vectorInput.subtraction(vectorR).scalarMultiply(vectorYc0);
        float resultZCoordinate = vectorInput.subtraction(vectorR).scalarMultiply(vectorZc0);

        //Finding 2D coordinates
        float newXCoordinate = ((resultXCoordinate) * hArgument) /
                (hArgument + resultYCoordinate);
        float newYCoordinate = ((resultZCoordinate) * hArgument) /
                (hArgument + resultYCoordinate);

        newXCoordinate = dto.scale * dto.multiplier * (255 + newXCoordinate * 2) / 2;
        newYCoordinate = dto.scale * dto.multiplier * (255 - newYCoordinate * 2) / 2;
        return new Point2D((int) newXCoordinate,
                (int) newYCoordinate,
                (int) resultYCoordinate * 100,
                rColor,
                gColor,
                bColor,
                this.indexOfCluster);
    }

    public void setPoint(Point3D point3D) {
        if (point3D != null) {
            this.xCoordinate = point3D.xCoordinate;
            this.yCoordinate = point3D.yCoordinate;
            this.zCoordinate = point3D.zCoordinate;
            this.rColor = point3D.rColor;
            this.gColor = point3D.gColor;
            this.bColor = point3D.bColor;
            this.indexOfCluster = point3D.indexOfCluster;
        }
    }

    public void setXCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public void setYCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public void setZCoordinate(int zCoordinate) {
        this.zCoordinate = zCoordinate;
    }

    public void setRColor(int rColor) {
        this.rColor = rColor;
    }

    public void setGColor(int gColor) {
        this.gColor = gColor;
    }

    public void setBColor(int bColor) {
        this.bColor = bColor;
    }

    public void setIndexOfCluster(int indexOfCluster) {
        this.indexOfCluster = indexOfCluster;
    }

    @Override
    public String toString() {
        return "Point3D{" +
                "xCoordinate=" + xCoordinate +
                ", yCoordinate=" + yCoordinate +
                ", zCoordinate=" + zCoordinate +
                ", rColor=" + rColor +
                ", gColor=" + gColor +
                ", bColor=" + bColor +
                ", indexOfCluster=" + indexOfCluster +
                ", count=" + count +
                '}';
    }
}
