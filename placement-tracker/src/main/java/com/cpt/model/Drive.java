package com.cpt.model;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

@Component
public class Drive {
    private String pldName;
    
    private LocalDate pldStartDate;
    private LocalDate pldEndDate;

    // Getters and Setters

    public String getPldName() {
        return pldName;
    }

    public void setPldName(String pldName) {
        this.pldName = pldName;
    }

    public LocalDate getPldStartDate() {
        return pldStartDate;
    }

    public void setPldStartDate(LocalDate pldStartDate) {
        this.pldStartDate = pldStartDate;
    }

    public LocalDate getPldEndDate() {
        return pldEndDate;
    }

    public void setPldEndDate(LocalDate pldEndDate) {
        this.pldEndDate = pldEndDate;
    }

	@Override
	public String toString() {
		return "Drive [pldName=" + pldName + ", pldStartDate=" + pldStartDate + ", pldEndDate=" + pldEndDate + "]";
	}
}
