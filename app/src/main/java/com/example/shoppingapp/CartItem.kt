package com.example.shoppingapp

class CartItem: Item {

    constructor()
    constructor(item: Item){
        this.id=item.id
        this.name=item.name
        this.icon=item.icon
        this.description=item.description
        this.price=item.price
        this.manufacture=item.manufacture
        this.stock=item.stock
        this.categoryId=item.categoryId
        this.vendorId=item.vendorId
    }
    var quantity=1
}