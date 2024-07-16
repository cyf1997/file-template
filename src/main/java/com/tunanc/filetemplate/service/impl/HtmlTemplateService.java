package com.tunanc.filetemplate.service.impl;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.tunanc.filetemplate.Exception.FileTemplateException;
import com.tunanc.filetemplate.constant.TemplateConstant;
import com.tunanc.filetemplate.entity.FileTemplateScript;
import com.tunanc.filetemplate.service.TemplateTypeService;
import com.tunanc.filetemplate.uitls.FileFieldUtil;
import com.tunanc.filetemplate.uitls.GroovyUtil;
import com.tunanc.filetemplate.uitls.PathUtil;
import com.tunanc.filetemplate.uitls.PdfUtil;
import com.tunanc.filetemplate.vo.FileTemplateDetail;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Service(TemplateConstant.TEMPLATE_TYPE_HTML+TemplateConstant.TEMPLATE_TYPE_SERVICE_SUFFIX)
public class HtmlTemplateService implements TemplateTypeService {
    @Override
    public File doFillInFields(Map<String, Object> params, FileTemplateDetail templateDetail){
        log.info("【HTML模板】[填充字段]入参：params:{}",params);
        String templateHtml = templateDetail.getFileTemplate().getTemplateHtml();

        FileTemplateScript templateScript = templateDetail.getTemplateScript();
        Map<String, Object> stringObjectMap = FileFieldUtil.paramAdapterToMap(params, templateDetail.getParamMap());
        if (templateScript!=null && StrUtil.isNotEmpty(templateScript.getScript())){
            //调用脚本处理难以在业务代码中实现的字段逻辑
            GroovyUtil.eval(templateScript.getScript(),templateScript.getId(),new Object[]{params,stringObjectMap});
        }
        //html占位符替换
        String filledHtml = StrUtil.format(templateHtml, stringObjectMap);
        String templateDirPath= PathUtil.concatDir(FileUtils.getUserDirectoryPath(),TemplateConstant.TEMP_DIRECTORY);
        String processedFileName = templateDirPath+ IdUtil.getSnowflakeNextIdStr()+".html";
        File file = new File(processedFileName);
        try {
            FileUtils.writeStringToFile(file,filledHtml, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("【HTML模板】写入文件异常", e);
            throw new FileTemplateException("HTML模板文件写入异常");
        }
        return file;
    }

    @Override
    public Map<String, Object> doFillInFieldsReturnMap(Map<String, Object> params, FileTemplateDetail templateDetail) {
        FileTemplateScript templateScript = templateDetail.getTemplateScript();
        Map<String, Object> stringObjectMap = FileFieldUtil.paramAdapterToMap(params, templateDetail.getParamMap());
        if (templateScript!=null && StrUtil.isNotEmpty(templateScript.getScript())){
            //调用脚本处理难以在业务代码中实现的字段逻辑
            GroovyUtil.eval(templateScript.getScript(),templateScript.getId(),new Object[]{params,stringObjectMap});
        }
        return stringObjectMap;
    }

    @Override
    public String convert2Html(File file, FileTemplateDetail templateDetail) {
        log.error("【HTML模板】[转换html]开始");
        String str;
        try {
            str = FileUtils.readFileToString(file,StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("【HTML模板】写入文件异常", e);
            throw new FileTemplateException("HTML读取文件异常");
        }
        return str;
    }

    @Override
    public File convert2Pdf(File file) {
        String str;
        try {
            str = FileUtils.readFileToString(file,StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("【HTML模板】[转换pdf]读取文件异常", e);
            throw new FileTemplateException("HTML读取文件异常");
        }
        ByteArrayOutputStream byteArrayOutputStream = PdfUtil.htmlToPdf(str);
        Assert.notNull(byteArrayOutputStream, "HTML转PDF失败");
        byte[] bytes = byteArrayOutputStream.toByteArray();
        String templateDirPath= PathUtil.concatDir(FileUtils.getUserDirectoryPath(),TemplateConstant.TEMP_DIRECTORY);
        File pdfFile = new File(templateDirPath + IdUtil.getSnowflakeNextIdStr() + ".pdf");
        try {
            FileUtils.writeByteArrayToFile(pdfFile,bytes);
        } catch (IOException e) {
            log.error("【HTML模板】[转换pdf]写入文件异常", e);
            throw new FileTemplateException("HTML写入文件异常");
        }
        return pdfFile;
    }
}
