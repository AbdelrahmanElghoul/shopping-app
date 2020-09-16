package com.example.shoppingapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Category implements Parcelable {
  private String Name;
  private int icon;
  private List<Item> itemList;

  public String getName() {
    return Name;
  }

  public void setName(String name) {
    Name = name;
  }

  public int getIcon() {
    return icon;
  }

  public void setIcon(int icon) {
    this.icon = icon;
  }

  public List<Item> getItemList() {

    return itemList==null? new ArrayList<>():itemList;
  }

  public void setItemList(List<Item> itemList) {
    this.itemList = itemList;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.Name);
    dest.writeInt(this.icon);
    dest.writeList(this.itemList);
  }

  public Category() {
  }

  protected Category(Parcel in) {
    this.Name = in.readString();
    this.icon = in.readInt();
    this.itemList = new ArrayList<Item>();
    in.readList(this.itemList, Item.class.getClassLoader());
  }

  public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
    @Override
    public Category createFromParcel(Parcel source) {
      return new Category(source);
    }

    @Override
    public Category[] newArray(int size) {
      return new Category[size];
    }
  };
}
