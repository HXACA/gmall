package com.xliu.gmall.service;

import com.xliu.gmall.bean.PmsBaseCatalog1;
import com.xliu.gmall.bean.PmsBaseCatalog2;
import com.xliu.gmall.bean.PmsBaseCatalog3;

import java.util.List;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/3/3 11:04
 */
public interface CatalogService {

    List<PmsBaseCatalog1>getCatalog1();

    List<PmsBaseCatalog2> getCatalog2(String catalog1Id);

    List<PmsBaseCatalog3> getCatalog3(String catalog2Id);
}
