package com.app.food.models;

public class AddOrder {
    private Long userid;
    private Long order_id;
    private Long id;
    private String title;
    private String description;
    private String price;

    private String owner_name;
    private String status;
    private Long count;
    private String doc_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public AddOrder() {
        // Default constructor required for Firestore
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public Long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
    }

    public String getStatus() {
        return status;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public AddOrder(Long userid, Long order_id, Long id, String title, String description, String price, String owner_name, String status, Long count, String doc_id) {
        this.userid = userid;
        this.order_id = order_id;
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.owner_name = owner_name;
        this.status = status;
        this.count = count;
        this.doc_id = doc_id;
    }


}
