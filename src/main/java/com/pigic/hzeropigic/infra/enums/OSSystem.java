package com.pigic.hzeropigic.infra.enums;

/**
 * @Author: 潘顾昌
 * @Date: 2019/8/12 9:12
 */
public enum OSSystem {
    WINDOWS("WINDOWS"),
    LINUX("LINUX"),
    ANDROID("ANDROID"),
    MAC("MAC");

    private String osName;
    OSSystem(String osName) {
        this.osName=osName;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public static String getOSName(OSSystem osSystem){
        String osName = osSystem.getOsName();
        return osName;
    }

}
