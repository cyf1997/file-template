package com.tunanc.filetemplate.service.impl;


import com.tunanc.filetemplate.Exception.FileTemplateException;
import com.tunanc.filetemplate.constant.TemplateConstant;
import com.tunanc.filetemplate.entity.FileTemplateScript;
import com.tunanc.filetemplate.service.TemplateTypeService;
import com.tunanc.filetemplate.uitls.FileFieldUtil;
import com.tunanc.filetemplate.uitls.GroovyUtil;
import com.tunanc.filetemplate.vo.FileTemplateDetail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

@Slf4j
@Service(TemplateConstant.TEMPLATE_TYPE_PDF+TemplateConstant.TEMPLATE_TYPE_SERVICE_SUFFIX)
public class PdfTemplateService implements TemplateTypeService {
    @Override
    public File doFillInFields(Map<String, Object> params, FileTemplateDetail templateDetail) {
        throw new FileTemplateException("pdf模板不支持此方法，请先实现");
    }

    @Override
    public Map<String, Object> doFillInFieldsReturnMap(Map<String, Object> params, FileTemplateDetail templateDetail) {
        FileTemplateScript templateScript = templateDetail.getTemplateScript();
        Map<String, Object> stringObjectMap = FileFieldUtil.paramAdapterToMap(params, templateDetail.getParamMap());
        if (templateScript!=null && StringUtils.isNotEmpty(templateScript.getScript())){
            //调用脚本处理难以在业务代码中实现的字段逻辑
            GroovyUtil.eval(templateScript.getScript(),templateScript.getId(),new Object[]{params,stringObjectMap});
        }
        return stringObjectMap;
    }

    @Override
    public String convert2Html(File file, FileTemplateDetail templateDetail) {
        throw new FileTemplateException("pdf模板不支持此方法，请先实现");
    }

    @Override
    public File convert2Pdf(File file) {
        throw new FileTemplateException("pdf模板不支持此方法，请先实现");
    }
}
