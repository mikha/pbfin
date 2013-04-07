package com.dewdrop.other.money;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatsParser {
	
	public static final Pattern P = Pattern.compile(
			"<tr bgcolor='#\\p{XDigit}{6}'>" +
			"<td>\\d{1,2}" +
			"<td>(?:<b>)?<a href=mng_roster\\.php\\?id=(\\d{1,4})>" +
			"([А-Яа-яA-Za-z0-9 \\-ё]+) \\(([А-Яа-яA-Za-z0-9 \\-ё]+), ([А-Яа-яA-Za-z0-9 \\-.]+)\\)</a>(?:</b>)?" +
			"<td>(?:<b>)?([0-9 ]+)(?:</b>)?");

	

	public static Set<ClubInfoWithValue> parse(String data) {
		Matcher matcher = P.matcher(data);
		Set<ClubInfoWithValue> set = new HashSet<ClubInfoWithValue>();
		while(matcher.find()) {
			if(5 != matcher.groupCount()) {
				System.err.println("Incorrect matcher count " + matcher.groupCount() + " for " + matcher.group());
				continue;
			}
			set.add(new ClubInfoWithValue(Integer.parseInt(matcher.group(1)), matcher.group(2), matcher.group(3), 
					matcher.group(4), Long.parseLong(matcher.group(5).replace(" ", ""))));
		}
		return set;
	}
}
