package com.tobdev.qywxinner.service;

import com.tobdev.qywxinner.mapper.QywxInnerUserMapper;
import com.tobdev.qywxinner.model.entity.QywxInnerUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QywxInnerUserService  {

    @Autowired
    private QywxInnerUserMapper qywxInnerUserMapper;


    public QywxInnerUser getUserByCorpIdAndUserId(String corpId,String userId) {
        QywxInnerUser user =  qywxInnerUserMapper.getUserByCorpIdAndUserId(corpId, userId);
        if(user == null) return  null;

        return user;
    }

    public Integer saveUser(QywxInnerUser user) {
        return  qywxInnerUserMapper.saveUser(user);
    }


    public Boolean deleteUserByCorpId(String corpId){
        Integer rs =   qywxInnerUserMapper.deleteUserByCorpId(corpId);
        if(rs>=0){
            return true;
        }
        return  false;
    }

}
