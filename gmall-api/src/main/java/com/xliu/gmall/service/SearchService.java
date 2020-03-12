package com.xliu.gmall.service;

import com.xliu.gmall.bean.PmsSearchParam;
import com.xliu.gmall.bean.PmsSearchSkuInfo;

import java.util.List;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/3/12 21:30
 */
public interface SearchService {
    List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam);

}
