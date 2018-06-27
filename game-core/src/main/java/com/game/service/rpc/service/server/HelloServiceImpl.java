package com.game.service.rpc.service.server;

import org.springframework.stereotype.Repository;

import com.game.common.annotation.RpcServiceAnnotation;
import com.game.common.annotation.RpcServiceBoEnum;
import com.game.common.enums.BOEnum;
import com.game.service.rpc.service.client.HelloService;

@RpcServiceAnnotation(HelloService.class)
@RpcServiceBoEnum(bo=BOEnum.WORLD)
@Repository
public class HelloServiceImpl implements HelloService{

	@Override
	public String hello(String name) {
		System.out.println("====>> Hello!"+name);
		return "Hello! " + name;
	}

}
