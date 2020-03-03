package com.xliu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.xliu.gmall.bean.PmsBaseAttrInfo;
import com.xliu.gmall.bean.PmsBaseAttrValue;
import com.xliu.gmall.manage.mapper.PmsAttrInfoMapper;
import com.xliu.gmall.manage.mapper.PmsAttrValueMapper;
import com.xliu.gmall.service.AttrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/3/3 13:29
 */

@Service
public class AttrServiceImpl implements AttrService {

    @Autowired
    PmsAttrInfoMapper pmsAttrInfoMapper;

    @Autowired
    PmsAttrValueMapper pmsAttrValueMapper;

    @Override
    public List<PmsBaseAttrInfo> attrInfoList(String catalogId3) {
        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalogId3);
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsAttrInfoMapper.select(pmsBaseAttrInfo);
        return pmsBaseAttrInfos;
    }

    @Override
    public String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {
        try {
            String id = pmsBaseAttrInfo.getId();
            List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseAttrInfo.getAttrValueList();
            //insert会插入空值，insertSelective只插入非空值
            if (StringUtils.isBlank(id)) {
                //id为空保存操作
                pmsAttrInfoMapper.insertSelective(pmsBaseAttrInfo);
                for (PmsBaseAttrValue pmsBaseAttrValue : pmsBaseAttrValues) {
                    pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());
                    pmsAttrValueMapper.insertSelective(pmsBaseAttrValue);
                }
            }else{
                //修改操作
                Example e = new Example(PmsBaseAttrInfo.class);
                e.createCriteria().andEqualTo("id",id);//根据Id匹配
                pmsAttrInfoMapper.updateByExampleSelective(pmsBaseAttrInfo,e);
                PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
                pmsBaseAttrValue.setAttrId(id);
                pmsAttrValueMapper.delete(pmsBaseAttrValue);
                for (PmsBaseAttrValue pmsAttrValue : pmsBaseAttrValues) {
                    pmsAttrValue.setAttrId(id);
                    pmsAttrValueMapper.insertSelective(pmsAttrValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("false");
            return "false";
        }
        return "success";
    }

    @Override
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {
        PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
        pmsBaseAttrValue.setAttrId(attrId);
        List<PmsBaseAttrValue> pmsBaseAttrValues = pmsAttrValueMapper.select(pmsBaseAttrValue);
        return pmsBaseAttrValues;
    }
}
