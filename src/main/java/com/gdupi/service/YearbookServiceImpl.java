package com.gdupi.service;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.Token;
import com.gdupi.entity.CityDatas;
import com.gdupi.repository.CityDatasRepository;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.nlpcn.es4sql.domain.Where;
import org.nlpcn.es4sql.exception.SqlParseException;
import org.nlpcn.es4sql.parse.ElasticSqlExprParser;
import org.nlpcn.es4sql.parse.SqlParser;
import org.nlpcn.es4sql.parse.WhereParser;
import org.nlpcn.es4sql.query.maker.QueryMaker;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class YearbookServiceImpl implements YearbookService{
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(YearbookService.class);

    private static final String INDEX_NAME = "yearbook";

    private static final String INDEX_TYPE = "doc";

    @Autowired
    private TransportClient esClient;

    @Autowired
    private CityDatasRepository cityDatasRepository;

    /**
     * 通过 ES 查询（通过搜索框查询）
     */
    @Override
    public List<YearbookResult> query(YearbookSearch yearbookSearch){
        SearchResponse response = searchResponse(yearbookSearch);
        List<YearbookResult> result=new ArrayList<YearbookResult>();

        //将搜索到的数据放入List
        for(SearchHit hit:response.getHits()){
            YearbookResult pointMap = new YearbookResult();
            pointMap.setCitytr(String.valueOf(hit.getSource().get(YearbookIndexKey.CITYTR)));
            pointMap.setDistinct(String.valueOf(hit.getSource().get(YearbookIndexKey.DISTINCT)));
            pointMap.setCountytr(String.valueOf(hit.getSource().get(YearbookIndexKey.COUNTYTR)));
            pointMap.setProvincetr(String.valueOf(hit.getSource().get(YearbookIndexKey.PROVINCETR)));
            pointMap.setStatisticNum(String.valueOf(hit.getSource().get(YearbookIndexKey.STATISTIC_NUM)));
            pointMap.setUnit(String.valueOf(hit.getSource().get(YearbookIndexKey.UNIT)));
            pointMap.setIndexName(String.valueOf(hit.getSource().get(YearbookIndexKey.INDEX_NAME)));
            pointMap.setYear(String.valueOf(hit.getSource().get(YearbookIndexKey.YEAR)));
            pointMap.setFrom(String.valueOf(hit.getSource().get(YearbookIndexKey.FROM)));
            result.add(pointMap);
        }
        return result;

    }

    /**
     * 通过 ES 查询（通过城市和年份查询指标列表--不通过搜索框）
     */
    @Override
    public List<String> queryByYearcity(YearbookSearch yearbookSearch){
        SearchResponse response = searchResponse(yearbookSearch);
        List<String> indexLists=new ArrayList<String>();

        //将搜索到的数据放入List
        for(SearchHit hit:response.getHits()){
            indexLists.add(String.valueOf(hit.getSource().get(YearbookIndexKey.INDEX_NAME)));
        }
        HashSet hash = new HashSet(indexLists);
        indexLists.clear();
        indexLists.addAll(hash);  //删除掉cityList里面重复数据
        return indexLists;
    }

    /**
     * 精确查找
     * 通过 ES 查询（通过城市、年份、指标查询，不通过搜索框）
     */
    @Override
    public List<YearbookResult> queryByYearCityIndex(YearbookSearch yearbookSearch){
        StringBuffer CityArr = new StringBuffer();
        StringBuffer YearArr = new StringBuffer();
        StringBuffer IndexArr = new StringBuffer();

        String[] Cityarr = yearbookSearch.getCitytr().split("\\s+");
        for (int i=0;i<Cityarr.length;i++){
            if(i>0){
                CityArr.append(",");
            }
            CityArr.append("'").append(Cityarr[i]).append("'");
        }

        String[] Yeararr = yearbookSearch.getYear().split("\\s+");
        for (int j=0;j<Yeararr.length;j++){
            if(j>0){
                YearArr.append(",");
            }
            YearArr.append("'").append(Yeararr[j]).append("'");
        }

        String[] Indexarr = yearbookSearch.getIndex_name().split("\\s+");
        for (int k=0;k<Indexarr.length;k++){
            if(k>0){
                IndexArr.append(",");
            }
            IndexArr.append("'").append(Indexarr[k]).append("'");
        }

        String whereExpress = "citytr in(" + CityArr +")" +
                " and " + "year in(" + YearArr +")" +
                " and " + "index_name in(" + IndexArr +")";
        SearchResponse response = searchBySql(whereExpress);

        List<YearbookResult> result=new ArrayList<YearbookResult>();
        //将搜索到的数据放入List
        for(SearchHit hit:response.getHits()){
            YearbookResult pointMap = new YearbookResult();
            pointMap.setCitytr(String.valueOf(hit.getSource().get(YearbookIndexKey.CITYTR)));
            pointMap.setDistinct(String.valueOf(hit.getSource().get(YearbookIndexKey.DISTINCT)));
            pointMap.setCountytr(String.valueOf(hit.getSource().get(YearbookIndexKey.COUNTYTR)));
            pointMap.setProvincetr(String.valueOf(hit.getSource().get(YearbookIndexKey.PROVINCETR)));
            pointMap.setStatisticNum(String.valueOf(hit.getSource().get(YearbookIndexKey.STATISTIC_NUM)));
            pointMap.setUnit(String.valueOf(hit.getSource().get(YearbookIndexKey.UNIT)));
            pointMap.setIndexName(String.valueOf(hit.getSource().get(YearbookIndexKey.INDEX_NAME)));
            pointMap.setYear(String.valueOf(hit.getSource().get(YearbookIndexKey.YEAR)));
            pointMap.setFrom(String.valueOf(hit.getSource().get(YearbookIndexKey.FROM)));
            result.add(pointMap);
        }
        return result;

    }

    //es搜索方法
    public SearchResponse searchResponse(YearbookSearch yearbookSearch){
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        //通过城市查询
        if(yearbookSearch.getCitytr()!=null){
            boolQuery.must(
                    QueryBuilders.matchQuery(YearbookIndexKey.CITYTR,
                            yearbookSearch.getCitytr()
                    )
            );
        }

        //通过年份查询
        if(yearbookSearch.getYear()!=null){
            boolQuery.must(
                    QueryBuilders.matchQuery(YearbookIndexKey.YEAR,yearbookSearch.getYear())
            );
        }

        //通过指标值查询
        if(yearbookSearch.getIndex_name()!=null){
            boolQuery.must(
                    QueryBuilders.matchQuery(YearbookIndexKey.INDEX_NAME,yearbookSearch.getIndex_name())
            );

        }

        //通过搜索框关键字查询
        if(yearbookSearch.getKeywords() != null && !yearbookSearch.getKeywords().isEmpty()){

            boolQuery.must(
                    QueryBuilders.multiMatchQuery(yearbookSearch.getKeywords(),
                            YearbookIndexKey.CITYTR,
                            YearbookIndexKey.INDEX_NAME,
                            YearbookIndexKey.YEAR,
                            YearbookIndexKey.PROVINCETR,
                            YearbookIndexKey.STATISTIC_NUM,
                            YearbookIndexKey.TAG,
                            YearbookIndexKey.UNIT
                    ).operator(Operator.AND).type("cross_fields"));

        }

        SearchRequestBuilder requestBuilder = this.esClient.prepareSearch(INDEX_NAME)
                .setTypes(INDEX_TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)  //当数据量足够多时可以用 QUERY_THEN_FETCH
                .setQuery(boolQuery)
                .setFrom(yearbookSearch.getStart())
                .setSize(20000);
        logger.debug(requestBuilder.toString());
        SearchResponse response = requestBuilder.get();
        return response;

    }

    /**
     * 选择省份对应的城市
     */
    @Override
    public List<String> getCitybyProvince(String province){
        if(province == null)
            return null;

        List<CityDatas> allList = cityDatasRepository.findAllByProvince(province);
        List<String> cityList = new ArrayList<>();
        for(int i=0;i<allList.size();i++){
            cityList.add(allList.get(i).getCity());
        }
        HashSet h = new HashSet(cityList);
        cityList.clear();
        cityList.addAll(h);  //删除掉cityList里面重复数据

        return cityList;

    }

    /**
     * 选择城市对应的区
     */
    @Override
    public List<String> getRegionbycity(String city){
        if(city == null)
            return null;

        List<CityDatas> allList = cityDatasRepository.findAllByCity(city);
        List<String> regionList = new ArrayList<>();
        for(int i=0;i<allList.size();i++){
            regionList.add(allList.get(i).getCountytr());
        }

        return regionList;
    }

    /**
     *sql查询
     * whereExpress 查询条件
     * whereExpress-查询条件:(f1=2 and f2=1) or (f3=1 and f4=1)
     */
    public SearchResponse searchBySql(String whereExpress){
        try{
            // 转换Elasticsearch格式的查询条件
            QueryBuilder queryBuilder = createQueryBuilderByWhere(whereExpress);
            //查询具体信息
            SearchRequestBuilder requestBuilder = this.esClient.prepareSearch(INDEX_NAME)
                    .setTypes(INDEX_TYPE)
                    .setQuery(queryBuilder)
                    .setFrom(0)
                    .setSize(20000);
            SearchResponse response = requestBuilder.get();
            logger.debug(requestBuilder.toString());
            return response;

        }catch (Exception e){
            logger.warn("EsQueryUtil.seatchTotalByApi-Exception{}", e);
        }
        return null;
    }


    /**r
     * sql转化公共方法--主要针对mysql
     * 根据表达式组装 ES 的 query 查询语句
     * whereExpress-查询条件:(f1=2 and f2=1) or (f3=1 and f4=1)
     */
    public static QueryBuilder createQueryBuilderByWhere(String whereExpress){
        BoolQueryBuilder boolQuery = null;
        try{
            String sql = "select * from " + INDEX_NAME;
            String whereTemp = "";
            if(!StringUtils.isEmpty(whereExpress)){
                whereTemp = " where " + whereExpress;
            }
            SQLQueryExpr sqlExpr = (SQLQueryExpr)toSqlExpr(sql + whereTemp);
            SqlParser sqlParser = new SqlParser();
            MySqlSelectQueryBlock query = (MySqlSelectQueryBlock) sqlExpr.getSubQuery().getQuery();
            WhereParser whereParser = new WhereParser(sqlParser,query);
            Where where = whereParser.findWhere();
            if(where!=null){
                boolQuery = QueryMaker.explan(where);
            }

        }catch (SqlParseException e){
            logger.warn("EsQueryUtil.createQueryBuilderByExpress-Exception", e);
        }
        return boolQuery;

    }

    /**
     * 验证sql
     * @param sql sql查询语句
     * @return
     */
    private static SQLExpr toSqlExpr(String sql){
        SQLExprParser parser = new ElasticSqlExprParser(sql);
        SQLExpr expr = parser.expr();

        if (parser.getLexer().token() != Token.EOF) {
            throw new ParserException("illegal sql expr : " + sql);
        }
        return expr;

    }



}
