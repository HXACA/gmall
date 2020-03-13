package com.xliu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.xliu.gmall.bean.PmsBaseAttrInfo;
import com.xliu.gmall.bean.PmsBaseAttrValue;
import com.xliu.gmall.bean.PmsBaseSaleAttr;
import com.xliu.gmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.xliu.gmall.manage.mapper.PmsBaseAttrValueMapper;
import com.xliu.gmall.manage.mapper.PmsBaseSaleAttrMapper;
import com.xliu.gmall.service.AttrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Set;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/3/3 13:29
 */

@Service
public class AttrServiceImpl implements AttrService {

    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;

    @Autowired
    PmsBaseAttrValueMapper pmsBaseAttrValueMapper;

    @Autowired
    PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;

    @Override
    public List<PmsBaseAttrInfo> attrInfoList(String catalogId3) {
        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalogId3);
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.select(pmsBaseAttrInfo);
        for (PmsBaseAttrInfo baseAttrInfo : pmsBaseAttrInfos) {
            PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
            pmsBaseAttrValue.setAttrId(baseAttrInfo.getId());
            List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
            baseAttrInfo.setAttrValueList(pmsBaseAttrValues);
        }
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
                pmsBaseAttrInfoMapper.insertSelective(pmsBaseAttrInfo);
                for (PmsBaseAttrValue pmsBaseAttrValue : pmsBaseAttrValues) {
                    pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());
                    pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue);
                }
            }else{
                //修改操作
                Example e = new Example(PmsBaseAttrInfo.class);
                e.createCriteria().andEqualTo("id",id);//根据Id匹配
                pmsBaseAttrInfoMapper.updateByExampleSelective(pmsBaseAttrInfo,e);
                PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
                pmsBaseAttrValue.setAttrId(id);
                pmsBaseAttrValueMapper.delete(pmsBaseAttrValue);
                for (PmsBaseAttrValue pmsAttrValue : pmsBaseAttrValues) {
                    pmsAttrValue.setAttrId(id);
                    pmsBaseAttrValueMapper.insertSelective(pmsAttrValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
        return "success";
    }

    @Override
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {
        PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
        pmsBaseAttrValue.setAttrId(attrId);
        List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
        return pmsBaseAttrValues;
    }

    @Override
    public List<PmsBaseSaleAttr> baseSaleAttrList() {
        return pmsBaseSaleAttrMapper.selectAll();
    }

    @Override
    public List<PmsBaseAttrInfo> getAttrValueListByValueId(Set<String> valueIdSet) {
        String valueIdStr = StringUtils.join(valueIdSet, ",");
        if(StringUtils.isNotBlank(valueIdStr))
            return pmsBaseAttrInfoMapper.selectAttrValueListByValueId(valueIdStr);
        else
            return null;
    }
}
