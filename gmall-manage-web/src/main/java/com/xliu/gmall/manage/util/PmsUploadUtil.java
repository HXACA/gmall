package com.xliu.gmall.manage.util;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/3/4 17:00
 */
public class PmsUploadUtil {

    public static String uploadImage(MultipartFile multipartFile){
        String imgUrl = "http://192.168.198.128";
        try {
            String path = PmsUploadUtil.class.getResource("/tracker.conf").getPath();
            ClientGlobal.init(path);
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getTrackerServer();
            StorageClient storageClient = new StorageClient(trackerServer,null);
            byte[] bytes = multipartFile.getBytes();
            String originalFilename = multipartFile.getOriginalFilename();
            int index = originalFilename.lastIndexOf(".");
            String ext_name = originalFilename.substring(index+1);
            String[] uploadInfos = storageClient.upload_file(bytes,ext_name,null);
            for (String uploadInfo : uploadInfos) {
                imgUrl+= "/"+uploadInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return imgUrl;
    }
}
