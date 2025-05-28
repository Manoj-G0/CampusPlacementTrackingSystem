package com.cpt.dao;

import java.time.LocalDate;
import java.util.List;

import com.cpt.model.Drive;

public interface DriveDAOInt {

	List<Drive> getDrives(LocalDate firstDateOfCurrentMonth);

}
