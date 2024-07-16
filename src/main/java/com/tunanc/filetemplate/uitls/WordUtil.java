package com.tunanc.filetemplate.uitls;

import com.aspose.words.Document;
import com.aspose.words.FontSettings;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.tunanc.filetemplate.Exception.FileTemplateException;
import fr.opensagres.poi.xwpf.converter.xhtml.Base64EmbedImgManager;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLConverter;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.xwpf.usermodel.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;


/**
 * 用于处理office文件的工具类
 *
 */
@Slf4j
public class WordUtil {

    static {
        try (InputStream is = WordUtil.class.getClassLoader().getResourceAsStream("license.xml")) {
            License license = new License();
            license.setLicense(is);
        } catch (Exception e) {
            log.error("aspose证书获取失败：{}",e.toString());
            throw new FileTemplateException("aspose证书获取失败");
        }
    }
    /**
     * Word2007(docx)格式转html
     * @return 返回转成String类型的html字符串
     * @throws IOException
     */
    public static String docxToHtml(File file) {
        try (ByteArrayOutputStream htmlStream = new ByteArrayOutputStream();
             XWPFDocument docxDocument = new XWPFDocument(FileUtils.openInputStream(file))) {
            XHTMLOptions options = XHTMLOptions.create();
            // 是否忽略未使用的样式
            options.setIgnoreStylesIfUnused(false);
            // 设置片段模式，<div>标签包裹
            options.setFragment(true);
            // 图片转base64
            options.setImageManager(new Base64EmbedImgManager());
            // 转换htm1
            XHTMLConverter.getInstance().convert(docxDocument, htmlStream, options);
            return htmlStream.toString();
        } catch (Exception e) {
            log.error("Word转Html过程出现异常！", e);
        }
        return null;
    }


    /**
     * Word2003(doc)格式转html
     * @param filePath 文件路径
     * @return 返回转成String类型的html字符串
     * @throws Exception
     */
    public static String docToHtml(String filePath) {
        try (StringWriter writer = new StringWriter();
             HWPFDocument document = new HWPFDocument(Files.newInputStream(new File(filePath).toPath()))) {
            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
            //将图片转成base64的格式
            wordToHtmlConverter.setPicturesManager((bytes, pictureType, s, v, v1) -> "data:image/png;base64," + Base64.encodeBase64String(bytes));
            wordToHtmlConverter.processDocument(document);
            org.w3c.dom.Document htmlDocument = wordToHtmlConverter.getDocument();
            DOMSource domSource = new DOMSource(htmlDocument);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer serializer = factory.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, new StreamResult(writer));
            return writer.toString();
        } catch (Exception e) {
            log.error("Word转Html过程出现异常！", e);
        }
        return null;
    }

    /**
     * word 转 html
     * 自动检测文件格式转换
     * @return 成功返回转换后的html字符串；失败返回null
     */
    public static String autoWord2Html(File file){
        String filePath = file.getAbsolutePath();
        int lastIndexOf = filePath.lastIndexOf(".");
        String suffix = filePath.substring(lastIndexOf + 1);
        if ("doc".equalsIgnoreCase(suffix)) {
            return docToHtml(filePath);
        } else if ("docx".equalsIgnoreCase(suffix)) {
            return docxToHtml(file);
        } else {
            log.info("文件格式错误，只支持Docx和Doc格式的文档！");
            return null;
        }
    }

    public static void docx2Pdf(File docxFile,File pdfFile){
        try {
            //新建一个空白pdf文档
            long old = System.currentTimeMillis();
            FileOutputStream os = new FileOutputStream(pdfFile);
            //Address是将要被转化的word文档
            Document doc = new Document(docxFile.getPath());
            //全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument,PDF,EPUB,XPS,SWF相互转换
            doc.save(os, SaveFormat.PDF);
            long now = System.currentTimeMillis();
            //转化用时
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒");
        } catch (Exception e) {
            e.printStackTrace();
        }{
            //以下内容只需JVM处理一次
            String osName = System.getProperties().getProperty("os.name");
            log.info("osName:{}",osName);
            if (osName.contains("Linux")){
                log.info("用户文件路径：{}",FileUtils.getUserDirectoryPath());
                //将window系统字体所有文件放到用户目录/fonts下面。如登录用户是fms,那么文件就放到/home/fms/fonts下面
                FontSettings.setFontsFolder(FileUtils.getUserDirectoryPath()+File.separator+"fonts"+File.separator,true);
            }
        }

    }

    /**
     * 加载license 用于破解 不生成水印
     *
     * @return
     */
    private static void getLicense() {
        try (InputStream is = WordUtil.class.getClassLoader().getResourceAsStream("license.xml")) {
            License license = new License();
            license.setLicense(is);
        } catch (Exception e) {
            log.error("aspose证书获取失败：{}",e.toString());
            throw new FileTemplateException("aspose证书获取失败");
        }

    }

}