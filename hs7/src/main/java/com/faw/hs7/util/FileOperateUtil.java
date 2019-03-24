package com.faw.hs7.util;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

/**
 * 文件解压缩工具类
 */
public class FileOperateUtil {

    public static void setLogPath(String logPath) {
        FileOperateUtil.logPath = logPath;
    }

    //    private static Logger logger = Logger.getLogger(FileOperateUtil.class);
    private static String logPath = "";

    public static String getLogPath() {
        return logPath;
    }

    /**
     * 压缩文件
     *
     * @param zipFilePath 压缩的文件完整名称(目录+文件名)
     * @param srcPathName 需要被压缩的文件或文件夹
     */
    public void compressFiles(String zipFilePath, String srcPathName) {
        File zipFile = new File(zipFilePath);
        File srcdir = new File(srcPathName);
        if (!srcdir.exists()) {
            throw new RuntimeException(srcPathName + "不存在！");
        }
        Project prj = new Project();
        FileSet fileSet = new FileSet();
        fileSet.setProject(prj);
        if (srcdir.isDirectory()) { //是目录
            fileSet.setDir(srcdir);
            fileSet.setIncludes("*.csv"); //包括哪些文件或文件夹 eg:zip.setIncludes("*.java");
            //fileSet.setExcludes(...); //排除哪些文件或文件夹
        } else {
            fileSet.setFile(srcdir);
        }
        Zip zip = new Zip();
        zip.setProject(prj);
        zip.setDestFile(zipFile);
        zip.setEncoding("gbk"); //以gbk编码进行压缩，注意windows是默认以gbk编码进行压缩的
        zip.addFileset(fileSet);
        zip.execute();
//        logger.debug("---compress files success---");
    }

    /**
     * 解压文件到指定目录
     *
     * @param //zipFile 目标文件
     * @param //descDir 解压目录
     * @author isDelete 是否删除目标文件
     */
    @SuppressWarnings("unchecked")
    public void unZipFiles(String zipFilePath, String fileSavePath, boolean isDelete) {
        FileOperateUtil fileOperateUtil = new FileOperateUtil();
        boolean isUnZipSuccess = true;
        try {
            (new File(fileSavePath)).mkdirs();
            File f = new File(zipFilePath);
            if ((!f.exists()) && (f.length() <= 0)) {
                throw new RuntimeException("not find " + zipFilePath + "!");
            }
            //一定要加上编码，之前解压另外一个文件，没有加上编码导致不能解压
            ZipFile zipFile = new ZipFile(f, "gbk");
            String gbkPath, strtemp;
            Enumeration<ZipEntry> e = zipFile.getEntries();
            while (e.hasMoreElements()) {
                ZipEntry zipEnt = e.nextElement();
                gbkPath = zipEnt.getName();
                strtemp = fileSavePath + File.separator + gbkPath;
                if (zipEnt.isDirectory()) { //目录
                    File dir = new File(strtemp);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    continue;
                } else {
                    // 读写文件
                    InputStream is = zipFile.getInputStream(zipEnt);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    // 建目录
                    String strsubdir = gbkPath;
                    for (int i = 0; i < strsubdir.length(); i++) {
                        if (strsubdir.substring(i, i + 1).equalsIgnoreCase("/")) {
                            String temp = fileSavePath + File.separator
                                    + strsubdir.substring(0, i);
                            File subdir = new File(temp);
                            if (!subdir.exists())
                                subdir.mkdir();
                        }
                    }
                    FileOutputStream fos = new FileOutputStream(strtemp);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    int len;
                    byte[] buff = new byte[5120];
                    while ((len = bis.read(buff)) != -1) {
                        bos.write(buff, 0, len);
                    }
                    bos.close();
                    fos.close();
                }
            }
            zipFile.close();
        } catch (Exception e) {
//            logger.error("解压文件出现异常：", e);
            isUnZipSuccess = false;
            System.out.println("extract file error: " + zipFilePath);
            fileOperateUtil.WriteStringToFile(fileOperateUtil.logPath, "extract file error: " + zipFilePath);
        }
        /**
         * 文件不能删除的原因：
         * 1.看看是否被别的进程引用，手工删除试试(删除不了就是被别的进程占用)
         2.file是文件夹 并且不为空，有别的文件夹或文件，
         3.极有可能有可能自己前面没有关闭此文件的流(我遇到的情况)
         */
        if (isDelete && isUnZipSuccess) {
            boolean flag = new File(zipFilePath).delete();
//            logger.debug("删除源文件结果: " + flag);
            fileOperateUtil.WriteStringToFile(fileOperateUtil.logPath, "delete " + zipFilePath + "result: " + flag);
        }
//        logger.debug("compress files success");
    }

    /**
     * 删除指定文件夹下所有文件
     * param path 文件夹完整绝对路径
     *
     * @param path
     * @return
     */

    public static boolean delAllFile(String path) {
        System.out.println(path);
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                boolean success = (new File(path + "/" + tempList[i])).delete();
                flag = success;
            }
        }
        return flag;
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public void copyFile(String oldPath, String newPath) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(oldPath));
            bos = new BufferedOutputStream(new FileOutputStream(newPath));

            int hasRead = 0;
            byte b[] = new byte[2048];
            while ((hasRead = bis.read(b)) > 0) {
                bos.write(b, 0, hasRead);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 复制整个文件夹内容
     *
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    public void copyFolder(String oldPath, String newPath) {
        System.out.println("copy path: " + oldPath);
        try {
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[5120];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错");
            e.printStackTrace();
        }

    }

    /**
     * 写内容到指定文件
     *
     * @param filePath
     * @param content
     */
    public void WriteStringToFile(String filePath, String content) {
        try {
            FileWriter fw = new FileWriter(filePath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content + "\r\n");// 往已有的文件上添加字符串
            bw.close();
            fw.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}