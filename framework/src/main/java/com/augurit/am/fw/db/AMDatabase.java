package com.augurit.am.fw.db;

import android.content.Context;


import com.augurit.am.fw.db.liteorm.LiteOrm;
import com.augurit.am.fw.db.liteorm.db.DataBaseConfig;
import com.augurit.am.fw.db.liteorm.db.assit.QueryBuilder;
import com.augurit.am.fw.db.liteorm.db.assit.WhereBuilder;
import com.augurit.am.fw.db.liteorm.db.model.ColumnsValue;
import com.augurit.am.fw.db.liteorm.db.model.ConflictAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  AMDatabase类，封装LiteOrm，提供基本的数据操作
 *
 * Created by GuoKunHu on 2016-07-17.
 */
public class AMDatabase {

    //默认数据库名字
    public static String DEFAULT_DB_NAME = "amdatabase";

    //默认数据库版本
    public static int DEFAULT_DB_VERSION = 1;

    //数据库密码
    private static String DEFAULT_DB_PASSWORD = "agmobile";

    //liteOrm对象
    public static LiteOrm liteOrm;

    //单例
    public static AMDatabase instance;

    private AMDatabase(){

    }

    /**
     * 初始化数据，创建一个数据库
     * @param context
     * @param name
     */
    public static void init(Context context,String name,int version, String password){
        String db_name = name +".db";
        DataBaseConfig config = new DataBaseConfig(context, db_name, password);
        //打开字记录
        config.debugged = true;
        //数据库版本
        config.dbVersion = version;
        //设置数据库更新监听器
        config.onUpdateListener = null;

        //使用级联操作创建 liteOrm
        liteOrm = LiteOrm.newCascadeInstance(config);
    }


    /**
     * 初始化数据库，创建一个数据库
     * @param context
     */
    public static void init(Context context){
        init(context, DEFAULT_DB_NAME, DEFAULT_DB_VERSION, DEFAULT_DB_PASSWORD);
    }

    /**
     * 获取单例数据库对象
     * @return
     */
    public static AMDatabase getInstance(){
        if(instance == null){
            synchronized (AMDatabase.class){
                if(instance == null){
                    instance = new AMDatabase();
                }
            }
        }
        return instance;
    }

    /**
     * 设置调试模式，开发版本打开可输出日志
     * 正式版关闭可提供性能
     * @param isOpen
     */
    public void setDebugged(boolean isOpen){
        liteOrm.setDebugged(isOpen);
    }

    /**
     * 获取LiteOrm
     * @return LiteOrm对象
     */
    public LiteOrm getLiteOrm(){
        if (liteOrm == null)
            throw new RuntimeException("AMDatabase should be inited before !");
        return liteOrm;
    }

    /**
     * 保存一条记录 （插入or更新）
     * @param t  数据实体类对象
     * @param <T>
     */
    public <T> void save(T t){
        liteOrm.save(t);
    }

    /**
     * 保存所有记录（插入or更新）
     * @param list 数据实体类对象列表
     * @param <T>
     */
    public <T> void saveAll(List<T> list){
        liteOrm.save(list);
    }

    /**
     * 插入一条记录
     * @param t 数据实体类对象
     * @param <T>
     */
    public <T> long insert(T t){
        return liteOrm.insert(t);
    }

    /**
     * 插入所有记录
     * @param list 数据实体类对象列表
     * @param <T>
     */
    public <T> void insertAll(List<T> list){
        liteOrm.insert(list);
    }

    /**
     * 查询所有
     * @param clazz 查询表类型
     * @param <T>
     * @return
     */
    public <T> List<T> getQueryAll(Class<T> clazz){
        return liteOrm.query(clazz);
    }

    /**
     * 查询某字段等于value的值
     * @param clazz 表类型
     * @param field 表字段
     * @param value  字段值
     * @param <T>
     * @return
     */
    public <T> List<T> getQueryByWhere(Class<T> clazz,String field,String value){
        return liteOrm.query(new QueryBuilder<T>(clazz).where(field + "=?", value));
    }

    /**
     * 查询某字段等于 Value的值  可以指定从1-20，就是分页
     * @param clazz 表类型
     * @param field 表字段
     * @param value 字段值
     * @param start  字段记录起始位置
     * @param end 字段记录截止位置
     * @param <T>
     * @return
     */
    public  <T> List<T> getQueryByWhereLength(Class<T> clazz,String field,String value,int start,int end){
        return liteOrm.query(new QueryBuilder<T>(clazz).where(field + "=?", value).limit(start, end));
    }

    /**
     * 根据ID来查询
     * @param clazz 表类型
     * @param id  表中记录id
     * @param <T>
     * @return
     */
    public <T>  T getQueryById(Class<T> clazz,long id){
        return liteOrm.queryById(id, clazz);
    }

    /**
     * 根据ID来查询
     * @param clazz 表类型
     * @param id  表中记录id
     * @param <T>
     * @return
     */
    public <T>  T getQueryById(Class<T> clazz,String id){
        return liteOrm.queryById(id, clazz);
    }

    public <T> ArrayList<T> query(AMQueryBuilder<T> amQueryBuilder){
        QueryBuilder<T> queryBuilder = QueryBuilder.create(amQueryBuilder.getQueryClass());
        queryBuilder.queryMappingInfo(amQueryBuilder.getClazzMapping());
        queryBuilder.distinct(amQueryBuilder.isDistinct());
        queryBuilder.columns(amQueryBuilder.getColumns());
        queryBuilder.groupBy(amQueryBuilder.getGroup());
        queryBuilder.having(amQueryBuilder.getHaving());
        queryBuilder.orderBy(amQueryBuilder.getOrder());
        queryBuilder.limit(amQueryBuilder.getLimit());
        AMWhereBuilder amWhereBuilder = amQueryBuilder.getWhereBuilder();
        queryBuilder.where(WhereBuilder.create(amWhereBuilder.getTableClass(),
                amWhereBuilder.getWhere(), amWhereBuilder.getWhereArgs()));
        return liteOrm.query(queryBuilder);
    }

    /**
     * 删除某项数据实体记录
     * @param t 数据实体对象
     * @param <T>
     */
    public <T> void delete(T t){
        liteOrm.delete(t);
    }

    /**
     * 删除所有 某字段等于 Vlaue的值
     * @param clazz Class
     * @param field  表字段
     * @param value 表字段值
     * @param <T> 表类型
     */
    public  <T> void deleteWhere(Class<T> clazz,String field,String value){
        liteOrm.delete(WhereBuilder.create(clazz).where(field + "=?", value));
    }

    /**
     * 根据条件删除记录
     * @param whereBuilder where条件
     * @param <T> 表类型
     * @return 受影响的行数
     */
    public <T> void deleteWhere(AMWhereBuilder whereBuilder){
        liteOrm.delete(WhereBuilder.create(whereBuilder.getTableClass(), whereBuilder.getWhere(), whereBuilder.whereArgs));
    }

    /**
     * 删除整张表
     * @param clazz 表类型
     * @param <T>
     */
    public  <T> void deleteAll(Class<T> clazz){
        liteOrm.deleteAll(clazz);
    }

    /**
     * 根据id排序范围删除指定数量
     * 按id升序，删除[2, size-1]，结果：仅保留第一个和最后一个，最后一个参数可为null，默认按 id 升序排列
     * @param clazz 表类型
     * @param start 起始位置
     * @param end   截止位置
     * @param field id字段名字
     * @param <T>
     */
    public <T> void deleteByCount(Class<T> clazz,long start,long end,String field){
        liteOrm.delete(clazz, start, end, field);
    }

    /**
     * 模糊匹配删除
     * @param clazz 表类型
     * @param field 表字段
     * @param like  匹配字符串
     * @param <T>
     */
    public <T> void deleteByLike(Class<T> clazz,String field,String like){
        liteOrm.delete(new WhereBuilder(clazz)
        .where(field + " LIKE ?", like));
    }

    /**
     * 根据ID大小范围来删除
     * @param clazz 表类型
     * @param formId 起始字段id
     * @param toId   截止字段id
     * @param idFiled 表id字段名称
     * @param <T>
     */
    public <T> void deleteById(Class<T> clazz,long formId,long toId,String idFiled){
        liteOrm.delete(new WhereBuilder(clazz)
        .greaterThan(idFiled,formId)
                .and()
                .lessThan(idFiled, toId));
    }

    /**
     * 仅在以存在时更新
     * @param t 更新的实体类数据对象
     * @param <T>
     */
    public  <T> void update(T t){
        liteOrm.update(t, ConflictAlgorithm.Replace);
    }

    /**
     * 更新所有
     * @param list 更新的实体类数据对象列表
     * @param <T>
     */
    public  <T> void updateALL(List<T> list){
        liteOrm.update(list);
    }

    /**
     * 更新指定列
     * @param list
     * @param map
     * @param <T>
     */
    public <T> void updateByCol(List<T> list,Map<String,Object> map){
        liteOrm.update(list,new ColumnsValue(map), ConflictAlgorithm.Fail);
    }

    /**
     * 删除数据库
     */
    public void deleteDatabase(){
        liteOrm.deleteDatabase();
    }


    /**
     * 关闭数据库
     */
    public void closeDatabase(){
        liteOrm.close();
    }

}
