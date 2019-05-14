package com.bee.supply.chain.finance.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.aspectj.weaver.ast.Test;

import com.aspose.words.Document;
import com.aspose.words.FontSettings;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;

/**
 * 
 * @Description word转pdf
 * @author chenxm66777123
 * @Date 2019年4月17日
 * @version 1.0.0
 */
public class Word2PdfUtil {

	public static boolean getLicense() {
		boolean result = false;
		try {
			InputStream is = Test.class.getClassLoader().getResourceAsStream("license.xml"); // license.xml应放在..\WebRoot\WEB-INF\classes路径下
			License aposeLic = new License();
			aposeLic.setLicense(is);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void doc2pdf(String tempWordFilePath, String tempPdfFilePath) {
		if (!getLicense()) { // 验证License 若不验证则转化出的pdf文档会有水印产生
			return;
		}
		FileOutputStream os = null;
		try {
			File file = new File(tempPdfFilePath);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			os = new FileOutputStream(file);
			 
			//设置多个字体目录

			FontSettings.setFontsFolder("/usr/share/fonts/winFonts", false);

			Document doc = new Document(tempWordFilePath); // Address是将要被转化的word文档
			doc.save(os, SaveFormat.PDF);// 全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF,
											// EPUB, XPS, SWF 相互转换
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(os);
		}
	}
}
