package com.teamdev.imagecreator.customizedelements;

import com.teamdev.imagecreator.listeners.*;
import com.teamdev.imagecreator.utils.DataTransferObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class CustomizedMenu extends JMenuBar{
    public JCheckBoxMenuItem showXAxis;
    public JCheckBoxMenuItem showYAxis;
    public JCheckBoxMenuItem showZAxis;
    public JCheckBoxMenuItem showCube;

    public CustomizedMenu(DataTransferObject dto){
        super();
        addFileMenu(dto);
        addEditMenu(dto);
        this.updateUI();
    }

    private void addFileMenu(DataTransferObject dto) {
        JMenu file = new JMenu("File");
        addOpenMenuItem(dto, file);
        addSaveAsMenuItem(dto, file);
        this.add(file);
    }

    private void addEditMenu(DataTransferObject dto) {
        JMenu edit = new JMenu("Edit");
        addCubeToTheImageMenuItem(dto, edit);
        addOriginalColorMenuItem(dto, edit);
        addShowXAxisMenuItem(edit, dto);
        addShowYAxisMenuItem(edit, dto);
        addShowZAxisMenuItem(edit, dto);
        addShowCubeMenuItem(dto, edit);
        addTimeOfImageCreatingMenuItem(dto, edit);
        this.add(edit);
    }

    private void addSaveAsMenuItem(DataTransferObject dto, JMenu file) {
        JMenuItem saveAs = new JMenuItem("Save as");
        saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
        saveAs.addActionListener(new SaveFileActionListener(dto));
        file.add(saveAs);
    }

    private void addOpenMenuItem(DataTransferObject dto, JMenu file) {
        JMenuItem open = new JMenuItem("Open");
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
        open.addActionListener(new OpenFileActionListener(dto));
        file.add(open);
    }

    private void addCubeToTheImageMenuItem(DataTransferObject dto, JMenu edit) {
        JMenuItem addCubeToTheImage = new JMenuItem("Add cube to the image");
        addCubeToTheImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, Event.CTRL_MASK));
        addCubeToTheImage.addActionListener(new AddCubeToTheImageActionListener(dto));
        edit.add(addCubeToTheImage);
    }

    private void addShowCubeMenuItem(DataTransferObject dto, JMenu menu){
        showCube = new JCheckBoxMenuItem("Show cube");
        showCube.setState(false);
        showCube.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, Event.CTRL_MASK));
        showCube.addItemListener(new ShowCubeItemListener(dto));
        menu.add(showCube);
    }

    private void addOriginalColorMenuItem(DataTransferObject dto, JMenu menu) {
        JCheckBoxMenuItem originalColor = new JCheckBoxMenuItem("Show original color");
        originalColor.setState(true);
        originalColor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Event.CTRL_MASK));
        originalColor.addItemListener(new OriginalColorItemListener(dto));
        menu.add(originalColor);
    }

    private void addShowXAxisMenuItem(JMenu menu, DataTransferObject dto) {
        showXAxis = new JCheckBoxMenuItem("Show X axis");
        showXAxis.setState(false);
        showXAxis.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, Event.CTRL_MASK));
        showXAxis.addItemListener(new ShowOrHideAxisOnTheImageItemListener(dto));
        menu.add(showXAxis);
    }

    private void addShowYAxisMenuItem(JMenu menu, DataTransferObject dto) {
        showYAxis = new JCheckBoxMenuItem("Show Y axis");
        showYAxis.setState(false);
        showYAxis.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, Event.CTRL_MASK));
        showYAxis.addItemListener(new ShowOrHideAxisOnTheImageItemListener(dto));
        menu.add(showYAxis);
    }

    private void addShowZAxisMenuItem(JMenu menu, DataTransferObject dto) {
        showZAxis = new JCheckBoxMenuItem("Show Z axis");
        showZAxis.setState(false);
        showZAxis.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, Event.CTRL_MASK));
        showZAxis.addItemListener(new ShowOrHideAxisOnTheImageItemListener(dto));
        menu.add(showZAxis);
    }

    private void addTimeOfImageCreatingMenuItem(DataTransferObject dto, JMenu edit) {
        JMenuItem timeOfImageCreating = new JMenuItem("Show time of image creating");
        timeOfImageCreating.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Event.CTRL_MASK));
        timeOfImageCreating.addActionListener(new ShowTimeActionListener(dto));
        edit.add(timeOfImageCreating);
    }
}
