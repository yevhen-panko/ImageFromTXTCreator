package com.teamdev.imagecreator;

import com.mortennobel.imagescaling.ResampleOp;
import com.teamdev.imagecreator.threads.PointsConverterThread;
import com.teamdev.imagecreator.threads.RendererThread;
import com.teamdev.imagecreator.utils.DataTransferObject;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.Semaphore;

public class ImageCreator {
    private int finishCalculationCounter;
    private int finishRenderingCounter;
    private final Logger logger = Logger.getLogger(this.getClass());
    public DataTransferObject dto;

    public ImageCreator(DataTransferObject dto) {
        this.dto = dto;
        for (int i = 0; i < this.dto.number_of_processors; i++) {
            this.dto.converterThreads[i].start();
            this.dto.rendererThreads[i].start();
        }
        finishCalculationCounter = finishRenderingCounter = dto.number_of_processors;
    }

    public void createImageWithBigPoint(DataTransferObject dto) {
        this.dto = dto;
        this.dto.multiplier = 2;
        logger.info("<Create image with big point method> is STARTING");
        this.startAllCalculatingThreads(dto);
    }

    public void createImageWithSmallPoint(DataTransferObject dto) {
        this.dto = dto;
        this.dto.multiplier = 1;
        logger.info("<Create image with small point> method is STARTING");
        this.dto.drawPanel.setImage(this.checkSmallPointsWithZBuffer());
    }

    //This method starts all threads
    public void startAllCalculatingThreads(final DataTransferObject dto) {
        logger.info("<Start all calculation threads> method is STARTING");
        dto.point2Ds = new Point2D[dto.point3Ds.length];
        resetMaxAndMinCoordinates();
        //If we have new work, we are canceling old work and suspending all threads
        if (finishCalculationCounter != dto.number_of_processors) {
            Semaphore s = new Semaphore(0);
            for (PointsConverterThread converterThread : dto.converterThreads) {
                converterThread.cancel(s);
            }
            s.release(dto.number_of_processors);
        }
        finishCalculationCounter = 0;
        //When thread finished work, it calling this method and increment finishCalculationCounter.
        //If finishCalculationCounter == count of threads, then control is passed to main thread.
        final Runnable onFinishRunnable = new Runnable() {
            @Override
            public void run() {
                finishCalculationCounter++;
                if (finishCalculationCounter == dto.number_of_processors) {
                    logger.info("MaxX: " + dto.maxXCoordinate);
                    logger.info("MinX: " + dto.minXCoordinate);
                    logger.info("MaxY: " + dto.maxYCoordinate);
                    logger.info("MinY: " + dto.minYCoordinate);
                    logger.info("maxXPoint: " + dto.maxXPoint);
                    logger.info("minXPoint: " + dto.minXPoint);
                    logger.info("FindDistance: " + dto.findDistance);
                    logger.info("Distance: " + dto.distance);
                    while (dto.findDistance) {
                        dto.maxXPoint.count = 0;
                        dto.minXPoint.count = 0;
                        Point2D max = dto.maxXPoint.convert3DTo2D(dto);
                        Point2D min = dto.minXPoint.convert3DTo2D(dto);
                        if (max.xCoordinate < 255 * dto.scale * dto.multiplier &&
                                min.xCoordinate >= 0) {
                            if (dto.distance > 1) {
                                dto.distance--;
                            }
                            if (((255 * dto.scale * dto.multiplier - max.xCoordinate) < 25 &&
                                    (255 * dto.scale * dto.multiplier - max.xCoordinate) >= 0 )||
                                    (min.xCoordinate < 25 && min.xCoordinate > 0) || dto.distance == 1) {
                                dto.findDistance = false;
                                startAllCalculatingThreads(dto);
                                break;
                            }
                        } else {
                            dto.distance++;
                        }
                        logger.info("MaxXCoordinate: " + max.xCoordinate);
                        logger.info("MinXCoordinate: " + min.xCoordinate);
                        logger.info("Distance: " + dto.distance);
                    }
                    startAllRenderingThreads();
                }
            }
        };
        int subContainerLength = dto.point3Ds.length / dto.number_of_processors;
        int startPosition = 0;
        int endPosition = 0;
        for (PointsConverterThread converterThread : dto.converterThreads) {
            startPosition = endPosition;
            endPosition = endPosition + subContainerLength;
            logger.info("New calculation is STARTING");
            converterThread.startNewCalculations(onFinishRunnable, startPosition, endPosition, dto);
        }
    }

    private void resetMaxAndMinCoordinates() {
        dto.maxXCoordinate = Integer.MIN_VALUE;
        dto.minXCoordinate = Integer.MAX_VALUE;
        dto.maxYCoordinate = Integer.MIN_VALUE;
        dto.minYCoordinate = Integer.MAX_VALUE;
    }

    public void startAllRenderingThreads() {
        logger.info("<Start all rendering threads> method is STARTING");
        //If we have new work, we are canceling old work and suspending all threads
        if (finishRenderingCounter != this.dto.number_of_processors) {
            Semaphore s = new Semaphore(0);
            for (RendererThread rendererThread : this.dto.rendererThreads) {
                rendererThread.cancel(s);
            }
            s.release(this.dto.number_of_processors);
        }
        finishRenderingCounter = 0;
        int imageWidth = 255 * this.dto.scale * this.dto.multiplier;
        int imageHeight = 255 * this.dto.scale * this.dto.multiplier;
        BufferedImage image = new BufferedImage(imageWidth,
                imageHeight, BufferedImage.TYPE_INT_RGB);
        //Fill background with white color
        image.getGraphics().setColor(Color.WHITE);
        image.getGraphics().fillRect(0, 0, imageWidth, imageHeight);
        final int[][][] zBuffer = new int[imageWidth][imageHeight][5];
        //Fill z-buffer of max float value
        for (int i = 0; i < zBuffer.length; i++) {
            for (int j = 0; j < zBuffer[i].length; j++) {
                zBuffer[i][j][0] = Integer.MAX_VALUE;
                zBuffer[i][j][1] = 255;
                zBuffer[i][j][2] = 255;
                zBuffer[i][j][3] = 255;
                zBuffer[i][j][4] = -1;
            }
        }
        this.dto.zBuffer = zBuffer;
        this.dto.image = image;
        //When thread finished work, it calling this method and increment finishCalculationCounter.
        //If finishCalculationCounter == count of threads, then control is passed to main thread.
        Runnable onFinishRunnable = new Runnable() {
            @Override
            public void run() {
                finishRenderingCounter++;
                if (finishRenderingCounter == dto.number_of_processors) {
                    dto.point2Ds = null;
                    ResampleOp resampleOp = new ResampleOp(dto.image.getWidth() / dto.multiplier,
                            dto.image.getHeight() / dto.multiplier);
                    dto.drawPanel.setImage(resampleOp.filter(dto.image, null));
                }
            }
        };
        int subContainerLength = zBuffer.length / this.dto.number_of_processors;
        int startPosition = 0;
        int endPosition = 0;
        for (RendererThread rendererThread : this.dto.rendererThreads) {
            startPosition = endPosition;
            endPosition = endPosition + subContainerLength;
            logger.info("New rendering is STARTING");
            rendererThread.startNewRendering(onFinishRunnable, startPosition, endPosition, this.dto);
        }
    }

    public BufferedImage checkSmallPointsWithZBuffer() {
        int imageWidth = 255 * this.dto.scale;
        int imageHeight = 255 * this.dto.scale;
        int[][] zBuffer = new int[imageWidth][imageHeight];
        //Fill z-buffer max value of float number
        for (int i = 0; i < zBuffer.length; i++) {
            for (int j = 0; j < zBuffer[i].length; j++) {
                zBuffer[i][j] = Integer.MAX_VALUE;
            }
        }
        //Create a new empty image
        BufferedImage image = new BufferedImage(imageWidth,
                imageHeight, BufferedImage.TYPE_INT_RGB);
        //Fill background with white color
        image.getGraphics().setColor(Color.WHITE);
        image.getGraphics().fillRect(0, 0, imageWidth, imageHeight);
        int rgb;
        Point2D point2D = null;
        Point3D buffer = new Point3D();
        //Check all points with z-buffer and paint only points with smaller y coordinate
        for (Point3D point3D : this.dto.point3Ds) {
            buffer.setPoint(point3D);
            point2D = buffer.convert3DTo2D(this.dto);
            if (point2D != null && point2D.xCoordinate > 0 &&
                    point2D.xCoordinate < zBuffer.length &&
                    point2D.yCoordinate > 0 &&
                    point2D.yCoordinate < zBuffer[0].length) {
                if (!this.dto.originalColor) {
                    rgb = (this.dto.colorList[point2D.indexOfCluster][0] << 16) |
                            (this.dto.colorList[point2D.indexOfCluster][1] << 8) |
                            this.dto.colorList[point2D.indexOfCluster][2];
                } else {
                    rgb = point2D.rColor << 16 | point2D.gColor << 8 | point2D.bColor;
                }
                int newXCoordinate = point2D.xCoordinate;
                int newYCoordinate = point2D.yCoordinate;

                if (zBuffer[newXCoordinate][newYCoordinate] > point2D.depth) {
                    zBuffer[newXCoordinate][newYCoordinate] = point2D.depth;
                    image.setRGB(newXCoordinate, newYCoordinate, rgb);
                }
            }
        }
        zBuffer = null;
        return image;
    }
}
