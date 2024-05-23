package com.example.manafood.model;

public class MenuItemModel {
    //Define fields that you want to store
    private String foodName;
    private String foodPrice;
    private String foodImage;
    private String foodDescription;
    private String foodIngredient;

    public MenuItemModel(){
        //default constructor required for Firebase
    }

    public MenuItemModel(String foodName, String foodPrice, String foodImage, String foodDescription, String foodIngredient){
        this.foodName = foodName;
        this.foodPrice= foodPrice;
        this.foodImage = foodImage;
        this.foodDescription= foodDescription;
        this.foodIngredient= foodIngredient;
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

    public String getFoodIngredient(){
        return foodIngredient;
    }

    public void setFoodIngredient(String foodIngredient){
        this.foodIngredient = foodIngredient;
    }
}
