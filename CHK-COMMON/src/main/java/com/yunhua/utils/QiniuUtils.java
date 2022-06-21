package com.yunhua.utils;

import com.alibaba.fastjson.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class QiniuUtils {

    private String accessKey = "dJQjxDTOw19cc-9lw0lXv1dY6xg9aXSSv3MgDcJo";
    private String secretKey = "4zoMgZFXns4skpKUH_ZYDJRIr_IQ_lQG65Bvqvnt";
    public String url = "http://rdm2fhuww.hd-bkt.clouddn.com/";

    public boolean upload(MultipartFile file, String filename) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huadong());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传

        String bucket = "chk";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        try {
            byte[] uploadBytes = file.getBytes();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            Response response = uploadManager.put(uploadBytes, filename, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(),DefaultPutRet.class);
            return true;
        } catch (Exception ex) {
                ex.printStackTrace();
        }
        return false;
    }

    public boolean delete(String filename){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        //...其他参数参考类注释
//        String accessKey = "your access key";
//        String secretKey = "your secret key";
        String bucket = "chk";
        String key = "your file key";
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            Response delete = bucketManager.delete(bucket, filename);
            return true;
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
        return false;
    }

    public boolean deleteBatch(String[] fileNameArr){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        //...其他参数参考类注释
//        String accessKey = "your access key";
//        String secretKey = "your secret key";
        String bucket = "chk";
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            //单次批量请求的文件数量不得超过1000

            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(bucket, fileNameArr);
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < fileNameArr.length; i++) {
                BatchStatus status = batchStatusList[i];
                String key = fileNameArr[i];
                System.out.print(key + "\t");
                if (status.code == 200) {
                    System.out.println("delete success");
                    return true;
                } else {
                    //删除失败需要考虑回退
                    System.out.println(status.data.error);
                }
            }
        } catch (QiniuException ex) {
            System.err.println(ex.response.toString());
        }
        return false;
    }


}
