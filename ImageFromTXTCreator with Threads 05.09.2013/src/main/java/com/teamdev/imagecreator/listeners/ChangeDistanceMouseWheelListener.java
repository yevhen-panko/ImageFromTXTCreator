package com.teamdev.imagecreator.listeners;

import com.teamdev.imagecreator.utils.DataTransferObject;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class ChangeDistanceMouseWheelListener implements MouseWheelListener {
    private DataTransferObject dto;
    private static final int step = 250;

    public ChangeDistanceMouseWheelListener(DataTransferObject dto) {
        this.dto = dto;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getUnitsToScroll() > 0) {
            this.dto.distance += step;
        } else {
            if (this.dto.distance > 0) {
                this.dto.distance -= step;
                if (this.dto.distance < 1){
                    this.dto.distance = 1;
                }
            }
        }
        this.dto.onePixelPoint = true;
        this.dto.drawPanel.drawImage(this.dto);
    }
}
