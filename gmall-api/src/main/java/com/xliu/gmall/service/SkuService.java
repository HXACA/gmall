package com.xliu.gmall.service;

import com.xliu.gmall.bean.PmsProductSaleAttr;
import com.xliu.gmall.bean.PmsSkuInfo;

import java.util.List;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/3/5 9:10
 */
public interface SkuService {
    String saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    PmsSkuInfo getSkuById(String skuId);
}
