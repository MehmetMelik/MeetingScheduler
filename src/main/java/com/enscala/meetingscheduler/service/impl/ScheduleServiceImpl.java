/**
 * 
 */
package com.enscala.meetingscheduler.service.impl;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.time.DateUtils;

import com.enscala.meetingscheduler.domain.Meeting;
import com.enscala.meetingscheduler.domain.comparator.MeetingComparator;
import com.enscala.meetingscheduler.domain.comparator.SubmissionComparator;
import com.enscala.meetingscheduler.service.ScheduleService;

/**
 * @author Melik
 *
 */
public class ScheduleServiceImpl implements ScheduleService {
	
	public static final String DATEFORMATSubmission = "yyyy-MM-dd HH:mm:ss";
	public static final String DATEFORMATStart = "yyyy-MM-dd HH:mm";
	public static final String DATEFORMATSchedule = "yyyy-MM-dd";
	public static final String DATEFORMATOfficeInit = "HHmm";
	public static final String DATEFORMATOffice = "HH:mm";
	private SimpleDateFormat sdfSchedule = new SimpleDateFormat(DATEFORMATSchedule);
	private SimpleDateFormat sdfOffice = new SimpleDateFormat(DATEFORMATOffice);

	@Override
	public String processSchedule(InputStream in) {
		
		Scanner scanner = new Scanner(in, "UTF-8");
		String officeHours = scanner.nextLine();
		List<Meeting> list = getMeetingsInOfficeHours(scanner, officeHours);
		scanner.close();
		return toString(getMeetingsResultList(list));
	}
	
	/**
	 * gets office start hour
	 * @param firstLine
	 * @return office start hour
	 */
	public Date getOfficeStart(String firstLine) {
		return getOfficeHour(firstLine.split(" ")[0]);
	}
	
	/**
	 * get office end hour
	 * @param firstLine
	 * @return office end hour
	 */
	public Date getOfficeEnd(String firstLine) {
		return getOfficeHour(firstLine.split(" ")[1]);
	}
	
	/**
	 * parses hour String to Date object
	 * @param hour
	 * @return date
	 */
	public Date getOfficeHour(String hour) {
		SimpleDateFormat sdfOfficeInit = new SimpleDateFormat(DATEFORMATOfficeInit);
		Date officeHour = null;
		try {
			officeHour = sdfOfficeInit.parse(hour);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return officeHour;
	}
	
	/**
	 * creates a meeting with given information
	 * @param submissionInfo
	 * @param startAndDurationInfo
	 * @return meeting
	 */
	public Meeting createMeeting(String submissionInfo, String startAndDurationInfo) {
		String employeeId;
		Date submissionDate = null, startDate = null, endDate = null, endTime = null, startTime = null;
		Integer duration;
		SimpleDateFormat sdfSub = new SimpleDateFormat(DATEFORMATSubmission);
		SimpleDateFormat sdfStart = new SimpleDateFormat(DATEFORMATStart);
		employeeId = submissionInfo.substring(DATEFORMATSubmission.length()+1);
		duration = Integer.parseInt(startAndDurationInfo.substring(DATEFORMATStart.length()+1));
		try {
			submissionDate = sdfSub.parse(submissionInfo.substring(0, DATEFORMATSubmission.length()));
			startDate = sdfStart.parse(startAndDurationInfo.substring(0, DATEFORMATStart.length()));
			startTime = sdfOffice.parse(startAndDurationInfo.substring(11, 16));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		endTime = DateUtils.addHours(startTime, duration);
		endDate = DateUtils.addHours(startDate, duration);
		Meeting meeting = new Meeting();
		meeting.setSubmissionTime(submissionDate);
		meeting.setEmployeeId(employeeId);
		meeting.setStartDate(startDate);
		meeting.setDurationInHours(duration);
		meeting.setEndDate(endDate);
		meeting.setEndTime(endTime);
		meeting.setStartTime(startTime);
		
		return meeting;
	}
	
	/**
	 * checks if the meeting is in the officeHours
	 * @param meeting
	 * @param officeHours
	 * @return true if it is in office hours, false otherwise
	 */
	public boolean isInOfficeHours(Meeting meeting, String officeHours) {
		return !meeting.getStartTime().before(getOfficeStart(officeHours)) 
				&& !meeting.getEndTime().after(getOfficeEnd(officeHours));
	}
	
	/**
	 * 
	 * @param m1
	 * @param m2
	 * @return false if meetings collide, true otherwise
	 */
	public boolean isNotColliding(Meeting m1, Meeting m2) {
		//m1 = resultIterator.next();
		Date end = m1.getEndDate();
		Date start = m1.getStartDate();
		if(!(m2.getStartDate().before(start) && !m2.getEndDate().after(start)) && m2.getStartDate().before(end))
			// neither the candidate meeting (m2) starts and finishes before m1, nor it starts after m1 finishes 
			return false;
		else return true;
	}
	
	/**
	 * 
	 * @param scanner
	 * @param officeHours
	 * @return
	 */
	public List<Meeting> getMeetingsInOfficeHours(Scanner scanner, String officeHours) {
		List<Meeting> list = new ArrayList<Meeting>();
		while (scanner.hasNextLine()) {
			Meeting meeting = createMeeting(scanner.nextLine(), scanner.nextLine());
			if (isInOfficeHours(meeting, officeHours))
				list.add(meeting); // the list holds the meetings in office hours.
		}
		return list;
	}
	
	/**
	 * returns the list of meetings in a first come first serve basis and in the order of date/time.
	 * @param list
	 * @return
	 */
	public List<Meeting> getMeetingsResultList(List<Meeting> list) {
		// sort by the order of submission
		Collections.sort(list, new SubmissionComparator());
		List<Meeting> resultList = new ArrayList<Meeting>();
		Iterator<Meeting> iterator = list.listIterator();
		// initialize resultList with first meeting (if any)
		if (iterator.hasNext())
			resultList.add(iterator.next());
		Meeting m2;
		// create an iterator on resultList
		Iterator<Meeting> resultIterator;
		boolean flag;
		while(iterator.hasNext()) {
			// get the candidate
			m2 = iterator.next();
			// re-initialize resultList iterator
			resultIterator = resultList.listIterator();
			// re-initialize flag as true
			flag = true;
			// compare the candidate meeting against all the meetings in the current resultList
			while(resultIterator.hasNext() && flag) {
				flag = isNotColliding(resultIterator.next(), m2);
			}
			// if test successful (means flag never turned to be false)
			if (flag)
				resultList.add(m2);
		}
		// sort by the order of start date of the meetings
		Collections.sort(resultList, new MeetingComparator());
		return resultList;
	}
	
	/**
	 * 
	 * @param list
	 * @return formatted string value of list
	 */
	public String toString(List<Meeting> list) {
		StringBuffer sb = new StringBuffer();
		Meeting init = list.get(0);
		Date initialMeetingDate = DateUtils.truncate(init.getStartDate(), Calendar.DATE);
		sb.append("\n");
		sb.append(sdfSchedule.format(initialMeetingDate));
		sb.append("\n");
		
		Date meetingDate;
		for(Meeting m: list) {
			meetingDate = DateUtils.truncate(m.getStartDate(), Calendar.DATE);
			if (meetingDate.after(initialMeetingDate)) {
				sb.append(sdfSchedule.format(meetingDate));
				sb.append("\n");
				initialMeetingDate = meetingDate;
			}
			sb.append(sdfOffice.format(m.getStartTime()));
			sb.append(" ");
			sb.append(sdfOffice.format(m.getEndTime()));
			sb.append(" ");
			sb.append(m.getEmployeeId());
			sb.append("\n");
			
		}
		return sb.toString().trim();
	}

}
