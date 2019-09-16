package com.pigic.hzeropigic.app.service;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * 公共调用Service
 *
 * @author dianzhang.zhou@hand-china.com 2019-05-23 11:47:19
 */
public interface PigicService {

    /**
     * 根据LovCode 获取LovValue
     * @param lovCode
     * @param lovValue
     * @param tenantId
     * @return
     */
    String GetLovMeaningByCode(String lovCode, String lovValue, Long tenantId);

    /**
     * 根据lovMeaning 获取LovCode
     * @param lovCode
     * @param lovMeaning
     * @param tenantId
     * @return
     */
    String GetLovCodeByMeaning(String lovCode, String lovMeaning, Long tenantId);

    /**
     * 根据LovCode 查询值集列表
     * @param lovCode
     * @param tenantId
     * @return
     */
    List<Map> GetLovInfoList(String lovCode, Long tenantId);

    /**
     * 根据LovCode 获取当前值集
     * @param lovCode
     * @param lovValue
     * @param tenantId
     * @return
     */
    Map GetLovInfo(String lovCode, String lovValue, Long tenantId);


    /**
     * 根据profileId获取Value
     * @param tenantId
     * @param profileId
     * @return
     */
    String GetProfileValueById(Long tenantId, Long profileId);

}