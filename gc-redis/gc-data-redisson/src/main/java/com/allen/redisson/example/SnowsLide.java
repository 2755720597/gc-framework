package com.allen.redisson.example;

import cn.hutool.core.util.StrUtil;
import com.allen.redisson.mapper.SysUserMapper;
import com.allen.redisson.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xuguocai on 2021/6/4 16:43  缓存雪崩
 */
public class SnowsLide {

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 缓存雪崩  使用锁处理。加锁排队只是为了减轻数据库的压力，并没有提高系统吞吐量。
     * @return
     */
    public String snowsLide(){
        int cacheTime = 30;
        String cacheKey = "product_list";
        String lockKey = cacheKey;

        String cacheValue = redisRepository.get(cacheKey);
        if (StrUtil.isNotBlank(cacheValue)) {
            return cacheValue;
        } else {
            // 加锁
            synchronized(lockKey) {
                cacheValue = redisRepository.get(cacheKey);
                if (cacheValue != null) {
                    return cacheValue;
                } else {
                    //这里一般是sql查询数据
                    cacheValue = sysUserMapper.selectAll().toString();
                    redisRepository.setExpire(cacheKey, cacheValue, cacheTime);
                }
            }
            return cacheValue;
        }
    }

    /**
     * 设置过期标志更新缓存
     * @return
     */
    public String etProductListNew() {
        int cacheTime = 30;
        String cacheKey = "product_list";
        //缓存标记
        String cacheSign = cacheKey + "_sign";
        String sign = redisRepository.get(cacheSign);
        //获取缓存值
        String cacheValue = redisRepository.get(cacheKey);
        if (StrUtil.isNotBlank(sign)) {
            return cacheValue; //未过期，直接返回
        } else {
            redisRepository.setExpire(cacheSign,"1",cacheTime);
            //这里一般是 sql查询数据
            cacheValue = sysUserMapper.selectAll().toString();
            //日期设缓存时间的2倍，用于脏读
            redisRepository.setExpire(cacheKey,cacheValue,cacheTime*2);
            return cacheValue;
        }
    }

    /**
     * 队列处理
     * @param cacheKey
     * @return
     */
    public String producer(String cacheKey){
        String cacheValue = redisRepository.get(cacheKey);
        if (StrUtil.isNotBlank(cacheValue)) {
            return cacheValue;
        } else {
            // 丢到 队列
            kafkaTemplate(cacheKey);
            // 返回默认值
            return "";
        }
    }

    private void kafkaTemplate(String cacheKey){
        // 队列处理 如MQ、Kafka
    }

    //@KafkaListener(topics = {"qywxopen-contactUserMsg"}, groupId = "wxGroup")
    public void cosumer(String xmlStr){
        int cacheTime = 30;
        // 解析，此处demo不详解
        String cacheKey = xmlStr;
        if (StrUtil.isBlank(cacheKey)){
            return;
        }
        String cacheValue = sysUserMapper.selectAll().toString();
        if (StrUtil.isBlank(cacheValue)){
            return;
        }
        redisRepository.setExpire(cacheKey, cacheValue, cacheTime);

    }

}
