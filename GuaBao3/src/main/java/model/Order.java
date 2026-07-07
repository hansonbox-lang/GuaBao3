package model;

import java.sql.Timestamp;

public class Order {
    private Integer orderId;
    private String memberId;
    private Integer totalAmount;
    private Integer earnedPoints;
    private Integer giftCount;
    private Timestamp orderTime;

    // 無參數建構子
    public Order() {
    }

    // 全參數建構子
    public Order(Integer orderId, String memberId, Integer totalAmount, Integer earnedPoints, Integer giftCount, Timestamp orderTime) {
        this.orderId = orderId;
        this.memberId = memberId;
        this.totalAmount = totalAmount;
        this.earnedPoints = earnedPoints;
        this.giftCount = giftCount;
        this.orderTime = orderTime;
    }

    // Getters and Setters
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getEarnedPoints() {
        return earnedPoints;
    }

    public void setEarnedPoints(Integer earnedPoints) {
        this.earnedPoints = earnedPoints;
    }

    public Integer getGiftCount() {
        return giftCount;
    }

    public void setGiftCount(Integer giftCount) {
        this.giftCount = giftCount;
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }
}