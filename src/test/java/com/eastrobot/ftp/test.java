package com.eastrobot.ftp;

import java.io.*;

/**
 * Created by carxiong on 22/11/2018.
 */
public class test {
    public static void main(String[] args) {
        String ftpHost = "180.168.251.245";
        String ftpUserName = "zl_user";
        String ftpPassword = "Qwer!234";
        int ftpPort = 2121;
        String ftpPath = "";
        String localPath = "C:\\Users\\carxiong\\Desktop\\ftp test.txt";
        String fileName = "ftp test.txt";

        //上传一个文件
        try {
            FileInputStream in = new FileInputStream(new File(localPath));
            boolean test = FtpUtil.uploadFile(ftpHost, ftpUserName, ftpPassword, ftpPort, ftpPath, fileName, in);
            System.out.println(test);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println(e);
        }

        //在FTP服务器上生成一个文件，并将一个字符串写入到该文件中
        try {
            InputStream input = new ByteArrayInputStream("test ftp jyf".getBytes("GBK"));
            boolean flag = FtpUtil.uploadFile(ftpHost, ftpUserName, ftpPassword, ftpPort, ftpPath, fileName, input);
            ;
            System.out.println(flag);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //下载一个文件
        FtpUtil.downloadFtpFile(ftpHost, ftpUserName, ftpPassword, ftpPort, ftpPath, localPath, fileName);
    }
}