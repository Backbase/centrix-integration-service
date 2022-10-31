package com.backbase.accelerators.centrix.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XPathUtilsTest {

    @Test
    public void should_extract_value_from_xml() {
        String xml = "<otpwd>999999999999999</otpwd>";
        String result = XPathUtils.getStringValue(xml, "//otpwd/text()");

        assertEquals("999999999999999", result);
    }
}