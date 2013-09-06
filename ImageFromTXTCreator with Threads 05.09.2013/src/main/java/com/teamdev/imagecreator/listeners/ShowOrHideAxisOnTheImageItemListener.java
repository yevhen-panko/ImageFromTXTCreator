package com.teamdev.imagecreator.listeners;

import com.teamdev.imagecreator.Point3D;
import com.teamdev.imagecreator.utils.DataTransferObject;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ShowOrHideAxisOnTheImageItemListener implements ItemListener {
    private DataTransferObject dto;
    private final int COUNT_OF_POINTS_IN_AXIS = 510;

    public ShowOrHideAxisOnTheImageItemListener(DataTransferObject dto) {
        this.dto = dto;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        switch (((JCheckBoxMenuItem) e.getItem()).getText()) {
            case "Show X axis": {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    addXAxisAndSaveStartPosition();
                } else {
                    this.dto.point3Ds = this.deleteAxisPointsFromPoint3DsArray(
                            this.dto.xAxisStartPositionInPoint3DArray);
                    if (this.dto.yAxisStartPositionInPoint3DArray > this.dto.xAxisStartPositionInPoint3DArray){
                        this.dto.yAxisStartPositionInPoint3DArray -= COUNT_OF_POINTS_IN_AXIS;
                    }
                    if (this.dto.zAxisStartPositionInPoint3DArray > this.dto.xAxisStartPositionInPoint3DArray){
                        this.dto.zAxisStartPositionInPoint3DArray -= COUNT_OF_POINTS_IN_AXIS;
                    }
                }
                break;
            }
            case "Show Y axis": {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    addYAxisAndSaveStartPosition();
                } else {
                    this.dto.point3Ds = this.deleteAxisPointsFromPoint3DsArray(
                            this.dto.yAxisStartPositionInPoint3DArray);
                    if (this.dto.xAxisStartPositionInPoint3DArray > this.dto.yAxisStartPositionInPoint3DArray){
                        this.dto.xAxisStartPositionInPoint3DArray -= COUNT_OF_POINTS_IN_AXIS;
                    }
                    if (this.dto.zAxisStartPositionInPoint3DArray > this.dto.yAxisStartPositionInPoint3DArray){
                        this.dto.zAxisStartPositionInPoint3DArray -= COUNT_OF_POINTS_IN_AXIS;
                    }
                }
                break;
            }
            case "Show Z axis": {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    addZAxisAndSaveStartPosition();
                } else {
                    this.dto.point3Ds = this.deleteAxisPointsFromPoint3DsArray(
                            this.dto.zAxisStartPositionInPoint3DArray);
                    if (this.dto.xAxisStartPositionInPoint3DArray > this.dto.zAxisStartPositionInPoint3DArray){
                        this.dto.xAxisStartPositionInPoint3DArray -= COUNT_OF_POINTS_IN_AXIS;
                    }
                    if (this.dto.yAxisStartPositionInPoint3DArray > this.dto.zAxisStartPositionInPoint3DArray){
                        this.dto.yAxisStartPositionInPoint3DArray -= COUNT_OF_POINTS_IN_AXIS;
                    }
                }
                break;
            }
        }
        this.dto.drawPanel.drawImage(this.dto);
    }

    private void addZAxisAndSaveStartPosition() {
        this.addAxisesPointsToPoint3DsArray(0, 0, 1);
        this.dto.zAxisStartPositionInPoint3DArray = this.dto.point3Ds.length - COUNT_OF_POINTS_IN_AXIS;
    }

    private void addYAxisAndSaveStartPosition() {
        this.addAxisesPointsToPoint3DsArray(0, 1, 0);
        this.dto.yAxisStartPositionInPoint3DArray = this.dto.point3Ds.length - COUNT_OF_POINTS_IN_AXIS;
    }

    private void addXAxisAndSaveStartPosition() {
        this.addAxisesPointsToPoint3DsArray(1, 0, 0);
        this.dto.xAxisStartPositionInPoint3DArray = this.dto.point3Ds.length - COUNT_OF_POINTS_IN_AXIS;
    }

    private void addAxisesPointsToPoint3DsArray(int xIncrement, int yIncrement, int zIncrement) {
        int xCoordinate = this.dto.averageOfXCoordinate;
        int yCoordinate = this.dto.averageOfYCoordinate;
        int zCoordinate = this.dto.averageOfZCoordinate;

        Point3D[] point3Ds = createNewBiggerPoint3DArrayFromOriginal();
        Point3D point3D;

        for (int i = 0; i < COUNT_OF_POINTS_IN_AXIS; i++) {
            if (i < 255) {
                point3D = new Point3D(xCoordinate -= xIncrement, yCoordinate -= yIncrement,
                        zCoordinate -= zIncrement, 0, 0, 0, 50);
            } else {
                if (i == 255) {
                    xCoordinate = this.dto.averageOfXCoordinate;
                    yCoordinate = this.dto.averageOfYCoordinate;
                    zCoordinate = this.dto.averageOfZCoordinate;
                }
                point3D = new Point3D(xCoordinate += xIncrement, yCoordinate += yIncrement,
                        zCoordinate += zIncrement, 0, 0, 0, 50);
            }

            point3Ds[this.dto.point3Ds.length + i] = point3D;
        }
        this.dto.point3Ds = point3Ds;
    }

    private Point3D[] createNewBiggerPoint3DArrayFromOriginal() {
        Point3D[] point3Ds = new Point3D[this.dto.point3Ds.length + COUNT_OF_POINTS_IN_AXIS];
        System.arraycopy(this.dto.point3Ds, 0, point3Ds, 0, this.dto.point3Ds.length);
        return point3Ds;
    }

    private Point3D[] deleteAxisPointsFromPoint3DsArray(int startPosition) {
        Point3D[] point3Ds = new Point3D[this.dto.point3Ds.length - COUNT_OF_POINTS_IN_AXIS];
        System.arraycopy(this.dto.point3Ds, 0, point3Ds, 0, startPosition);
        if (startPosition + COUNT_OF_POINTS_IN_AXIS != this.dto.point3Ds.length) {
            System.arraycopy(this.dto.point3Ds, startPosition + COUNT_OF_POINTS_IN_AXIS,
                    point3Ds, startPosition, this.dto.point3Ds.length - startPosition - COUNT_OF_POINTS_IN_AXIS);
        }
        return point3Ds;
    }
}
