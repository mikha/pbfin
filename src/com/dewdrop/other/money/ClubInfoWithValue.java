package com.dewdrop.other.money;

public class ClubInfoWithValue extends ClubInfo {
	public final long value;

	public ClubInfoWithValue(int id, String name, String city, String country,
			long value) {
		super(id, name, city, country);
		this.value = value;
	}

	@Override
	public String toString() {
		return "ClubInfoWithValue [" + super.toString() + ", value=" + value + "]";
	}
	
}
