package com.saltware.enface.cache;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * 데이터를 캐시하는 캐시매니저
 * 
 * @author smna
 * 
 */
public class DataCacheMngForMenu implements Serializable{

	private class DataCacheEntry implements Serializable{
		// 데이터 생성시간(캐시시간)
		private long createTime = 0;
		// 데이터
		private Object data = null;

		public DataCacheEntry() {
		}

		public DataCacheEntry(Object data) {
			this.createTime = System.currentTimeMillis();
			this.data = data;
		}

		public long getCreateTime() {
			return createTime;
		}

		public void setCreateTime(long createTime) {
			this.createTime = createTime;
		}

		public Object getData() {
			return data;
		}

		public void setData(Object data) {
			this.data = data;
		}
	}

	private long expireTime = 0;
	private Hashtable<String, DataCacheEntry> dataCache = new Hashtable<String, DataCacheEntry>();

	/**
	 * 데이터 캐시매니저 생성자
	 * 
	 * @param expireTime
	 *            폐기시간
	 */
	public DataCacheMngForMenu(long expireTime) {
		this.expireTime = expireTime;
	}

	/**
	 * 캐시에서 데이터를 가져온다. 데이터가 존재하지 않거나 폐기사간이 지났으면 null을 리턴한다.
	 * 
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		DataCacheEntry dataCacheEntry = null;
		
		if(dataCache.size() > 30) {
			dataCache.clear();
		}
		dataCacheEntry = dataCache.get(key);
		
		if (dataCacheEntry == null) {
			return null;
		} else {
			if (System.currentTimeMillis() - dataCacheEntry.getCreateTime() > expireTime) {
				dataCache.remove(key);
				return null;
			} else {
				return dataCacheEntry.getData();
			}
		}
	}

	/**
	 * 캐시에 데이터를 넣는다.
	 * 
	 * @param key
	 * @return
	 */
	public void put(String key, Object value) {
		dataCache.put(key, new DataCacheEntry(value));
	}
}
