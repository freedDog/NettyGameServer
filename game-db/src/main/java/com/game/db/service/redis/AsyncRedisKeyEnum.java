package com.game.db.service.redis;

/**
 * 异步处理的 redis key
 * @author JiangBangMing
 *
 * 2018年6月20日 下午5:55:34
 */
public enum AsyncRedisKeyEnum {
    ASYNC_DB("ay_db#"),
    ;

    private String key;

    AsyncRedisKeyEnum(String key){
        this.key = key;
    }

    public static AsyncRedisKeyEnum getAsyncRedisKeyEnum(String key){
        AsyncRedisKeyEnum result = null;
        for(AsyncRedisKeyEnum temp: AsyncRedisKeyEnum.values()){
            if(temp.getKey().equals(key)){
                result = temp;
                break;
            }
        }
        return result;
    }

    public String getKey(){
        return this.key;
    }
}
