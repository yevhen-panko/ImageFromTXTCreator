package com.teamdev.imagecreator.listeners;

import com.teamdev.imagecreator.utils.DataTransferObject;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ShowCubeItemListener implements ItemListener {
    private DataTransferObject dto;
    private final int COUNT_OF_POINTS_IN_AXIS = 510;

    public ShowCubeItemListener(DataTransferObject dto) {
        this.dto = dto;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

    }
}
