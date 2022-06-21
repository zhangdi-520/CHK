package com.yunhua;

import com.alibaba.fastjson.JSON;
import com.yunhua.constant.KafkaConstant;
import com.yunhua.domain.WebLog;
import io.swagger.annotations.Authorization;
import net.sf.jsqlparser.statement.create.table.Index;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.text.Highlighter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChkLogApplication.class)
class ChkLogApplicationTests {

    @Autowired
    private RestHighLevelClient highLevelClient;

    @Test
    void createIndexTest() throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(KafkaConstant.WEBLOG);
        CreateIndexResponse createIndexResponse = highLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse);
    }

    @Test
    void deleteIndexTest() throws IOException{
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("index6");
        AcknowledgedResponse deleteIndexResponse = highLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        System.out.println(deleteIndexResponse);
    }

    @Test
    void addDocumentTest() throws IOException{
        Book book = new Book(10003, "美好", 2.3, new Date());

        IndexRequest addIndexRequest = new IndexRequest("index6");

        addIndexRequest.id(book.getBookId()+"");
        addIndexRequest.source(JSON.toJSONString(book), XContentType.JSON);
        IndexResponse addIndexResponse = highLevelClient.index(addIndexRequest, RequestOptions.DEFAULT);
        System.out.println(addIndexResponse);
    }


    @Test
    void searchDocumentById() throws IOException{

        SearchRequest searchRequest = new SearchRequest();

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        IdsQueryBuilder idsQueryBuilder = QueryBuilders.idsQuery();
        idsQueryBuilder.addIds("ba3bf510f43e4c4cbd409f18209675a8");
        sourceBuilder.query(idsQueryBuilder);


        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        System.out.println(searchResponse.getHits().iterator().hasNext());
        for (SearchHit hit : searchResponse.getHits()) {
            System.out.println(hit.getSourceAsMap());
        }

    }

    @Test
    void searchDocumentByUserIdTest() throws IOException{
        List<WebLog> webLogList = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest("weblog");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        sourceBuilder.query(QueryBuilders.termsQuery("userId","2123"));

        searchRequest.source(sourceBuilder);
        SearchResponse response = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit hit : response.getHits()) {
            String weblogJSON = hit.getSourceAsString();
            WebLog webLog = JSON.parseObject(weblogJSON, WebLog.class);
            webLogList.add(webLog);
        }
        System.out.println(webLogList);
    }


    @Test
    void searchDocumentTest() throws IOException{
        int pageNum = 1;
        int pageSize = 1;

        SearchRequest searchRequest = new SearchRequest();


        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.matchQuery("bookName","声音"));

        searchSourceBuilder.from( (pageNum-1) * pageSize );
        searchSourceBuilder.size(pageSize);


        HighlightBuilder highlightBuilder = new HighlightBuilder();

        highlightBuilder.field(new HighlightBuilder.Field("bookName"));
        highlightBuilder.preTags("<label style = 'color:red' >");
        highlightBuilder.postTags("</label>");

        searchSourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        for (SearchHit hit : searchResponse.getHits()) {
            System.out.println(hit);
        }


        //System.out.println(searchResponse);

    }

}
