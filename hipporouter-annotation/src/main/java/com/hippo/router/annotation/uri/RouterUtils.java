package com.hippo.router.annotation.uri;


import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Kevin on 2016/12/29.
 */

public class RouterUtils {
    public static final String SUFFIX = "_RouterInitializer";

    public static Class<?> getGenerateClass(String url) throws MalformedURLException, ClassNotFoundException {
        String name = new StringBuilder()
                .append(getGeneratePackageName(url))
                .append(".")
                .append(getGenerateClassName(url))
                .toString();
        return Class.forName(name);
    }

    public static String getGeneratePackageName(String url) throws MalformedURLException {
        Uri uri = Uri.parse(url);

        return new StringBuilder("com.hippo.router")
                .append(".")
                .append(uri.getScheme())
                .toString();
    }

    public static String getGenerateClassName(String routeUrl) throws MalformedURLException {
        Uri uri = Uri.parse(routeUrl);
        StringBuffer sb = new StringBuffer();
        List<String> infos = getClassInfoFromUri(uri);
        for (String info : infos) {
            sb.append(info);
        }
        return sb.append(SUFFIX).toString();
    }

    private static List<String> getClassInfoFromUri(Uri uri) {
        List<String> list = new ArrayList<>();
        String scheme = uri.getScheme();
        String host = uri.getHost();
        list.add(toUpperCaseFirstOne(scheme));
        list.add(toUpperCaseFirstOne(host));

        List<String> segments = Arrays.asList(uri.getPath().split("/"));
        for (String segment : segments) {
            if (segment != null && !segment.isEmpty() && !segment.contains(":")) {
                list.add(toUpperCaseFirstOne(segment));
            }
        }
        Collections.reverse(list);
        return list;
    }

    public static String toUpperCaseFirstOne(String s){
        if(Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }


    public static String toLowerCaseFirstOne(String s){
        if(Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }

}
