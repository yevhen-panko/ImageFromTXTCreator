package com.teamdev.imagecreator.customizedelements;

import com.teamdev.imagecreator.ImageCreator;
import com.teamdev.imagecreator.listeners.ChangeDistanceMouseWheelListener;
import com.teamdev.imagecreator.listeners.PanelMouseMotionListener;
import com.teamdev.imagecreator.utils.DataTransferObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DrawPanel extends JPanel{
    public ImageCreator imageCreator;
    public DataTransferObject dto;

    public DrawPanel(DataTransferObject dto){
        super();
        this.dto = dto;
        imageCreator = new ImageCreator(this.dto);
        Dimension windowSize = this.dto.mainFrame.getSize();
        this.setPreferredSize(windowSize);
        this.setLayout(null);
        this.addMouseWheelListener(new ChangeDistanceMouseWheelListener(this.dto));
        PanelMouseMotionListener listener = new PanelMouseMotionListener(this.dto);
        this.addMouseMotionListener(listener);
        this.addMouseListener(listener);
    }

    public void drawImage(DataTransferObject dto) {
        this.dto = dto;
        long startAppWorkingTime = System.currentTimeMillis();
        createImage();
        long endAppWorkingTime = System.currentTimeMillis();
        this.dto.times.add(endAppWorkingTime - startAppWorkingTime);
    }

    private void createImage() {
        if (this.dto.onePixelPoint){
            imageCreator.createImageWithSmallPoint(this.dto);
        } else {
            imageCreator.createImageWithBigPoint(this.dto);
        }
    }

    public boolean saveImageToFile(File file){
        try {
            ImageIO.write(this.dto.image, "png", file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setImage(BufferedImage image){
        this.dto.image = image;
        this.repaint();
        this.dto.mainFrame.repaint();
    }

    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        if (this.dto.image != null){
            g2.drawImage(this.dto.image, null, this.dto.centerX - this.dto.image.getWidth() / 2,
                    this.dto.centerY - this.dto.image.getHeight() / 5 * 3);
        }
    }
}