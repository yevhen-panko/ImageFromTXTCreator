package com.teamdev.imagecreator.listeners;

import com.teamdev.imagecreator.utils.DataTransferObject;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class ResizeWindowComponentListener implements ComponentListener {
    private DataTransferObject dto;

    public ResizeWindowComponentListener(DataTransferObject dto) {
        this.dto = dto;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        int width = e.getComponent().getWidth();
        int height = e.getComponent().getHeight();
        this.dto.centerX = width / 2;
        this.dto.centerY = height / 2;
        this.dto.scale = height / 255;
        this.dto.clearTimes();
        this.dto.onePixelPoint = true;
        if (this.dto.image != null) {
            this.dto.drawPanel.drawImage(this.dto);
        }
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }
}
