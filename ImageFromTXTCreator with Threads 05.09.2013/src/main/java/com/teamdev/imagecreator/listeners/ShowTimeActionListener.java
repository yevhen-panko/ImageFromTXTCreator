package com.teamdev.imagecreator.listeners;

import com.teamdev.imagecreator.utils.DataTransferObject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShowTimeActionListener implements ActionListener {
    private DataTransferObject dto;

    public ShowTimeActionListener(DataTransferObject dto) {
        this.dto = dto;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.dto.drawPanel.getGraphics().drawString("Time of image creating: " + this.dto.getTime(),
                50, this.dto.mainFrame.getHeight() - 100);
    }
}
