package com.example.manafood.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class OrderDetailsModel implements Parcelable {
    String userUid;
    String username;
    String address;
    String phone;

    List<String> foodNames;
    List<String> foodImages;
    List<String> foodPrice;
    List<Integer> foodQuantities;
    String totalPrice;

    Boolean orderAccepted;
    Boolean paymentAccepted;
    String itemPushKey;
    long currentTime;

    // Default constructor required for Firebase
    public OrderDetailsModel() {}

    // Parameterized constructor
    public OrderDetailsModel(String userId, String name, String address, String phone, List<String> foodItemName,
                             List<String> foodItemImage, List<String> foodItemPrice, List<Integer> foodItemQuantities,
                             String totalAmount, long time, String itemPushKey, boolean orderAccepted, boolean paymentAccepted) {
        this.userUid = userId;
        this.username = name;
        this.address = address;
        this.phone = phone;
        this.foodNames = foodItemName;
        this.foodImages = foodItemImage;
        this.foodPrice = foodItemPrice;
        this.foodQuantities = foodItemQuantities;
        this.totalPrice = totalAmount;
        this.currentTime = time;
        this.itemPushKey = itemPushKey;
        this.orderAccepted = orderAccepted;
        this.paymentAccepted = paymentAccepted;
    }

    // Parcelable constructor
    protected OrderDetailsModel(Parcel in) {
        userUid = in.readString();
        username = in.readString();
        address = in.readString();
        phone = in.readString();
        foodNames = in.createStringArrayList();
        foodImages = in.createStringArrayList();
        foodPrice = in.createStringArrayList();
        foodQuantities = in.createTypedArrayList(null); // Modified for correct reading of list
        totalPrice = in.readString();
        byte tmpOrderAccepted = in.readByte();
        orderAccepted = tmpOrderAccepted == 0 ? null : tmpOrderAccepted == 1;
        byte tmpPaymentAccepted = in.readByte();
        paymentAccepted = tmpPaymentAccepted == 0 ? null : tmpPaymentAccepted == 1;
        itemPushKey = in.readString();
        currentTime = in.readLong();
    }

    public static final Creator<OrderDetailsModel> CREATOR = new Creator<OrderDetailsModel>() {
        @Override
        public OrderDetailsModel createFromParcel(Parcel in) {
            return new OrderDetailsModel(in);
        }

        @Override
        public OrderDetailsModel[] newArray(int size) {
            return new OrderDetailsModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(userUid);
        parcel.writeString(username);
        parcel.writeString(address);
        parcel.writeString(phone);
        parcel.writeString(totalPrice);
        parcel.writeString(itemPushKey);
        parcel.writeByte((byte) (orderAccepted == null ? 0 : orderAccepted ? 1 : 2));
        parcel.writeByte((byte) (paymentAccepted == null ? 0 : paymentAccepted ? 1 : 2));
        parcel.writeLong(currentTime);
        parcel.writeStringList(foodNames);
        parcel.writeStringList(foodImages);
        parcel.writeStringList(foodPrice);
        parcel.writeList(foodQuantities);
    }

    // Getters and Setters for all fields

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getFoodNames() {
        return foodNames;
    }

    public void setFoodNames(List<String> foodNames) {
        this.foodNames = foodNames;
    }

    public List<String> getFoodImages() {
        return foodImages;
    }

    public void setFoodImages(List<String> foodImages) {
        this.foodImages = foodImages;
    }

    public List<String> getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(List<String> foodPrice) {
        this.foodPrice = foodPrice;
    }

    public List<Integer> getFoodQuantities() {
        return foodQuantities;
    }

    public void setFoodQuantities(List<Integer> foodQuantities) {
        this.foodQuantities = foodQuantities;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Boolean getOrderAccepted() {
        return orderAccepted;
    }

    public void setOrderAccepted(Boolean orderAccepted) {
        this.orderAccepted = orderAccepted;
    }

    public Boolean getPaymentAccepted() {
        return paymentAccepted;
    }

    public void setPaymentAccepted(Boolean paymentAccepted) {
        this.paymentAccepted = paymentAccepted;
    }

    public String getItemPushKey() {
        return itemPushKey;
    }

    public void setItemPushKey(String itemPushKey) {
        this.itemPushKey = itemPushKey;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }




}
