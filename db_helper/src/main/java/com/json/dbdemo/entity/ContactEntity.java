package com.json.dbdemo.entity;

/**
 * Created by Ext on 2016/1/22.
 */
public class ContactEntity extends BaseEntity{

    private int id;
    private String phone;
    private String name;
    private String address;

    public ContactEntity() {
    }

    public ContactEntity(int id, String phone, String name, String address) {
        this.id = id;
        this.phone = phone;
        this.name = name;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "ContactEntity{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
