package com.hogan.common.txthelper;

import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.util.Iterator;

public abstract class AbstractTxtInputStream<T> implements Iterable<T> {

    private InputStreamReader read = null;
    private BufferedReader br = null;

    protected AbstractTxtInputStream(File file) {
        try {
            read = new InputStreamReader(new FileInputStream(file), "GBK");
            br = new BufferedReader(read);
        } catch (Exception e) {
            try {
                close();
            } catch (IOException ie) {
                throw new RuntimeException(ie);
            }
        }
    }


    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private T t = null;
            String line;

            @Override
            public boolean hasNext() {
                try {
                    line = br.readLine();
                    while (line != null && !line.isEmpty()) {
                        String[] fields = line.split("\\" + "|", -1);
                        try {
                            t = createRecordFromStringArray(fields);
                        } catch (Exception e) {
                            String msg = "解析出现异常：" + e.toString() + ", 记录：" + ArrayUtils.toString(fields);
                            throw new RuntimeException(msg);
                        }
                        return true;
                    }
                    close();
                } catch (Exception e) {
                    try {
                        close();
                    } catch (IOException ie) {
                        throw new RuntimeException(ie);
                    }
                    return false;
                }
                return false;
            }

            @Override
            public T next() {
                return t;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("UnsupportedOperationException");
            }

        };
    }

    public void close() throws IOException {
        if (read != null) {
            read.close();
        }
        if (br != null) {
            br.close();
        }
    }

    protected abstract T createRecordFromStringArray(String[] fields) throws Exception;
}
