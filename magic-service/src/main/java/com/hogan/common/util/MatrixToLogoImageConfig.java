package com.hogan.common.util;

import java.awt.*;

/**
* @Description: 二维码LOGO配置类
* @author:      hogan.li
* @date:        2016年9月24日 下午4:14:13
*/
public class MatrixToLogoImageConfig {

    //logo默认边框宽度
    public static final int DEFAULT_BORDER = 1;
    //logo默认边框颜色
    public static final Color DEFAULT_BORDERCOLOR = Color.WHITE;
    //logo大小默认为照片的1/4
    public static final int DEFAULT_LOGOPART = 4;
  
    private final int border = DEFAULT_BORDER;
    private final Color borderColor;  
    private final int logoPart;  
      
    /** 
     * Creates a default config with on color {@link #BLACK} and off color {@link #WHITE}
     */
    public MatrixToLogoImageConfig() {  
        this(DEFAULT_BORDERCOLOR, DEFAULT_LOGOPART);
    }  
  
      
    public MatrixToLogoImageConfig(Color borderColor, int logoPart) {  
        this.borderColor = borderColor;  
        this.logoPart = logoPart;  
    }

    public int getBorder() {
        return border;
    }

    public Color getBorderColor() {  
        return borderColor;  
    }  
  
    public int getLogoPart() {
        return logoPart;  
    }  
}  
