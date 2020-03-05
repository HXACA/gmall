package com.xliu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.xliu.gmall.bean.PmsSkuAttrValue;
import com.xliu.gmall.bean.PmsSkuImage;
import com.xliu.gmall.bean.PmsSkuInfo;
import com.xliu.gmall.bean.PmsSkuSaleAttrValue;
import com.xliu.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.xliu.gmall.manage.mapper.PmsSkuImageMapper;
import com.xliu.gmall.manage.mapper.PmsSkuInfoMapper;
import com.xliu.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.xliu.gmall.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/3/5 9:10
 */
@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;

    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;


    @Override
    public String saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
        try {
            pmsSkuInfoMapper.insertSelective(pmsSkuInfo);

            List<PmsSkuImage> skuImageList = pmsSkuInfo.getSkuImageList();
            for (PmsSkuImage pmsSkuImage : skuImageList) {
                pmsSkuImage.setSkuId(pmsSkuInfo.getId());
                pmsSkuImage.setProductImgId(pmsSkuImage.getSpuImgId());
                pmsSkuImageMapper.insertSelective(pmsSkuImage);
            }

            List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
            for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
                pmsSkuAttrValue.setSkuId(pmsSkuInfo.getId());
                pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
            }

            List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
            for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
                pmsSkuSaleAttrValue.setSkuId(pmsSkuInfo.getId());
                pmsSkuSaleAttrValueMapper.insert(pmsSkuSaleAttrValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
        return "success";
    }
}
