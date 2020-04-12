package com.pigic.hzeropigic.utils;

import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;

import java.util.ArrayList;
import java.util.List;

/**
 * 对List进行分页
 * @author guchang.pan@hand-china.com
 * @Date: 2019/7/13 16:36
 */
public final class ListPageUtil {
    private ListPageUtil(){
        throw new CommonException("不能创建该实例");
    }
    public static <T> Page<T>  paging(List<T> list, int pageNumber, int pageSize){
        List<T> pageList = new ArrayList<T>();
        int currIdx = (pageNumber > 1 ? (pageNumber -1) * pageSize : 0);
        for (int i = 0; i < pageSize && i < list.size() - currIdx; i++){
            T listNew = list.get(currIdx + i);
            pageList.add(listNew);
        }
        Page<T> page = new Page<T>();
        page.setContent(pageList);
        page.setTotalElements(list.size());
        if (page.getTotalElements()%pageSize == 0){
            page.setTotalPages((int)page.getTotalElements()/pageSize);
        }else {
            page.setTotalPages((int)page.getTotalElements()/pageSize + 1);
        }
        page.setNumber(pageNumber);
        page.setSize(pageSize);
        page.setNumberOfElements(pageList.size());
        return page;
    }
}
