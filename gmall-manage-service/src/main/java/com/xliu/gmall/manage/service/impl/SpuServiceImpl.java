package com.xliu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.xliu.gmall.bean.PmsProductImage;
import com.xliu.gmall.bean.PmsProductInfo;
import com.xliu.gmall.bean.PmsProductSaleAttr;
import com.xliu.gmall.bean.PmsProductSaleAttrValue;
import com.xliu.gmall.manage.mapper.PmsProductImageMapper;
import com.xliu.gmall.manage.mapper.PmsProductInfoMapper;
import com.xliu.gmall.manage.mapper.PmsProductSaleAttrMapper;
import com.xliu.gmall.manage.mapper.PmsProductSaleAttrValueMapper;
import com.xliu.gmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/3/4 10:51
 */
@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    PmsProductInfoMapper pmsProductInfoMapper;

    @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;

    @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;

    @Autowired
    PmsProductImageMapper pmsProductImageMapper;

    @Override
    public List<PmsProductInfo> spuList(String catalog3Id) {
        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3Id);
        List<PmsProductInfo> pmsProductInfos = pmsProductInfoMapper.select(pmsProductInfo);
        return pmsProductInfos;
    }

    @Override
    public String saveSpuInfo(PmsProductInfo pmsProductInfo) {
        try {
            pmsProductInfoMapper.insertSelective(pmsProductInfo);
            List<PmsProductSaleAttr> spuSaleAttrList = pmsProductInfo.getSpuSaleAttrList();
            for (PmsProductSaleAttr pmsProductSaleAttr : spuSaleAttrList) {
                pmsProductSaleAttr.setProductId(pmsProductInfo.getId());
                pmsProductSaleAttrMapper.insertSelective(pmsProductSaleAttr);
                List<PmsProductSaleAttrValue> spuSaleAttrValueList = pmsProductSaleAttr.getSpuSaleAttrValueList();
                for (PmsProductSaleAttrValue pmsProductSaleAttrValue : spuSaleAttrValueList) {
                    pmsProductSaleAttrValue.setProductId(pmsProductInfo.getId());
                    pmsProductSaleAttrValueMapper.insertSelective(pmsProductSaleAttrValue);
                }
            }
            List<PmsProductImage> spuImageList = pmsProductInfo.getSpuImageList();
            for (PmsProductImage pmsProductImage : spuImageList) {
                pmsProductImage.setProductId(pmsProductInfo.getId());
                pmsProductImageMapper.insertSelective(pmsProductImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
        return "success";
    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId) {
        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        pmsProductSaleAttr.setProductId(spuId);
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);
        for (PmsProductSaleAttr productSaleAttr : pmsProductSaleAttrs) {
            PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
            pmsProductSaleAttrValue.setProductId(spuId);
            pmsProductSaleAttrValue.setSaleAttrId(productSaleAttr.getSaleAttrId());
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValues = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);
            productSaleAttr.setSpuSaleAttrValueList(pmsProductSaleAttrValues);
        }
        return pmsProductSaleAttrs;
    }

    @Override
    public List<PmsProductImage> spuImageList(String spuId) {
        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(spuId);
        List<PmsProductImage> pmsProductImages = pmsProductImageMapper.select(pmsProductImage);
        return pmsProductImages;
    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId, String skuId) {
        return pmsProductSaleAttrMapper.spuSaleAttrListCheckBySku(productId,skuId);
    }
}
