package com.alpha.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class RequestSound {

	public static void main(String[] args) throws IOException {
		File file = new File("C:\\work\\wordDB1.txt");

		List<String> allLines = FileUtils.readLines(file, "UTF-8");

		for (String line : allLines) {
			if (StringUtils.isEmpty(line)) {
				continue;
			}

			String[] datas = line.split("\\|");

			if (datas.length < 6) {
				continue;
			}

			if (datas.length == 7 || StringUtils.isEmpty(datas[7])) {
				continue;
			}

			download(datas[1], datas[7]);
		}
	}

	public static void download(String word, String url) throws IOException {

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet getMethod = new HttpGet(url);

		CloseableHttpResponse response = httpClient.execute(getMethod);

		// Site Page存在しない
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			return;
		}

		HttpEntity entity = response.getEntity();

		InputStream is = entity.getContent();
		OutputStream os = new FileOutputStream(
				new File("C:\\work\\sound\\" + word + StringUtils.substring(url, url.length() - 4)));

		IOUtils.copy(is, os);

		is.close();
		os.close();
	}

}
