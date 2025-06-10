package com.demo;
import com.f1.ami.amicommon.customobjects.AmiScriptAccessible;

@AmiScriptAccessible(name = "TestAccount")
public class TestClass {
    private double price;
    private int quantity;
    private String name;

    @AmiScriptAccessible
    public TestClass(String name) { 
        this.name = name; 
    }

    @AmiScriptAccessible(name = "setValue", params = { "px", "qty" })
    public void setValue(double price, int quantity) {     
        this.price = price;
        this.quantity = quantity;
    }

    @AmiScriptAccessible(name = "print")
    public String print() {
        return quantity + "@" + price + " for " + name + " is " + (quantity * price);
    }
} 