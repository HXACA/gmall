package com.xliu.gmail.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xliu.gmall.bean.PmsProductSaleAttr;
import com.xliu.gmall.bean.PmsSkuInfo;
import com.xliu.gmall.bean.PmsSkuSaleAttrValue;
import com.xliu.gmall.service.SkuService;
import com.xliu.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/3/5 12:42
 */
@Controller
@CrossOrigin
public class ItemController {

    @Reference
    SkuService skuService;

    @Reference
    SpuService spuService;

    @RequestMapping("{skuId}.html")
    public String item(@PathVariable String skuId, ModelMap modelMap){
        PmsSkuInfo pmsSkuInfo = skuService.getSkuById(skuId);
        List<PmsProductSaleAttr> pmsProductSaleAttr = spuService.spuSaleAttrListCheckBySku(pmsSkuInfo.getProductId(),skuId);
        modelMap.put("skuInfo",pmsSkuInfo);
        modelMap.put("spuSaleAttrListCheckBySku",pmsProductSaleAttr);
        return "item";
    }
}
