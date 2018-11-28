package com.eastrobot.security.pwd;

import com.eastrobot.watermark.WatermarkException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.PresentationMLPackage;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;

/**
 * Created by carxiong on 28/11/2018.
 */
public class PassWordUtils {

    public static void pdfPwd(String inputFilePath, String outputFilePath, String password) throws WatermarkException {
        PdfReader reader = null;
        PdfStamper stamper = null;
        try {
            reader = new PdfReader(new FileInputStream(inputFilePath));
            stamper = new PdfStamper(reader, new FileOutputStream(outputFilePath));
            stamper.setEncryption(password.getBytes("UTF8"), password.getBytes("UTF8"),
                    PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
            if (stamper != null) {
                stamper.close();
            }
        } catch (FileNotFoundException e) {
            throw new WatermarkException("FileNotFoundException", e);
        } catch (BadElementException e) {
            throw new WatermarkException("BadElementException", e);
        } catch (MalformedURLException e) {
            throw new WatermarkException("MalformedURLException", e);
        } catch (IOException e) {
            throw new WatermarkException("IOException", e);
        } catch (DocumentException e) {
            throw new WatermarkException("DocumentException", e);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public static void wordPwd(String inputFilePath, String outputFilePath, String password) throws Docx4JException {
        WordprocessingMLPackage wordMLPackage = Docx4J.load(new File(inputFilePath));
        Docx4J.save(wordMLPackage, new File(outputFilePath), 4, password);
    }

    public static void excelPwd(String inputFilePath, String outputFilePath, String password) throws Docx4JException {
        SpreadsheetMLPackage excelMLPackage = SpreadsheetMLPackage.load(new File(inputFilePath));
        excelMLPackage.save(new File(outputFilePath), 4, password);
    }

    public static void pptPwd(String inputFilePath, String outputFilePath, String password) throws Docx4JException {
        PresentationMLPackage presentationMLPackage = PresentationMLPackage.load(new File(inputFilePath));
        presentationMLPackage.save(new File(outputFilePath), 4, password);
    }

    public static void csv2ExcelPwd(String csvFilePath, String outputFilePath, String password) throws IOException, Docx4JException {
        try {
            XSSFWorkbook workBook = new XSSFWorkbook();
            XSSFSheet sheet = workBook.createSheet("sheet1");
            String currentLine;
            int rowNum = 0;
            BufferedReader br = new BufferedReader(new FileReader(csvFilePath));
            while ((currentLine = br.readLine()) != null) {
                String[] strings = currentLine.split(",");
                XSSFRow currentRow = sheet.createRow(rowNum);
                rowNum++;
                for (int i = 0; i < strings.length; i++) {
                    currentRow.createCell(i).setCellValue(strings[i]);
                }
            }
            FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath);
            workBook.write(fileOutputStream);
            fileOutputStream.close();
            excelPwd(outputFilePath, outputFilePath, password);
        } catch (Exception e) {
            throw e;
        }
    }

    public static void picture2PdfPwd(String pictureFilePath, String outputFilePath, String password) throws DocumentException, IOException {
        // 输入流
        FileOutputStream fos = new FileOutputStream(outputFilePath);
        // 创建文档
        Document doc = new Document(null, 0, 0, 0, 0);
        // 写入PDF文档
        PdfWriter pdfWriter = PdfWriter.getInstance(doc, fos);
        pdfWriter.setEncryption(password.getBytes("UTF8"), password.getBytes("UTF8"),
                PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
        // 读取图片流
        BufferedImage img = ImageIO.read(new File(pictureFilePath));
        // 根据图片大小设置文档大小
        doc.setPageSize(new Rectangle(img.getWidth(), img.getHeight()));
        // 实例化图片
        Image image = Image.getInstance(pictureFilePath);
        // 添加图片到文档
        doc.open();
        doc.add(image);
        // 关闭文档
        doc.close();
        pdfWriter.close();
    }
}
