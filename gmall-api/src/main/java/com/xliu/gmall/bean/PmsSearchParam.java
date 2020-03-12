package com.xliu.gmall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/3/12 21:18
 */
public class PmsSearchParam implements Serializable {

    private String catalog3Id;
    private String keyword;
    private List<PmsSkuAttrValue> skuAttrValueList;

    public String getCatalog3Id() {
        return catalog3Id;
    }

    public void setCatalog3Id(String catalog3Id) {
        this.catalog3Id = catalog3Id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<PmsSkuAttrValue> getSkuAttrValueList() {
        return skuAttrValueList;
    }

    public void setSkuAttrValueList(List<PmsSkuAttrValue> skuAttrValueList) {
        this.skuAttrValueList = skuAttrValueList;
    }
}
