package com.enscala.meetingscheduler.domain;

import java.util.Date;

/**
 * 
 * @author Melik
 * 	[request submission time, in the format YYYY-MM-DD HH:MM:SS] [ARCH:employee id]
	[meeting start time, in the format YYYY-MM-DD HH:MM] [ARCH:meeting duration in hours]

 *
 */
public class Meeting {
	
	private Date submissionTime;
	private String employeeId;
	private Date startDate;
	private Integer durationInHours;
	private Date endDate;
	private Date startTime;
	private Date endTime;
	
	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the submissionTime
	 */
	public Date getSubmissionTime() {
		return submissionTime;
	}
	/**
	 * @param submissionTime the submissionTime to set
	 */
	public void setSubmissionTime(Date submissionTime) {
		this.submissionTime = submissionTime;
	}
	/**
	 * @return the employeeId
	 */
	public String getEmployeeId() {
		return employeeId;
	}
	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	/**
	 * @return the startTime
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartDate(Date startTime) {
		this.startDate = startTime;
	}
	/**
	 * @return the durationInHours
	 */
	public Integer getDurationInHours() {
		return durationInHours;
	}
	/**
	 * @param durationInHours the durationInHours to set
	 */
	public void setDurationInHours(Integer durationInHours) {
		this.durationInHours = durationInHours;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((durationInHours == null) ? 0 : durationInHours.hashCode());
		result = prime * result
				+ ((employeeId == null) ? 0 : employeeId.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result
				+ ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result
				+ ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result
				+ ((submissionTime == null) ? 0 : submissionTime.hashCode());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Meeting other = (Meeting) obj;
		if (durationInHours == null) {
			if (other.durationInHours != null)
				return false;
		} else if (!durationInHours.equals(other.durationInHours))
			return false;
		if (employeeId == null) {
			if (other.employeeId != null)
				return false;
		} else if (!employeeId.equals(other.employeeId))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		if (submissionTime == null) {
			if (other.submissionTime != null)
				return false;
		} else if (!submissionTime.equals(other.submissionTime))
			return false;
		return true;
	}

	
}
