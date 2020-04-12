package com.pigic.hzeropigic.utils;

import org.springframework.web.context.request.async.DeferredResult;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author guchang.pan@hand-china.com
 * @Date: 2019/1/16 14:16
 */
public class DeferredResultQueue {

    private static Queue<DeferredResult<String>> queue = new ConcurrentLinkedQueue<DeferredResult<String>>();

    public static void save(DeferredResult<String> object) {
        queue.add(object);
    }

    public static DeferredResult<String> get() {
        return queue.poll();
    }

}