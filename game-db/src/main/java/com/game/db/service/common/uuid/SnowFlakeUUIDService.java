package com.game.db.service.common.uuid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.game.db.common.DbServiceName;
import com.game.db.service.common.service.IDbService;
import com.game.db.service.config.DbConfig;

/**
 * 低15位为同一时间序列号32768，中间10位为服务器节点最大为1024，高38位为当前时间开始时间的差值
 * （1L<<38）/(1000L*60*60*24*365)可以用8年
 * @author JiangBangMing
 *
 * 2018年6月21日 上午10:47:05
 */
@Service
public class SnowFlakeUUIDService implements IUUIDService,IDbService{
	/**开始时间戳(2017-01-01*/
	private final long twepoch=1483200000000L;

	/**node id 所占的位数*/
	private final long nodeIdBits=10L;
	
	/**支持的最大机器nodeid,结果是1024（这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数*/
	private final long maxNodeId=-1L^(-1L<<nodeIdBits);
	
	/**序列在id中的位数*/
	private final long sequenceBits=15L;
	
	/**时间戳向左移（10+15）*/
	private final long timestampLeftShift=sequenceBits+nodeIdBits;
	/**生成序列的掩码，这里是4095*8 */
	private final long sequenceMask=-1L^(-1L<<sequenceBits);
	//序列号
	private long sequence;
	//生产的时间(毫秒)
	private long referenceTime;
	//节点号（0-1024）
	private int nodeId;
	
	@Autowired
	private DbConfig dbConfig;
	//用于spring构造
	public SnowFlakeUUIDService() {
	}
	
	public SnowFlakeUUIDService(int nodeId) {
		if(nodeId<0||nodeId>maxNodeId) {
			throw new IllegalArgumentException(String.format("node must be between %s and %s", 0,maxNodeId));
		}
		this.nodeId=nodeId;
	}
	@Override
	public String getDbServiceName() {
		return DbServiceName.uuidService;
	}

	@Override
	public void startUp() throws Exception {
		setNodeId(dbConfig.getDbId());
	}

	@Override
	public void shutdown() throws Exception {
		
	}

	@Override
	public long nextId() {
		long timestamp=timeGen();
		//如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
		if(timestamp<referenceTime) {
			throw new RuntimeException(
					String.format("Clock moved backwards. Refusing to generate id for %d milliseconds.",referenceTime-timestamp));
		}
		//如果是同一时间生成的，则进行毫秒内序列
		if(referenceTime==timestamp) {
			sequence=(sequence+1)&sequenceMask;
			//毫秒内序列溢出
			if(sequence==0) {
				//阻塞到下一个毫秒，获取新的时间戳
				timestamp=tilNextMillis(referenceTime);
				if(timestamp<=referenceTime) {
					throw new RuntimeException(
							String.format("Clock moved backwards. Refusing to generate id for %d milliseconds.",referenceTime-timestamp));
				}
			}
		}else {
			//时间戳改变，毫秒内序列重置
			sequence=0L;
		}
		//上次生成的ID的时间戳
		referenceTime=timestamp;
		return (referenceTime-twepoch) << timestampLeftShift | nodeId << sequenceBits | sequence;
	}
	
	public void setNodeId(int nodeId) {
        if (nodeId < 0 || nodeId > maxNodeId) {
            throw new IllegalArgumentException(String.format("node must be between %s and %s", 0, maxNodeId));
        }
        this.nodeId = nodeId;
	}
	
	
	public DbConfig getDbConfig() {
		return dbConfig;
	}

	public void setDbConfig(DbConfig dbConfig) {
		this.dbConfig = dbConfig;
	}

	/**
	 * 返回以毫秒为单位的当前时间
	 * @return 当前时间(毫秒)
	 */
	protected long timeGen() {
		return System.currentTimeMillis();
	}
	
	protected long tilNextMillis(long lastTimestamp) {
		long timestamp=timeGen();
		while(timestamp<=lastTimestamp) {
			timestamp=timeGen();
		}
		return timestamp;
	}
}
