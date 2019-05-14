package com.bee.supply.chain.finance.common.utils;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * notes 对象工具类
 * author junyang.li
 * create 2018/11/2 0002 15:40
 **/
public class BeanUtils {

    /**
     * 通过 tClass 生成示例并且将 source 对应的字段 copy
     * @param source 源对象
     * @param tClass 期望对象类型
     * @param <S> 源对象
     * @param <T> 期望对象
     * @return 期望对象实例
     */
    public static <S,T> T copyProperties(S source, Class<T> tClass) {
        try {
            T target = tClass.newInstance();
            if (null == source) {
                return target;
            }
            org.springframework.beans.BeanUtils.copyProperties(source, target);
            return target;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException( String.format("%s must declaring none arguments constructor!", tClass.getTypeName()));
        }
    }

    /**
     * 根据类型生成，生成转换的新列表
     *
     * @param tClass 目标列表类型
     * @param list   原列表数据
     * @param <S>    source class
     * @param <T>    target class
     * @return 目标列表
     */
    public static <S, T> List<T> assemble(Class<T> tClass, List<S> list){
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<T> lists=new ArrayList<>(list.size());
        for (S s : list) {
            lists.add(BeanUtils.copyProperties(s, tClass));
        }
        return lists;
    }

    /**
     * 提供源数据列表和转换规则，生成新的列表
     *
     * @param list              原列表
     * @param transferInterface 转换规则接口实现
     * @param <S>   source class
     * @param <T>   target class
     * @return 转换后的列表
     */
    public static <S, T> List<T>  assemble(List<S> list, TransferInterface<S, T> transferInterface){
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<T> lists=new ArrayList<>(list.size());
        for (S s : list) {
            lists.add(transferInterface.transfer(s));
        }
        return lists;
    }

    public interface TransferInterface<S, T>  {
        T transfer(S s);
    }
}
