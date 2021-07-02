package com.tobdev.qywxinner.mapper;

import com.tobdev.qywxinner.model.entity.QywxInnerCompany;
import org.apache.ibatis.annotations.Param;


public interface QywxInnerCompanyMapper {

    QywxInnerCompany  getCompanyByCorpId(@Param("corp_id") String corpId);

    Integer  saveCompany(QywxInnerCompany company);

    Integer deleteCompanyByCorpId(@Param("corp_id") String corpId);

}
