package com.augurit.am.fw.db.liteorm.kvdb;

import java.util.List;

/**
 * 数据操作定义
 * 
 * @author mty
 * @date 2013-6-2上午2:37:56
 */
public interface DataCache<K, V> {
	
	Object save(K key, V data);

	Object delete(K key);

	Object update(K key, V data);

	List<V> query(String arg);
}
