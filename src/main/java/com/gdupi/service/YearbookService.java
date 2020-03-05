package com.gdupi.service;



import java.util.List;


public interface YearbookService {

    /**
     * 通过 ES 查询（通过搜索框查询）
     */
    List<YearbookResult> query(YearbookSearch yearbookSearch);

    /**
     * 通过 ES 查询（通过城市、年份查询）
     */
    List<String> queryByYearcity(YearbookSearch yearbookSearch);

    /**
     * 通过 ES 查询（通过搜索框查询）
     */
    List<YearbookResult> queryByYearCityIndex(YearbookSearch yearbookSearch);

    /**
     * 根据省名称查询城市列表接口
     */
    List<String> getCitybyProvince(String province);

    /**
     * 根据省名称查询城市列表接口
     */
    List<String> getRegionbycity(String city);



}
