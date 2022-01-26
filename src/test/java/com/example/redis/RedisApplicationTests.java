package com.example.redis;

import com.example.redis.pojo.User;
import com.example.redis.util.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.*;

@SpringBootTest
class RedisApplicationTests {
    @Autowired
    private RedisUtils redisUtils;

    //                                                 类型String                                                        //
    //===================================================================================================================//
    @Test
    void testPutOrGetString() {
        redisUtils.set("name", "yh");
        String value = (String) redisUtils.get("name");
        System.out.println("get name value:" + value);
        redisUtils.set("name", "yh,update");
        System.out.println("after update get name value:" + (String) redisUtils.get("name"));
        redisUtils.del("name");
    }

    @Test
    public void testPutOrGetObject() {
        User user = new User();
        user.setUserId(2);
        user.setUserName("yanghe");
        user.setUserSex("M");
        user.setUserAddress("北京市海淀区");
        redisUtils.set("userId2", user);
        User result = (User) redisUtils.get("userId2");
        System.out.println(result.toString());
        redisUtils.del("userId2");
    }

    //                                                 类型Hash                                                          //
    //===================================================================================================================//

    @Test
    public void testPutOrGetHash() {
        HashMap<String, Object> info = new HashMap<>();
        info.put("userId", "1");
        info.put("name", "yanghe1");
        info.put("age", 17);
        redisUtils.hmset("userId1", info);
        Map<Object, Object> result = redisUtils.hmget("userId1");
        System.out.println("get result info:");
        result.forEach((key, value) -> {
            System.out.println(key + "---" + value);
        });
        /**
         * 用户年龄加1
         */
        redisUtils.hincr("userId1", "age", 1);
        /**
         * 新增一个字段地址
         */
        redisUtils.hset("userId1","address","北京市海淀区");
        Map<Object, Object> resultCr = redisUtils.hmget("userId1");
        System.out.println("after update get result info:");
        resultCr.forEach((key, value) -> {
            System.out.println(key + "---" + value);
        });
        redisUtils.del("userId1");
    }


    //                                                 类型List                                                          //
    //===================================================================================================================//
    @Test
    public void testPutOrGetList() {
        redisUtils.lSet("list", "A");
        List<Object> list = redisUtils.lGet("list", 0, -1);
        System.out.println(list);
        redisUtils.lSet("list", Arrays.asList("B", "C", "D"));
        list = redisUtils.lGet("list", 0, -1);
        System.out.println(list);
        redisUtils.del("list");
    }

    //                                                 类型Set                                                           //
    //===================================================================================================================//
    @Test
    public void testPutOrGetSet() {
        redisUtils.sSet("set", "A", "B", "C");
        Set<Object> set1 = redisUtils.sGet("set");
        System.out.println(set1);
        System.out.println("A is member:" + redisUtils.sIsMember("set", "A"));
        System.out.println("F is member:" + redisUtils.sIsMember("set", "F"));
        redisUtils.setRemove("set", "A");
        Set<Object> set2 = redisUtils.sGet("set");
        System.out.println(set2);
        redisUtils.del("set");
    }

    //                                                 类型zSet                                                           //
    //===================================================================================================================//
    @Test
    public void testPutOrGetZSet() {
        redisUtils.add("zset", "A", 10);
        redisUtils.add("zset", "B", 9);
        redisUtils.add("zset", "C", 8);
        redisUtils.add("zset", "D", 7);
        // 根据分数从小到大排名
        Set<Object> range = redisUtils.range("zset", 0, -1);
        System.out.println(range);
        redisUtils.removeZset("zset", "A", "B");

        // 根据分数从小到大排名带分数
        Set<ZSetOperations.TypedTuple<Object>> zset = redisUtils.rangeWithScores("zset", 0, -1);
        zset.forEach(objectTypedTuple -> {
            System.out.println(objectTypedTuple.getValue() + "---" + objectTypedTuple.getScore());
        });
        redisUtils.del("zset");
    }
}
