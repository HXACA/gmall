package com.xliu.gmall.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xliu.gmall.bean.UmsMember;
import com.xliu.gmall.bean.UmsMemberReceiveAddress;
import com.xliu.gmall.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/3/2 10:06
 */

@Controller
public class UserController {
        @Reference
        UserService userService;

        @RequestMapping("index")
        @ResponseBody
        public String index(){
            return "hello user";
        }

        @RequestMapping("getAllUser")
        @ResponseBody
        public List<UmsMember> getAllUser(){
                List<UmsMember> umsMembers = userService.getAllUser();
                return umsMembers;
        }

//        @RequestBody 接收Json参数
        @RequestMapping("getReceiveAddressByMemberId")
        @ResponseBody
        public List<UmsMemberReceiveAddress>getReceiveAddressByMemberId(String memberId){
                List<UmsMemberReceiveAddress>umsMemberAddresses = userService.getReceiveAddressByMemberId(memberId);
                return umsMemberAddresses;
        }
}
