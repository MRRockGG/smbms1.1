package com.kuang.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class Bill {

    private int id;//'账单编码',
    private String billCode;//'商品名称',
    private String productName;//'商品描述',
    private String productDesc;//'商品单位',
    private String productUnit;//'商品数量',
    private BigDecimal productCount;//'商品总额',
    private BigDecimal totalPrice;//'是否支付（1：未支付 2：已支付）',
    private long isPayment;//'创建者（userId）',
    private long createdBy;//'创建时间',
    private Date creationDate;//'更新者（userId）',
    private long modifyBy;//'更新时间',
    private Date modifyDate;//'供应商ID'
    private String providerName;//供应商名称

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public BigDecimal getProductCount() {
        return productCount;
    }

    public void setProductCount(BigDecimal productCount) {
        this.productCount = productCount;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public long getIsPayment() {
        return isPayment;
    }

    public void setIsPayment(long isPayment) {
        this.isPayment = isPayment;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public long getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(long modifyBy) {
        this.modifyBy = modifyBy;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

}
