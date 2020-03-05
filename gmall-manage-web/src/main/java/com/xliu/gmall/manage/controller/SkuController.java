package com.xliu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xliu.gmall.bean.PmsSkuInfo;
import com.xliu.gmall.service.SkuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/3/5 9:12
 */
@Controller
@CrossOrigin
public class SkuController {

    @Reference
    SkuService skuService;

    @RequestMapping("saveSkuInfo")
    @ResponseBody
    public String saveSkuInfo(@RequestBody PmsSkuInfo pmsSkuInfo){
        pmsSkuInfo.setProductId(pmsSkuInfo.getSpuId());
        /*处理无默认图像*/
        if(StringUtils.isBlank(pmsSkuInfo.getSkuDefaultImg())){
            pmsSkuInfo.setSkuDefaultImg(pmsSkuInfo.getSkuImageList().get(0).getImgUrl());
            pmsSkuInfo.getSkuImageList().get(0).setIsDefault("1");
        }
        return skuService.saveSkuInfo(pmsSkuInfo);
    }

}
