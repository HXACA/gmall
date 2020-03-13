package com.xliu.gmall.cart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/3/13 18:03
 */
@Controller
public class CartController {

    @RequestMapping("addToCart")
    public String addToCart(){
        return "redirect:/success";
    }
}
