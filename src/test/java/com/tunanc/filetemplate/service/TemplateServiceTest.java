package com.tunanc.filetemplate.service;

import com.tunanc.filetemplate.FileTemplateApplication;
import com.tunanc.filetemplate.vo.GetHtmlReqVO;
import com.tunanc.filetemplate.vo.GetHtmlRespVO;
import com.tunanc.filetemplate.vo.GetPdfReqVO;
import com.tunanc.filetemplate.vo.GetPdfRespVO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = FileTemplateApplication.class)
@RunWith(SpringRunner.class)
class TemplateServiceTest {

    @Autowired
    private TemplateService templateService;


    @Test
    void getHtml() {
        GetHtmlReqVO getHtmlReqVO = new GetHtmlReqVO();
        getHtmlReqVO.setTemplateCode("10001");
        getHtmlReqVO.setParams(Map.of("名字","张三"));
        GetHtmlRespVO html = templateService.getHtml(getHtmlReqVO);
        System.out.printf(html.toString());
    }

    @Test
    void getPdf() {
        GetPdfReqVO getPdfReqVO = new GetPdfReqVO();
        getPdfReqVO.setTemplateCode("10001");
        getPdfReqVO.setParams(Map.of("名字","张三"));
        GetPdfRespVO pdf = templateService.getPdf(getPdfReqVO);
    }

    @Test
    void handleParams() {
    }
}