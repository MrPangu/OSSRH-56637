package com.pigic.hzeropigic.utils;

import cn.pigicutils.core.util.StrUtil;
import com.github.pagehelper.PageInfo;
import io.choerodon.mybatis.pagehelper.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * 分页插件中间件
 * @date: 2019/6/22
 * @author guchang.pan@hand-china.com
 * @version: 0.0.1
 * @copyright Copyright (c) 2019, Hand
 */
public class PageHelperUtil {

    /**
     * 将猪齿鱼的排序转换成PageHelper需要的string。
     * @param sort
     * @return
     */
    public static String Middleware(Sort sort){
        if (sort==null){
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        String result = sort.toString().replaceAll(":", "");
        String[] split = result.split(",");
        Arrays.stream(split).forEach(obj->{
            String[] split1 = obj.split(" ");
            String underline = StrUtil.toUnderlineCase(split1[0]);
            stringBuilder.append(",");
            stringBuilder.append(underline);
            stringBuilder.append(" ");
            stringBuilder.append(split1[1]);
        });
        String finalResult = stringBuilder.substring(1);
        return finalResult;
    }
    /**
     * 将猪齿鱼的PageInfo转换成github的PageInfo。
     * @param pageInfo
     * @param objectList
     * @return
     */
    public static <T> PageInfo<T> MiddlewarePageInfo(io.choerodon.core.domain.PageInfo pageInfo, List<T> objectList){
        PageInfo<T> pageInfo1 = new PageInfo<>();
        int page = pageInfo.getPage();
        long total = pageInfo.getTotal();
        int size = pageInfo.getSize();
        pageInfo1.setPageSize(size);
        pageInfo1.setPageNum(page);
        pageInfo1.setTotal(total);
        ArrayList<T> arrayList = new ArrayList<>(objectList);
        pageInfo1.setList(arrayList);
        return pageInfo1;
    }


}