package com.gngsn.apressbatch.job;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

public class BigDecimalConverter extends AbstractSingleValueConverter {
    public BigDecimalConverter() {
    }

    @Override
    public boolean canConvert(Class type) {
        return type == BigDecimal.class;
    }

    @Override
    public Object fromString(String str) {
        if (StringUtils.hasText(str)) {
            return new BigDecimal(str);
        } else {
            return new BigDecimal(0);
        }
    }
}
