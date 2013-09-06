package com.teamdev.imagecreator.listeners;

import com.teamdev.imagecreator.utils.DataTransferObject;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class OriginalColorItemListener implements ItemListener{
    private DataTransferObject dto;

    public OriginalColorItemListener(DataTransferObject dto) {
        this.dto = dto;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        this.dto.originalColor = e.getStateChange() == ItemEvent.SELECTED;
        this.dto.drawPanel.drawImage(this.dto);
    }
}
