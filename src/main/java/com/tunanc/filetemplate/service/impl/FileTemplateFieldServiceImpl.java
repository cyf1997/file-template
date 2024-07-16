package com.tunanc.filetemplate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tunanc.filetemplate.entity.FileTemplateField;
import com.tunanc.filetemplate.mapper.FileTemplateFieldMapper;
import com.tunanc.filetemplate.service.IFileTemplateFieldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 模板字段映射表 业务层实现类
 *
 */
@Service
@Slf4j
public class FileTemplateFieldServiceImpl extends ServiceImpl<FileTemplateFieldMapper, FileTemplateField> implements IFileTemplateFieldService {



    @Override
    public Map<String, FileTemplateField> queryParamBeanMapping(String templateId) {
        List<FileTemplateField> list = queryByTemplateId(templateId);
        return list.stream().collect(Collectors.toMap(FileTemplateField::getTemplateKey, Function.identity()));
    }

    private List<FileTemplateField> queryByTemplateId(String templateId) {
        LambdaQueryWrapper<FileTemplateField> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FileTemplateField::getTemplateId,templateId);
        return list(queryWrapper);
    }


}