package com.tobdev.qywxinner.service;

import com.alibaba.fastjson.JSONObject;
import com.tobdev.qywxinner.config.QywxInnerConfig;
import com.tobdev.qywxinner.mapper.QywxInnerCompanyMapper;
import com.tobdev.qywxinner.model.entity.QywxInnerCompany;
import com.tobdev.qywxinner.service.QywxInnerCompanyService;
import com.tobdev.qywxinner.service.QywxInnerService;
import com.tobdev.qywxinner.utils.QywxSHA;
import com.tobdev.qywxinner.utils.RedisUtils;
import com.tobdev.qywxinner.utils.RestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class QywxInnerCompanyService  {

    private final static Logger logger = LoggerFactory.getLogger("test");

    @Autowired
    private QywxInnerCompanyMapper qywxInnerCompanyMapper;

    @Autowired
    private QywxInnerDepartmentService qywxInnerDepartmentService;
    @Autowired
    private QywxInnerUserService qywxInnerUserService;


    @Autowired
    private QywxInnerConfig qywxInnerConfig;
    @Autowired
    private RedisUtils strRedis;
    @Autowired
    private QywxInnerService qywxInnerService;


    public QywxInnerCompany getCompanyByCorpId(String corpId) {
        return qywxInnerCompanyMapper.getCompanyByCorpId(corpId);
    }


    public Integer saveCompany(QywxInnerCompany company){
        return qywxInnerCompanyMapper.saveCompany(company);
    }




    public Boolean deleteCompanyByCorpId(String corpId){
        //删除公司
        Integer comResult =   qywxInnerCompanyMapper.deleteCompanyByCorpId(corpId);

        //删除部门
        Boolean deptResult =   qywxInnerDepartmentService.deleteDepartmentByCorpId(corpId);
        //删除人
        Boolean usrResult =   qywxInnerUserService.deleteUserByCorpId(corpId);

        if(comResult>=0){
            return  true;
        }

        return  false;
    }







}
