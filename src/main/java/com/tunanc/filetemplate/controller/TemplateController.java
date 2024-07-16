package com.tunanc.filetemplate.controller;


import com.tunanc.filetemplate.service.TemplateService;
import com.tunanc.filetemplate.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 模板处理控制器
 */
@RestController
@Slf4j
@RequestMapping("/template")
public class TemplateController {

    @Resource
    private TemplateService templateService;


    /**
     * 获取html
     * @param getHtmlReqVO
     * @return
     */
    @PostMapping("getHtml")
    public GetHtmlRespVO getHtml(@RequestBody GetHtmlReqVO getHtmlReqVO){
        return templateService.getHtml(getHtmlReqVO);
    }



    /**
     * 获取PDF
     * @param reqVO
     * @return
     */
    @PostMapping("getPdf")
    public GetPdfRespVO getPdf(@Validated @RequestBody GetPdfReqVO reqVO){
        return templateService.getPdf(reqVO);
    }

    /**
     * 处理参数
     * @param reqVO
     * @return
     */
    @PostMapping("handleParams")
    public HandleParamsRespVO handleParams(@Validated @RequestBody HandleParamsReqVO reqVO){
        return templateService.handleParams(reqVO);
    }
}
