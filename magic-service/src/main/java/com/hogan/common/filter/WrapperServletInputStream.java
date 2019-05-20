package com.hogan.common.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * ClassName:WrapperServletInputStream
 * Description:请求流对象
 * User:dada
 * Date:2018/5/16 11:36
 */
public class WrapperServletInputStream extends ServletInputStream {

    private InputStream inputStream;

    /**
     * 有参构造器，目的是将参数重新写回到请求流
     */
    public WrapperServletInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
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
        return 0;
    }
}
