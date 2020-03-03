package com.xliu.gmall.service;


import com.xliu.gmall.bean.UmsMember;
import com.xliu.gmall.bean.UmsMemberReceiveAddress;

import java.util.List;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/3/2 10:06
 */
public interface UserService {
    List<UmsMember> getAllUser();

    List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId);
}
