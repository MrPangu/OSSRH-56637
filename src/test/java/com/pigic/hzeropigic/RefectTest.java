package com.pigic.hzeropigic;

import cn.pigicutils.core.bean.BeanUtil;
import cn.pigicutils.core.bean.copier.CopyOptions;
import cn.pigicutils.core.lang.Console;
import cn.pigicutils.core.lang.Dict;
import cn.pigicutils.core.util.CharsetUtil;
import com.pigic.hzeropigic.utils.Reflect;
import com.pigic.hzeropigic.utils.TempUtils;
import jodd.util.SystemUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @Author: 潘顾昌
 * @Date: 2019/2/3 15:30
 */
public class RefectTest {
    /**
     * 包装类
     */
    @Test
    public void test0() {
        Reflect.ClassReflect cr = Reflect.on(Person.class);
        Reflect.ClassReflect cr1 = Reflect.on("com.pigic.hzeropigic.Person");
        Reflect.ClassReflect cr2 = Reflect.on("com.pigic.hzeropigic.Person",ClassLoader.getSystemClassLoader());

    }

    /**
     * 映射实体，创建实体对象
     */
    @Test
    public void test1() {
        Reflect.ClassReflect or = Reflect.on("com.pigic.hzeropigic.Person");
        Person off = or.create("张三", 14).asObject().off();
        System.out.println(off);
    }

    /**
     * 调用方法
     */
    @Test
    public void test2() {
        Reflect.ClassReflect or = Reflect.on("com.pigic.hzeropigic.Person");
//        String name = or.create("张三", 14).call("getName").off();
        String name = or.create("张三", 14).call("getName",null).off();
        System.out.println(name);
    }

    /**
     * 获取所有参数
     */
    @Test
    public void test3() {
        Reflect reflect = Reflect.on("com.pigic.hzeropigic.Person").create("张三", 14);
        Reflect.MethodReflect methodReflect = reflect.method("setName", String.class);
        Object[] arguments = methodReflect.getArguments();
        System.out.println(arguments.length);
    }

    /**
     * 强制修改属性的值
     */
    @Test
    public void test4() {
        Reflect reflect = Reflect.on("com.pigic.hzeropigic.Person").create("张三", 14);
        reflect.field("name").set("潘顾昌", true);
        String name = reflect.call("getName").off();
        System.out.println(name);
    }

    /**
     * 通过new Object创建反射
     */
    @Test
    public void test5() {
        Reflect reflect = Reflect.on(new Person("李四", 12));
        String name = reflect.call("getName").off();
        System.out.println(name);
    }

    /**
     * 查找方法(方法的使用)
     */
    @Test
    public void test6() {
//        Reflect reflect = Reflect.on("com.pigic.hzeropigic.Person").create("张三", 14);
//        Reflect reflect1 = reflect.method("setAge", Integer.class, String.class);
//        int size = ((Reflect.MethodReflect) reflect1).parameterSize();
//        System.out.println(size);   //参数个数
//        String[] parameterNames = ((Reflect.MethodReflect) reflect1).getParameterNames();
//        Class<?>[] parameterTypes = ((Reflect.MethodReflect) reflect1).getParameterTypes();
//        System.out.println(parameterTypes[0]);
//        System.out.println(parameterTypes[1]);
//        Reflect refect = reflect1.back();
//        Object off = refect.off();
//        System.out.println(off);
    }

    /**
     * 处理注解
     */
    @Test
    public void test7() {
        Reflect reflect = Reflect.on("com.pigic.hzeropigic.Person").create("张三", 14);
        Reflect.MethodReflect methodReflect = reflect.method("sayHello");
        Pigict pigict = methodReflect.getAnnotation(Pigict.class);
        String name = pigict.name();
        int value = pigict.value();
        System.out.println(name+","+value);
    }

    @Test
    public void test8() {
        Reflect reflect = Reflect.on("com.pigic.hzeropigic.Person").create("张三", 14);
        Reflect.MethodReflect methodReflect = reflect.method("sayHello");
        List<Annotation> annotations = methodReflect.getAnnotations();
        for (Annotation annotation: annotations){
            Object defaultValue = AnnotationUtils.getValue(annotation);
            Map<String, Object> map = AnnotationUtils.getAnnotationAttributes(annotation);
            System.out.println(defaultValue);
            System.out.println(map);
        }
    }

    @Test
    public void test11() {
        Reflect reflect = Reflect.on("com.pigic.hzeropigic.Person").create("张三", 14);
        Reflect.MethodReflect methodReflect = reflect.method("getName");
        Class<?> aClass = methodReflect.off().getReturnType();
        Object[] arguments = methodReflect.getArguments();
        Class<?>[] parameterTypes = methodReflect.getParameterTypes();
        String[] parameterNames = methodReflect.getParameterNames();
    }

    @Test
    public void test13() {
        boolean windows = SystemUtil.isHostLinux();
        System.out.println(windows);
    }

    @Test
    public void test14() throws MalformedURLException {
        String urlString = "http://10.9.35.82:9000/StarChage/log/20190812/K3C_CUSTOMER_SYNC_JOB_6070f347d9af4f1eb124e6922964179a.txt";
        URL url = new URL(urlString);
        String path = url.getPath();
        Console.log(path);
    }

    @Test
    public void test15() {
        Person person = new Person();
        person.setAge(15);
        person.setName("周虹曦");
        Person person1 = new Person();
        person1.setName("潘顾昌");
        Dict dict = Dict.parse(person);
        dict.parseBean(person1);
        Console.log(dict);
    }

    @Test
    public void test16() throws InvocationTargetException, IllegalAccessException {
        Person person = new Person();
        person.setAge(15);
        person.setName("周虹曦");
        Person person1 = new Person();
        person1.setName("潘顾昌");
        BeanUtils.copyProperties(person1, person);
        Console.log(person1);
        Console.log(person);
    }

    @Test
    public void test17() {
        Map<String, Object> objectObjectHashMap = new LinkedHashMap<>();
        objectObjectHashMap.put("aa",11);
        Dict dict = new Dict(objectObjectHashMap);
        Console.log(dict);
    }

    @Test
    public void test18() {
        Set<String> hashSet = new HashSet<>();
        hashSet.add("1");
        hashSet.add("2");
        boolean add = hashSet.add("1");
        Console.log(add);
        Console.log(hashSet);
    }

    @Test
    public void test19() {

    }

    @Test
    public void test20() {

    }

    @Test
    public void test21() {

    }

    @Test
    public void test22() {

    }

    @Test
    public void test23() {

    }

    @Test
    public void test24() {

    }

    @Test
    public void test25() {

    }

    @Test
    public void test26() {

    }

    @Test
    public void test27() {

    }

    @Test
    public void test28() {

    }

    @Test
    public void test29() {

    }

    @Test
    public void test30() {

    }

    @Test
    public void test31() {

    }

    @Test
    public void test32() {

    }

    @Test
    public void test33() {

    }

    @Test
    public void test34() {

    }

    @Test
    public void test35() {

    }

    @Test
    public void test36() {

    }

    @Test
    public void test37() {

    }

    @Test
    public void test38() {

    }

    @Test
    public void test39() {

    }

}
