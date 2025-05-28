package com.cpt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import com.cpt.dao.ResourceDAOInt;
import com.cpt.model.DriveSubmit;
import com.cpt.model.Resource;

@Component
public class ResourceAllocation {

	@Autowired
	private ResourceDAOInt rd;

	// To allocate the resource
	public Map<Integer, Integer> resAlloc(Map<Integer, Integer> lab, int noofstu) {
		Map<Integer, Integer> res = new HashMap<>();
		int prev = 0;
		for (Integer rno : lab.keySet()) {
			if (true) {
				if (lab.get(rno) >= noofstu) {
					prev += noofstu;
					noofstu = 0;
					res.put(rno, prev);
					break;
				} else {
					prev += lab.get(rno);
					noofstu -= lab.get(rno);
					res.put(rno, prev);
				}

			}
		}
		return res;

	}

	// To get the colleges
	public List<String> getColleges() {
		List<String> collist = rd.getCollegesList();
		return collist;
	}

	public void addRes(Resource res) {
		rd.addRes(res);
	}

	// To assign the resources
	public Map<Integer, Integer> assignRes(DriveSubmit ds) {
		Map<Integer, Integer> labs = new HashMap<>();
		List<Pair<String, String>> resources = rd.getAsRes(ds);
		for (Pair<String, String> res : resources) {

			int lno = Integer.parseInt(res.getFirst());
			int lc = Integer.parseInt(res.getSecond());
			labs.put(lno, lc);
		}

		return resAlloc(labs, ds.getNumStudents());
	}

	// To get the students for round
	public int getStudForRound(int round, int pid) {
		if (round == 1) {
			return rd.getAppliedStu(pid);
		} else
			return rd.getStu(round - 1);
	}

	// To allocate the resources
	public void allocate(DriveSubmit ds, Map<Integer, Integer> res, List<String> fac) {
		rd.clearExistingAllocationsByPldId(ds.getPld_id());
		rd.insertAllocationsWithDelta(res, fac, ds.getPld_id(), ds);
	}

}
