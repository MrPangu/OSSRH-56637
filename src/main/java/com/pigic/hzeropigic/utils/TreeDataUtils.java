package com.pigic.hzeropigic.utils;

import io.choerodon.core.exception.CommonException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hzero.core.algorithm.tree.Child;
import org.hzero.core.base.BaseConstants;
import org.springframework.beans.BeanUtils;

import java.util.*;

/*
 * 树结构处理
 *
 * @author guchang.pan@hand-china.com
 * @version: 0.0.1
 * @copyright Copyright (c) 2019, Hand
 */
public class TreeDataUtils<T extends Child<T>> {

    /**
     * 父节点标志字段名（暂时为Long类型ID）
     */
    private String parentFieldName;
    /**
     * 主键字段名
     */
    private String primaryKeyName;

    public TreeDataUtils(String parentFieldName, String primaryKeyName) {
        this.parentFieldName = parentFieldName;
        this.primaryKeyName = primaryKeyName;
    }

    /**
     * 数据转换
     *
     * @param treeData 树形结构数据
     * @param clazz    结果类型
     */
    public <K> void treeDataToList(List<T> treeData, Class<K> clazz, List<K> targetList) {
        if (CollectionUtils.isNotEmpty(treeData)) {
            for (T t : treeData) {
                K k;
                try {
                    k = clazz.newInstance();
                } catch (Exception e) {
                    throw new CommonException(e);
                }
                BeanUtils.copyProperties(t, k);
                targetList.add(k);
                List<T> childList = t.getChildren();
                if (CollectionUtils.isNotEmpty(childList)) {
                    treeDataToList(childList, clazz, targetList);
                }
            }
        }
    }


    /**
     * 将dbMap中的数据通过treeIdMap填充到treeMap中
     *
     * @param treeMap   结果集
     * @param dbMap     数据源
     * @param treeIdMap 映射关系
     */
    public void fillEntity(Map<Long, List<T>> treeMap, Map<Long, T> dbMap, Map<Long, Set<Long>> treeIdMap) {
        for (Map.Entry<Long, Set<Long>> entry : treeIdMap.entrySet()) {
            Long parentId = entry.getKey();
            Set<Long> treeIds = entry.getValue();

            List<T> treeData = new ArrayList<>();
            treeIds.forEach(id -> treeData.add(dbMap.get(id)));
            treeMap.put(parentId, treeData);
        }
    }

    /**
     * 找出父节点和子节点ID放入treeIdMap
     *
     * @param t               当前节点
     * @param dbMap           数据集
     * @param treeIdMap       ID树
     */
    public void gatherSubTreeIds(T t, Map<Long, T> dbMap, Map<Long, Set<Long>> treeIdMap, Integer showChildFlag) throws IllegalAccessException {
        Long existParentId = gatherParentIds(t, dbMap, treeIdMap);
        if (Objects.equals(BaseConstants.Flag.YES, showChildFlag)){
            // 比数据库查询更加效率
            Set<Long> childIdSet = new HashSet<>();
            gatherChildIds(t, dbMap, childIdSet);

            treeIdMap.get(existParentId).addAll(childIdSet);
        }
    }

    /**
     * 找出父节点ID放入treeIdMap
     *
     * @param t         当前节点
     * @param dbMap     数据集
     * @param treeIdMap ID树
     * @return treeIdMap中存在的当前节点的根节点
     */
    private Long gatherParentIds(T t, Map<Long, T> dbMap, Map<Long, Set<Long>> treeIdMap) throws IllegalAccessException {
        List<Long> parentIds = new ArrayList<>();
        this.findParent(t, dbMap, parentIds);

        Long subTreeParentId = parentIds.get(parentIds.size() - 1);
        Long existParentId = this.searchMapForParentId(treeIdMap, subTreeParentId);
        if (Objects.isNull(existParentId)) {
            existParentId = subTreeParentId;
            treeIdMap.put(existParentId, new HashSet<>(parentIds));
        } else {
            treeIdMap.get(existParentId).addAll(new HashSet<>(parentIds));
        }

        return existParentId;
    }

    /**
     * 找出当前节点的子节点
     *
     * @param t        当前节点
     * @param dbMap    数据集
     * @param childIds 子节点集合
     */
    private void gatherChildIds(T t, Map<Long, T> dbMap, Set<Long> childIds) throws IllegalAccessException {
        Long currentPrimaryKey = (Long) FieldUtils.readField(t, primaryKeyName, true);
        for (Map.Entry<Long, T> entry : dbMap.entrySet()) {
            T entity = entry.getValue();
            Long parentId = (Long) FieldUtils.readField(entity, parentFieldName, true);

            if (Objects.equals(parentId, currentPrimaryKey)) {
                childIds.add(entry.getKey());
                gatherChildIds(entity, dbMap, childIds);
            }
        }
    }

    /**
     * 从treeMap获取当前节点的父节点
     *
     * @param treeIdMap 数据源
     * @param current   当前节点
     * @return 父节点ID
     */
    private Long searchMapForParentId(Map<Long, Set<Long>> treeIdMap, Long current) {

        for (Map.Entry<Long, Set<Long>> entry : treeIdMap.entrySet()) {
            Long parentId = entry.getKey();
            Set<Long> treeIds = entry.getValue();
            if (treeIds.contains(current)) {
                return parentId;
            }
        }
        return null;
    }

    /**
     * 查找所有的父节点
     *
     * @param current   当前节点
     * @param dbMap     总数居
     * @param parentIds 结果集
     */
    private void findParent(T current, Map<Long, T> dbMap, List<Long> parentIds) throws IllegalAccessException {

        Long parentId = (Long) FieldUtils.readField(current, parentFieldName, true);
        T parent = dbMap.get(parentId);

        parentIds.add((Long) FieldUtils.readField(current, primaryKeyName, true));
        if (!Objects.isNull(parent)) {
            // 若不为根节点
            findParent(parent, dbMap, parentIds);
        }
    }
}
