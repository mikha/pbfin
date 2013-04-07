package com.dewdrop.other.money;

import java.util.HashMap;
import java.util.Map;

public class Finances {
	
	public enum Income {
		TICKETS("Билеты"),
		TV("Трансляции"),
		FRIENDLY("Товы"),
		NATIONAL_PLAYERS("Сб. Вызов"),
		NATIONAL_MANAGER("Сб. Упр-ние"),
		PRIZE("Призовые"),
		SPONSOR("Спонсоры");
		private final String desc;
		public String getDesc() {
			return desc;
		}
		private Income(String desc) {
			this.desc = desc;
		}
	}
	
	public enum Expense {
		WAGES("Зарплаты"),
		PERSONNEL("Персонал"),
		ASSIST("Помы"),
		SCHOOL("Спортшкола");
		private final String desc;
		public String getDesc() {
			return desc;
		}
		private Expense(String desc) {
			this.desc = desc;
		}
	}
	
	public final Map<Income, Long> income = new HashMap<Income, Long>();

	public final Map<Expense, Long> expense = new HashMap<Expense, Long>();
	
	public Finances income(Income type, long amount) {
		Long m = income.get(type);
		if(null != m) {
			amount += m;
		}
		income.put(type, amount);
		return this;
	}
	
	public Finances expense(Expense type, long amount) {
		Long m = expense.get(type);
		if(null != m) {
			amount += m;
		}
		expense.put(type, amount);
		return this;
	}
}
