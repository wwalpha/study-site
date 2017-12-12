package com.alpha.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alpha.tools.XFileUtils;

public class Test {

	public static void main(String[] args) {
		List<String> allLines = XFileUtils.readLines(new File("C:\\Intel\\aaaa.txt"), "UTF-8");

		List<String> result = new ArrayList<String>();
		for (String line : allLines) {
			String str = line.split("\t")[2];
			
			if (StringUtils.indexOf(str, "R") != -1) {
				System.out.println(line);
			}
		}
		
	
	}

}
