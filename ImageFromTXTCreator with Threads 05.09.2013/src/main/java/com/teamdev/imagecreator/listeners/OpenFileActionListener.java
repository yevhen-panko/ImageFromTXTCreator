package com.teamdev.imagecreator.listeners;

import com.teamdev.imagecreator.customizedelements.CustomizedMenu;
import com.teamdev.imagecreator.utils.DataTransferObject;
import com.teamdev.imagecreator.utils.FileParser;
import com.teamdev.imagecreator.utils.FileWatcher;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OpenFileActionListener implements ActionListener {
    FileWatcher watcher;
    FileParser parser;
    DataTransferObject dto;

    public OpenFileActionListener(DataTransferObject dto) {
        this.dto = dto;
        this.parser = new FileParser();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        openFileAndAddFileWatcher();
    }

    private void openFileAndAddFileWatcher() {
        clearDataFields();
        JFileChooser openFileChooser = new JFileChooser(getFile());
        if (openFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = openFileChooser.getSelectedFile();
            saveDataFieldsToWatcher(selectedFile);
            startWatching();
            saveDataFieldsInDTO(selectedFile);
            //Create image from opened file and draw it on the screen
            this.dto.distance = 700f;
            this.dto.findDistance = true;
            this.dto.drawPanel.drawImage(this.dto);
            this.dto.clearTimes();
            resetShowAxisesMenuItemsToFalse();
        }
    }

    private void saveDataFieldsToWatcher(File selectedFile) {
        Path path = Paths.get(selectedFile.getPath().substring(0,
                selectedFile.getPath().lastIndexOf('\\') + 1));
        watcher.setPath(path);
        watcher.setDTO(this.dto);
        watcher.setParser(this.parser);
    }

    private void resetShowAxisesMenuItemsToFalse() {
        ((CustomizedMenu) this.dto.mainFrame.getJMenuBar()).showXAxis.setState(false);
        ((CustomizedMenu) this.dto.mainFrame.getJMenuBar()).showYAxis.setState(false);
        ((CustomizedMenu) this.dto.mainFrame.getJMenuBar()).showZAxis.setState(false);
    }

    private void startWatching() {
        if (!watcher.isAlive()) {
            watcher.start();
        }
    }

    private void saveDataFieldsInDTO(File selectedFile) {
        this.dto.file = selectedFile;
        this.dto.point3Ds = parser.parseFileAndFill3DContainer(selectedFile);
        this.dto.averageOfXCoordinate = parser.findAverageOfXCoordinate();
        this.dto.averageOfYCoordinate = parser.findAverageOfYCoordinate();
        this.dto.averageOfZCoordinate = parser.findAverageOfZCoordinate();
        this.dto.xAxisStartPositionInPoint3DArray = 0;
        this.dto.yAxisStartPositionInPoint3DArray = 0;
        this.dto.zAxisStartPositionInPoint3DArray = 0;
    }

    private File getFile() {
        File file;
        if (this.dto.file == null) {
            file = null;
        } else file = this.dto.file;
        return file;
    }

    private void clearDataFields() {
        this.watcher = new FileWatcher();
        this.dto.zBuffer = null;
        this.dto.onePixelPoint = false;
    }
}
