package com.yunhua.dao;

import com.alibaba.fastjson.JSON;
import com.yunhua.constant.KafkaConstant;
import com.yunhua.mapper.WebLogMapper;
import com.yunhua.domain.WebLog;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class WebLogDao {

    @Autowired
    private WebLogMapper webLogMapper;

    @Autowired
    private RestHighLevelClient highLevelClient;

    public int insertWebLog(WebLog webLog){
        int insertStatus = webLogMapper.insert(webLog);
        return insertStatus;
    }

    /**
     * 将weblog插入到ES中
     * @param webLog
     * @throws IOException
     */

    public void insertWebLogIntoES(WebLog webLog , String index) throws IOException {

        if (isExistInESById(webLog.getId(),index)){
            return;
        }

        IndexRequest addIndexRequest = new IndexRequest(index);

        addIndexRequest.id(webLog.getId());
        addIndexRequest.source(JSON.toJSONString(webLog), XContentType.JSON);
        IndexResponse addIndexResponse = highLevelClient.index(addIndexRequest, RequestOptions.DEFAULT);
        System.out.println(addIndexResponse);
    }


    /**
     * 根据id去ES中查询该文档是否存在
     * @param id
     * @return
     * @throws IOException
     */

    public boolean isExistInESById(String id , String index) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        IdsQueryBuilder idsQueryBuilder = QueryBuilders.idsQuery();
        idsQueryBuilder.addIds(id);

        sourceBuilder.query(idsQueryBuilder);

        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        return searchResponse.getHits().iterator().hasNext();
    }

    /**
     * 根据用户Id查看该用户的所有WebLog
     * @param userId
     * @return
     * @throws IOException
     */

    public List<WebLog> listWebLogByUserId(long userId , String index) throws IOException{
        List<WebLog> webLogList = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        sourceBuilder.query(QueryBuilders.termQuery("userId",userId));
        searchRequest.source(sourceBuilder);
        SearchResponse response = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit hit : response.getHits()) {
            String weblogJSON = hit.getSourceAsString();
            WebLog webLog = JSON.parseObject(weblogJSON, WebLog.class);
            webLogList.add(webLog);
        }
        return webLogList;
    }

    /**
     * 根据用户Id分页查询该用户的所有日志
     * @param pageNum
     * @param pageSize
     * @param userId
     * @param index
     * @return
     * @throws IOException
     */

    public List<WebLog> listWebLogPageByUserId(int pageNum , int pageSize ,long userId , String index) throws IOException{
        List<WebLog> webLogList = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        sourceBuilder.query(QueryBuilders.termQuery("userId",userId));
        sourceBuilder.from( (pageNum-1) * pageSize );
        sourceBuilder.size(pageSize);
        searchRequest.source(sourceBuilder);
        SearchResponse response = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit hit : response.getHits()) {
            String weblogJSON = hit.getSourceAsString();
            WebLog webLog = JSON.parseObject(weblogJSON, WebLog.class);
            webLogList.add(webLog);
        }
        return webLogList;
    }


    /**
     * 根据状态码code来分页查找所有的日志
     * @param pageNum
     * @param pageSize
     * @param code
     * @param index
     * @return
     * @throws IOException
     */

    public List<WebLog> listWebLogPageByCode(int pageNum , int pageSize ,int code , String index) throws IOException{
        List<WebLog> webLogList = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        sourceBuilder.query(QueryBuilders.termQuery("code",code));
        sourceBuilder.from( (pageNum-1) * pageSize );
        sourceBuilder.size(pageSize);
        searchRequest.source(sourceBuilder);
        SearchResponse response = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit hit : response.getHits()) {
            String weblogJSON = hit.getSourceAsString();
            WebLog webLog = JSON.parseObject(weblogJSON, WebLog.class);
            webLogList.add(webLog);
        }
        return webLogList;
    }


    /**
     * 根据用户Id和状态码Code分页查询日志
     * @param pageNum
     * @param pageSize
     * @param userId
     * @param code
     * @param index
     * @return
     * @throws IOException
     */

    public List<WebLog> listWebLogPageByUserIdAndCode(int pageNum , int pageSize ,long userId, int code , String index) throws IOException{
        List<WebLog> webLogList = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        TermQueryBuilder queryBuilderCode = QueryBuilders.termQuery("code", code);
        TermQueryBuilder queryBuilderUserId = QueryBuilders.termQuery("userId", userId);
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(queryBuilderCode);
        boolQueryBuilder.must(queryBuilderUserId);


        sourceBuilder.query(boolQueryBuilder);
        sourceBuilder.from( (pageNum-1) * pageSize );
        sourceBuilder.size(pageSize);
        searchRequest.source(sourceBuilder);
        SearchResponse response = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit hit : response.getHits()) {
            String weblogJSON = hit.getSourceAsString();
            WebLog webLog = JSON.parseObject(weblogJSON, WebLog.class);
            webLogList.add(webLog);
        }
        return webLogList;
    }




    /**
     * 分页查询所有日志
     * @param pageNum
     * @param pageSize
     * @param index
     * @return
     * @throws IOException
     */

    public List<WebLog> listAllWebLogPage(int pageNum , int pageSize , String index) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        List<WebLog> webLogList = new ArrayList<>();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from( (pageNum-1) * pageSize );
        sourceBuilder.size(pageSize);

        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit hit : searchResponse.getHits()) {
            String weblogJSON = hit.getSourceAsString();
            WebLog webLog = JSON.parseObject(weblogJSON, WebLog.class);
            webLogList.add(webLog);
        }
        return webLogList;
    }

}
