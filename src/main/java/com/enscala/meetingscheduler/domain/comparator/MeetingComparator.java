package com.enscala.meetingscheduler.domain.comparator;

import java.util.Comparator;

import com.enscala.meetingscheduler.domain.Meeting;

public class MeetingComparator implements Comparator<Meeting>{
	
    @Override
    public int compare(Meeting o1, Meeting o2) {
    	return o1.getStartDate().compareTo(o2.getStartDate());
    }

}
