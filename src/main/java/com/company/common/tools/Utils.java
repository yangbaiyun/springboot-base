package com.company.common.tools;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    //根据name获取cookie的值
    public static String getCookieByName(Cookie[] cookies, String name) {
        String value = "";

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                value = cookie.getValue();
            }
        }

        return value;
    }

    //设置cookie的值
    public static void setCookie(HttpServletResponse response, String key, String value, int timeout) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(timeout);
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    //Object转map
    //入参可以是hashmap或者treeMap
    public static Map<String, String> objectToMap(Object obj, Map<String, String> map) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();
        System.out.println(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            String value = field.get(obj).toString();
            map.put(fieldName, value);
        }
        return map;
    }

    //Object转xml
    public static String beanToXml(Object obj, Class<?> load) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(load);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, writer);
        return writer.toString();
    }

    //xml转Object
    public static <T> T xmlToBean(String xml, Class<T> load) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(load);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T) unmarshaller.unmarshal(new StringReader(xml));
    }

    /**
     * md5 加密
     */
    public static String md5Encoding(String str) {
        StringBuilder sb = new StringBuilder(32);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(str.getBytes("utf-8"));

            for (int i = 0; i < array.length; i++) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).toUpperCase().substring(1, 3));
            }
        } catch (Exception e) {
            return null;
        }
        return sb.toString();
    }
}
