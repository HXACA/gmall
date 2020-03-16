package com.xliu.gmall.service;

import com.xliu.gmall.bean.OmsCartItem;
import com.xliu.gmall.bean.PmsBaseAttrInfo;
import com.xliu.gmall.bean.PmsBaseAttrValue;
import com.xliu.gmall.bean.PmsBaseSaleAttr;

import java.util.List;
import java.util.Set;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/3/3 13:30
 */
public interface CartService {

    OmsCartItem ifCartExistByUser(String memberId, String skuId);

    void addCart(OmsCartItem omsCartItem);

    void updateCart(OmsCartItem omsCartItemsFromDb);

    void flushCartCache(String memberId);

    List<OmsCartItem> cartList(String userId);

    void checkCart(OmsCartItem omsCartItem);
}
