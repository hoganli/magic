package com.hogan.common.util;


import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by grando.lu on 2018/5/21.
 */
public class CsvUtil {

    public static void export(HttpServletResponse response, String fileName, String[][] contents) throws Exception {
        response.reset();
        response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", fileName));
        response.setContentType("application/csv;charset=utf-8");
        OutputStream os = response.getOutputStream();
        // 写入bom头
        byte[] uft8bom={(byte)0xef,(byte)0xbb,(byte)0xbf};
        //String bOM = new String(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
        os.write(uft8bom);

        for (int i = 0; i < contents.length; i++) {
            for (int j = 0; j < contents[i].length; j++) {
                os.write(contents[i][j].getBytes("UTF-8"));
                os.write(",".getBytes());
            }
            os.write("\n".getBytes());
        }
        os.flush();
        os.close();
    }


    public static void export(HttpServletResponse response, String fileName, List<List<String>> rowList) throws Exception {
        response.reset();
        response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", fileName));
        response.setContentType("application/csv;charset=utf-8");
        OutputStream os = response.getOutputStream();

        export(os, rowList);
    }

    public static void export(OutputStream os, List<List<String>> rowList) throws Exception {
        // 写入bom头
        byte[] uft8bom={(byte)0xef,(byte)0xbb,(byte)0xbf};
        //String bOM = new String(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
        os.write(uft8bom);

        for (List<String> colList : rowList) {
            for (String col : colList) {
                col = (col == null ? "" : col);
                os.write(col.getBytes("UTF-8"));
                os.write(",".getBytes());
            }
            os.write("\n".getBytes());
        }
        os.flush();
        os.close();
    }

    public static void main(String args[]) {
        String[][] contents = {
                {"项目", "名称", "类型", "属性"},
                {"卡组织", "组织LOGO-001", "上", "UU"},
                {"客户", "客户LOGO-001", "中", "WW"},
                {"烫印", "YL-001", "下", "XX"},
                {"立金", "BANKLOGO-001", "左", "YY"},
                {"个人化", "凸字", "右", "ZZ"}
        };

        List<List<String>> rowList = new ArrayList<>();

        for (int i = 0; i < contents.length; i++) {
            List<String> colList = new ArrayList<>();
            for (int j = 0; j < contents[i].length; j++) {
                colList.add(contents[i][j]);
            }
            rowList.add(colList);
        }

        try {
            FileOutputStream fos = new FileOutputStream("D:/test/testExportCsv.csv");
            export(fos, rowList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
