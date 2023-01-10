package com.dimas.jumpstart.attendance.payload;

public class TwoDatesInString {
	
	private String start;
	private String end;
	public TwoDatesInString(String start, String end) {
		super();
		this.start = start;
		this.end = end;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}

}
