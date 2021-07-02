package com.tobdev.qywxinner.mapper;

import com.tobdev.qywxinner.model.entity.QywxInnerDepartment;
import org.apache.ibatis.annotations.Param;


public interface QywxInnerDepartmentMapper {

    QywxInnerDepartment  getDepartmentByCorpId(@Param("corp_id") String corpId);

    Integer  saveDepartment(QywxInnerDepartment Department);

    Integer deleteDepartmentByCorpId(@Param("corp_id") String corpId);

}
