package com.hogan.common.mailhelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * ClassName:MailContentBuilder
 * Description:负责构建邮件模板
 * User:hogan.li
 * Date:2018/11/21 18:23
 */
@Component
public class MailContentBuilder {

    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 构建html模板
     */
    public String buildHtml(Context context, String template) {
        return templateEngine.process(template, context);
    }
}
