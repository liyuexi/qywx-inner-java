package com.tobdev.qywxinner.controller;

import com.alibaba.fastjson.JSONObject;
import com.tobdev.qywxinner.service.QywxInnerService;
import com.tobdev.qywxinner.utils.CommonUtils;
import com.tobdev.qywxinner.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

@RestController
public class MediaController {

    @Autowired
    private QywxInnerService qywxInnerService;



    @RequestMapping({"/media/download"})
    void download(HttpServletRequest request, HttpServletResponse response,@RequestParam("corp_id") String corpId, @RequestParam(name = "media_id") String mediaId) throws IOException {

        byte[]  filebytes =  qywxInnerService.downloadMedia(corpId,mediaId);

        OutputStream out = response.getOutputStream();
        out.write(filebytes);
        out.flush();
        out.close();

    }

    @RequestMapping({"/media/upload"})
    JsonData upload(HttpServletRequest request, @RequestParam("corp_id") String corpId, @RequestParam("media") MultipartFile file){
        //.MaxUploadSizeExceededException: Maximum upload size exceeded; nested exception is java.lang.IllegalStateException: org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException: The field media exceeds its maximum permitted size of 1048576 bytes.]
        //https://blog.csdn.net/ifu25/article/details/90173264
        MultiValueMap<String, Object> params= new LinkedMultiValueMap<>();
        params.add("media",file.getResource());
        //type	是	媒体文件类型，分别有图片（image）、语音（voice）、视频（video），普通文件（file）
        JSONObject resData =   qywxInnerService.uploadMedia(corpId,params,"image");
        return    JsonData.buildSuccess(resData);
    }

    @RequestMapping({"/media/uploadImg"})
    JsonData uploadImg(HttpServletRequest request, @RequestParam("corp_id") String corpId, @RequestParam("media") MultipartFile file){
        //.MaxUploadSizeExceededException: Maximum upload size exceeded; nested exception is java.lang.IllegalStateException: org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException: The field media exceeds its maximum permitted size of 1048576 bytes.]
        //https://blog.csdn.net/ifu25/article/details/90173264
        MultiValueMap<String, Object> params= new LinkedMultiValueMap<>();
        params.add("media",file.getResource());
        JSONObject resData =   qywxInnerService.uploadImg(corpId,params);
        return    JsonData.buildSuccess(resData);
    }

}
