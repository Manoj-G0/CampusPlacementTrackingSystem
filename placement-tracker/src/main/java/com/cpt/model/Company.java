package com.cpt.model;

public class Company {
	private Integer cmpId;
	private Integer cmpCctId;
	private String cmpName;
	private String cmpDesc;
	private String categoryName;

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Company() {
	}

	public Integer getCmpId() {
		return cmpId;
	}

	public void setCmpId(Integer cmpId) {
		this.cmpId = cmpId;
	}

	public Integer getCmpCctId() {
		return cmpCctId;
	}

	public void setCmpCctId(Integer cmpCctId) {
		this.cmpCctId = cmpCctId;
	}

	public String getCmpName() {
		return cmpName;
	}

	public void setCmpName(String cmpName) {
		this.cmpName = cmpName;
	}

	public String getCmpDesc() {
		return cmpDesc;
	}

	public void setCmpDesc(String cmpDesc) {
		this.cmpDesc = cmpDesc;
	}
}
