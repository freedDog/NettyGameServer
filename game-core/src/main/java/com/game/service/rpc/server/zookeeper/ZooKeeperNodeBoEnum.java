package com.game.service.rpc.server.zookeeper;

import com.game.common.enums.BOEnum;
/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月12日 下午7:55:16
 */
public enum ZooKeeperNodeBoEnum {

    WORLD(BOEnum.WORLD, "/world_registry_adress"),
    GAME(BOEnum.GAME, "/game_registry_adress"),
    DB(BOEnum.DB, "/db_registry_adress"),
    ;
    private BOEnum boEnum;
    private String rootPath;

    ZooKeeperNodeBoEnum(BOEnum boEnum, String rootPath) {
        this.boEnum = boEnum;
        this.rootPath = rootPath;
    }

    public BOEnum getBoEnum() {
        return boEnum;
    }

    public void setBoEnum(BOEnum boEnum) {
        this.boEnum = boEnum;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }
}
