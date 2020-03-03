package com.xliu.gmall.user.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.xliu.gmall.bean.UmsMember;
import com.xliu.gmall.bean.UmsMemberReceiveAddress;
import com.xliu.gmall.service.UserService;
import com.xliu.gmall.user.mapper.UmsMemberMapper;
import com.xliu.gmall.user.mapper.UmsMemberReceivedAddressMapper;
import org.springframework.beans.factory.annotation.Autowired;

import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/3/2 10:07
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UmsMemberMapper umsMemberMapper;

    @Autowired
    UmsMemberReceivedAddressMapper umsMemberReceivedAddressMapper;

    @Override
    public List<UmsMember> getAllUser() {
        List<UmsMember> umsMembers = umsMemberMapper.selectAll();
        return umsMembers;
    }

    @Override
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId) {
        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setMemberId(memberId);
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = umsMemberReceivedAddressMapper.select(umsMemberReceiveAddress);
//        Example example = new Example(UmsMemberReceiveAddress.class);
//        //此处为映射对象，所以为驼峰，不是 _
//        example.createCriteria().andEqualTo("memberId",memberId);
//        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = umsMemberReceivedAddressMapper.selectByExample(example);
        return umsMemberReceiveAddresses;
    }
}
