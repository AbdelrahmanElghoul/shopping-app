package com.example.shoppingapp;

import java.util.List;


public class Item {

  private String name;
  private int icon;
  private String description;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getIcon() {
    return icon;
  }

  public void setIcon(int icon) {
    this.icon = icon;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public String getManufactureDetails() {
    return manufactureDetails;
  }

  public void setManufactureDetails(String manufactureDetails) {
    this.manufactureDetails = manufactureDetails;
  }

  private double price;
  private String manufactureDetails;

}