package com.xliu.gmall.service;

import com.xliu.gmall.bean.PmsProductImage;
import com.xliu.gmall.bean.PmsProductInfo;
import com.xliu.gmall.bean.PmsProductSaleAttr;

import java.util.List;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/3/4 10:50
 */
public interface SpuService {
    List<PmsProductInfo> spuList(String catalog3Id);

    String saveSpuInfo(PmsProductInfo pmsProductInfo);

    List<PmsProductSaleAttr> spuSaleAttrList(String spuId);

    List<PmsProductImage> spuImageList(String spuId);

    List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId, String skuId);
}
