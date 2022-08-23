package com.gngsn.apressbatch.config;

import com.gngsn.apressbatch.domain.Transaction;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.BigDecimalConverter;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.xstream.XStreamMarshaller;

import static com.thoughtworks.xstream.XStream.PRIORITY_NORMAL;

@Configuration
public class XStreamConfiguration {

    public XStreamConfiguration(XStreamMarshaller marshaller) {
        XStream xstream = marshaller.getXStream();
        xstream.allowTypesByWildcard(new String[]{"com.gngsn.**"});
        xstream.processAnnotations(Transaction.class);

        xstream.registerConverter(new BigDecimalConverter(), PRIORITY_NORMAL);
        xstream.registerConverter(new DateConverter("yyyy-MM-dd HH:mm:ss",
            new String[] {"yyyy-MM-dd hh:mm:ss"}), PRIORITY_NORMAL);
    }
}