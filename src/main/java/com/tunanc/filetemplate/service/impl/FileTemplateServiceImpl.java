package com.tunanc.filetemplate.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tunanc.filetemplate.Exception.FileTemplateException;
import com.tunanc.filetemplate.entity.FileTemplate;
import com.tunanc.filetemplate.entity.FileTemplateField;
import com.tunanc.filetemplate.entity.FileTemplateScript;
import com.tunanc.filetemplate.mapper.FileTemplateMapper;
import com.tunanc.filetemplate.mapper.FileTemplateScriptMapper;
import com.tunanc.filetemplate.service.IFileTemplateFieldService;
import com.tunanc.filetemplate.service.IFileTemplateService;
import com.tunanc.filetemplate.vo.FileTemplateDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 文件模版表 业务层实现类
 *
 */
@Service
@Slf4j
public class FileTemplateServiceImpl extends ServiceImpl<FileTemplateMapper, FileTemplate> implements IFileTemplateService {


    @Resource
    private IFileTemplateFieldService fileTemplateFieldService;

    @Resource
    private FileTemplateScriptMapper fileTemplateScriptMapper;



    @Override
    public FileTemplateDetail getTemplateInfoDetail(String templateCode) {
        if (log.isInfoEnabled()) {
            log.info("根据模板代码获取模板详细数据,入参templateCode：{}", templateCode);
        }
        //根据模板代码查询模板表
        FileTemplate templateInfo = getTemplateInfo(templateCode);
        String templateId = null;
        FileTemplateDetail TFmsTemplateDetail = new FileTemplateDetail();


        Assert.notNull(templateInfo, () -> new FileTemplateException("模板为空"));
        templateId = templateInfo.getId();
        TFmsTemplateDetail.setFileTemplate(templateInfo);

        //根据模板ID查询签署表TEcsEsignSigner
        //获取模板字段映射
        Map<String, FileTemplateField> stringStringMap = fileTemplateFieldService.queryParamBeanMapping(templateId);
        TFmsTemplateDetail.setParamMap(stringStringMap);
        //字段映射不为空才查询脚本tEcsEsignTemplateScriptMapper
        TFmsTemplateDetail.setTemplateScript(new LambdaQueryChainWrapper<>(fileTemplateScriptMapper).eq(FileTemplateScript::getTemplateId, templateId).one());
        return TFmsTemplateDetail;
    }

    private FileTemplate getTemplateInfo(String templateCode) {
        LambdaQueryWrapper<FileTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FileTemplate::getTemplateCode, templateCode);
        return getOne(queryWrapper);
    }


}
