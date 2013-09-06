package com.teamdev.imagecreator.utils;

import java.io.*;

public class FileUtils {

    public int countLinesInFile(File file) {
        try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            while ((readChars = is.read(c)) != -1) {
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        count++;
                    }
                }
            }
            return count+1;
        } catch (IOException e){
            e.printStackTrace();
            return 0;
        }
    }
}
