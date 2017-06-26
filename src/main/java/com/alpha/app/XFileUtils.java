package com.alpha.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

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

	public static List<String> readLines(File file, String encode) {
		try {
			return FileUtils.readLines(file, "UTF-8");
		} catch (IOException e) {
		}

		return new ArrayList<>();
	}
}
