/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.watermark;
/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年9月17日 下午4:33:28
 * @version 1.0
 */

import java.io.File;

import org.junit.Test;

import com.eastrobot.watermark.PowerPointProcessor;
import com.eastrobot.watermark.WatermarkException;

public class PowerPointProcessorTests {

	@Test
	public void testProcess() {
		File file = new File("C:\\Users\\carxiong\\Desktop\\熊常徽简历.pptx");
		File imgFile = new File("C:\\Users\\carxiong\\Desktop\\kbs-watermark-20181116161710.png");
		
		PowerPointProcessor processor = new PowerPointProcessor(file, imgFile);
		try {
			processor.process();
		} catch (WatermarkException e) {
			e.printStackTrace();
		}
	}
}
