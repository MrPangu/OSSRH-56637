package cn.pigicutils.core.lang;

import cn.pigicutils.core.bean.BeanPath;
import cn.pigicutils.core.bean.BeanUtil;
import cn.pigicutils.core.bean.copier.CopyOptions;
import cn.pigicutils.core.collection.CollectionUtil;
import cn.pigicutils.core.convert.Convert;
import cn.pigicutils.core.exceptions.UtilException;
import cn.pigicutils.core.getter.BasicTypeGetter;
import cn.pigicutils.core.lang.func.ConvertFun;
import cn.pigicutils.core.util.ObjectUtil;
import cn.pigicutils.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pigic.hzeropigic.api.vo.UserVO;
import com.pigic.hzeropigic.feign.PigicIamFeignClient;
import com.pigic.hzeropigic.feign.PigicPlatFormFeignClient;
import com.pigic.hzeropigic.infra.constant.Constants;
import com.pigic.hzeropigic.utils.SpringUtils;
import io.choerodon.core.exception.CommonException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 字典对象，扩充了HashMap中的方法
 * 
 * @author guchang.pan@hand-china.com
 *
 */
public class Dict extends LinkedHashMap<String, Object> implements BasicTypeGetter<String> {
	private static final long serialVersionUID = 6135423866861206530L;

	static final float DEFAULT_LOAD_FACTOR = 0.75f;
	static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16
	
	/** 是否大小写不敏感 */
	private boolean caseInsensitive;

	// --------------------------------------------------------------- Static method start
	/**
	 * 创建Dict
	 * 
	 * @return Dict
	 */
	public static Dict create() {
		return new Dict();
	}


	/**
	 * 将PO对象转为Dict
	 * 
	 * @param <T> Bean类型
	 * @param bean Bean对象
	 * @return Vo
	 */
	public static <T> Dict parse(T bean) {
		return create().parseBean(bean);
	}

	// --------------------------------------------------------------- Static method end

	// --------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 */
	public Dict() {
		this(false);
	}
	
	/**
	 * 构造
	 * 
	 * @param caseInsensitive 是否大小写不敏感
	 */
	public Dict(boolean caseInsensitive) {
		this(DEFAULT_INITIAL_CAPACITY, caseInsensitive);
	}

	/**
	 * 构造
	 * 
	 * @param initialCapacity 初始容量
	 */
	public Dict(int initialCapacity) {
		this(initialCapacity, false);
	}
	
	/**
	 * 构造
	 * 
	 * @param initialCapacity 初始容量
	 * @param caseInsensitive 是否大小写不敏感
	 */
	public Dict(int initialCapacity, boolean caseInsensitive) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR, caseInsensitive);
	}
	
	/**
	 * 构造
	 * 
	 * @param initialCapacity 初始容量
	 * @param loadFactor 容量增长因子，0~1，即达到容量的百分之多少时扩容
	 */
	public Dict(int initialCapacity, float loadFactor) {
		this(initialCapacity, loadFactor, false);
	}

	/**
	 * 构造
	 * 
	 * @param initialCapacity 初始容量
	 * @param loadFactor 容量增长因子，0~1，即达到容量的百分之多少时扩容
	 * @param caseInsensitive 是否大小写不敏感
	 *
	 */
	public Dict(int initialCapacity, float loadFactor, boolean caseInsensitive) {
		super(initialCapacity, loadFactor);
		this.caseInsensitive = caseInsensitive;
	}

	/**
	 * 构造
	 * 
	 * @param m Map
	 */
	public Dict(Map<String, Object> m) {
		super((null == m) ? new HashMap<String, Object>() : m);
	}
	// --------------------------------------------------------------- Constructor end

	/**
	 * 转换为Bean对象
	 * 
	 * @param <T> Bean类型
	 * @param bean Bean
	 * @return Bean
	 */
	public <T> T toBean(T bean) {
		return toBean(bean, false);
	}


	/**
	 * 转换为Bean对象
	 * 
	 * @param <T> Bean类型
	 * @param bean Bean
	 * @return Bean
	 *
	 */
	public <T> T toBeanIgnoreCase(T bean) {
		BeanUtil.fillBeanWithMapIgnoreCase(this, bean, false);
		return bean;
	}

	/**
	 * 转换为Bean对象
	 * 
	 * @param <T> Bean类型
	 * @param bean Bean
	 * @param isToCamelCase 是否转换为驼峰模式
	 * @return Bean
	 */
	public <T> T toBean(T bean, boolean isToCamelCase) {
		BeanUtil.fillBeanWithMap(this, bean, isToCamelCase, false);
		return bean;
	}

	/**
	 * 转换为Bean对象,并使用驼峰法模式转换
	 * 
	 * @param <T> Bean类型
	 * @param bean Bean
	 * @return Bean
	 */
	public <T> T toBeanWithCamelCase(T bean) {
		BeanUtil.fillBeanWithMap(this, bean, true, false);
		return bean;
	}

	public <T> T toBeanWithCamelCase(Class<T> clazz) {
		T bean = null;
		try {
			bean = clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		BeanUtil.fillBeanWithMap(this, bean, true, false);
		return bean;
	}

	/**
	 * 填充Value Object对象
	 * 
	 * @param <T> Bean类型
	 * @param clazz Value Object（或者POJO）的类
	 * @return vo
	 */
	public <T> T toBean(Class<T> clazz) {
		return BeanUtil.mapToBean(this, clazz, false);
	}

	/**
	 * 填充Value Object对象，忽略大小写
	 * 
	 * @param <T> Bean类型
	 * @param clazz Value Object（或者POJO）的类
	 * @return vo
	 */
	public <T> T toBeanIgnoreCase(Class<T> clazz) {
		return BeanUtil.mapToBeanIgnoreCase(this, clazz, false);
	}

	/**
	 * 将值对象转换为Dict<br>
	 * 类名会被当作表名，小写第一个字母
	 * 
	 * @param <T> Bean类型
	 * @param bean 值对象
	 * @return 自己
	 */
	public <T> Dict parseBean(T bean) {
		Assert.notNull(bean, "Bean class must be not null");
		this.putAll(BeanUtil.beanToMap(bean));
		return this;
	}

	/**
	 * 将值对象转换为Dict<br>
	 * 类名会被当作表名，小写第一个字母
	 * 
	 * @param <T> Bean类型
	 * @param bean 值对象
	 * @param isToUnderlineCase 是否转换为下划线模式
	 * @param ignoreNullValue 是否忽略值为空的字段
	 * @return 自己
	 */
	public <T> Dict parseBean(T bean, boolean isToUnderlineCase, boolean ignoreNullValue) {
		Assert.notNull(bean, "Bean class must be not null");
		this.putAll(BeanUtil.beanToMap(bean, isToUnderlineCase, ignoreNullValue));
		return this;
	}

	public <T> Dict parseBeanNoNull(T bean) {
		Assert.notNull(bean, "Bean class must be not null");
		parseBean(bean, false, true);
		return this;
	}


	public <T> Dict parseBeanWithNull(T bean) {
		Assert.notNull(bean, "Bean class must be not null");
		parseBean(bean, false, false);
		return this;
	}

	/**
	 * 与给定实体对比并去除相同的部分<br>
	 * 此方法用于在更新操作时避免所有字段被更新，跳过不需要更新的字段 version from 2.0.0
	 * 
	 * @param <T> 字典对象类型
	 * @param dict 字典对象
	 * @param names 不需要去除的字段名
	 */
	public <T extends Dict> void removeNoEqual(T dict, String... names) {
		HashSet<String> withoutSet = CollectionUtil.newHashSet(names);
		Dict cloneDict = dict.clone();
		for (Map.Entry<String, Object> entry : dict.entrySet()) {
			if (withoutSet.contains(entry.getKey())) {
				continue;
			}

			final Object value = this.get(entry.getKey());
			if (null != value && value.equals(entry.getValue())) {
				cloneDict.remove(entry.getKey());
			}
		}
		dict.clear();
		dict.putAll(cloneDict);
	}

	public <T extends Dict> void removeEqual(T dict, String... names) {
		HashSet<String> withoutSet = CollectionUtil.newHashSet(names);
		Dict cloneDict = dict.clone();
		for (Map.Entry<String, Object> entry : dict.entrySet()) {
			if (!withoutSet.contains(entry.getKey())) {
				continue;
			}

			final Object value = this.get(entry.getKey());
			if (null != value && value.equals(entry.getValue())) {
				cloneDict.remove(entry.getKey());
			}
		}
		dict.clear();
		dict.putAll(cloneDict);
	}

	/**
	 * 过滤Map保留指定键值对，如果键不存在跳过
	 * 
	 * @param keys 键列表
	 * @return Dict 结果
	 *
	 */
	public Dict filter(String... keys) {
		final Dict result = new Dict(keys.length, 1);

		for (String key : keys) {
			if (this.containsKey(key)) {
				result.put(key, this.get(key));
			}
		}
		return result;
	}

	// -------------------------------------------------------------------- Set start
	/**
	 * 设置列
	 * 
	 * @param attr 属性
	 * @param value 值
	 * @return 本身
	 */
	public Dict set(String attr, Object value) {
		this.put(attr, value);
		return this;
	}

	/**
	 * 设置列，当键或值为null时忽略
	 * 
	 * @param attr 属性
	 * @param value 值
	 * @return 本身
	 */
	public Dict setIgnoreNull(String attr, Object value) {
		if (null != attr && null != value) {
			set(attr, value);
		}
		return this;
	}
	// -------------------------------------------------------------------- Set end

	// -------------------------------------------------------------------- Get start

	@Override
	public Object getObj(String key) {
		return super.get(key);
	}

	/**
	 * 获得特定类型值
	 * 
	 * @param <T> 值类型
	 * @param attr 字段名
	 * @param defaultValue 默认值
	 * @return 字段值
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String attr, T defaultValue) {
		final Object result = get(attr);
		return (T) (result != null ? result : defaultValue);
	}

	public static Dict parseJsonObject(String json){
		JSONObject jsonObject = JSONObject.parseObject(json);
		Dict dict = new Dict(jsonObject);
		return dict;
	}

	public static List<Dict> parseJsonArrayWithDict(String json){
		JSONArray jsonArray = JSONArray.parseArray(json);
		Dict dict = Dict.create().set("___", jsonArray);
		List<Dict> dictList = dict.getPathList("___", Dict.class);
		return dictList;
	}

	public static List<Object> parseJsonArray(String json){
		JSONArray jsonArray = JSONArray.parseArray(json);
		Dict dict = Dict.create().set("___", jsonArray);
		List<Object> objectList = dict.getPathList("___");
		List<Object> objectList1 = objectList.stream().map(obj -> {
			if (obj instanceof JSONObject) {
				return new Dict((JSONObject) obj);
			}
			return obj;
		}).collect(Collectors.toList());
		return objectList1;
	}

	public static Dict parseJsonObject(JSONObject jsonObject){
		Dict dict = new Dict(jsonObject);
		return dict;
	}

	public static List<Dict> parseJsonArrayWithDict(JSONArray jsonArray){
		Dict dict = Dict.create().set("___", jsonArray);
		List<Dict> dictList = dict.getPathList("___", Dict.class);
		return dictList;
	}

	public static List<Object> parseJsonArray(JSONArray jsonArray){
		Dict dict = Dict.create().set("___", jsonArray);
		List<Object> objectList = dict.getPathList("___");
		List<Object> objectList1 = objectList.stream().map(obj -> {
			if (obj instanceof JSONObject) {
				return new Dict((JSONObject) obj);
			}
			return obj;
		}).collect(Collectors.toList());
		return objectList1;
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	@Override
	public String getStr(String attr) {
		return Convert.toStr(get(attr), null);
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	@Override
	public Integer getInt(String attr) {
		return Convert.toInt(get(attr), null);
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	@Override
	public Long getLong(String attr) {
		return Convert.toLong(get(attr), null);
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	@Override
	public Float getFloat(String attr) {
		return Convert.toFloat(get(attr), null);
	}

	@Override
	public Short getShort(String attr) {
		return Convert.toShort(get(attr), null);
	}

	@Override
	public Character getChar(String attr) {
		return Convert.toChar(get(attr), null);
	}

	@Override
	public Double getDouble(String attr) {
		return Convert.toDouble(get(attr), null);
	}

	@Override
	public Byte getByte(String attr) {
		return Convert.toByte(get(attr), null);
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	@Override
	public Boolean getBool(String attr) {
		return Convert.toBool(get(attr), null);
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	@Override
	public BigDecimal getBigDecimal(String attr) {
		return Convert.toBigDecimal(get(attr));
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	@Override
	public BigInteger getBigInteger(String attr) {
		return Convert.toBigInteger(get(attr));
	}

	@Override
	public <E extends Enum<E>> E getEnum(Class<E> clazz, String key) {
		return Convert.toEnum(clazz, get(key));
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public byte[] getBytes(String attr) {
		return get(attr, null);
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	@Override
	public Date getDate(String attr) {
		return get(attr, null);
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public Time getTime(String attr) {
		return get(attr, null);
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public Timestamp getTimestamp(String attr) {
		return get(attr, null);
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public Number getNumber(String attr) {
		return get(attr, null);
	}
	// -------------------------------------------------------------------- Get end
	
	@Override
	public Object put(String key, Object value) {
		return super.put(customKey(key), value);
	}

	@Override
	public Dict clone() {
		return (Dict) super.clone();
	}
	
	/**
	 * 将Key转为小写
	 * 
	 * @param key KEY
	 * @return 小写KEY
	 */
	private String customKey(String key) {
		if (this.caseInsensitive && null != key) {
			key = key.toLowerCase();
		}
		return key;
	}

	/**
	 * 获取层级key的值
	 * @param levelKey
	 * @return
	 */
	@Deprecated
	public <T> T getLevelVal(String levelKey, Class<T> clazz){
		String[] keys = StrUtil.splitToArray(levelKey, '.');
		Map map=this;
		for (int i=0; i<keys.length; i++){
			Object o = map.get(keys[i]);
			if (o instanceof Map){
				map= (Map)o;
			}else{
				if (i == keys.length-1){
					Object o1 = map.get(keys[i]);
					try {
						T t = Convert.convert(clazz, map.get(keys[i]));
						return t;
					} catch (Exception e) {
						throw new UtilException("返回类型无法转换，请检查!");
					}
				}else{
					throw new UtilException("该层级不存在值，请检查层级结构!");
				}
			}
		}
		return Convert.convert(clazz, new Dict(map));
	}

	@Deprecated
	public Object getLevelVal(String levelKey){
		String[] keys = StrUtil.splitToArray(levelKey, '.');
		Map map=this;
		for (int i=0; i<keys.length; i++){
			Object o = map.get(keys[i]);
			if (o instanceof Map){
				map= (Map)o;
			}else{
				if (i == keys.length-1){
					Object o1 = map.get(keys[i]);
					try {
						return map.get(keys[i]);
					} catch (Exception e) {
						throw new UtilException("返回类型无法转换，请检查!");
					}
				}else{
					throw new UtilException("该层级不存在值，请检查层级结构!");
				}
			}
		}
		return new Dict(map);
	}

	public <T> T getPathVal(String path, Class<T> clazz){
		Object pathVal = getPathVal(path);
		return Convert.convert(clazz,pathVal);
	}

	public Object getPathVal(String path){
		BeanPath pattern = BeanPath.create(path);
		Object o = pattern.get(this);
		return o;
	}

	public Dict setPathVal(String path, Object val){
		BeanPath pattern = BeanPath.create(path);
		pattern.set(this, val);
		return this;
	}

	public Dict getPathDict(String path){
		Object pathVal = getPathVal(path);
		return Convert.convert(Dict.class, pathVal);
	}

	@Deprecated
	public Dict getLevelDict(String levelKey){
		String[] keys = StrUtil.splitToArray(levelKey, '.');
		Map map=this;
		for (int i=0; i<keys.length; i++){
			Object o = map.get(keys[i]);
			if (o instanceof Map){
				map= (Map)o;
			}else{
				if (i == keys.length-1){
					Object o1 = map.get(keys[i]);
					try {
						Dict dict = Convert.convert(Dict.class, map.get(keys[i]));
						return dict;
					} catch (Exception e) {
						throw new UtilException("返回类型无法转换，请检查!");
					}
				}else{
					throw new UtilException("该层级不存在值，请检查层级结构!");
				}
			}
		}
		return new Dict(map);
	}

	/**
	 * 获取层级key的值
	 * @param levelKey
	 * @return
	 */
	@Deprecated
	public Dict setLevelVal(String levelKey, Object obj){
		String[] keys = StrUtil.splitToArray(levelKey, '.');
		if (keys.length == 1){
			this.set(levelKey, obj);
		}
		String realKey = keys[keys.length - 1];
		Dict dict = Dict.create();
		dict.set(realKey, obj);
		Dict finalDict = Dict.create();
		for (int i=keys.length - 2; i>=0; i--){
			String key = keys[i];
			if (i != 0){
				Dict dict1 = Dict.create();
				dict = dict1.set(key,dict);
			}else{
				finalDict.set(key, dict);
			}
		}
		Dict dict1 = this;
		for (String key: keys){
			Object o = dict1.get(key);
			Object o1 = finalDict.get(key);
			if (o==null || !(o instanceof Map)){
				dict1.set(key, o1);
				break;
			}else{
				dict1= (Dict) o;
				finalDict= (Dict) o1;
			}
		}
		return this;
	}

	public Dict convertFieldCamelCase(){
		Iterator<String> keys = this.keySet().iterator();
		Dict dict = Dict.create();
		while (keys.hasNext()){
			String sk = keys.next();
			String camelCase = StrUtil.toCamelCase(sk);
			dict.set(sk, camelCase);
		}
		Dict dict1 = convertFieldLR(() -> dict);
		return dict1;
	}

	public Dict convertFieldUnderLine(){
		Iterator<String> keys = this.keySet().iterator();
		Dict dict = Dict.create();
		while (keys.hasNext()){
			String sk = keys.next();
			String camelCase = StrUtil.toUnderlineCase(sk);
			dict.set(sk, camelCase);
		}
		Dict dict1 = convertFieldLR(() -> dict);
		return dict1;
	}

	public Dict convertFieldLR(ConvertFun convertFun){
		Map<String, String> dict = new HashMap(convertFun.call());
		Iterator<String> keys = dict.keySet().iterator();
		Iterator<String> values = dict.values().iterator();
		for (int i=0; i<dict.size();i++){
			String key = keys.next();
			String value = values.next();
			Object o = this.get(key);
			if (this.containsKey(key)){
				this.remove(key);
				this.set(value,o);
			}
		}
		return this;
	}
	public Dict convertFieldRL(ConvertFun convertFun){
		Map<String, String> dict = new HashMap(convertFun.call());
		Iterator<String> values = dict.keySet().iterator();
		Iterator<String> keys = dict.values().iterator();
		for (int i=0; i<dict.size();i++){
			String key = keys.next();
			String value = values.next();
			Object o = this.get(key);
			if (this.containsKey(key)){
				this.remove(key);
				this.set(value,o);
			}
		}
		return this;
	}
	@Deprecated
	public static void handleLovMeaningList(List<Dict> dictList, ConvertFun convertFun){
		for (Dict dict: dictList){
			dict.handleLovMeaning(convertFun);
		}
	}

	public static void handleLovMeaningList(List<Dict> dictList, List<LovConvert> lovList){
		for (Dict dict: dictList){
			dict.handleLovMeaning(lovList);
		}
	}

	@Deprecated
	public void handleLovMeaning(ConvertFun convertFun){
		PigicPlatFormFeignClient platFormFeignClient = SpringUtils.getBean(PigicPlatFormFeignClient.class);
		Map<String, String> map = new HashMap(convertFun.call());
		Iterator<String> keys = map.keySet().iterator();
		Iterator<String> values = map.values().iterator();
		for (int i=0; i<map.size();i++){
			String lovName = keys.next();
			String value = values.next();
			String[] splits = StrUtil.split(value, "->");
			String code = splits[0];
			String meaning = splits[1];
			List<Dict> dicts = platFormFeignClient.queryLovValue(lovName, Constants.Common.TENANTID);
			dicts.stream().forEach(dict->{
				if (StrUtil.equals(this.getStr(code), dict.getStr("value"))){
					this.set(meaning, dict.getStr("meaning"));
				}
			});
			if (ObjectUtil.isNull(this.get(meaning))){
				this.set(meaning, "");
			}
		}
	}

	public void handleLovMeaning(List<LovConvert> lovList){
		PigicPlatFormFeignClient platFormFeignClient = SpringUtils.getBean(PigicPlatFormFeignClient.class);
		for (LovConvert lovConvert: lovList){
			String lovCode = lovConvert.getLovCode();
			String meaningFiled = lovConvert.getMeaningFiled();
			String valueFiled = lovConvert.getValueFiled();
			List<Dict> dicts = platFormFeignClient.queryLovValue(lovCode, Constants.Common.TENANTID);
			dicts.stream().forEach(dict->{
				if (StrUtil.equals(this.getStr(valueFiled), dict.getStr("value"))){
					this.set(meaningFiled, dict.getStr("meaning"));
				}
			});
			if (ObjectUtil.isNull(this.get(meaningFiled))){
				this.set(meaningFiled, "");
			}
		}
	}



	public static List<Dict> parseList(Object obj){
		List<Object> objectList= (List<Object>) obj;
		List<Dict> dictList = objectList.stream().map(o -> {
			Dict dict = Dict.parse(o);
			return dict;
		}).collect(Collectors.toList());
		return dictList;
	}

	public static <T> Dict parse(Map<String, T> obj){
		Dict dict = Dict.create();
		obj.forEach((k,v)->{
			T t = obj.get(k);
			dict.set(k,t);
		});
		return dict;
	}

	public Map<String, String> toMapStr(){
		Map<String, String> map=Maps.newHashMap();
		this.forEach((key, val)->{
			map.put(key, Convert.convert(String.class, val));
		});
		return map;
	}

	public List<Dict> parseListBean(Object obj){
		List<Object> objectList= (List<Object>) obj;
		List<Dict> dictList = objectList.stream().map(o -> {
			Dict dict = Dict.parse(o);
			return dict;
		}).collect(Collectors.toList());
		return dictList;
	}

	public <T> List<T> getPathList(String key, Class<T> clazz){
		List<Object> objects = getPathList(key);
		if (clazz.equals(Map.class)){
			List<T> dictList = objects.stream().map(obj -> {
				Dict dict = Dict.parse(obj);
				return (T)dict;
			}).collect(Collectors.toList());
			return dictList;
		}else{
			List<T> objList = objects.stream().map(obj -> {
				return Convert.convert(clazz, obj);
			}).collect(Collectors.toList());
			return objList;
		}
	}

	@Deprecated
	public <T> List<T> getLevelList(String key, Class<T> clazz){
		Object levelVal = getLevelVal(key);
		List<Object> objects= (List<Object>) levelVal;
		if (clazz.equals(Map.class)){
			List<T> dictList = objects.stream().map(obj -> {
				Dict dict = Dict.parse(obj);
				return (T)dict;
			}).collect(Collectors.toList());
			return dictList;
		}else{
			List<T> objList = objects.stream().map(obj -> {
				return Convert.convert(clazz, obj);
			}).collect(Collectors.toList());
			return objList;
		}
	}

	public List<Object> getPathList(String key){
		Object pathVal = getPathVal(key);
		List<Object> objects= (List<Object>) pathVal;
		return objects;
	}

	@Deprecated
	public List<Object> getLevelList(String key){
		Object levelVal = getLevelVal(key);
		List<Object> objects= (List<Object>) levelVal;
		return objects;
	}

	public static UserVO getCurrentUser(){
		PigicIamFeignClient feignClient = SpringUtils.getBean(PigicIamFeignClient.class);
		UserVO userVO = feignClient.selectSelf();
		UserVO userVO1 = feignClient.selectSelfDetail();
		BeanUtil.copyProperties(userVO1,userVO,CopyOptions.create().ignoreNullValue());
		return userVO;
	}

	public static String getProfileValue(String profileName){
		UserVO userVO = getCurrentUser();
		Long roleId = userVO.getCurrentRoleId();
		Long id = userVO.getId();
		Long organizationId = userVO.getOrganizationId();
		PigicPlatFormFeignClient platFormFeignClient = SpringUtils.getBean(PigicPlatFormFeignClient.class);
		String profileVal = platFormFeignClient.selectProfile(organizationId, profileName, id, roleId);
		return profileVal;
	}

	public static String getCommonProfileValue(String profileName){
		Long organizationId = Constants.Common.TENANTID;
		Long id = -1L;
		Long roleId = -1L;
		PigicPlatFormFeignClient platFormFeignClient = SpringUtils.getBean(PigicPlatFormFeignClient.class);
		String profileVal = platFormFeignClient.selectProfile(organizationId, profileName, id, roleId);
		return profileVal;
	}

	public <T> T get(String key, Class<T> clazz){
		Object o = this.get(key);
		return Convert.convert(clazz,o);
	}

	public static List<Lov> getLovList(String lovCode){
		List<Lov> lovList = new ArrayList();
		PigicPlatFormFeignClient platFormFeignClient = SpringUtils.getBean(PigicPlatFormFeignClient.class);
		List<Dict> dicts = platFormFeignClient.queryLovValue(lovCode, Constants.Common.TENANTID);
		for (Dict dict: dicts){
			String value = dict.getStr("value");
			String meaning = dict.getStr("meaning");
			String tag = dict.getStr("tag");
			String parentValue = dict.getStr("parentValue");
			String description = dict.getStr("description");
			Integer orderSeq = dict.getInt("orderSeq");
			Lov lov = new Lov()
					.setValue(value)
					.setTag(tag)
					.setParentValue(parentValue)
					.setDescription(description)
					.setOrderSeq(orderSeq)
					.setMeaning(meaning);
			lovList.add(lov);
		}
		return lovList;
	}

	public static Lov getLovInfo(String lovCode, String value){
		List<Lov> lovList = getLovList(lovCode);
		Assert.notNull(lovList,"找不对该值集编码：{}",lovCode);
		Assert.isTrue(lovList.size()>0,"值集编码：{}，没有数据",lovCode);
		for (Lov lov: lovList){
			if (lov.getValue().equals(value)){
				return lov;
			}
		}
		throw new CommonException("值集：{}，找不到对应的值：{}", lovCode, value);
	}

	public Dict handleDate(String format){
		Set<String> keySet = this.keySet();
		for (String str: keySet){
			Object val = this.get(str);
			if (val instanceof Date){
				Date date = (Date) val;
				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				String newVal = dateFormat.format(date);
				this.set(str, newVal);
			}
		}
		return this;
	}

	public Dict handleDate(){
		return handleDate("yyyy-MM-dd HH:mm:dd");
	}
}
