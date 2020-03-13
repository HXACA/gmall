package com.xliu.gmall.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xliu.gmall.bean.*;
import com.xliu.gmall.service.AttrService;
import com.xliu.gmall.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/3/12 20:51
 */
@Controller
public class SearchController {

    @Reference
    SearchService searchService;

    @Reference
    AttrService attrService;

    @RequestMapping("index")
    public String index() {
        return "index";
    }

    @RequestMapping("list.html")
    public String list(PmsSearchParam pmsSearchParam, ModelMap modelMap) {
        //根据查询条件查询
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = searchService.list(pmsSearchParam);
        //全部的平台属性集合
        Set<String> valueIdSet = new HashSet<>();
        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfos) {
            List<PmsSkuAttrValue> skuAttrValueList = pmsSearchSkuInfo.getSkuAttrValueList();
            for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
                String valueId = pmsSkuAttrValue.getValueId();
                valueIdSet.add(valueId);
            }
        }
        //根据属性值反查所属属性
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = attrService.getAttrValueListByValueId(valueIdSet);
        //删除已选属性值得所属属性
        String[] delValueIds = pmsSearchParam.getValueId();
        String urlParam = getUrlParam(pmsSearchParam);
        List<PmsSearchCrumb> pmsSearchCrumbs = new ArrayList<>();

        if (delValueIds != null) {
            Iterator<PmsBaseAttrInfo> iterator = pmsBaseAttrInfos.iterator();
            while (iterator.hasNext()) {
                PmsBaseAttrInfo pmsBaseAttrInfo = iterator.next();
                List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
                for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                    String valueId = pmsBaseAttrValue.getId();
                    for (String delValueId : delValueIds) {
                        if (delValueId.equals(valueId)) {
                            PmsSearchCrumb pmsSearchCrumb = new PmsSearchCrumb();
                            pmsSearchCrumb.setValueId(delValueId);
                            pmsSearchCrumb.setValueName(pmsBaseAttrValue.getValueName());
                            pmsSearchCrumb.setUrlParam(urlParam.replace("&valueId="+delValueId,""));
                            pmsSearchCrumbs.add(pmsSearchCrumb);
                            iterator.remove();
                            break;
                        }
                    }
                }
            }
        }

        modelMap.put("attrValueSelectedList", pmsSearchCrumbs);
        modelMap.put("attrList", pmsBaseAttrInfos);
        modelMap.put("skuLsInfoList", pmsSearchSkuInfos);
        modelMap.put("urlParam", urlParam );
        String keyword = pmsSearchParam.getKeyword();
        if (StringUtils.isNotBlank(keyword))
            modelMap.put("keyword", keyword);
        return "list";
    }

    private String getUrlParam(PmsSearchParam pmsSearchParam) {
        String keyword = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String[] skuAttrValueList = pmsSearchParam.getValueId();
        String urlParam = "";
        if (StringUtils.isNotBlank(keyword))
            urlParam += ("&keyword=" + keyword);
        if (StringUtils.isNotBlank(catalog3Id))
            urlParam += ("&catalog3Id=" + catalog3Id);
        if (skuAttrValueList != null) {
            for (String s : skuAttrValueList) {
                urlParam += ("&valueId=" + s);
            }
        }
        if (StringUtils.isNotBlank(urlParam))
            return urlParam.substring(1);
        else
            return urlParam;
    }

}
