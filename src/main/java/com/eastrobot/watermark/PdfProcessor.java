/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.watermark;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;

import com.eastrobot.domain.WaterMarkInfo;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.poi.util.IOUtils;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年9月17日 下午4:36:50
 * @version 1.0
 */
public class PdfProcessor extends AbstractProcessor {

	public PdfProcessor(File file, File imageFile) {
		super(file, imageFile);
	}

	@Override
	public void process() throws WatermarkException {
		PdfReader reader = null;
		PdfStamper stamper = null;
		try {
			reader = new PdfReader(new FileInputStream(file));
			stamper = new PdfStamper(reader, new FileOutputStream(file));
			int pageNo = reader.getNumberOfPages();
			// image watermark
			Image img = Image.getInstance(IOUtils.toByteArray(new FileInputStream(imageFile)));
			float w = Math.min(img.getScaledWidth(), 460);
			float h = Math.min(img.getScaledHeight(), 300);
			for (float f : img.matrix()) {
				System.out.println(f);
			}
			// transparency
			PdfGState gs = new PdfGState();
			gs.setFillOpacity(0.3f);
			// properties
			PdfContentByte over;
			Rectangle pagesize;
			float x, y;
			// loop over every page
			for (int i = 1; i <= pageNo; i++) {
				pagesize = reader.getPageSizeWithRotation(i);
				x = (pagesize.getLeft() + pagesize.getRight()) / 2;
				y = (pagesize.getTop() + pagesize.getBottom()) / 2;
				over = stamper.getOverContent(i);
				over.saveState();
				over.setGState(gs);
				System.out.println(w + ", " + h + ", " + (x - (w / 2)) + ", " + (y - (h / 2)));
//				617.0, 400.0, -10.839996, 220.95999
				over.addImage(img, w, 0, 0, h, x - (w / 2), y - (h / 2));
				over.restoreState();
			}
			if (stamper!=null) {
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
			if (reader!=null) {
				reader.close();
			}
		}
	}

	@Override
	public void writeTextWaterMark(WaterMarkInfo waterMarkInfo) throws WatermarkException {
		PdfReader reader = null;
		try {
			//待加水印的文件
			 reader = new PdfReader(new FileInputStream(file));
			//加完水印的文件
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(file));
			//总页码
			int total = reader.getNumberOfPages() + 1;
			PdfContentByte content;
			//设置字体
			BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
					BaseFont.EMBEDDED);
			//循环对每页插入水印
			for (int i = 1; i < total; i++) {
				// 水印的起始
				//在内容下方
				//content = stamper.getUnderContent(i);
				//在内容上方
				content = stamper.getOverContent(i);
				PdfGState gs = new PdfGState();
				//设置透明度为0.2
				gs.setFillOpacity(0.4f);
				content.setGState(gs);
				//下边这一行代码写上会报错
				//content.restoreState();
				// 开始
				content.beginText();
				// 设置颜色
				content.setColorFill(BaseColor.RED);
				// 设置字体及字号
				content.setFontAndSize(base, 20);
				// 设置起始位置
				content.setTextMatrix(waterMarkInfo.getX(), waterMarkInfo.getY());
				float x = 300f;
				float y = 500f;
				float rotation = 45f;
				if (waterMarkInfo.getX()!=null){
					x = waterMarkInfo.getX();
				}
				if (waterMarkInfo.getY()!=null){
					y = waterMarkInfo.getY();
				}
				if (waterMarkInfo.getRotation()!=null){
					rotation = waterMarkInfo.getRotation();
				}
				//开始写入水印
				content.showTextAligned(Element.ALIGN_CENTER, waterMarkInfo.getText(), x, y, rotation);
				//开始写入水印
				content.endText();
			}
			stamper.close();
			System.out.println("PDF水印写入完成");
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
			if (reader!=null) {
				reader.close();
			}
		}
	}


}
