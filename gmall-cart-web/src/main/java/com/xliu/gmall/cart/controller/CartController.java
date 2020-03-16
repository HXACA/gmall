package com.xliu.gmall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xliu.gmall.bean.OmsCartItem;
import com.xliu.gmall.bean.PmsSkuInfo;
import com.xliu.gmall.service.CartService;
import com.xliu.gmall.service.SkuService;
import com.xliu.gmall.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/3/13 18:03
 */
@Controller
public class CartController {


    @Reference
    SkuService skuService;

    @Reference
    CartService cartService;

    @RequestMapping("checkCart")
    public String checkCart(String isChecked,String skuId,HttpServletRequest request, HttpServletResponse response, ModelMap modelMap){
        String userId = "1";
        if(StringUtils.isNotBlank(userId)){
            OmsCartItem omsCartItem = new OmsCartItem();
            omsCartItem.setMemberId(userId);
            omsCartItem.setIsChecked(isChecked);
            omsCartItem.setProductSkuId(skuId);
            cartService.checkCart(omsCartItem);
            List<OmsCartItem> omsCartItems = cartService.cartList(userId);
            BigDecimal sum = new BigDecimal("0");
            for (OmsCartItem cartItem : omsCartItems) {
                if(cartItem.getIsChecked().equals("1"))
                    sum = sum.add(cartItem.getPrice().multiply(cartItem.getQuantity()));
            }
            modelMap.put("totalPrice",sum);
            modelMap.put("cartList",omsCartItems);
        }else{

        }

        return "cartListInner";
    }

    @RequestMapping("cartList")
    public String cartList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap){
        List<OmsCartItem>omsCartItems = new ArrayList<>();
        String userId = "1";
        if(StringUtils.isNotBlank(userId)){
            omsCartItems = cartService.cartList(userId);
        }else{
            String cartListCookis = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if(StringUtils.isNotBlank(cartListCookis)){
                omsCartItems = JSON.parseArray(cartListCookis,OmsCartItem.class);
            }
        }
        BigDecimal sum = new BigDecimal("0");
        for (OmsCartItem omsCartItem : omsCartItems) {
            omsCartItem.setTotalPrice(omsCartItem.getPrice().multiply(omsCartItem.getQuantity()));
            if(omsCartItem.getIsChecked().equals("1"))
                sum = sum.add(omsCartItem.getPrice().multiply(omsCartItem.getQuantity()));
        }
        modelMap.put("totalPrice",sum);
        modelMap.put("cartList",omsCartItems);
        return "cartList";
    }

    @RequestMapping("addToCart")
    public String addToCart(String skuId, BigDecimal quantity, HttpServletRequest request, HttpServletResponse response){
        PmsSkuInfo pmsSkuInfo = skuService.getSkuById(skuId);
        OmsCartItem omsCartItem = new OmsCartItem();
        //封装数据
        omsCartItem.setCreateDate(new Date());
        omsCartItem.setDeleteStatus(0);
        omsCartItem.setModifyDate(new Date());
        omsCartItem.setPrice(pmsSkuInfo.getPrice());
        omsCartItem.setProductAttr("");
        omsCartItem.setProductBrand("");
        omsCartItem.setProductCategoryId(pmsSkuInfo.getCatalog3Id());
        omsCartItem.setProductId(pmsSkuInfo.getProductId());
        omsCartItem.setProductName(pmsSkuInfo.getSkuName());
        omsCartItem.setProductPic(pmsSkuInfo.getSkuDefaultImg());
        omsCartItem.setProductSkuCode("1111111111111");
        omsCartItem.setProductSkuId(pmsSkuInfo.getId());
        omsCartItem.setQuantity(quantity);
        omsCartItem.setIsChecked("1");
        String memberId = "1";
        List<OmsCartItem> omsCartItems = new ArrayList<>();

        if(StringUtils.isBlank(memberId)){
            //没有登录
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if(StringUtils.isBlank(cartListCookie)){
                omsCartItems.add(omsCartItem);
            }else{
                omsCartItems = JSON.parseArray(cartListCookie,OmsCartItem.class);
                boolean exist = if_cart_exist(omsCartItems,omsCartItem,quantity);
                if(!exist) {
                    omsCartItems.add(omsCartItem);
                }
            }
            //更新cookie
            CookieUtil.setCookie(request,response,"cartListCookie", JSON.toJSONString(omsCartItems),60*60*72,true);
        }else{
            //从数据库中查询
            OmsCartItem omsCartItemsFromDb = cartService.ifCartExistByUser(memberId,skuId);

            if(omsCartItemsFromDb==null){
                omsCartItem.setMemberId(memberId);
                cartService.addCart(omsCartItem);
            }else{
                omsCartItemsFromDb.setQuantity(omsCartItem.getQuantity().add(omsCartItemsFromDb.getQuantity()));
                cartService.updateCart(omsCartItemsFromDb);
            }

            cartService.flushCartCache(memberId);
        }

        return "redirect:/success.html";
    }

    private boolean if_cart_exist(List<OmsCartItem> omsCartItems, OmsCartItem omsCartItem,BigDecimal quantity) {
        for (OmsCartItem cartItem : omsCartItems) {
            if(cartItem.getProductSkuId().equals(omsCartItem.getProductSkuId())) {
                cartItem.setQuantity(cartItem.getQuantity().add(quantity));
                return true;
            }
        }
        return false;
    }
}
