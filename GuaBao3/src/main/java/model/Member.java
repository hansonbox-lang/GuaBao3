package model;

import java.sql.Timestamp;

public class Member {
    private String memberId;
    private int totalPoints;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Member() {}
    public Member(String memberId, int totalPoints) {
        this.memberId = memberId;
        this.totalPoints = totalPoints;
    }
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public int getTotalPoints() {
		return totalPoints;
	}
	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

}