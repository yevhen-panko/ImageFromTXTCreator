package com.teamdev.imagecreator.threads;

import com.teamdev.imagecreator.Point2D;
import com.teamdev.imagecreator.utils.DataTransferObject;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.concurrent.Semaphore;

public class RendererThread extends Thread {
    public boolean hasWork = true;
    public Runnable onFinishRunnable;
    public boolean cancelRendering;
    public Semaphore cancelSemaphore;
    public final Logger logger = Logger.getLogger(this.getName());
    public int startPosition;
    public int endPosition;
    public DataTransferObject dto;


    public void startNewRendering(Runnable onFinishRunnable, int startPosition,
                                  int endPosition, DataTransferObject dto){
        this.onFinishRunnable = onFinishRunnable;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.dto = dto;
        hasWork = true;
        cancelRendering = false;
        synchronized (this) {
            this.notify();
        }
    }

    public void cancel(Semaphore s) {
        logger.info("Thread renderer " + this.getName() + " canceled.");
        cancelRendering = true;
        cancelSemaphore = s;
    }


    @Override
    public void run() {
        try {
            logger.info("Thread renderer " + this.getName() + " started.");
            float opacityCrossPoints = 0.5f;
            float opacityCornerPoints = 0.25f;
            float oneMinusOpacityCornerPoints = 0.75f;
            while (!isInterrupted()) {
                synchronized (this) {
                    logger.info("Thread renderer " + this.getName() + " is waiting.");
                    this.wait();
                }
                if (hasWork) {
                    logger.info("Thread renderer " + this.getName() + " has work.");
                    int rgb;
                    int rColor = 0;
                    int gColor = 0;
                    int bColor = 0;
                    int rColorIn = 0;
                    int gColorIn = 0;
                    int bColorIn = 0;
                    //Check all points with z-buffer and paint only points with smaller y coordinate
                    if (this.dto.point2Ds != null) {
                        for (Point2D point2D : this.dto.point2Ds) {
                            if (point2D != null && point2D.xCoordinate > 0 &&
                                    point2D.xCoordinate < dto.zBuffer.length &&
                                    point2D.yCoordinate > 0 &&
                                    point2D.yCoordinate < dto.zBuffer[0].length) {
                                int newXCoordinate = point2D.xCoordinate;
                                int newYCoordinate = point2D.yCoordinate;
                                if (this.dto.originalColor) {
                                    rColorIn = point2D.rColor;
                                    gColorIn = point2D.gColor;
                                    bColorIn = point2D.bColor;
                                } else {
                                    rColorIn = this.dto.colorList[point2D.indexOfCluster][0];
                                    gColorIn = this.dto.colorList[point2D.indexOfCluster][1];
                                    bColorIn = this.dto.colorList[point2D.indexOfCluster][2];
                                }
                                for (int i = newXCoordinate - 1; i < newXCoordinate + 2; i++) {
                                    for (int j = newYCoordinate - 1; j < newYCoordinate + 2; j++) {
                                        if (i >= startPosition && i < endPosition && j >= 0 &&
                                                j < this.dto.zBuffer[i].length) {
                                            if (this.dto.zBuffer[i][j][0] >= point2D.depth) {
                                                //If point is center of Big point - fill this point in full color
                                                if (i == newXCoordinate && j == newYCoordinate) {
                                                    rColor = rColorIn;
                                                    gColor = gColorIn;
                                                    bColor = bColorIn;
                                                } else if (Math.abs(i - j) + 1 == Math.abs(newXCoordinate - newYCoordinate)
                                                        || Math.abs(i - j) - 1 == Math.abs(newXCoordinate - newYCoordinate)) {
                                                    rColor = (int) (rColorIn * opacityCrossPoints +
                                                            this.dto.zBuffer[i][j][1] * opacityCrossPoints);
                                                    gColor = (int) (gColorIn * opacityCrossPoints +
                                                            this.dto.zBuffer[i][j][2] * opacityCrossPoints);
                                                    bColor = (int) (bColorIn * opacityCrossPoints +
                                                            this.dto.zBuffer[i][j][3] * opacityCrossPoints);
                                                } else {
                                                    rColor = (int) (rColorIn * opacityCornerPoints +
                                                            this.dto.zBuffer[i][j][1] * oneMinusOpacityCornerPoints);
                                                    gColor = (int) (gColorIn * opacityCornerPoints +
                                                            this.dto.zBuffer[i][j][2] * oneMinusOpacityCornerPoints);
                                                    bColor = (int) (bColorIn * opacityCornerPoints +
                                                            this.dto.zBuffer[i][j][3] * oneMinusOpacityCornerPoints);
                                                }
                                                this.dto.zBuffer[i][j][0] = point2D.depth;
                                                this.dto.zBuffer[i][j][1] = rColor;
                                                this.dto.zBuffer[i][j][2] = gColor;
                                                this.dto.zBuffer[i][j][3] = bColor;
                                                this.dto.zBuffer[i][j][4] = point2D.indexOfCluster;
                                                rgb = (rColor << 16) | (gColor << 8) | bColor;
                                                if (i >= 0 && i < this.dto.image.getWidth() &&
                                                        j >= 0 && j < this.dto.image.getHeight()) {
                                                    this.dto.image.setRGB(i, j, rgb);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (cancelRendering) {
                                cancelSemaphore.acquire();
                                break;
                            }
                        }
                    }
                    hasWork = false;
                    if (!cancelRendering) {
                        logger.info("Thread renderer " + this.getName() + " finished work.");
                        SwingUtilities.invokeLater(onFinishRunnable);
                    }
                }
            }
        } catch (InterruptedException | NullPointerException e) {
            e.printStackTrace();
        }

    }
}
