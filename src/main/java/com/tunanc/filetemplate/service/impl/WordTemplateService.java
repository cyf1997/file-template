package com.tunanc.filetemplate.service.impl;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.policy.HackLoopTableRenderPolicy;

import com.tunanc.filetemplate.Exception.FileTemplateException;
import com.tunanc.filetemplate.constant.TemplateConstant;
import com.tunanc.filetemplate.entity.FileTemplateField;
import com.tunanc.filetemplate.entity.FileTemplateScript;
import com.tunanc.filetemplate.enums.FieldTypeEnum;
import com.tunanc.filetemplate.service.TemplateTypeService;
import com.tunanc.filetemplate.uitls.GroovyUtil;
import com.tunanc.filetemplate.uitls.PathUtil;
import com.tunanc.filetemplate.uitls.WordUtil;
import com.tunanc.filetemplate.vo.FileTemplateDetail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * word模板服务
 * 目前只支持docx类型的Word
 * @author Yunfei Cheng
 */
@Slf4j
@Service(TemplateConstant.TEMPLATE_TYPE_WORD+TemplateConstant.TEMPLATE_TYPE_SERVICE_SUFFIX)
public class WordTemplateService implements TemplateTypeService {



    private File getTemplateFile(String fileId) {
        try {
            URL resource = ResourceUtil.getResource("template" + File.separator + fileId);
            return ResourceUtils.getFile(resource);
        } catch (FileNotFoundException e) {
            throw new FileTemplateException("模板未找到");
        }
       /* byte[] fileContent = fileDetail.getFileContent();
        String templateDirPath= PathUtil.concatDir(FileUtils.getUserDirectoryPath(),TemplateConstant.TEMP_DIRECTORY);
        //模板临时文件
        String templateTempFileName = templateDirPath + IdUtil.getSnowflakeNextIdStr() + fileDetail.getFileForm();
        log.debug("模板文件存入临时目录：{}",templateTempFileName);
        File templateTempFile = new File(templateTempFileName);
        templateTempFile.getParentFile().setWritable(true,true);
        try {
            FileUtils.writeByteArrayToFile(templateTempFile,fileContent);
        } catch (IOException e) {
            log.error("模板文件写入系统临时目录失败:{}",e.toString());
            throw new FileTemplateException("模板文件写入本地系统失败");
        }finally {
            //程序退出，删除临时文件
            templateTempFile.deleteOnExit();
        }*/
        //TODO 由于demo没有文件服务，所以fileId暂时当做path路径来获取文件，如有文件服务器，请使用相关接口根据文件ID进行获取,并转成File文件
    }



    @Override
    public File doFillInFields(Map<String, Object> params, FileTemplateDetail templateDetail){
        Map<String, FileTemplateField> configuredParamMap = templateDetail.getParamMap();
        FileTemplateScript templateScript = templateDetail.getTemplateScript();
        String fileId = templateDetail.getFileTemplate().getFileId();

        HashMap<String, Object> hashMap = new HashMap<>();
        if (templateScript!=null && StringUtils.isNotEmpty(templateScript.getScript())){
            //调用脚本处理难以在业务代码中实现的字段逻辑
            GroovyUtil.eval(templateScript.getScript(),templateScript.getId(),new Object[]{params,hashMap});
        }


        File templateTempFile = getTemplateFile(fileId);
        ConfigureBuilder configureBuilder = Configure.newBuilder();
        configuredParamMap.forEach((k, v) -> {
            String paramKey = v.getParamKey();
            String paramType = v.getParamType();

            Object o = params.get(paramKey);
            if ("0".equals(v.getFillValidationFlg())){
                Assert.notNull(o, ()->new FileTemplateException(StrFormatter.format("模板填充参数{}不能为空",paramKey)) );

            }
            if (ObjectUtil.isNotNull(o)){
               if (FieldTypeEnum.TABLE.getValue().equals(paramType)) {
                    log.debug("处理table类型字段");
                    if (o instanceof Collection){
                            // 使用replaceAll方法替换匹配的占位符为空字符串
                        hashMap.put(k,o);
                        configureBuilder.bind(k, new HackLoopTableRenderPolicy());
                    }else{
                        log.error("模板字段：{}的配置类型为：{},但是当前参数字段类型并不是Collection类型",v.getParamKey(),FieldTypeEnum.LIST.getValue());
                        throw new FileTemplateException("当前字段类型需为Collection");
                    }
                }else{
                    hashMap.put(k,o);
                }
            }else{
                //如果业务数据为空，考虑将配置中的默认值取出赋值
                String defaultValue = v.getDefaultValue();
                if (StrUtil.isNotEmpty(defaultValue)){
                    hashMap.put(k,defaultValue);
                }
            }
        });
        String templateDirPath= PathUtil.concatDir(FileUtils.getTempDirectoryPath(),TemplateConstant.TEMP_DIRECTORY);
        File file = new File(templateDirPath);
        if (!file.exists()){
            file.mkdirs();
        }
        File processedFile = FileUtils.getFile(templateDirPath,IdUtil.getSnowflakeNextIdStr()+".docx");
        log.debug("已处理完的模板文件存入系统临时目录：{}",processedFile.getAbsolutePath());
        try(FileOutputStream fos = new FileOutputStream(processedFile);) {
            XWPFTemplate xwpfTemplate = XWPFTemplate.compile(templateTempFile, configureBuilder.build()).render(
                   hashMap
            );
            xwpfTemplate.write(fos);
        } catch (Exception e) {
            log.error("已处理完的模板文件写入系统临时目录失败:{}",e.toString());
            throw new FileTemplateException("生成模板写入本地系统错误");
        }finally {
            //程序退出，删除临时文件
            processedFile.deleteOnExit();
        }
        return processedFile;
    }

    @Override
    public Map<String, Object> doFillInFieldsReturnMap(Map<String, Object> params, FileTemplateDetail templateDetail) {
        //此方法不会用到
        throw new FileTemplateException("word模板不支持此方法，请先实现");
    }

    @Override
    public String convert2Html(File file, FileTemplateDetail templateDetail) {
        return WordUtil.autoWord2Html(file);
    }

    @Override
    public File convert2Pdf(File file) {
        File pdfFile = FileUtils.getFile(FileUtils.getTempDirectoryPath(), TemplateConstant.TEMP_DIRECTORY, IdUtil.getSnowflakeNextIdStr() + ".pdf");
//        String templateDirPath= PathUtil.concatDir(FileUtils.getTempDirectoryPath(),TemplateConstant.TEMP_DIRECTORY);
//        File pdfFile = new File(templateDirPath + IdUtil.getSnowflakeNextIdStr() + ".pdf");
        //转pdf
        WordUtil.docx2Pdf(file, pdfFile);
        return pdfFile;
    }
}
