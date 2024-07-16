package com.tunanc.filetemplate.uitls;


import java.io.File;

public class PathUtil {


    public static String concatDir(String... dirs){
        StringBuilder result = new StringBuilder();
        for (String dir : dirs) {
            result.append(dir);
            if (!dir.endsWith(File.separator)){
                result.append(File.separator);
            }

        }
        return result.toString();
    }
}
