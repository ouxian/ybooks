package com.gdupi.controller;

import com.gdupi.service.YearbookResult;
import com.gdupi.service.YearbookSearch;
import com.gdupi.service.YearbookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(description = "查询接口")
@Controller
@RequestMapping("query")
public class YearbookController {

    @Autowired
    private YearbookService yearbookService;

    /**
     * 通过 ES 查询（通过搜索框查询）
     */
    @ApiOperation(value = "通过搜索框查询详细信息",notes = "通过搜索框查询详细信息(keywords不为空，其他为空)")
    @RequestMapping(value = "yearbook/search",method = RequestMethod.POST,produces = { MediaType.APPLICATION_JSON_VALUE },consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity query(@RequestBody YearbookSearch yearbookSearch){

        List<YearbookResult> result = yearbookService.query(yearbookSearch);

        return new ResponseEntity(result, HttpStatus.OK);

    }

    /**
     * 通过 ES 查询（通过城市和年份查询--不通过搜索框）
     * 返回指标列表
     */
    @ApiOperation(value = "通过城市和年份查询指标列表",notes = "通过城市和年份查询指标列表(keywords、index_name必须都为空)")
    @RequestMapping(value = "yearbook/search/cityAndyear",method = RequestMethod.POST,produces = { MediaType.APPLICATION_JSON_VALUE },consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity queryByYearcity(@RequestBody YearbookSearch yearbookSearch){

        List<String> result = yearbookService.queryByYearcity(yearbookSearch);

        return new ResponseEntity(result, HttpStatus.OK);

    }

    /**
     * 通过 ES 查询（通过城市、年份、指标查询，不通过搜索框）
     */
    @ApiOperation(value = "通过城市、年份、指标查询详细信息(精确查询)",notes = "通过城市、年份、指标查询详细信息(keywords必须为空)")
    @RequestMapping(value = "yearbook/search/yearCityIndex",method = RequestMethod.POST,produces = { MediaType.APPLICATION_JSON_VALUE },consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity queryByYearIndexCity(@RequestBody YearbookSearch yearbookSearch){

        List<YearbookResult> result = yearbookService.queryByYearCityIndex(yearbookSearch);

        return new ResponseEntity(result, HttpStatus.OK);

    }

    /**
     * 选择省份对应的城市
     */
    @ApiOperation(value = "通过省份查询对应的市",notes = "通过 Mysql 查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "province",value = "省份名称",required = true,paramType = "query",dataType = "String")
    })
    @GetMapping("province/city")
    @ResponseBody
    public List<String> getCitybyProvince(@Param(value = "province")String province){

        List<String> cityList = yearbookService.getCitybyProvince(province);

        return cityList;
    }

    /**
     * 选择城市对应的区
     */
    @ApiOperation(value = "通过城市查询对应的区",notes = "通过 Mysql 查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "city",value = "城市名称",required = true,paramType = "query",dataType = "String")
    })
    @GetMapping("province/city/region")
    @ResponseBody
    public List<String> getRegionbycity(@Param(value = "city")String city){

        List<String> regionList = yearbookService.getRegionbycity(city);

        return regionList;
    }


}















