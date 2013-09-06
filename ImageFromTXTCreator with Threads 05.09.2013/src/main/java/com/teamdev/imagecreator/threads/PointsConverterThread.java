package com.teamdev.imagecreator.threads;

import com.teamdev.imagecreator.Point2D;
import com.teamdev.imagecreator.Point3D;
import com.teamdev.imagecreator.utils.DataTransferObject;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.concurrent.Semaphore;

public class PointsConverterThread extends Thread {
    private int startPosition;
    private int endPosition;
    public boolean hasWork = true;
    private Runnable onFinishRunnable;
    private boolean cancelCalculations;
    private Semaphore cancelSemaphore;
    private final Logger logger = Logger.getLogger(this.getName());
    public DataTransferObject dto;

    public void startNewCalculations(Runnable onFinishRunnable, int startPosition,
                                     int endPosition, DataTransferObject dto) {
        this.onFinishRunnable = onFinishRunnable;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.dto = dto;
        hasWork = true;
        cancelCalculations = false;
        synchronized (this) {
            this.notify();
        }
    }

    public void cancel(Semaphore s) {
        logger.info("Thread calculation " + this.getName() + " canceled.");
        cancelCalculations = true;
        cancelSemaphore = s;
    }


    @Override
    public void run() {
        try {
            logger.info("Thread calculation " + this.getName() + " started.");
            while (!isInterrupted()) {
                synchronized (this) {
                    logger.info("Thread calculation " + this.getName() + " is waiting.");
                    this.wait();
                }
                if (hasWork) {
                    logger.info("Thread calculation " + this.getName() + " has work.");
                    Point2D point2D;
                    Point3D buffer = new Point3D();
                    for (int i = startPosition; i < endPosition; i++) {
                        buffer.setPoint(this.dto.point3Ds[i]);
                        point2D = buffer.convert3DTo2D(dto);
                        if (point2D != null) {
                            this.dto.point2Ds[i] = point2D;
                            findMaxAndMinCoordinates(point2D, this.dto.point3Ds[i]);
                        }
                        if (cancelCalculations) {
                            if (cancelSemaphore != null) {
                                cancelSemaphore.acquire();
                            }
                            break;
                        }
                    }
                    hasWork = false;
                    if (!cancelCalculations) {
                        logger.info("Thread calculation " + this.getName() + " finished work.");
                        SwingUtilities.invokeLater(onFinishRunnable);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void findMaxAndMinCoordinates(Point2D point2D, Point3D point3D) {
        if (point2D.xCoordinate > this.dto.maxXCoordinate){
            this.dto.maxXCoordinate = point2D.xCoordinate;
            this.dto.maxXPoint = point3D;
        }
        if (point2D.xCoordinate < this.dto.minXCoordinate){
            this.dto.minXCoordinate = point2D.xCoordinate;
            this.dto.minXPoint = point3D;
        }
        if (point2D.yCoordinate > this.dto.maxYCoordinate){
            this.dto.maxYCoordinate = point2D.yCoordinate;
        }
        if (point2D.yCoordinate < this.dto.minYCoordinate){
            this.dto.minYCoordinate = point2D.yCoordinate;
        }
    }
}
