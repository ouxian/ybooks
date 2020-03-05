package com.gdupi.entity;

import javax.persistence.*;

@Entity
@Table(name = "city_datas")
public class CityDatas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "province")
    private String province;

    @Column(name = "city")
    private String city;

    @Column(name = "countytr")
    private String countytr;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountytr() {
        return countytr;
    }

    public void setCountytr(String countytr) {
        this.countytr = countytr;
    }
}
