package com.colorchen.utils;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by color on 16/4/26 10:34.
 */
public class FileUtil {
    /**
     * 获得此路径下的所有文件夹的数量
     * @param dir
     * @return
     */
    public static long getFileLength(File dir){
        long length = 0;
        for(File file:dir.listFiles()){
            if (file.isFile()){
                length += file.length();
            }else {
                length += getFileLength(file);
            }
        }
        return length;
    }

    /**
     * 获得此路径下的所有文件的大小
     * @param dir
     * @return
     */
    public static String getFileSize(File dir){
        BigDecimal bd;
        if (getFileLength(dir) > 1024*1024){
            bd = new BigDecimal(getFileLength(dir)/1000000);
            return bd.setScale(2,BigDecimal.ROUND_HALF_EVEN)+" M";
        }else{
            //length of file is less than 1 mb, use K as a unit
            bd = new BigDecimal(getFileLength(dir) / 1000);
            return bd.setScale(0, BigDecimal.ROUND_HALF_EVEN) + " k";
        }
    }

}
