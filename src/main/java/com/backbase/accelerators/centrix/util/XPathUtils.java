package com.backbase.accelerators.centrix.util;

import lombok.SneakyThrows;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPathFactory;
import java.io.StringReader;

public class XPathUtils {

    @SneakyThrows
    public static String getStringValue(String xml, String path) {
        return XPathFactory.newInstance()
                .newXPath()
                .evaluateExpression(path, createInputSource(xml), String.class)
                .trim();
    }

    private static InputSource createInputSource(String xml) {
        return new InputSource(new StringReader(xml));
    }
}
