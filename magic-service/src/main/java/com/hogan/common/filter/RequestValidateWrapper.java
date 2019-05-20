package com.hogan.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:RequestValidateWrapper
 * Description:请求包装类，目的是为了获取请求参数
 * User:dada
 * Date:2018/5/15 17:34
 */
public class RequestValidateWrapper extends HttpServletRequestWrapper {

    private Map<String, Object> paramMap;
    private byte[] bytes;
    private WrapperServletInputStream wrapperServletInputStream;
    private String requestParamString;

    public RequestValidateWrapper(HttpServletRequest request) throws Exception {
        super(request);

        String method = request.getMethod().toUpperCase();
        if ("POST".equals(method) || "PUT".equals(method)) {
            // 读取post请求参数，并保存到bytes里
            bytes = IOUtils.toByteArray(request.getInputStream());
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            this.wrapperServletInputStream = new WrapperServletInputStream(byteArrayInputStream);

            // 把post参数重新写入请求流
            writeToInputStream();

            requestParamString = new String(bytes, this.getCharacterEncoding());
        } else if ("GET".equals(method) || "DELETE".equals(method)) {

            paramMap = new HashMap<>();

            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String param = parameterNames.nextElement();
                paramMap.put(param, request.getParameter(param));
            }

            if (!paramMap.isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                requestParamString = mapper.writeValueAsString(paramMap);
            } else {
                requestParamString = "";
            }
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return wrapperServletInputStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(wrapperServletInputStream));
    }

    /**
     * 将求情参数重新写回到流
     */
    public void writeToInputStream() {
        wrapperServletInputStream.setInputStream(new ByteArrayInputStream(bytes != null ? bytes : new byte[0]));
    }

    /**
     * 获取请求参数字符串（核心的业务目的：获取请求参数）
     */
    public String getRequestParamString() throws UnsupportedEncodingException {
        return requestParamString;
    }

    /**
     * 内部类：用于封装ServletInputStream
     */
    private class WrapperServletInputStream extends ServletInputStream {

        private InputStream inputStream;

        /**
         * 有参构造器，目的是接收请求参数输入流
         */
        public WrapperServletInputStream(InputStream inputStream) {
            super();
            this.inputStream = inputStream;
        }

        public void setInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        public InputStream getInputStream() {
            return inputStream;
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setReadListener(ReadListener readListener) {

        }

        @Override
        public int read() throws IOException {
            return inputStream.read();
        }
    }
}
