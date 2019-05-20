package com.hogan.common.util;

import com.hogan.app.AppConstants;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.security.GeneralSecurityException;

/**
 * ClassName:OfficeUtil
 * Description:OfficeUtil
 * User:dada
 * Date:2018/11/13 16:16
 */
public class OfficeUtil {
    /**
     * Excel转Html
     */
    public static String excelToHtml(String fullFileName, String password) throws Exception {

        // 解析excel
        HSSFWorkbook hssfWorkbook = parseExcel(fullFileName, password);

        // 设置转换参数
        ExcelToHtmlConverter excelToHtmlConverter = new ExcelToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        excelToHtmlConverter.setOutputColumnHeaders(false);     //去掉Excel头行
        excelToHtmlConverter.setOutputRowNumbers(false);        //去掉Excel行号
        excelToHtmlConverter.processWorkbook(hssfWorkbook);

        // 将excel转换成完整的html
        Document htmlDocument = excelToHtmlConverter.getDocument();
        DOMSource domSource = new DOMSource(htmlDocument);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        StreamResult streamResult = new StreamResult(os);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, System.getProperty("file.encoding"));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "html");
        transformer.transform(domSource, streamResult);

        String html = new String(os.toByteArray());

        os.close();

        return parseHtml(html);
    }

    /**
     * 解析excel文件
     */
    private static HSSFWorkbook parseExcel(String fullFileName, String password) {

        InputStream is = null;

        try {
            is = new FileInputStream(fullFileName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(AppConstants.ERROR_MSG_FILE_DELETED, e);
        }

        // 判断excel版本（如果是07+版本，则转换为03版本，因为ExcelToHtmlConverter 只支持 Converts xls files (97-2007) to HTML file）
        String suffix = fullFileName.substring(fullFileName.lastIndexOf(".") + 1);
        HSSFWorkbook hssfWorkbook = null;

        if (AppConstants.EXCEL_XLS.equalsIgnoreCase(suffix)) {
            try {
                POIFSFileSystem pfs = new POIFSFileSystem(is);
                Biff8EncryptionKey.setCurrentUserPassword(password);
                hssfWorkbook = new HSSFWorkbook(pfs);
            } catch (EncryptedDocumentException e) {
                throw new RuntimeException(AppConstants.ERROR_MSG_PASSWORD_INVALID, e);
            } catch (IOException e) {
                throw new RuntimeException(AppConstants.ERROR_MSG_EXCEL_PARSE_EXCEPTION, e);
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new RuntimeException(AppConstants.ERROR_RELEASE_RESOURCE_EXCEPTION, e);
                }
            }
        } else if (AppConstants.EXCEL_XLSX.equalsIgnoreCase(suffix)) {
            // 创建空白HSSFWorkbook
            hssfWorkbook = new HSSFWorkbook();

            if (StringUtils.isBlank(password)) {
                try {
                    // 无需解密
                    XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
                    Transform transform = new Transform();
                    transform.XssfToHssf(xssfWorkbook, hssfWorkbook);
                } catch (Exception e) {
                    throw new RuntimeException(AppConstants.ERROR_MSG_EXCEL_PARSE_EXCEPTION, e);
                } finally {
                    try {
                        is.close();
                    } catch (IOException e) {
                        throw new RuntimeException(AppConstants.ERROR_RELEASE_RESOURCE_EXCEPTION, e);
                    }
                }
            } else {
                try {
                    // 文件解密
                    POIFSFileSystem pfs = new POIFSFileSystem(is);
                    EncryptionInfo encryptionInfo = new EncryptionInfo(pfs);
                    Decryptor decryptor = Decryptor.getInstance(encryptionInfo);
                    decryptor.verifyPassword(password);
                    XSSFWorkbook xssfWorkbook = new XSSFWorkbook(decryptor.getDataStream(pfs));

                    // 版本转换
                    Transform transform = new Transform();
                    transform.XssfToHssf(xssfWorkbook, hssfWorkbook);
                } catch (GeneralSecurityException e) {
                    throw new RuntimeException(AppConstants.ERROR_MSG_PASSWORD_INVALID, e);
                } catch (Exception e) {
                    throw new RuntimeException(AppConstants.ERROR_MSG_EXCEL_PARSE_EXCEPTION, e);
                } finally {
                    try {
                        is.close();
                    } catch (IOException e) {
                        throw new RuntimeException(AppConstants.ERROR_RELEASE_RESOURCE_EXCEPTION, e);
                    }
                }
            }
        } else {
            throw new RuntimeException(AppConstants.ERROR_MSG_NO_SUPPORTED_EXCEL_VERSION);
        }

        return hssfWorkbook;
    }

    /**
     * 使用jsoup解析html，获取文件内容片段
     */
    private static String parseHtml(String html) {
        org.jsoup.nodes.Document document = Jsoup.parse(html);
        Element body = document.body();
        Elements elements = body.children();
        if (elements != null) {
            return elements.toString();
        } else {
            return null;
        }
    }

    /**
     * Txt转Html
     */
    public static String txtToHtml(String fullFileName) {

        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        try {
            isr = new InputStreamReader(new FileInputStream(fullFileName), "GBK");
            br = new BufferedReader(isr);
            sb = new StringBuilder();

            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append("<p>");
                sb.append(line);
                sb.append("</p>");
            }
        } catch (UnsupportedEncodingException e) {
            new RuntimeException(AppConstants.ERROR_MSG_UNSUPPORTED_ENCODING, e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(AppConstants.ERROR_MSG_FILE_DELETED, e);
        } catch (IOException e) {
            throw new RuntimeException(AppConstants.ERROR_MSG_TXT_PARSE_EXCEPTION, e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    throw new RuntimeException(AppConstants.ERROR_RELEASE_RESOURCE_EXCEPTION, e);
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    throw new RuntimeException(AppConstants.ERROR_RELEASE_RESOURCE_EXCEPTION, e);
                }
            }
        }

        return new String(sb);
    }

    public static void main(String[] args) throws Exception {
        String fullFileName = "D:\\test\\gcm\\java应用服务器详细信息.txt";

        String html = txtToHtml(fullFileName);
        System.out.println(html);
    }
}
