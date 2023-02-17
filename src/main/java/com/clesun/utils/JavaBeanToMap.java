package com.clesun.utils;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JavaBeanToMap
 */
@Component
public class JavaBeanToMap {

    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    public static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取属性名数组
     * */
    public static String[] getFiledName(Object o){
        Field[] fields=o.getClass().getDeclaredFields();
        String[] fieldNames=new String[fields.length];
        for(int i=0;i<fields.length;i++){
            System.out.println(fields[i].getType());
            fieldNames[i]=fields[i].getName();
        }
        return fieldNames;
    }

    /**
     * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
     * */
    public List getFiledsInfo(Object o){
        Field[] fields=o.getClass().getDeclaredFields();
        String[] fieldNames=new String[fields.length];
        List list = new ArrayList();
        Map infoMap=null;
        for(int i=0;i<fields.length;i++){
            infoMap = new HashMap();
            infoMap.put("type", fields[i].getType().toString());
            infoMap.put("name", fields[i].getName());
            infoMap.put("value", getFieldValueByName(fields[i].getName(), o));
            list.add(infoMap);
        }
        return list;
    }

     //  属性名(name)，属性值(value)的map
    public static Map<String,String> getFiledsToMap(Object o){
        Field[] fields=o.getClass().getDeclaredFields();
        Map infoMap=  new HashMap();
        for(int i=0;i<fields.length;i++){

            infoMap.put(fields[i].getName(), getFieldValueByName(fields[i].getName(), o));

        }
        return infoMap;
    }
     //  属性名(name)，属性值(value)的map
    public static List<String> getFiledsToList(Object o){
        Field[] fields=o.getClass().getDeclaredFields();
        List<String> stringList=  new ArrayList<>();
        for(int i=0;i<fields.length;i++){
//            获取当前字段的修饰符
            String modifier = Modifier.toString(fields[i].getModifiers());
//            获取当前字段的类型  String ,Integer ...
            String typeName = fields[i].getType().getTypeName();
            if(StringUtils.isNotNull(modifier) && !modifier.contains("transient") && ("java.lang.String").equals(typeName)){
                stringList.add(columnsToDatabase(fields[i].getName()));
            }
        }
        //移除不查询字段
        stringList.remove("");
        return stringList;
    }

    //解决mybatis plus 下划线命名规则  javabean转数据库字段
    public static String columnsToDatabase(String name){
        String column="";
        char[] intStream= name.toCharArray();
        for (char c:intStream
        ) {
            if(Character.isUpperCase(c)) {
                column+="_"+Character.toLowerCase(c);
            }else{
                column+=c;
            }
        }
        return column;
    }
    //解决mybatis plus 下划线命名规则  javabean转数据库字段
    public static String databaseTOColumns(String name){
        return name.replaceAll("_","");
    }

    public static  String mysqlTOColumns(String name){
        String column="";
        char[] intStream= name.toCharArray();
        boolean ch=false;
        for (char c:intStream
        ) {
            if("_".equals(c+"")) {
                ch=false;
            }else{
                if(ch) {
                    column+=c;
                }else{
                    column += Character.toUpperCase(c);
                    ch=true;
                }
            }
        }
        return column;
    }
    /**
     * 获取对象的所有属性值，返回一个对象数组
     * */
    public static Object[] getFiledValues(Object o){
        String[] fieldNames=getFiledName(o);
        Object[] value=new Object[fieldNames.length];
        for(int i=0;i<fieldNames.length;i++){
            value[i]=getFieldValueByName(fieldNames[i], o);
        }
        return value;
    }

    /**
     * 判断实体类中是否存在字属性
     * @param object 实体类对象
     * @param fieldName 属性名称
     * @return true：存在 false：不存在
     */
    public static boolean hasFiledName(Object object, String fieldName){
        try{
            object.getClass().getDeclaredField(fieldName);
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    public static String humpToLine(String str) {
        char[] cs = str.toCharArray();
        if (Character.isUpperCase(cs[0])) {
            cs[0] += 32;
            str = String.valueOf(cs);
        }
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String mysqlTOColumns2(String name) {
        String column = "";
        char[] intStream = name.toCharArray();
        boolean ch = false;
        for (char c : intStream  ) {
            if ("_".equals(c + "")) {
                ch = false;
            } else {
                if (ch) {
                    column += c;
                } else {
                    if(column.equals("")){
                        column += c;
                        ch = true;
                    }else{
                        column += Character.toUpperCase(c);
                        ch = true;
                    }
                }
            }
        }
        return column;
    }

    public static  String mysqlTOColumnsTable(String name){
        name= name.toLowerCase();
        String column="";
        char[] intStream= name.toCharArray();
        boolean ch=false;
        for (char c:intStream
                ) {
            if("_".equals(c+"")) {
                ch=false;
            }else{
                if(ch) {
                    column+=c;
                }else{
                    column += Character.toUpperCase(c);
                    ch=true;
                }
            }
        }
        return column;
    }

    public static String stringToCamel(String columnName){
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (columnName == null || columnName.isEmpty()) {
            // 不必转换
            return "";
        } else if (!columnName.contains("_")) {
            // 不含下划线，仅将首字母小写
            return columnName.substring(0, 1).toLowerCase() + columnName.substring(1);
        }
        // 用下划线将原始字符串分割
        String camels[] = columnName.split("_");
        for (String camel :  camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 处理真正的驼峰片断
            if (result.length() == 0) {
                // 第一个驼峰片断，所有字母都小写
                result.append(camel.toLowerCase());
            } else {
                // 其余的驼峰片断，首字母大写
                result.append(camel.substring(0, 1).toUpperCase());
                result.append(camel.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

}
