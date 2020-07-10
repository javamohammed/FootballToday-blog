package com.footbaltoday.entity;

public class Chart {

	
	private String count;
	private String date;
	
	
	public Chart(String count, String date) {
		super();
		this.count = count;
		this.date = date;
	}

	public Chart() {
		// TODO Auto-generated constructor stub
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public Object getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Chart [count=" + count + ", date=" + date + "]";
	}
	
	
}
