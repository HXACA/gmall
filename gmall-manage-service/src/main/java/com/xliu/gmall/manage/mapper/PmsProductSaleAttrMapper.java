package com.xliu.gmall.manage.mapper;

import com.xliu.gmall.bean.PmsProductSaleAttr;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/3/4 13:11
 */
public interface PmsProductSaleAttrMapper extends Mapper<PmsProductSaleAttr> {
    List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(@Param("productId") String productId, @Param("skuId") String skuId);
}
