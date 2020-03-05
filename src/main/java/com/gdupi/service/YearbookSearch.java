package com.gdupi.service;

/**
 *  统计年鉴请求参数结构体
 */
public class YearbookSearch {
    public String keywords;

    public String year;

    public String citytr;

    public String index_name;

    private int start = 0;

    private int size = 20000;

    public int getStart() {
        return start > 0 ? start : 0;
    }

    public void setStart(int start) {
        this.start = start;
    }

    //分页
    public int getSize() {
        if (this.size < 1) {
            return 5;
        } else if (this.size > 500) {
            return 500;
        } else {
            return this.size;
        }
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCitytr() {
        return citytr;
    }

    public void setCitytr(String citytr) {
        this.citytr = citytr;
    }

    public String getIndex_name() {
        return index_name;
    }

    public void setIndex_name(String index_name) {
        this.index_name = index_name;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return "YearbookSearch {" +
                ", year='" + year + '\'' +
                ", citytr='" + citytr + '\'' +
                ", index_name='" + index_name + '\'' +
                ", keywords='" + keywords + '\'' +
                '}';
    }

}
