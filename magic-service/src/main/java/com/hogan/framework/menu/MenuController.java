package com.hogan.framework.menu;

import com.hogan.common.base.ReturnVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * ClassName:MenuController
 * Description:MenuController
 * User:dada
 * Date:2018/07/18
 */
@Controller
@RequestMapping(value = "/ep/api")
public class MenuController {

    private static Logger log = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;

    /**
     * 查询菜单列表（含权限列表）
     */
    @RequestMapping(value = "/menus", method = RequestMethod.GET)
    @ResponseBody
    public ReturnVO getMenuList() {

        ReturnVO vo = new ReturnVO();

        try {
            List<Menu> dataList = menuService.getMenusWithPermissions();
            vo.setDataList(dataList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }
}