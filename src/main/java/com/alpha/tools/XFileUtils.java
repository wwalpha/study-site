package com.alpha.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class XFileUtils {

	public static Properties LoadProperties(String file) {
		return LoadProperties(new File(file));
	}

	public static Properties LoadProperties(File file) {
		Properties properties = new Properties();

		InputStream is = null;
		try {
			is = new FileInputStream(file);
			properties.load(is);
		} catch (IOException e) {
			return properties;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}

		return properties;
	}

	public static List<String> readLines(File file, String encoding) {
		try {
			return FileUtils.readLines(file, encoding);
		} catch (IOException e) {
		}

		return new ArrayList<>();
	}

	public static String getSQL(String fileName) {
		String filePath = getContext().getRealPath("/WEB-INF/classes/sql/" + fileName + ".sql");
		
		System.out.print(filePath + ":");
		System.out.println(new File(filePath).exists());
		try {
			return FileUtils.readFileToString(new File(filePath), "UTF-8");
		} catch (IOException e) {
		}

		return StringUtils.EMPTY;
	}

	private static ServletContext getContext() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
				.getServletContext();
	}
}
