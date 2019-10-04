package com.tophant;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App
{
    public static String s = "sss";
    static {
        System.out.println("Code in static");
        System.out.println(s);
    }

    public App(){
        System.out.println("Code in Constructor");
    }
    public static void main( String[] args )
   {
       Proxy.newProxyInstance(App.class.getClassLoader(), new Class[]{Map.class}, new InvocationHandler() {
           @Override
           public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
               return null;
           }
       });

        System.out.println( "Hello World!" );
    }
}
