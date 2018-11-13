package nus.day02.model;

import java.util.Date;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.bind.JsonbBuilder;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "purchase_order")

@NamedQueries({
    @NamedQuery (
        name = "PurchaseOrder.findByCustomerId",
        query = "select po from PurchaseOrder po join po.customer c where c.customerId = :custId"
    ),
    @NamedQuery (
        name = "PurchaseOrder.findByOrderNum",
        query = "select po from PurchaseOrder po where po.orderNum = :orderNum"
    )
})
public class PurchaseOrder {
    
    @Id
    @Column(name="ORDER_NUM")
    private Integer orderNum;
    
    @Column(name="product_id")
    private Integer productId;
    
    private Integer quantity;
    
    @Column(name="shipping_cost")
    private Float shippingCost;
    
    @Column(name="sales_date")
    @Temporal(TemporalType.DATE)
    private Date salesDate;

    @Column(name="shipping_date")
    @Temporal(TemporalType.DATE)
    private Date shippingDate;
    
    @Column(name="freight_company")
    private String freightCompany;

    @ManyToOne
    @JoinColumn(name="customer_id",
            referencedColumnName = "customer_id")
    private Customer customer;
  
    
//    @Transient
//    private long tempDate;

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(Float shippingCost) {
        this.shippingCost = shippingCost;
    }

    public Date getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(Date salesDate) {
        this.salesDate = salesDate;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
    }

    public String getFreightCompany() {
        return freightCompany;
    }

    public void setFreightCompany(String freightCompany) {
        this.freightCompany = freightCompany;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("orderNum", orderNum)
                .add("productId", productId)
                .add("freightCompany", freightCompany);
        return builder.build();
    }
    
}
