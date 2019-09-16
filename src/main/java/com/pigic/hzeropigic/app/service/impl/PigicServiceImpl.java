package com.pigic.hzeropigic.app.service.impl;

import com.pigic.hzeropigic.app.service.PigicService;
import com.pigic.hzeropigic.feign.PigicPlatFormFeignClient;
import com.pigic.hzeropigic.utils.UtilsHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/*
 *
 * @date: 2019/9/3
 * @author: 潘顾昌 <guchang.pan@hand-china.com>
 * @version: 0.0.1
 * @copyright Copyright (c) 2019, Hand
 */
@Service
public class PigicServiceImpl implements PigicService {

    //平台Feign
    @Autowired
    private PigicPlatFormFeignClient pigicPlatFormFeignClient;

    /**
     * 根据LovCode 获取LovValue
     * @param lovCode
     * @return
     */
    @Override
    public String GetLovMeaningByCode(String lovCode,String lovValue,Long tenantId) {
        if(!Strings.isBlank(lovCode)) {
            List<Map> lovValuesList=pigicPlatFormFeignClient.queryLovValue(lovCode,tenantId);
            if(CollectionUtils.isNotEmpty(lovValuesList)){
                Optional<Map> optionalMap=lovValuesList.stream().filter(x->x.get("value").toString().equalsIgnoreCase(lovValue)).findFirst();
                if(optionalMap.isPresent()){
                    return UtilsHelper.ObjectToString(optionalMap.get().get("meaning"));
                }
            }
        }
        return null;
    }

    /**
     * 根据lovMeaning 获取LovValue
     * @param lovCode
     * @return
     */
    @Override
    public String GetLovCodeByMeaning(String lovCode, String lovMeaning, Long tenantId) {
        if(!Strings.isBlank(lovCode)) {
            List<Map> lovValuesList=pigicPlatFormFeignClient.queryLovValue(lovCode,tenantId);
            if(CollectionUtils.isNotEmpty(lovValuesList)){
                Optional<Map> optionalMap=lovValuesList.stream().filter(x->x.get("meaning").toString().equalsIgnoreCase(lovMeaning)).findFirst();
                if(optionalMap.isPresent()){
                    return UtilsHelper.ObjectToString(optionalMap.get().get("value"));
                }
            }
        }
        return null;
    }

    /**
     * 根据LovCode 查询值集列表
     * @param lovCode
     * @param tenantId
     * @return
     */
    @Override
    public List<Map> GetLovInfoList(String lovCode, Long tenantId) {
        if(!Strings.isBlank(lovCode)) {
            List<Map> lovValuesList=pigicPlatFormFeignClient.queryLovValue(lovCode,tenantId);
            if(CollectionUtils.isNotEmpty(lovValuesList)){
                return lovValuesList;
            }
        }
        return null;
    }

    /**
     * 根据LovCode 获取当前值集
     * @param lovCode
     * @param lovValue
     * @param tenantId
     * @return
     */
    @Override
    public Map GetLovInfo(String lovCode, String lovValue, Long tenantId) {
        if(!Strings.isBlank(lovCode)) {
            List<Map> lovValuesList=pigicPlatFormFeignClient.queryLovValue(lovCode,tenantId);
            if(CollectionUtils.isNotEmpty(lovValuesList)){
                Optional<Map> optionalMap=lovValuesList.stream().filter(x->UtilsHelper.ObjectToString(x.get("value")).equalsIgnoreCase(lovValue)).findFirst();
                if(optionalMap.isPresent()) {
                    return optionalMap.get();
                }
            }
        }
        return null;
    }


    /**
     * 根据profileId获取Value
     * @param tenantId
     * @param profileId
     * @return
     */
    @Override
    public String GetProfileValueById(Long tenantId, Long profileId) {
        Map profileMap = pigicPlatFormFeignClient.selectProfile(tenantId, profileId);

        if (MapUtils.isNotEmpty(profileMap)) {

            List<Map> list = (List<Map>) profileMap.get("profileValueDTOList");

            if (CollectionUtils.isNotEmpty(list)) {

                if (!Objects.isNull(list.get(0).get("value"))) {
                    return list.get(0).get("value").toString();
                }
            }
        }
        return null;
    }

}

