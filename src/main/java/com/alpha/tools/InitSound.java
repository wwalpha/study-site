package com.alpha.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alpha.bean.WordBean;

public class InitSound {

	private static final String PREFIX_URL = "http://ejje.weblio.jp/content/";
	private static final String KEYWORD_START1 = "<audio class=contentAudio";
	private static final String KEYWORD_END1 = "</audio>";
	private static final String KEYWORD_START2 = "<source src=\"";
	private static final String KEYWORD_END2 = ".mp3";

	private static final String EXTEND = ".wav";

	public static void main(String[] args) throws IOException {
		File file = new File("D:\\Java\\workspace\\English\\src\\main\\resources\\wordDB.txt");

		List<String> allLines = FileUtils.readLines(file, "UTF-8");

		List<WordBean> wordList = new ArrayList<WordBean>();

		int count = 0;

		for (String line : allLines) {
			if (StringUtils.isEmpty(line)) {
				continue;
			}

			String[] datas = line.split("\\|");

			if (datas.length < 6) {
				continue;
			}

			if (datas.length == 7 || StringUtils.isEmpty(datas[7])) {
				count++;
			}
			WordBean bean = new WordBean();
			bean.setUserName(datas[0]);
			bean.setWord(datas[1]);
			bean.setPronounce(datas[2]);
			bean.setVocabulary(datas[3]);

			try {
				bean.setNextTime(Integer.parseInt(datas[4]));
				bean.setTimes(Integer.parseInt(datas[5]));
			} catch (NumberFormatException e) {
				// not use error date
				continue;
			}

			bean.setFavorite(Boolean.parseBoolean(datas[6]));

			if (datas.length == 7) {
				bean.setSound(getDownloadURL(bean.getWord()));
			} else {
				bean.setSound(datas[7]);

				if (StringUtils.isEmpty(bean.getSound())) {
					bean.setSound(getDownloadURL(bean.getWord()));
				}
			}

			wordList.add(bean);
		}

		List<String> newLines = new ArrayList<>();

		wordList.stream().forEach(b -> newLines.add(b.toString()));

		System.out.println(StringUtils.join(newLines, "\n"));
		System.out.println(count);
		try {
			FileUtils.writeLines(file, "UTF-8", newLines);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getDownloadURL(String word) throws IOException {
		word = StringUtils.replaceAll(word, " ", "+");

		String strURL = PREFIX_URL + word;

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet getMethod = new HttpGet(strURL);

		CloseableHttpResponse response = httpClient.execute(getMethod);

		// Site Page存在しない
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			return null;
		}

		HttpEntity entity = response.getEntity();

		String strPage = EntityUtils.toString(entity, StandardCharsets.UTF_8);

		int startIdx = StringUtils.indexOf(strPage, KEYWORD_START1);

		// File file = new
		// File("D:\\Java\\workspace\\English\\src\\main\\resources\\wordDB1.txt");
		// FileUtils.writeStringToFile(file, strPage, "UTF-8");
		// System.out.println(strPage);
		// Keyword 存在しない
		if (startIdx == -1) {
			System.out.println(word + " is not exsit.");

			return null;
		}

		int endIdx = StringUtils.indexOf(strPage, KEYWORD_END1, startIdx);

		// Keyword 存在しない
		if (endIdx == -1) {
			System.out.println(word + " is not exsit.");

			return null;
		}

		startIdx = StringUtils.indexOf(strPage, KEYWORD_START2, startIdx);

		// Keyword 存在しない
		if (startIdx == -1) {
			System.out.println(word + " is not exsit.");

			return null;
		}

		endIdx = StringUtils.indexOf(strPage, KEYWORD_END2, startIdx);

		// Keyword 存在しない
		if (endIdx == -1) {
			System.out.println(word + " is not exsit.");

			return null;
		}

		if (endIdx - startIdx > 200) {
			System.out.println(word + " is not found.");

			return null;
		}

		String data = StringUtils.substring(strPage, startIdx + KEYWORD_START2.length(), endIdx) + ".mp3";

		data = data.replaceAll(" ", StringUtils.EMPTY);

		return data;
	}

	public static boolean saveFile(String folder, String word, String dataURL) throws IOException {
		if (StringUtils.isEmpty(dataURL)) {
			return false;
		}

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet getMethod = new HttpGet(dataURL);

		CloseableHttpResponse response = httpClient.execute(getMethod);

		// Site Page存在しない
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			System.out.println(word + " : data can not download");

			return false;
		}

		HttpEntity entity = response.getEntity();

		byte[] result = EntityUtils.toByteArray(entity);

		FileOutputStream fos = new FileOutputStream(new File(folder + "\\" + word + EXTEND));
		fos.write(result);
		fos.flush();
		fos.close();

		response.close();

		return true;
	}

}
