package com.tunanc.filetemplate.service;

import com.tunanc.filetemplate.vo.FileTemplateDetail;

import java.io.File;
import java.util.Map;

public interface TemplateTypeService {


    File doFillInFields(Map<String, Object> params, FileTemplateDetail templateDetail);

    Map<String,Object> doFillInFieldsReturnMap(Map<String, Object> params, FileTemplateDetail templateDetail);

    String convert2Html(File file, FileTemplateDetail templateDetail);

    File convert2Pdf(File file);




}
