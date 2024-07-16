package com.tunanc.filetemplate.service;

import com.tunanc.filetemplate.vo.*;

public interface TemplateService {



    GetHtmlRespVO getHtml(GetHtmlReqVO getHtmlReqVO);


    /**
     * 获取pdf
     * @param reqVO
     * @return
     */
    GetPdfRespVO getPdf(GetPdfReqVO reqVO);
    /**
     * 处理参数
     * @param reqVO
     * @return
     */
    HandleParamsRespVO handleParams(HandleParamsReqVO reqVO);
}
