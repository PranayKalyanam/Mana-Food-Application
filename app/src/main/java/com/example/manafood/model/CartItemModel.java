package com.example.manafood.model;

public class CartItemModel {
    //Define fields that you want to store
    private String foodName;
    private String foodPrice;
    private String foodImage;
    private String foodDescription;
    private Integer foodQuantity;
    private String foodIngredients;

    public CartItemModel(){
        //default constructor required for Firebase
    }

    public CartItemModel(String foodName, String foodPrice, String foodImage, String foodDescription, int foodQuantity, String foodIngredients){
        this.foodName = foodName;
        this.foodPrice= foodPrice;
        this.foodImage = foodImage;
        this.foodDescription= foodDescription;
        this.foodQuantity= foodQuantity;
        this.foodIngredients = foodIngredients;

    }

    //Getters and Setters
    public String getFoodName(){
        return foodName;
    }

    public void setFoodName(String foodName){
        this.foodName = foodName;
    }

    public String getFoodPrice(){
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice){
        this.foodPrice = foodPrice;
    }

    public String getFoodImage(){
        return foodImage;
    }

    public void setFoodImage(String foodImage){
        this.foodImage = foodImage;
    }

    public String getFoodDescription(){
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription){
        this.foodDescription = foodDescription;
    }

    public int getFoodQuantity(){
        return foodQuantity;
    }

    public void setFoodQuantity(int foodQuantity){
        this.foodQuantity = foodQuantity;
    }

    public String getFoodIngredients() {
        return foodIngredients;
    }

    public void setFoodIngredients(String foodIngredients) {
        this.foodIngredients = foodIngredients;
    }
}
