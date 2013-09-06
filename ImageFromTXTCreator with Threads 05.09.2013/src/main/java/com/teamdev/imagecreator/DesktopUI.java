package com.teamdev.imagecreator;

import com.teamdev.imagecreator.customizedelements.CustomizedMenu;
import com.teamdev.imagecreator.customizedelements.DrawPanel;
import com.teamdev.imagecreator.listeners.ResizeWindowComponentListener;
import com.teamdev.imagecreator.utils.DataTransferObject;

import javax.swing.*;
import java.awt.*;

public class DesktopUI extends JFrame{

    public DesktopUI(String title, DataTransferObject dto){
        super(title);
        Dimension windowSize = new Dimension(550, 550);
//        Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(windowSize);
//        this.setExtendedState(MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setVisible(true);
        this.setBackground(Color.WHITE);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        dto.mainFrame = this;
        dto.scale = this.getHeight()/255;
        dto.centerX = windowSize.width/2;
        dto.centerY = windowSize.height/2;
        dto.fillThreads();
        DrawPanel drawPanel = new DrawPanel(dto);
        dto.drawPanel = drawPanel;
        this.setJMenuBar(new CustomizedMenu(dto));
        this.add(drawPanel, BorderLayout.CENTER);
        drawPanel.updateUI();
        this.repaint();
        this.addComponentListener(new ResizeWindowComponentListener(dto));
    }

    public static void main (String[] args){
        new DesktopUI("Image creator", new DataTransferObject());
    }
}
