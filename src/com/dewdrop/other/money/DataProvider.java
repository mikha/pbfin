package com.dewdrop.other.money;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class DataProvider {
	public static String readUrl(String url, String sessionCookie) throws Exception {
		StringBuilder sb = new StringBuilder();
		URL u = new URL(url);
		URLConnection conn = u.openConnection();
		conn.setRequestProperty("Cookie", "PHPSESSID=" + sessionCookie);
		conn.connect();
		InputStream is = conn.getInputStream();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(is, "windows-1251"));
			String line;
			while(null != (line = br.readLine())) {
				sb.append(line);
			}
		} finally {
			is.close();
			if(null != br) br.close();
		}
		return sb.toString();
	}

	public static String readFile(String file) throws Exception {
		StringBuilder sb = new StringBuilder();
		InputStream is = new FileInputStream(file);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(is, "windows-1251"));
			String line;
			while(null != (line = br.readLine())) {
				sb.append(line);
			}
		} finally {
			is.close();
			if(null != br) br.close();
		}
		return sb.toString();
	}
}
