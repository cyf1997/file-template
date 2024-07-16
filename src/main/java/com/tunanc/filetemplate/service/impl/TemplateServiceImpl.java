package com.tunanc.filetemplate.service.impl;

import com.tunanc.filetemplate.Exception.FileTemplateException;
import com.tunanc.filetemplate.entity.FileTemplate;
import com.tunanc.filetemplate.service.IFileTemplateService;
import com.tunanc.filetemplate.service.TemplateService;
import com.tunanc.filetemplate.service.TemplateTypeService;
import com.tunanc.filetemplate.service.TemplateTypeStrategy;
import com.tunanc.filetemplate.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.Map;

@Slf4j
@Service
public class TemplateServiceImpl implements TemplateService {

    @Resource
    private IFileTemplateService fileTemplateService;

    @Resource
    private TemplateTypeStrategy templateTypeStrategy;

    @Override
    public GetHtmlRespVO getHtml(GetHtmlReqVO getHtmlReqVO){
        log.info("html替换参数入参:{}",getHtmlReqVO);
        String templateCode = getHtmlReqVO.getTemplateCode();
        //查询合同模板相关信息
        FileTemplateDetail templateDetail = fileTemplateService.getTemplateInfoDetail(templateCode);
        FileTemplate fileTemplate = templateDetail.getFileTemplate();

//        String templateName = fileTemplate.getTemplateName() + "." + TemplateTypeEnum.getSuffixByKey(fileTemplate.getTemplateFormat());
        //获取模板填充数据
        TemplateTypeService templateTypeService = templateTypeStrategy.getService(fileTemplate.getTemplateFormat());
        //上传文件
        File file = templateTypeService.doFillInFields(getHtmlReqVO.getParams(), templateDetail);
        //转换成html
        String string = templateTypeService.convert2Html(file, templateDetail);
        GetHtmlRespVO getHtmlRespVO = new GetHtmlRespVO();
        getHtmlRespVO.setTemplateFormat(fileTemplate.getTemplateFormat());
        getHtmlRespVO.setTemplateName(fileTemplate.getTemplateName());
        getHtmlRespVO.setHtmlContent(string);
        //根据文件转换html
        return getHtmlRespVO;
    }




    @Override
    public GetPdfRespVO getPdf(GetPdfReqVO reqVO) {
        log.info("获取pdf入参:{}",reqVO);
        String templateCode = reqVO.getTemplateCode();
        //查询合同模板相关信息
        FileTemplateDetail templateDetail = fileTemplateService.getTemplateInfoDetail(templateCode);
        FileTemplate fileTemplate = templateDetail.getFileTemplate();


        //获取模板填充数据
        TemplateTypeService templateTypeService = templateTypeStrategy.getService(fileTemplate.getTemplateFormat());
        //上传文件
        File file = templateTypeService.doFillInFields(reqVO.getParams(), templateDetail);

        File pdfFile = templateTypeService.convert2Pdf(file);
        log.info("生成完成的pdf路径：{}",pdfFile.getAbsolutePath());
        GetPdfRespVO getPdfRespVO = new GetPdfRespVO();
        if (reqVO.isNeedPdfContent()){
            try {
                getPdfRespVO.setPdfContent(FileUtils.readFileToByteArray(pdfFile));
            } catch (Exception e) {
                log.error("获取file的字节数组报错:{}",e.toString());
                throw new FileTemplateException("获取file的字节数组报错");
            }
        }
        return getPdfRespVO;
    }

    @Override
    public HandleParamsRespVO handleParams(HandleParamsReqVO reqVO) {
        String templateCode = reqVO.getTemplateCode();
        //查询合同模板相关信息
        FileTemplateDetail templateDetail = fileTemplateService.getTemplateInfoDetail(templateCode);
        FileTemplate fileTemplate = templateDetail.getFileTemplate();

        //获取模板填充数据
        TemplateTypeService templateTypeService = templateTypeStrategy.getService(fileTemplate.getTemplateFormat());
        Map<String, Object> stringObjectMap = templateTypeService.doFillInFieldsReturnMap(reqVO.getBizParamsMap(), templateDetail);
        HandleParamsRespVO handleParamsRespVO = new HandleParamsRespVO();
        handleParamsRespVO.setHandledMap(stringObjectMap);
        return handleParamsRespVO;
    }
}
