package com.example.shoppingapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


public class Item implements Parcelable {

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

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.name);
    dest.writeInt(this.icon);
    dest.writeString(this.description);
    dest.writeDouble(this.price);
    dest.writeString(this.manufactureDetails);
  }

  public Item() {
  }

  protected Item(Parcel in) {
    this.name = in.readString();
    this.icon = in.readInt();
    this.description = in.readString();
    this.price = in.readDouble();
    this.manufactureDetails = in.readString();
  }

  public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
    @Override
    public Item createFromParcel(Parcel source) {
      return new Item(source);
    }

    @Override
    public Item[] newArray(int size) {
      return new Item[size];
    }
  };
}