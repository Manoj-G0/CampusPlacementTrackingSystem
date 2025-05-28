package com.cpt.model;

import org.springframework.stereotype.Component;

@Component
public class DriveDates {
	String ddate;
	String dname;
	String denddate;
	public String getDenddate() {
		return denddate;
	}
	public void setDenddate(String denddate) {
		this.denddate = denddate;
	}
	public String getDdate() {
		return ddate;
	}
	public void setDdate(String ddate) {
		this.ddate = ddate;
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	public DriveDates()
	{
		
	}
	public DriveDates(String ddate, String dname,String denddate) {
		super();
		this.ddate = ddate;
		this.dname = dname;
		this.denddate = denddate;
	}
}
