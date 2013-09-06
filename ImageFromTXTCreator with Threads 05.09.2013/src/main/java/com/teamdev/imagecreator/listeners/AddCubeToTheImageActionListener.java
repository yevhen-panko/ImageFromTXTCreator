package com.teamdev.imagecreator.listeners;

import com.teamdev.imagecreator.utils.DataTransferObject;
import com.teamdev.imagecreator.utils.FileCreator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddCubeToTheImageActionListener implements ActionListener {
    private DataTransferObject dto;

    public AddCubeToTheImageActionListener(DataTransferObject dto) {
        this.dto = dto;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new FileCreator().addCubeToTheFile(this.dto.file);
    }
}
