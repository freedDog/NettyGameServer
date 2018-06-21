package com.game.db.util;

import java.util.List;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月20日 下午7:36:21
 */
public class PageUtils {
    /**
     * 截取某列表的部分数据
     * @param <T>
     * @param list
     * @param skip
     * @param pageSize
     */
    public static <T> List<T> getSubListPage(List<T> list, int skip, int pageSize) {
        if(list==null||list.isEmpty()){
            return null;
        }
        int startIndex = skip;
        int endIndex = skip+pageSize;
        if(startIndex>endIndex||startIndex>list.size()){
            return null;
        }
        if(endIndex>list.size()){
            endIndex = list.size();
        }
        return list.subList(startIndex, endIndex);
    }
}
