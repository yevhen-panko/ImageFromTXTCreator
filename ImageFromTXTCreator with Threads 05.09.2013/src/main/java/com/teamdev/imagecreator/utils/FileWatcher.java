package com.teamdev.imagecreator.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.WatchEvent.Kind;

public class FileWatcher extends Thread{
    public Path path;
    public FileParser parser;
    public DataTransferObject dto;

    public FileWatcher(){
    }

    public void setPath(Path path) {
        this.path = path;
    }

    @Override
    public void run(){
        try {
            try (WatchService watchService = FileSystems.getDefault().newWatchService()) {

                //Registering Objects with the Watch Service.
                path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

                while (true) {

                    final WatchKey key = watchService.take();
                    for (WatchEvent<?> watchEvent : key.pollEvents()) {
                        final Kind<?> kind = watchEvent.kind();
                        if (kind == StandardWatchEventKinds.OVERFLOW) {
                            continue;
                        }
                        final WatchEvent<Path> watchEventPath = (WatchEvent<Path>) watchEvent;
                        final Path entry = watchEventPath.context();
                        int count = 0;
                        if (entry.toString().equals(this.dto.file.getName()) && count == 0){
                            File file = new File(this.path.toString() + "\\" + entry);
                            this.dto.point3Ds = this.parser.parseFileAndFill3DContainer(file);
                            this.dto.drawPanel.drawImage(this.dto);
                            this.dto.clearTimes();
                            count++;
                        } else count = 0;
                    }
                    key.reset();
                    if (!key.isValid()) {
                        break;
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setDTO(DataTransferObject dto) {
        this.dto = dto;
    }

    public void setParser(FileParser parser) {
        this.parser = parser;
    }
}