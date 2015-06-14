package com.tachys.moneyshare.model;

public class Member {

    public Member(long Id, String Name, String Email, String Phone) {
        this.Id = Id;
        this.Email = Email;
        this.Name = Name;
        this.Phone = Phone;
    }

    public Member(String Name, String Email, String Phone) {
        this.Email = Email;
        this.Name = Name;
        this.Phone = Phone;
    }

    public long Id;
    public String Name;
    public String Email;
    public String Phone;
}
