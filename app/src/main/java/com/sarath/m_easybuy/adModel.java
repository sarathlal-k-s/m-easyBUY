package com.sarath.m_easybuy;

import java.io.Serializable;

public class adModel implements Serializable {

    private String title;
    private String price;
    private String description;
    private String phone;
    private String publisherName;

    private adModel(){
    }

    public adModel(String title, String price, String description, String phone, String publisherName){

        this.title=title;
        this.price=price;
        this.description=description;
        this.phone =phone;
        this.publisherName=publisherName;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
