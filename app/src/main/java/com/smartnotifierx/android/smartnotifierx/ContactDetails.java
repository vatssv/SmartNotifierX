package com.smartnotifierx.android.smartnotifierx;

/**
 * Created by sv300_000 on 12-Jul-17.
 */

public class ContactDetails {
    private String Name,Date,ContactName;

    public ContactDetails(){

    }

    public ContactDetails(String name, String date){
        this.Name = name;
        this.Date = date;
        this.ContactName = null;
    }

    public ContactDetails(String name,String date,String contactName)
    {
        this.Name = name;
        this.Date = date;
        this.ContactName = contactName;
    }

    public String getName(){
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getDate(){
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }

    public String getContactName(){
        return ContactName;
    }

    public void setContactName(String contactName){
        this.ContactName = contactName;
    }
}
