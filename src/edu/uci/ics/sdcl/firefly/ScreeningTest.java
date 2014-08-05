package edu.uci.ics.sdcl.firefly;

import java.util.Date;

public class ScreeningTest {
	String userId;
	String hitId;
	Date consentDate;
	
	public ScreeningTest(String userId, String hitId, Date consentDate) {
		this.userId = userId;
		this.hitId = hitId;
		this.consentDate = consentDate;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getHitId() {
		return hitId;
	}

	public void setHitId(String hit) {
		this.hitId = hit;
	}

	public Date getConsentDate() {
		return consentDate;
	}

	public void setConsentDate(Date consentDate) {
		this.consentDate = consentDate;
	}	
	
}
