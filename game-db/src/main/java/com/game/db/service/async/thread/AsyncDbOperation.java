package com.game.db.service.async.thread;

import java.lang.reflect.ParameterizedType;
import java.util.TimerTask;

import org.apache.curator.framework.api.GetDataWatchBackgroundStatable;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.game.common.util.StringUtils;
import com.game.db.common.Loggers;
import com.game.db.service.async.transaction.entity.AsyncDBSaveTransactionEntity;
import com.game.db.service.async.transaction.factory.DbGameTransactionCauseFactory;
import com.game.db.service.async.transaction.factory.DbGameTransactionEntityCauseFactory;
import com.game.db.service.async.transaction.factory.DbGameTransactionEntityFactory;
import com.game.db.service.entity.EntityService;
import com.game.db.service.proxy.EntityProxyFactory;
import com.game.db.service.redis.AsyncRedisKeyEnum;
import com.game.db.service.redis.RedisService;
import com.game.db.sharding.EntityServiceShardingStrategy;
import com.redis.transaction.enums.GameTransactionCause;
import com.redis.transaction.enums.GameTransactionCommitResult;
import com.redis.transaction.enums.GameTransactionEntityCause;
import com.redis.transaction.service.RGTRedisService;
import com.redis.transaction.service.TransactionService;
import com.snowcattle.game.thread.executor.NonOrderedQueuePoolExecutor;

/**
 * 异步执行更新中心
 * 这个类采用模版编程
 * @author JiangBangMing
 *
 * 2018年6月21日 上午11:28:34
 */
@Service
public abstract class AsyncDbOperation<T extends EntityService> extends TimerTask {

	private Logger logger=Loggers.dbLogger;
	/**
	 * db里面的redis服务
	 */
	@Autowired
	private RedisService redisService;
	/**
	 * 事务redis服务
	 */
	@Autowired
	private RGTRedisService rgtRedisService;
	/**
	 * 事务服务
	 */
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private DbGameTransactionEntityFactory dbGameTransactionEntityFactory;
	
	@Autowired
	private DbGameTransactionEntityCauseFactory dbGameTransactionEntityCauseFactory;
	
	@Autowired
	private DbGameTransactionCauseFactory dbGameTransactionCauseFactory;
	
	@Autowired
	private EntityProxyFactory entityProxyFactory;
	
	/**
	 * 执行db落得第线程数量
	 */
	private NonOrderedQueuePoolExecutor operationExecutor;
	
	/**
	 * 监视器
	 */
	private AsyncDbOperationMonitor asyncDbOperationMonitor;

	@Override
	public void run() {
		if(logger.isDebugEnabled()) {
			logger.debug("start async db operation");
		}
		asyncDbOperationMonitor.start();
		EntityService entityService=getWrapperEntityService();
		EntityServiceShardingStrategy entityServiceShardingStrategy=entityService.getEntityServiceShardingStrategy();
		int size=entityServiceShardingStrategy.getDbCount();
		for(int i=0;i<size;i++) {
			saveDb(i, entityService);
		}
		asyncDbOperationMonitor.printInfo(this.getClass().getSimpleName());
		asyncDbOperationMonitor.stop();
	}

	public void saveDb(int dbId,EntityService entityService) {
		String simpleClassName=entityService.getEntityTClass().getSimpleName();
		String dbRedisKey=AsyncRedisKeyEnum.ASYNC_DB.getKey()+dbId+"#"+simpleClassName;
		long saveSize=redisService.scardString(dbRedisKey);
		for(long k=0;k<saveSize;k++) {
			String playerKey=redisService.spopString(dbRedisKey);
			if(StringUtils.isEmpty(playerKey)) {
				break;
			}
			//如果性能不够话，这里可以采用countdownlatch,将下面逻辑进行封装，执行多线程更新
			//查找玩家数据进行存储 进行redis-game-transaction 加锁
			GameTransactionEntityCause gameTransactionCause=dbGameTransactionEntityCauseFactory.getAsyncDbSave();
			AsyncDBSaveTransactionEntity asyncDBSaveTransactionEntity=dbGameTransactionEntityFactory.
					createAsyncDBSaveTransactionEntity(gameTransactionCause, rgtRedisService, simpleClassName, playerKey, entityService, redisService);
			asyncDBSaveTransactionEntity.setAsyncDbOperationMonitor(asyncDbOperationMonitor);
			GameTransactionCommitResult commitResult=transactionService.commitTransaction(dbGameTransactionCauseFactory.getAsyncDbSave(),asyncDBSaveTransactionEntity);
			if(!commitResult.equals(GameTransactionCommitResult.SUCCESS)) {
				//如果事务失败，说明没有权限进行数据库存储操作，需要放回去下次继续存储
				redisService.saddStrings(dbRedisKey, playerKey);
			}
			if(logger.isDebugEnabled()) {
				logger.debug("async save success "+playerKey);
			}
		}
	}
	
	/**
	 * 获取模版参数类
	 * @return
	 */
	public Class<T> getEntityTClass(){
		Class classes=getClass();
		Class result=(Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		return result;
	}
	public abstract EntityService getWrapperEntityService();
	
	public RedisService getRedisService() {
		return redisService;
	}

	public void setRedisService(RedisService redisService) {
		this.redisService = redisService;
	}

	public RGTRedisService getRgtRedisService() {
		return rgtRedisService;
	}

	public void setRgtRedisService(RGTRedisService rgtRedisService) {
		this.rgtRedisService = rgtRedisService;
	}

	public TransactionService getTransactionService() {
		return transactionService;
	}

	public void setTransactionService(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	public DbGameTransactionEntityFactory getDbGameTransactionEntityFactory() {
		return dbGameTransactionEntityFactory;
	}

	public void setDbGameTransactionEntityFactory(DbGameTransactionEntityFactory dbGameTransactionEntityFactory) {
		this.dbGameTransactionEntityFactory = dbGameTransactionEntityFactory;
	}

	public DbGameTransactionEntityCauseFactory getDbGameTransactionEntityCauseFactory() {
		return dbGameTransactionEntityCauseFactory;
	}

	public void setDbGameTransactionEntityCauseFactory(
			DbGameTransactionEntityCauseFactory dbGameTransactionEntityCauseFactory) {
		this.dbGameTransactionEntityCauseFactory = dbGameTransactionEntityCauseFactory;
	}



	public DbGameTransactionCauseFactory getDbGameTransactionCauseFactory() {
		return dbGameTransactionCauseFactory;
	}

	public void setDbGameTransactionCauseFactory(DbGameTransactionCauseFactory dbGameTransactionCauseFactory) {
		this.dbGameTransactionCauseFactory = dbGameTransactionCauseFactory;
	}

	public EntityProxyFactory getEntityProxyFactory() {
		return entityProxyFactory;
	}

	public void setEntityProxyFactory(EntityProxyFactory entityProxyFactory) {
		this.entityProxyFactory = entityProxyFactory;
	}

	public NonOrderedQueuePoolExecutor getOperationExecutor() {
		return operationExecutor;
	}

	public void setOperationExecutor(NonOrderedQueuePoolExecutor operationExecutor) {
		this.operationExecutor = operationExecutor;
	}

	public AsyncDbOperationMonitor getAsyncDbOperationMonitor() {
		return asyncDbOperationMonitor;
	}

	public void setAsyncDbOperationMonitor(AsyncDbOperationMonitor asyncDbOperationMonitor) {
		this.asyncDbOperationMonitor = asyncDbOperationMonitor;
	}
	
	
}
