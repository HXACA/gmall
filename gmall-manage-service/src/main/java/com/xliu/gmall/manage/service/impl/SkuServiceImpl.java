package com.xliu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.xliu.gmall.bean.*;
import com.xliu.gmall.manage.mapper.*;
import com.xliu.gmall.service.SkuService;
import com.xliu.gmall.service.SpuService;
import com.xliu.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/3/5 9:10
 */

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;

    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;

    @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public String saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
        try {
            pmsSkuInfoMapper.insertSelective(pmsSkuInfo);

            List<PmsSkuImage> skuImageList = pmsSkuInfo.getSkuImageList();
            for (PmsSkuImage pmsSkuImage : skuImageList) {
                pmsSkuImage.setSkuId(pmsSkuInfo.getId());
                pmsSkuImage.setProductImgId(pmsSkuImage.getSpuImgId());
                pmsSkuImageMapper.insertSelective(pmsSkuImage);
            }

            List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
            for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
                pmsSkuAttrValue.setSkuId(pmsSkuInfo.getId());
                pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
            }

            List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
            for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
                pmsSkuSaleAttrValue.setSkuId(pmsSkuInfo.getId());
                pmsSkuSaleAttrValueMapper.insert(pmsSkuSaleAttrValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
        return "success";
    }

    public PmsSkuInfo getSkuByIdFromDB(String skuId) {
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(skuId);
        PmsSkuInfo skuInfo = pmsSkuInfoMapper.selectOne(pmsSkuInfo);

        //查询图片
        PmsSkuImage pmsSkuImage = new PmsSkuImage();
        pmsSkuImage.setSkuId(skuId);
        List<PmsSkuImage> pmsSkuImages = pmsSkuImageMapper.select(pmsSkuImage);
        skuInfo.setSkuImageList(pmsSkuImages);

        return skuInfo;
    }

    @Override
    public PmsSkuInfo getSkuById(String skuId) {
        Jedis jedis = null;
        try {
            PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
            //链接缓存
            jedis = redisUtil.getJedis();
            //查询缓存
            String skuKey = "sku:"+skuId+":info";
            String skuJson = jedis.get(skuKey);
            if(StringUtils.isNotBlank(skuJson))
                pmsSkuInfo = JSON.parseObject(skuJson, PmsSkuInfo.class);
            else{
                //随机生成数据，当作value传入，避免出现锁过期后再删除了别人的锁
                String token = UUID.randomUUID().toString();
                //设置分布式锁,防止缓存击穿,设置
                String result = jedis.set("sku:" + skuId + ":lock", token, "nx", "px", 10*1000);
                if(StringUtils.isNotBlank(result) && result.equals("OK")){
                    //缓存中没有查询数据库,并存入缓存
                    pmsSkuInfo = getSkuByIdFromDB(skuId);
                    if (pmsSkuInfo!=null){
                        jedis.set(skuKey, JSON.toJSONString(pmsSkuInfo));
                    }else{
                        //防止缓存穿透,保护往数据库查询的语句,3分钟以内不会再访问数据库
                        //缓存穿透指无效的key值重复请求，造成数据库压力
                        int time = new Random().nextInt(4)+1;
                        jedis.setex(skuKey,60*time,JSON.toJSONString(""));
                    }
                    //释放锁
                    String lockToken = jedis.get("sku:" + skuId + ":lock");
                    if(StringUtils.isNotBlank(lockToken) && lockToken.equals(token)){
                        //使用lua脚本执行，在redis中查询的同时执行操作，避免意外
                        String script ="if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                        jedis.eval(script, Collections.singletonList("lock"),Collections.singletonList(token));
                    }

                }else{
                    Thread.sleep(3000);
                    return getSkuById(skuId);
                }
            }
            return pmsSkuInfo;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            if(jedis!=null)
                jedis.close();
        }
    }

    @Override
    public List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String productId) {
        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectSkuSaleAttrValueListBySpu(productId);
        return pmsSkuInfos;
    }

    @Override
    public List<PmsSkuInfo> getAllSku() {
        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectAll();
        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
            String id = pmsSkuInfo.getId();
            PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
            pmsSkuAttrValue.setSkuId(id);
            List<PmsSkuAttrValue> pmsSkuAttrValues = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);
            pmsSkuInfo.setSkuAttrValueList(pmsSkuAttrValues);
        }
        return pmsSkuInfos;
    }
}
