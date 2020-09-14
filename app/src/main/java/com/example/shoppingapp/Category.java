package com.example.shoppingapp;

import java.util.ArrayList;
import java.util.List;

public class Category{
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
}
