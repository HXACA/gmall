package com.xliu.gmall.service;

import com.xliu.gmall.bean.PmsBaseAttrInfo;
import com.xliu.gmall.bean.PmsBaseAttrValue;
import com.xliu.gmall.bean.PmsBaseSaleAttr;

import java.util.List;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/3/3 13:30
 */
public interface AttrService {
    List<PmsBaseAttrInfo> attrInfoList(String catalogId3);

    String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    List<PmsBaseAttrValue> getAttrValueList(String attrId);

    List<PmsBaseSaleAttr> baseSaleAttrList();
}
