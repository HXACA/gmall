package com.xliu.gmall.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xliu.gmall.bean.PmsSearchParam;
import com.xliu.gmall.bean.PmsSearchSkuInfo;
import com.xliu.gmall.service.SearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/3/12 20:51
 */
@Controller
public class SearchController {

    @Reference
    SearchService searchService;

    @RequestMapping("index")
    public String index(){
        return "index";
    }

    @RequestMapping("list.html")
    public String list(PmsSearchParam pmsSearchParam, ModelMap modelMap){
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = searchService.list(pmsSearchParam);
        modelMap.put("skuLsInfoList",pmsSearchSkuInfos);
        return "list";
    }

}
