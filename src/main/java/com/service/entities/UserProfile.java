package com.service.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "USER_PROFILE")
public class UserProfile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(name = "USER_NAME", nullable = false)
    public String username;
    @Column(name = "ADDRESS")
    public String address;
    @Column(name = "PROFILE_PICTURE")
    public String user_pic_id;
    @Column(name = "PRODUCT_SUBSCRIPTION")
    public boolean subscription;
    @Column(name = "ORDER_STATUS")
    public String orderStatus;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    public Product product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Product getProduct() {
        return product;
    }


    public String getUser_pic_id() {
        return user_pic_id;
    }

    public void setUser_pic_id(String user_pic_id) {
        this.user_pic_id = user_pic_id;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public boolean isSubscription() {
        return subscription;
    }

    public void setSubscription(boolean subscription) {
        this.subscription = subscription;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", address='" + address + '\'' +
                ", user_pic_id='" + user_pic_id + '\'' +
                ", subscription=" + subscription +
                ", product=" + product +
                '}';
    }
}
