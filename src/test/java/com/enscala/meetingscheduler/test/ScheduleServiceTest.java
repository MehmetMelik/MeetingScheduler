package com.enscala.meetingscheduler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

import com.enscala.meetingscheduler.domain.Meeting;
import com.enscala.meetingscheduler.service.impl.ScheduleServiceImpl;

public class ScheduleServiceTest {
	private ScheduleServiceImpl service;
	
	@Before
	public void setup() throws IOException {
		service = new ScheduleServiceImpl();
	
	}
	
	@Test
	public void shouldReturnProcessSchedule() {
		String result = "2011-03-21"
				+ "\n09:00 11:00 EMP002"
				+ "\n2011-03-22"
				+ "\n14:00 16:00 EMP003"
				+ "\n16:00 17:00 EMP004";
		InputStream in = IOUtils.toInputStream("0900 1730"
											+"\n2011-03-17 10:17:06 EMP001"
											+"\n2011-03-21 09:00 2"
											+"\n2011-03-16 12:34:56 EMP002"
											+"\n2011-03-21 09:00 2"
											+"\n2011-03-16 09:28:23 EMP003"
											+"\n2011-03-22 14:00 2"
											+"\n2011-03-17 11:23:45 EMP004"
											+"\n2011-03-22 16:00 1"
											+"\n2011-03-15 17:29:12 EMP005"
											+"\n2011-03-21 16:00 3"
											);
		assertEquals(result,service.processSchedule(in));
	}
	
	@Test
	public void shouldReturnOfficeHour() {
		String hour = "0900";
		Date date = new Date(0l);
		date = DateUtils.setHours(date, 9);
		assertEquals(date, service.getOfficeHour(hour));
	}
	
	@Test
	public void shouldReturnOfficeStart() {
		String firstLine = "0900 1700";
		Date date = new Date(0l);
		date = DateUtils.setHours(date, 9);
		assertEquals(date, service.getOfficeStart(firstLine));
	}
	
	@Test
	public void shouldReturnOfficeEnd() {
		String firstLine = "0900 1700";
		Date date = new Date(0l);
		date = DateUtils.setHours(date, 17);
		assertEquals(date, service.getOfficeEnd(firstLine));
	}
	
	@Test
	public void shouldReturnTrueIfMeetingIsInOfficeHours() {
		String officeHours = "0900 1700";
		Meeting meeting = new Meeting();
		Date date = new Date(0l);
		Date start = DateUtils.setHours(date, 9);
		Date end = DateUtils.setHours(date, 11);
		meeting.setStartTime(start);
		meeting.setEndTime(end);
		assertTrue(service.isInOfficeHours(meeting, officeHours));
	}
	
	@Test
	public void shouldReturnTrueifMeeting1IsNotCollidingWithMeeting2() {
		Meeting m1 = new Meeting();
		Date date = new Date(0l);
		Date start1 = DateUtils.setHours(date, 9);
		Date end1 = DateUtils.setHours(date, 11);
		m1.setStartDate(start1);
		m1.setEndDate(end1);
		Meeting m2 = new Meeting();
		Date start2 = DateUtils.setHours(date, 11);
		Date end2 = DateUtils.setHours(date, 13);
		m2.setStartDate(start2);
		m2.setEndDate(end2);
		assertTrue(service.isNotColliding(m1, m2));		
	}
	
	@Test
	public void shouldCreateMeeting() {
		String submissionInfo = "2011-03-17 10:17:06 EMP001";
		String startAndDurationInfo = "2011-03-21 09:00 2";
		Meeting meeting = new Meeting();
		Date submissionTime = null, startDate = null, endDate = null, startTime = null, endTime = null;
		try {
			submissionTime = DateUtils.parseDate("2011-03-17 10:17:06", ScheduleServiceImpl.DATEFORMATSubmission);
			startDate = DateUtils.parseDate("2011-03-21 09:00", ScheduleServiceImpl.DATEFORMATStart);
			endDate = DateUtils.parseDate("2011-03-21 11:00", ScheduleServiceImpl.DATEFORMATStart);
			startTime = DateUtils.parseDate("09:00", ScheduleServiceImpl.DATEFORMATOffice);
			endTime = DateUtils.parseDate("11:00", ScheduleServiceImpl.DATEFORMATOffice);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		meeting.setEmployeeId("EMP001");
		meeting.setSubmissionTime(submissionTime);
		meeting.setStartTime(startTime);
		meeting.setStartDate(startDate);
		meeting.setEndTime(endTime);
		meeting.setEndDate(endDate);
		meeting.setDurationInHours(2);
		assertEquals(meeting, service.createMeeting(submissionInfo, startAndDurationInfo));
	}
	
	@Test
	public void ShouldReturnListOfMeetingsInOfficeHours() {
		InputStream in = IOUtils.toInputStream(
				"2011-03-17 10:17:06 EMP001"
				+"\n2011-03-21 09:00 2"
				+"\n2011-03-16 12:34:56 EMP002"
				+"\n2011-03-21 09:00 2"
				+"\n2011-03-16 09:28:23 EMP003"
				+"\n2011-03-22 14:00 2"
				+"\n2011-03-17 11:23:45 EMP004"
				+"\n2011-03-22 16:00 1"
				+"\n2011-03-15 17:29:12 EMP005"
				+"\n2011-03-21 16:00 3"
				);
		Scanner scanner = new Scanner(in, "UTF-8");
		List<Meeting> listExpected = new ArrayList<Meeting>();
		listExpected.add(service.createMeeting("2011-03-17 10:17:06 EMP001", "2011-03-21 09:00 2"));
		listExpected.add(service.createMeeting("2011-03-16 12:34:56 EMP002", "2011-03-21 09:00 2"));
		listExpected.add(service.createMeeting("2011-03-16 09:28:23 EMP003", "2011-03-22 14:00 2"));
		listExpected.add(service.createMeeting("2011-03-17 11:23:45 EMP004", "2011-03-22 16:00 1"));
		
		List<Meeting> listActual = service.getMeetingsInOfficeHours(scanner, "0900 1730");
		Iterator<Meeting> iteratorActual = listActual.iterator();
		Iterator<Meeting> iteratorExpected = listExpected.iterator();
		while(iteratorActual.hasNext()) {
			if(iteratorExpected.hasNext())
				assertEquals(iteratorExpected.next(),iteratorActual.next());
			else 
				fail("The lists of meetings in office hours do not match.");
		}
		
		scanner.close();
	}
	
	@Test
	public void ShouldReturnResultListOfMeetings() {
		InputStream in = IOUtils.toInputStream(
				"2011-03-17 10:17:06 EMP001"
				+"\n2011-03-21 09:00 2"
				+"\n2011-03-16 12:34:56 EMP002"
				+"\n2011-03-21 09:00 2"
				+"\n2011-03-16 09:28:23 EMP003"
				+"\n2011-03-22 14:00 2"
				+"\n2011-03-17 11:23:45 EMP004"
				+"\n2011-03-22 16:00 1"
				+"\n2011-03-15 17:29:12 EMP005"
				+"\n2011-03-21 16:00 3"
				);
		Scanner scanner = new Scanner(in, "UTF-8");
		List<Meeting> listExpected = new ArrayList<Meeting>();
		listExpected.add(service.createMeeting("2011-03-16 12:34:56 EMP002", "2011-03-21 09:00 2"));
		listExpected.add(service.createMeeting("2011-03-16 09:28:23 EMP003", "2011-03-22 14:00 2"));
		listExpected.add(service.createMeeting("2011-03-17 11:23:45 EMP004", "2011-03-22 16:00 1"));
		
		List<Meeting> list = service.getMeetingsInOfficeHours(scanner, "0900 1730");
		List<Meeting> listActual = service.getMeetingsResultList(list);
		Iterator<Meeting> iteratorActual = listActual.iterator();
		Iterator<Meeting> iteratorExpected = listExpected.iterator();
		while(iteratorActual.hasNext()) {
			if(iteratorExpected.hasNext())
				assertEquals(iteratorExpected.next(),iteratorActual.next());
			else 
				fail("The lists of meetings in resultList do not match.");
		}
		
		scanner.close();
	}
	
	@Test
	public void ShouldReturnStringRepresentationOfSchedule() {
		String expected = "2011-03-21"
		+ "\n09:00 11:00 EMP002"
		+ "\n2011-03-22"
		+ "\n14:00 16:00 EMP003"
		+ "\n16:00 17:00 EMP004";

		List<Meeting> list = new ArrayList<Meeting>();
		list.add(service.createMeeting("2011-03-16 12:34:56 EMP002", "2011-03-21 09:00 2"));
		list.add(service.createMeeting("2011-03-16 09:28:23 EMP003", "2011-03-22 14:00 2"));
		list.add(service.createMeeting("2011-03-17 11:23:45 EMP004", "2011-03-22 16:00 1"));
		
		List<Meeting> listActual = service.getMeetingsResultList(list);
		assertEquals(expected, service.toString(listActual));

	}
}
