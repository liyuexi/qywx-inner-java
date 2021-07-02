package com.tobdev.qywxinner.service;

import com.tobdev.qywxinner.config.QywxInnerConfig;
import com.tobdev.qywxinner.mapper.QywxInnerDepartmentMapper;
import com.tobdev.qywxinner.model.entity.QywxInnerDepartment;
import com.tobdev.qywxinner.service.QywxInnerDepartmentService;
import com.tobdev.qywxinner.service.QywxInnerService;
import com.tobdev.qywxinner.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class QywxInnerDepartmentService  {

    private final static Logger logger = LoggerFactory.getLogger("test");

    @Autowired
    private QywxInnerDepartmentMapper qywxInnerDepartmentMapper;

    @Autowired
    private QywxInnerConfig qywxInnerConfig;
    @Autowired
    private RedisUtils strRedis;
    @Autowired
    private QywxInnerService qywxInnerService;


    public QywxInnerDepartment getDepartmentByCorpId(String corpId) {
        return qywxInnerDepartmentMapper.getDepartmentByCorpId(corpId);
    }

    public Integer saveDepartment(QywxInnerDepartment Department){
        return qywxInnerDepartmentMapper.saveDepartment(Department);
    }

    public Boolean deleteDepartmentByCorpId(String corpId){
        Integer result = qywxInnerDepartmentMapper.deleteDepartmentByCorpId(corpId);
        return  false;
    }


}
