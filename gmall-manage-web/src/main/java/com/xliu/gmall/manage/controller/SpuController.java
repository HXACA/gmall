package com.xliu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xliu.gmall.bean.PmsProductImage;
import com.xliu.gmall.bean.PmsProductInfo;
import com.xliu.gmall.bean.PmsProductSaleAttr;
import com.xliu.gmall.manage.util.PmsUploadUtil;
import com.xliu.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/3/4 10:47
 */
@Controller
@CrossOrigin
public class SpuController {

    @Reference
    SpuService spuService;

    @RequestMapping("spuList")
    @ResponseBody
    public List<PmsProductInfo>spuList(String catalog3Id){
        return spuService.spuList(catalog3Id);
    }

    @RequestMapping("saveSpuInfo")
    @ResponseBody
    public String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo){
        return spuService.saveSpuInfo(pmsProductInfo);
    }

    @RequestMapping("fileUpload")
    @ResponseBody
    public String fileUpload(@RequestParam("file") MultipartFile multipartFile){
        //将文件上传到分布式存储系统
        String imgUrl = PmsUploadUtil.uploadImage(multipartFile);
        return imgUrl;
    }

    @RequestMapping("spuSaleAttrList")
    @ResponseBody
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId){
        return spuService.spuSaleAttrList(spuId);
    }

    @RequestMapping("spuImageList")
    @ResponseBody
    public List<PmsProductImage> spuImageList(String spuId){
        return spuService.spuImageList(spuId);
    }

}
