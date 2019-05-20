package com.hogan.framework.log;


import com.hogan.common.base.ReturnVO;
import com.hogan.common.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * ClassName:LogController
 * Description:LogController
 * User:hogan.li
 * Date:2018/07/18
 */
@Controller
@RequestMapping(value = "/magic/api")
public class LogController {

    private static Logger log = LoggerFactory.getLogger(LogController.class);

    @Autowired
    private LogService logService;

    /**
     * 查询Log列表
     */
    @RequestMapping(value = "/logs", method = RequestMethod.POST)
    @RequiresPermissions("log:list")
    @ResponseBody
    public ReturnVO findLogList(@RequestBody Map<String, Object> paramMap) {

        ReturnVO vo = new ReturnVO();

        try {
            Page<Log> page = logService.findLogByCondition(paramMap);
            vo.setDataList(page.getContent());
            vo.setTotalProperty(page.getTotalElements());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }

    /**
     * 下载用户操作日志（带查询参数）
     */
    @RequestMapping("/logs/export")
    @RequiresPermissions(value = { "log:list", "log:export" }, logical = Logical.AND)
    @ResponseBody
    public void exportLogList(  HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String userName){

        //设置查询参数
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);
        paramMap.put("userName", userName);

        String fileName = null;
        String fullName = null;
        File tempFile = null;

        try {

            //获取发送数据
            Page<Log> logPage = logService.findLogByCondition(paramMap);
            List<Log> dataList = logPage.getContent();
            if (dataList != null && dataList.size() > 0) {

                //构建输出文件
                String fileSep = System.getProperty("file.separator");
                String date = DateUtil.formatDate(new Date(), "yyyyMMddHHmmssSSS");
                String path = request.getSession().getServletContext().getRealPath(fileSep+"export");
                path = getExportDir(path);
                fileName = "用户操作日志_" + date + ".csv";
                fullName = path + fileName;
                tempFile = new File(fullName + ".tmp");

                //生成输出文件
                log.info("输出日志csv文件开始："+ DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss.SSS"));
                BufferedWriter writer = null;
                try {
                    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile), "GBK"));
                    //写header
                    writer.write("用户账户,用户名称,操作类型,操作方法,操作参数,操作结果,操作ip,操作host,操作时间");
                    writer.newLine();
                    //写line
                    for (Log log : dataList) {
                        String opArgs = StringUtils.isNotBlank(log.getOpArgs()) ? log.getOpArgs().replace(",",";") : "";
                        String opResult = "";
                        if(log.getOpResult() != null){
                            opResult = log.getOpResult()?"成功":"失败";
                        }
                        String createTime = log.getCreateDate();
                        createTime = createTime.substring(0,createTime.lastIndexOf("."));
                        writer.write(
                                log.getUserAccount() + ","
                                        + log.getUserName() + ","
                                        + log.getOpType() + ","
                                        + log.getOpMethod() + ","
                                        + opArgs + ","
                                        + opResult + ","
                                        + log.getOpIp() + ","
                                        + log.getOpHost() + ","
                                        + createTime
                        );
                        writer.newLine();
                    }
                    writer.flush();
                    writer.close();
                    log.info("输出日志csv文件完成："+ DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss.SSS"));
                } catch (IOException e) {
                    throw new RuntimeException("输出日志csv文件失败：" + e);
                } finally {
                    if (writer != null) {
                        writer.close();
                    }
                }

                //重命名输出文件（去掉.TMP后缀）
                tempFile.renameTo(new File(fullName));
                log.info("重命名日志csv文件完成：" + fileName);
            } else {
                log.info("没有日志csv文件可以下载：" + fileName);
                return;
            }
        } catch (Exception e) {
            log.error("生成日志csv文件失败：" + e);
            if (tempFile != null) {
                tempFile.delete();
            }
            return;
        }

        log.info("生成日志csv文件完成：" + fileName);

        //检查生成文件
        File exportFile = new File(fullName);
        if(!exportFile.exists()){
            log.info("没有日志csv文件可以下载：" + fileName);
            return;
        }

        //设置响应头
        try {
            response.setContentType("text/csv;charset=UTF-8");
            String userAgent = request.getHeader("user-agent");
            //浏览器校验：IE11版本请求头部不带'IE' 标识，变成了rv:11.0
            if (userAgent != null && userAgent.indexOf("IE") >= 0 || userAgent.indexOf("rv:11.0") >= 0) {
                response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName,"UTF8"));
            }else{
                response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes(), "ISO8859-1"));
            }

        } catch (UnsupportedEncodingException e) {
            log.error("设置响应编码异常：" + e);
        }

        //下载文件
        BufferedInputStream bin = null;
        BufferedOutputStream bout = null;
        try {
            bin = new BufferedInputStream(new FileInputStream(exportFile));
            bout = new BufferedOutputStream(response.getOutputStream());
            byte[] buf = new byte[8192];
            int length = 0;
            while ((length = bin.read(buf)) != -1) {
                bout.write(buf, 0, length);
            }
            bout.flush();
        } catch (IOException e) {
            log.error("下载日志csv文件异常：" + e);
        } finally {
            try {
                if (bout != null) {
                    bout.close();
                }
                if (bin != null) {
                    bin.close();
                }
            } catch (IOException e) {
                log.error("关闭日志csv文件下载输入/输出流异常：" + e);
            }
            //删除已经生成的日志csv文件
            if(exportFile  != null){
                exportFile.delete();
            }
        }

        log.info("下载日志csv文件完成：" + fileName);
    }

    /**
     * 获取下载文件输出路径：/export
     */
    private String getExportDir(String path){
        String fileSep = System.getProperty("file.separator");
        File exportDir = new File(path);
        //判断文件夹是否存在
        if(!exportDir.exists()){
            exportDir.mkdir();
        }

        //判断路径是否已分隔符结尾
        if(!path.endsWith(fileSep)){
            path += fileSep;
        }

        return path;
    }
}