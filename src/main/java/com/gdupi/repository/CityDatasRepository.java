package com.gdupi.repository;

import com.gdupi.entity.CityDatas;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CityDatasRepository extends CrudRepository<CityDatas,Long> {

    //根据省份查找所有信息
    List<CityDatas> findAllByProvince(String province);

    //根据城市查找所有信息
    List<CityDatas> findAllByCity(String city);


}
