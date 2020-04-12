package com.pigic.hzeropigic.utils;

import java.io.*;

/**
 * @author guchang.pan@hand-china.com
 * @Date: 2019/7/1 21:18
 */
public class DeepCopyUtil {
    public static Object copy(Object orig) throws NotSerializableException {
        Object obj = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(orig);
            out.flush();
            out.close();

            ObjectInputStream in = new ObjectInputStream(
                    new ByteArrayInputStream(bos.toByteArray()));
            obj = in.readObject();
        } catch (NotSerializableException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return obj;
    }
}
