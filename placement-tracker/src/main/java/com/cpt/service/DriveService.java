package com.cpt.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpt.dao.DriveDAOInt;
import com.cpt.model.Drive;

@Service
public class DriveService {

	@Autowired
	private DriveDAOInt driveDao;

	// to get the drives based on the date
	public List<Drive> getDrives(LocalDate firstDateOfCurrentMonth) {
		return driveDao.getDrives(firstDateOfCurrentMonth);
	}
}
