package com.xliu.gmail.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        //查询SkuInfo
        PmsSkuInfo pmsSkuInfo = skuService.getSkuById(skuId);
        modelMap.put("skuInfo",pmsSkuInfo);
        //查询关联的销售属性并标记当前Sku为选中状态
        List<PmsProductSaleAttr> pmsProductSaleAttr = spuService.getSpuSaleAttrListCheckBySku(pmsSkuInfo.getProductId(),skuId);
        modelMap.put("spuSaleAttrListCheckBySku",pmsProductSaleAttr);
        //查询当前Spu下的所有Sku
        List<PmsSkuInfo> pmsSkuInfos = skuService.getSkuSaleAttrValueListBySpu(pmsSkuInfo.getProductId());
        Map<String,String> skuSaleAttrHash = new HashMap<>();
        for (PmsSkuInfo skuInfo : pmsSkuInfos) {
            String v = skuInfo.getId();
            String k = "";
            List<PmsSkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
            for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
                k += pmsSkuSaleAttrValue.getSaleAttrValueId()+"|";
            }
            skuSaleAttrHash.put(k,v);
        }
        String s = JSON.toJSONString(skuSaleAttrHash);
        modelMap.put("skuSaleAttrHash",s);
        return "item";
    }
}
