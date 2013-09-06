package com.teamdev.imagecreator.listeners;

import com.teamdev.imagecreator.utils.DataTransferObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SaveFileActionListener implements ActionListener {
    private DataTransferObject dto;

    public SaveFileActionListener(DataTransferObject dto) {
        this.dto = dto;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser saveFileChooser = new JFileChooser(this.dto.file);
        if (saveFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
            File selectedFile = saveFileChooser.getSelectedFile();
            if (!selectedFile.getPath().contains(".png")){
                this.dto.drawPanel.saveImageToFile(new File(selectedFile.getPath() + ".png"));
            } else this.dto.drawPanel.saveImageToFile(selectedFile);
        }
    }
}
