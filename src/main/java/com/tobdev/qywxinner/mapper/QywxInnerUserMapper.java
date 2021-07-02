package com.tobdev.qywxinner.mapper;

import com.tobdev.qywxinner.model.entity.QywxInnerUser;
import org.apache.ibatis.annotations.Param;


public interface QywxInnerUserMapper {

    QywxInnerUser getUserByCorpIdAndUserId(@Param("corp_id") String corpId, @Param("user_id") String userId);

    Integer  saveUser(QywxInnerUser user);

    Integer deleteUser(@Param("user_id") String userId);

    Integer deleteUserByCorpId(@Param("corp_id") String corpId);

}
