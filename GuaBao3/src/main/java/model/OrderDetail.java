package model;

public class OrderDetail {
    private Integer detailId;
    private Integer orderId;
    private String itemName;
    private Integer unitPrice;
    private Integer quantity;
    private Integer subtotal;

    // 無參數建構子
    public OrderDetail() {
    }

    // 專門提供給前台點餐結帳時使用的便利建構子 (對應 GuaBao3.java 呼叫：new OrderDetail(itemName, unitPrice, quantity, subtotal))
    public OrderDetail(String itemName, Integer unitPrice, Integer quantity, Integer subtotal) {
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    // 全參數建構子
    public OrderDetail(Integer detailId, Integer orderId, String itemName, Integer unitPrice, Integer quantity, Integer subtotal) {
        this.detailId = detailId;
        this.orderId = orderId;
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    // Getters and Setters
    public Integer getDetailId() {
        return detailId;
    }

    public void setDetailId(Integer detailId) {
        this.detailId = detailId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Integer subtotal) {
        this.subtotal = subtotal;
    }
}