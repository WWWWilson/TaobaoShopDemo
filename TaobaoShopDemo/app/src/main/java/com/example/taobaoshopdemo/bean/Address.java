package com.example.taobaoshopdemo.bean;

import java.io.Serializable;

/**
        * Comparable接口对实现它的每个类的对象施加总顺序。
        * 这种排序称为类的自然排序，而类的compareTo方法称为其自然比较方法。
        *
        * 实现此接口的对象的列表（和数组）可以按自动排序集合.
        * 排序（和数组.排序).
        * 实现此接口的对象可以用作排序映射中的键或排序集中的元素，而无需指定比较器。
        *
        * */
public class Address implements Serializable,Comparable<Address> {

    private Long id;

    private String consignee;
    private String phone;
    private String addr;
    private String zipCode;
    private Boolean isDefault;

    public Address(){};

    public Address(String consignee, String phone, String addr, String zipCode){
        this.consignee = consignee;
        this.phone=phone;
        this.addr= addr;
        this.zipCode = zipCode;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }


    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }


    @Override
    public int compareTo(Address another) {
        //一个值和另一个值进行比较
        if(another.getIsDefault()!=null && this.getIsDefault() !=null)
            return another.getIsDefault().compareTo(this.getIsDefault());

        return -1;
    }
}
