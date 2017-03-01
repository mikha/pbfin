package com.dewdrop.other.money;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Aggregator {
	private final Map<ClubInfo, Finances> clubFinances = new HashMap<ClubInfo, Finances>();
	private final Map<String, Finances> countryFinances = new HashMap<String, Finances>();
	
	private void addIncomeStats(Finances.Income type, Set<ClubInfoWithValue> stats) {
		for (ClubInfoWithValue stat : stats) {
			//club
			Finances finances = clubFinances.get(stat);
			if(null == finances) {
				clubFinances.put(stat, finances = new Finances());
			}
			finances.income(type, stat.value);
			//country
			finances = countryFinances.get(stat.country);
			if(null == finances) {
				countryFinances.put(stat.country, finances = new Finances());
			}
			finances.income(type, stat.value);
		}
	}

	private void addExpenseStats(Finances.Expense type, Set<ClubInfoWithValue> stats) {
		for (ClubInfoWithValue stat : stats) {
			//club
			Finances finances = clubFinances.get(stat);
			if(null == finances) {
				clubFinances.put(stat, finances = new Finances());
			}
			finances.expense(type, stat.value);
			//country
			finances = countryFinances.get(stat.country);
			if(null == finances) {
				countryFinances.put(stat.country, finances = new Finances());
			}
			finances.expense(type, stat.value);
		}
	}
	
	protected String printCountryStats() {
		StringBuilder sb = new StringBuilder();
		sb.append("Страна");
		for (Finances.Income type : Finances.Income.values()) {
			sb.append("\t").append(type.getDesc());
		}
		for (Finances.Expense type : Finances.Expense.values()) {
			sb.append("\t").append(type.getDesc());
		}
		sb.append("\n");
		for (Map.Entry<String, Finances> entry : countryFinances.entrySet()) {
			sb.append(entry.getKey());
			Finances f = entry.getValue();
			for (Finances.Income type : Finances.Income.values()) {
				Long value = f.income.get(type);
				sb.append("\t").append(null == value? 0 : value.longValue());
			}
			for (Finances.Expense type : Finances.Expense.values()) {
				Long value = f.expense.get(type);
				sb.append("\t").append(null == value? 0 : value.longValue());
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	protected String printClubStats() {
		StringBuilder sb = new StringBuilder();
		sb.append("Клуб\tГород\tСтрана");
		for (Finances.Income type : Finances.Income.values()) {
			sb.append("\t").append(type.getDesc());
		}
		for (Finances.Expense type : Finances.Expense.values()) {
			sb.append("\t").append(type.getDesc());
		}
		sb.append("\n");
		for (Map.Entry<ClubInfo, Finances> entry : clubFinances.entrySet()) {
			sb.append(entry.getKey().name).append("\t").append(entry.getKey().city).append("\t").append(entry.getKey().country);
			Finances f = entry.getValue();
			for (Finances.Income type : Finances.Income.values()) {
				Long value = f.income.get(type);
				sb.append("\t").append(null == value? 0 : value.longValue());
			}
			for (Finances.Expense type : Finances.Expense.values()) {
				Long value = f.expense.get(type);
				sb.append("\t").append(null == value? 0 : value.longValue());
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public static final int[] COUNTRY_IDS = {
		3, 5, 7, 12, 17, 19, 21, 31, 41, 43, 48, 49, 
		50, 58, 63, 64, 65, 66, 69, 75, 90, 95, 100, 
		105, 115, 122, 123, 125, 127, 132, 175, 137, 
		138, 152, 155, 157, 160, 161, 162, 164, 166, 
		167, 168, 172};
	
	private static final Map<Finances.Income, String> INCOME_SUFFIX = new HashMap<Finances.Income, String>();
	private static final Map<Finances.Expense, String> EXPENSE_SUFFIX = new HashMap<Finances.Expense, String>();
	static {
		INCOME_SUFFIX.put(Finances.Income.TICKETS, "stat_ext.php?action=topgameall");
		INCOME_SUFFIX.put(Finances.Income.TV, "stat_ext.php?action=toptv");
		INCOME_SUFFIX.put(Finances.Income.FRIENDLY, "stat_ext.php?action=topgamefr");
		INCOME_SUFFIX.put(Finances.Income.NATIONAL_PLAYERS, "stat_ext.php?action=topsb");
		INCOME_SUFFIX.put(Finances.Income.NATIONAL_MANAGER, "stat_ext.php?action=topgamentall");
		INCOME_SUFFIX.put(Finances.Income.PRIZE, "stat_ext.php?action=topprize");
		INCOME_SUFFIX.put(Finances.Income.SPONSOR, "stat_ext.php?action=topsponsor");
		EXPENSE_SUFFIX.put(Finances.Expense.WAGES, "stat_ext.php?action=topigr");
		EXPENSE_SUFFIX.put(Finances.Expense.PERSONNEL, "stat_ext.php?action=toptren");
		EXPENSE_SUFFIX.put(Finances.Expense.ASSIST, "stat_ext.php?action=topassist");
		EXPENSE_SUFFIX.put(Finances.Expense.SCHOOL, "stat_ext.php?action=topschool");
	}
	
	public static void main(String[] args) throws Exception {
		if(args.length < 1) {
			System.err.println("Session Cookie!");
			System.exit(-1);
			return;
		}
		int seasonId = -1;
		if(args.length > 1) {
			try {
				seasonId = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		Aggregator aggregator = aggregateCountries(args[0], seasonId, COUNTRY_IDS);
		
		System.out.println(aggregator.printClubStats());
		System.out.println();
		System.out.println("***************");
		System.out.println();
		System.out.println(aggregator.printCountryStats());
	}
	
	protected static Aggregator aggregateCountries(String sessionCookie, int seasonId, int... ids) throws Exception {
		Aggregator aggregator = new Aggregator();
		for (int id : ids) {
			for (Map.Entry<Finances.Income, String> entry : INCOME_SUFFIX.entrySet()) {
				String url = "http://pbliga.com/" + entry.getValue() + "&country=" + id;
				if (seasonId > 0) {
					url += "&sezon=" + seasonId;
				}
				Set<ClubInfoWithValue> stats = StatsParser.parse(DataProvider.readUrl(url, sessionCookie));
				aggregator.addIncomeStats(entry.getKey(), stats);
			}
			
			for (Map.Entry<Finances.Expense, String> entry : EXPENSE_SUFFIX.entrySet()) {
				String url = "http://pbliga.com/" + entry.getValue() + "&country=" + id;
				if (seasonId > 0) {
					url += "&sezon=" + seasonId;
				}
				Set<ClubInfoWithValue> stats = StatsParser.parse(DataProvider.readUrl(url, sessionCookie));
				aggregator.addExpenseStats(entry.getKey(), stats);
			}
			System.out.println("Processed country " + id);
		}
		return aggregator;
	}
}
