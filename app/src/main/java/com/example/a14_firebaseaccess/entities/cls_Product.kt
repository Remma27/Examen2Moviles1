package com.example.a14_firebaseaccess.entities

class cls_Product {
    var ProductID: String = ""
    var ProductName: String = ""
    var SupplierID: String = ""
    var CategoryID: String = ""
    var QuantityPerUnit: String = ""
    var UnitPrice: String = ""
    var UnitsInStock: String = ""
    var UnitsOnOrder: String = ""
    var ReorderLevel: String = ""
    var Discontinued: String = ""

    constructor() {}

    constructor(
        ProductID: String,
        ProductName: String,
        SupplierID: String,
        CategoryID: String,
        QuantityPerUnit: String,
        UnitPrice: String,
        UnitsInStock: String,
        UnitsOnOrder: String,
        ReorderLevel: String,
        Discontinued: String
    ) {
        this.ProductID = ProductID
        this.ProductName = ProductName
        this.SupplierID = SupplierID
        this.CategoryID = CategoryID
        this.QuantityPerUnit = QuantityPerUnit
        this.UnitPrice = UnitPrice
        this.UnitsInStock = UnitsInStock
        this.UnitsOnOrder = UnitsOnOrder
        this.ReorderLevel = ReorderLevel
        this.Discontinued = Discontinued
    }
}
