package com.tunanc.filetemplate.uitls;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.font.FontProvider;
import com.tunanc.filetemplate.Exception.FileTemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * pdf工具类
 */
@Slf4j
public class PdfUtil {


    private static volatile ConverterProperties converterProperties;


    /**
     * 填充表单
     * @param inputStream
     * @param map
     */
    public static ByteArrayOutputStream fillForm(InputStream inputStream,Map<String, Object> map) throws Exception {
        PdfDocument pdfDocument = null;
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            pdfDocument = new PdfDocument(new PdfReader(inputStream), new PdfWriter(outputStream));
            ClassPathResource classPathResource = new ClassPathResource("fonts/simsun.ttc,0");
            PdfFont font = PdfFontFactory.createFont(classPathResource.getPath(), PdfEncodings.IDENTITY_H);
            PdfAcroForm acroForm = PdfAcroForm.getAcroForm(pdfDocument, false);
            Map<String, PdfFormField> formFields = acroForm.getFormFields();
            for (String key : formFields.keySet()) {
                PdfFormField pdfFormField = formFields.get(key);
                String tmpKey = key;
                if (key.contains("#")){
                    tmpKey = key.substring(0,key.indexOf("#"));
                }
                Object value = map.get(tmpKey);
                //Assert.notNull(value,"未获取到参数{}的值",tmpKey);
                if (value == null) {
                    continue;
                }
                pdfFormField.setValue(value.toString()).setFont(font);
            }
            acroForm.flattenFields();

        } catch (Exception exception) {
            log.error("【生成pdf】报错：{}", exception.getMessage());
            throw new Exception("生成pdf报错");
        } finally {
            try {
                pdfDocument.close();
                outputStream.flush();
                outputStream.close();
            } catch (IOException ioException) {
                log.error("【生成pdf】关闭资源时报错：{}", ioException.getMessage());
            }
        }
        return outputStream;
    }

    private static String getAlibabaFontDirectory() throws IOException {
        final File fontDir = new File(System.getProperty("user.dir"), "font");
        if (!fontDir.exists() && !fontDir.mkdirs()) {
            throw new IOException("【生成pdf】创建字体目录失败：" + fontDir.getAbsolutePath());
        }

        if (!FileUtils.getFile(fontDir,"SourceHanSansSC-Light-2.otf").exists()){
            final InputStream fontLight = Objects.requireNonNull(PdfUtil.class.getResourceAsStream("/fonts/SourceHanSansSC-Light-2.otf"));
            IOUtils.copy(fontLight, Files.newOutputStream(new File(fontDir, "SourceHanSansSC-Light-2.otf").toPath()));
        }
        if (!FileUtils.getFile(fontDir,"SourceHanSansSC-Bold-2.otf").exists()){
            final InputStream fontBold = Objects.requireNonNull(PdfUtil.class.getResourceAsStream("/fonts/SourceHanSansSC-Bold-2.otf"));
            IOUtils.copy(fontBold, Files.newOutputStream(new File(fontDir, "SourceHanSansSC-Bold-2.otf").toPath()));
        }



        return fontDir.getAbsolutePath();
    }


    /**
     * html转pdf
     */
    public static ByteArrayOutputStream htmlToPdf(String html) {
        PdfDocument pdfDocument = null;
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            pdfDocument = new PdfDocument(new PdfWriter(outputStream));
            //设置为A4
            pdfDocument.setDefaultPageSize(PageSize.A4);


            //处理html内容
            Document document = new Document(pdfDocument);
            List<IElement> iElements = HtmlConverter.convertToElements(html, getConverterProperties());
            int size = iElements.size();
            for (int i = 0; i < size; i++) {
                document.add((IBlockElement)iElements.get(i));
//                if (!(i==size-1)){
//                    document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
//                }
            }
            document.close();
        } catch (Exception exception) {
            log.error("【生成pdf】报错：{}", ExceptionUtils.getStackTrace(exception));
            throw new FileTemplateException("生成pdf报错");
        } finally {
            try {
                pdfDocument.close();
                outputStream.flush();
                outputStream.close();
            } catch (IOException ioException) {
                log.error("【生成pdf】关闭资源时报错：{}", ioException.getMessage());
            }
        }
        return outputStream;
    }


    private static PdfFont getAlibabaBlobFont() throws IOException {
        //获取字体
        ClassPathResource classPathResource = new ClassPathResource("fonts/SourceHanSansSC-Light-2.otf");
        return PdfFontFactory.createFont(classPathResource.getPath(), PdfEncodings.IDENTITY_H);
    }


    private static ConverterProperties getConverterProperties() throws IOException {
        //单例模式
        if (converterProperties == null){
            synchronized (PdfUtil.class){
                if (converterProperties == null){
                    //转换器设置字体
                    converterProperties = new ConverterProperties();
                    FontProvider fontProvider = new FontProvider();
                    fontProvider.addDirectory(getAlibabaFontDirectory());
                    converterProperties.setFontProvider(fontProvider);
                }
            }

        }
        return converterProperties;
    }

}
