package com.teamdev.imagecreator.listeners;

import com.teamdev.imagecreator.utils.DataTransferObject;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.*;

public class PanelMouseMotionListener implements MouseMotionListener, MouseListener {
    private DataTransferObject dto;
    private int lastX = 0;
    private int lastY = 0;
    private final static double stepAlpha = 4;
    private final static double stepBetta = 2;
    private double betta;
    private double alpha;
    private boolean dragMouseFlag = false;
    private boolean showClusterNumber;

    public PanelMouseMotionListener(DataTransferObject dto) {
        this.dto = dto;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (dragMouseFlag) {
            if (e.getX() - lastX < 0) {
                if (alpha < 180) {
                    alpha += stepAlpha;
                }
            } else {
                if (alpha > -180) {
                    alpha -= stepAlpha;
                }
            }
            if (e.getY() - lastY < 0) {
                if (betta > -90) {
                    betta -= stepBetta;
                }
            } else {
                if (betta < 90) {
                    betta += stepBetta;
                }
            }
            lastX = e.getX();
            lastY = e.getY();
            this.showClusterNumber = false;
            this.dto.alpha = this.alpha * Math.PI / 180;
            this.dto.betta = this.betta * Math.PI / 180;
            this.dto.onePixelPoint = true;
            this.dto.drawPanel.drawImage(this.dto);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (this.dto.image != null && this.dto.zBuffer != null) {
            int startX = this.dto.centerX - this.dto.image.getWidth() / 2;
            int startY = this.dto.centerY - this.dto.image.getHeight() / 5 * 3;
            //Find coordinates in image coordinates system (panel size is screen size)
            int newXCoordinate;
            int newYCoordinate;
            if (!this.dto.onePixelPoint) {
                newXCoordinate = (e.getX() - startX) * 2;
                newYCoordinate = (e.getY() - startY) * 2;
            } else {
                newXCoordinate = e.getX() - startX;
                newYCoordinate = e.getY() - startY;
            }
            if (e.getX() > startX && e.getX() < startX + this.dto.image.getWidth() &&
                    e.getY() > startY && e.getY() < startY + this.dto.image.getHeight() &&
                    newXCoordinate < this.dto.zBuffer.length &&
                    newYCoordinate < this.dto.zBuffer[0].length) {
                if (this.dto.zBuffer[newXCoordinate][newYCoordinate][4] < 0) {
                    this.dto.drawPanel.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                    this.dragMouseFlag = true;
                } else {
                    this.dto.drawPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    this.dragMouseFlag = false;
                }
            } else {
                this.dto.drawPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                this.dragMouseFlag = false;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (this.dto.image != null) {
            //Find coordinates of image left corner (0,0)
            int xCoordinateOfLeftCornerOfImage = this.dto.centerX - this.dto.image.getWidth() / 2;
            int yCoordinateOfLeftCornerOfImage = this.dto.centerY - (this.dto.image.getHeight() / 5) * 3;
            //Find coordinates in image coordinates system (panel size is screen size)
            int newXCoordinate;
            int newYCoordinate;
            if (!this.dto.onePixelPoint) {
                newXCoordinate = (e.getX() - xCoordinateOfLeftCornerOfImage) * 2;
                newYCoordinate = (e.getY() - yCoordinateOfLeftCornerOfImage) * 2;
            } else {
                newXCoordinate = e.getX() - xCoordinateOfLeftCornerOfImage;
                newYCoordinate = e.getY() - yCoordinateOfLeftCornerOfImage;
            }
            int[][][] zBuffer = this.dto.zBuffer;
            if (newXCoordinate >= 0 && newXCoordinate < zBuffer.length &&
                    newYCoordinate >= 0 && newYCoordinate < zBuffer[newXCoordinate].length) {
                try {
                    String path = this.dto.file.getPath().substring(0,
                            this.dto.file.getPath().lastIndexOf('\\') + 1) + "indexOfCluster.txt";
                    File file = new File(path);
                    PrintWriter out = new PrintWriter(new OutputStreamWriter
                            (new FileOutputStream(file)), true);
                    out.println(zBuffer[newXCoordinate][newYCoordinate][1]);
                    out.close();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                //Draw string with number of cluster on the screen
                this.dto.drawPanel.getGraphics().drawString("Number of cluster: " +
                        zBuffer[newXCoordinate][newYCoordinate][4],
                        50, this.dto.mainFrame.getHeight() - 100);
                //Clear index of Cluster on the screen with delay 1sec.
                try {
                    Thread.sleep(1000);
                    this.dto.mainFrame.repaint();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!this.showClusterNumber) {
            if (this.dto.image != null) {
                this.dto.onePixelPoint = false;
                this.dto.drawPanel.drawImage(this.dto);
                showClusterNumber = true;
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
