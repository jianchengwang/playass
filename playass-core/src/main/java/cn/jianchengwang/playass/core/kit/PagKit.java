package cn.jianchengwang.playass.core.kit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PagKit {

    private static final Logger log = LoggerFactory.getLogger(PagKit.class);

    /**
     * 获取某个包下所有类 默认遍历子包 考虑粗心打包的情况
     * @param packageName 包名 允许 两种参数 com/xx/xx 或者com.xx.xx
     * @return
     */
    public static Set findClasses(String packageName){
        //默认需要遍历子包
        return findClasses(packageName,true,true);
    }
    /**
     * 获取某个包下所有类
     * @param packageName 包名  允许 两种参数 com/xx/xx 或者com.xx.xx
     * @param recursion 是否需要遍历子包
     * @param recursion 是否考虑打包的时候粗心了 忘了添加包映射
     * @return
     */
    public static Set findClasses(String packageName,boolean recursion,boolean careless){
        if(packageName.contains(".")){
            packageName = packageName.replace(".", "/");
        }
        try {
            return _findClasses(packageName,recursion,careless);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptySet();
        }
    }
    /**
     * 获取某个包下所有类
     * @param packageName 包名
     * @param recursion 是否需要遍历子包
     * @return
     * @throws IOException
     */
    private static Set _findClasses(String packageName,boolean recursion,boolean careless) throws IOException{
        Set result = new LinkedHashSet<>();
        //底层就是urlClassLoader所有这里强转没有问题
        URLClassLoader classLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
        //从类路径下加载指定的资源包
        Enumeration resources = classLoader.getResources(packageName);
        while(resources.hasMoreElements()){
            URL resource = (URL) resources.nextElement();
            //resource  这个返回值  如果你输入的包路径是src下的报名 那么返回的是 file:/E:/xxx包
            //如果jar包包含的类路径 返回的是 类似 jar:file:/E:/eclipseworkspace/mars2space/dot-tool/spring-aop-4.3.3.RELEASE.jar!/org/springframework
            //这时候我们需要 根据不同的返回结果做处理
            if(resource!=null){
                String protocol = resource.getProtocol();
                if(protocol.equals("file")){
                    findClassesByFile(packageName,resource.getPath(),recursion,result);
                }else{
                    findClassesByJar(resource.getPath(),recursion,result);
                }
            }
        }
//        if(careless){
//            //说明上面的方法 中有可能没有找到资源  这时候我们启动 这种方式查找  该方式会获取类路径下的所有jar文件  包括jar打包的时候
//            URL[] urls = classLoader.getURLs();//这个方法可以获取所有的类路径下的资源路径 包含了src下的类 和xx.jar文件
//            //如果 输入的包路径中 在类路径下的class文件
//            for (URL url : urls) {
//                if (url.getPath().endsWith(".jar")) {
//                    @SuppressWarnings("resource")
//                    JarFile jarFile = new JarFile(url.getPath());
//                    Enumeration entries = jarFile.entries();
//                    while (entries.hasMoreElements()) {
//                        JarEntry jarEntry = (JarEntry) entries.nextElement();
//                        String name = jarEntry.getName();
//                        if(name.endsWith(".class")&&name.startsWith(packageName)){
//                            name=name.replace("/", ".");
//                            result.add(name.substring(0, name.lastIndexOf(".class")));
//                        }
//                    }
//                }
//            }
//        }
        return result;
    }
    /**
     *
     * @param path
     * @param recursion
     * @param result
     * @return
     * @throws IOException
     */
    private static void findClassesByJar(String path, boolean recursion, Set result) throws IOException {
        String[] jarInfo = path.split("!");
        String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
        String packagePath = jarInfo[1].substring(1); //查找的包名
        @SuppressWarnings("resource")
        JarFile jarFile = new JarFile(jarFilePath);
        Enumeration entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) entries.nextElement();
            String name = jarEntry.getName();
            if(name.endsWith(".class")&&name.startsWith(packagePath)){
                name=name.replace("/", ".");
                result.add(name.substring(0, name.lastIndexOf(".class")));
            }
        }
    }
    /**
     * @param packageName
     * @param resource
     * @param recursion 是否需要遍历子包
     * @param result
     * @return
     */
    private static void findClassesByFile(String packageName,String resource, boolean recursion, Set result) {
        File directory = new File(resource);
        File[] listFiles = directory.listFiles();
        //遍历文件夹
        for (File file : listFiles) {
            if(file.isDirectory()){
                if(recursion){
                    //是否遍历子包
                    findClassesByFile(packageName,file.getPath(),recursion,result);
                }
            }else{
                //是文件的 话 只要.class结尾的文件
                String path = file.getPath();
                if(path.endsWith(".class")){
                    //当前获取的是文件路径  比如 E:\eclipseworkspace\mars2space\dot-tool\bin\com\jk1123
                    //我们需要的 只是类的全限定名   当我要扫描的包是com/jk1123包的时候 截取掉前缀 只要类的全名
                    int packageIndex = path.indexOf(packageName.replace("/", File.separator));
                    String classPath=path.substring(packageIndex, path.length()-6);
                    result.add(classPath.replace(File.separator, "."));
                }
            }
        }
    }
    public static void main(String[] args) throws IOException {
        Set findClasses = findClasses("cn.jianchengwang.playass.example.demo");
        System.out.println(".......................");
        for (Object string : findClasses) {
            System.out.println(string);
        }
    }
}
