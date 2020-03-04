package com.xliu.gmall.service;

import com.xliu.gmall.bean.PmsProductInfo;

import java.util.List;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/3/4 10:50
 */
public interface SpuService {
    List<PmsProductInfo> spuList(String catalog3Id);

    String saveSpuInfo(PmsProductInfo pmsProductInfo);
}
