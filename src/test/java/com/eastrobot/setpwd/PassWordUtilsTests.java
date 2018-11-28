/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.setpwd;

import com.eastrobot.security.pwd.PassWordUtils;
import com.eastrobot.watermark.WatermarkException;
import com.itextpdf.text.DocumentException;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.junit.Test;

import java.io.IOException;

/**
 */
public class PassWordUtilsTests {

    @Test
    public void testPdfPwd() throws WatermarkException {
        String inputFilePath = "C:\\Users\\carxiong\\Desktop\\下一代实时流数据处理平台介绍yangxujun - Copy.pdf";
        String outputFilePath = "C:\\Users\\carxiong\\Desktop\\officetest\\Test1.pdf";
        String passwrod = "awsedrf";
        PassWordUtils.pdfPwd(inputFilePath, outputFilePath, passwrod);
    }

    @Test
    public void testWordPwd() throws WatermarkException, Docx4JException {
        String inputFilePath = "C:\\Users\\carxiong\\Desktop\\【大数据平台开发工程师  重庆 12k-15k】李半仓 8年.docx";
        String outputFilePath = "C:\\Users\\carxiong\\Desktop\\officetest\\awsedrf.docx";
        String passwrod = "awsedrf";
        PassWordUtils.wordPwd(inputFilePath, outputFilePath, passwrod);
    }

    @Test
    public void testExcelPwd() throws WatermarkException, Docx4JException {
        String inputFilePath = "C:\\Users\\carxiong\\Desktop\\IDDC timesheet填写规范.xlsx";
        String outputFilePath = "C:\\Users\\carxiong\\Desktop\\officetest\\awsedrf.xlsx";
        String passwrod = "awsedrf";
        PassWordUtils.excelPwd(inputFilePath, outputFilePath, passwrod);
    }

    @Test
    public void testCsv2ExcelPwd() throws WatermarkException, Docx4JException, IOException {
        String inputFilePath = "C:\\Users\\carxiong\\Desktop\\yyy.csv";
        String outputFilePath = "C:\\Users\\carxiong\\Desktop\\officetest\\awsedrf22.xlsx";
        String passwrod = "awsedrf";
        PassWordUtils.csv2ExcelPwd(inputFilePath, outputFilePath, passwrod);
    }

    @Test
    public void testPptPwd() throws WatermarkException, Docx4JException {
        String inputFilePath = "C:\\Users\\carxiong\\Desktop\\New Microsoft PowerPoint Presentation.pptx";
        String outputFilePath = "C:\\Users\\carxiong\\Desktop\\officetest\\awsedrf.pptx";
        String passwrod = "awsedrf";
        PassWordUtils.pptPwd(inputFilePath, outputFilePath, passwrod);
    }

    @Test
    public void testPicture2PdfPwd() throws WatermarkException, Docx4JException, IOException, DocumentException {
        String inputFilePath = "C:\\Users\\carxiong\\Desktop\\Capture.PNG";
        String outputFilePath = "C:\\Users\\carxiong\\Desktop\\officetest\\awsedrf12.pdf";
        String passwrod = "awsedrf12";
        PassWordUtils.picture2PdfPwd(inputFilePath, outputFilePath, passwrod);
    }

}