package com.hogan.common.util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description:
 * User:hogan.li
 * Date:2016/12/15-18:29
 */
public class FileUtil {

    public static String getOutputFullFileName(String outputDir, String prefix) {
        String exportTime = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
        return outputDir + prefix + exportTime + ".TXT";
    }

    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }

    /**
     * 移动文件到目标文件夹
     */
    /*public static void moveFile(String srcFileFullName, String destPath){
        File srcFile = new File(srcFileFullName);
        // Destination directory
        File destFile = new File(destPath);
        //new create a folder
        if(!destFile.exists()) {
            destFile.mkdirs();
        }
        File newFile = new File(destPath + File.separator + srcFile.getName());
        //if destination file has exist, then delete it first
        if(newFile.exists()){
            newFile.delete();
        }
        srcFile.renameTo(newFile);
    }*/

    /**
     * 移动文件到目标文件夹并重命名
     */
    /*public static void moveFile(String srcFileFullName, String destPath,String destFileName){
        File srcFile = new File(srcFileFullName);
        // Destination directory
        File destFile = new File(destPath);
        //new create a folder
        if(!destFile.exists()) {
            destFile.mkdirs();
        }
        File newFile = new File(destPath + File.separator+ destFileName);
        //if destination file has exist, then delete it first
        if(newFile.exists()){
            newFile.delete();
        }
        srcFile.renameTo(newFile);
    }*/

    /**
     * 根据文件名前缀和后缀筛选文件
     */
    public static List<String> selectFileByPrefixAndSuffix(String path, final String prefix, final String suffix) {
        File inputDir = new File(path);
        if (!inputDir.exists()) {
            throw new RuntimeException("输入路径不存在");
        }
        if (!path.endsWith(File.separator)) {
            path += File.separator;
        }

        String[] fileNames = inputDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File inputDir, String name) {
                return name.toUpperCase().startsWith(prefix.toUpperCase())
                        && name.toUpperCase().endsWith(suffix.toUpperCase());
            }
        });

        List<String> fileNameList = null;
        if (fileNames != null && fileNames.length > 0) {
            fileNameList = new ArrayList<>();
            for (String fileName : fileNames) {
                fileNameList.add(path + fileName);
            }
        }

        return fileNameList;
    }
}
