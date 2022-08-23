package com.gngsn.apressbatch.batch;

import com.gngsn.apressbatch.domain.CustomerUpdate;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.file.transform.PatternMatchingCompositeLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * STEP1. 변경할 고객 정보 불러오기
 * p.508 ~ p.514
 */
@Configuration
public class CustomerUpdateItemReader {

    /**
     * customerUpdatesLineTokenizer
     *
     * 읽어들일 데이터는 세 종류이기 때문에 LineTokenizer도 세 종류가 필요 (합성패턴 사용)
     * PatternMatchingCompositeLineTokenizer에 세 가지 종류의 LineTokenizer를 등록
     * LineTokenizer는 구현체를 정의하고 패턴을 정의한다.
     *
     * @return PatternMatchingCompositeLineTokenizer
     * @throws Exception
     */
    @Bean(name = "customerUpdatesLineTokenizer")
    public LineTokenizer customerUpdatesLineTokenizer() throws Exception {
        DelimitedLineTokenizer recordType1 = new DelimitedLineTokenizer();

        recordType1.setNames("recordId", "customerId", "firstName", "middleName", "lastName");
        recordType1.afterPropertiesSet();

        DelimitedLineTokenizer recordType2 = new DelimitedLineTokenizer();

        recordType2.setNames("recordId", "customerId", "address1", "address2", "city", "state", "postalCode");
        recordType2.afterPropertiesSet();

        DelimitedLineTokenizer recordType3 = new DelimitedLineTokenizer();

        recordType3.setNames("recordId", "customerId", "email", "homePhone", "cellPhone", "workPhone", "notificationPreference");
        recordType3.afterPropertiesSet();

        Map<String, LineTokenizer> tokenizers = new HashMap<>(3);
        tokenizers.put("1*", recordType1);
        tokenizers.put("2*", recordType2);
        tokenizers.put("3*", recordType3);

        PatternMatchingCompositeLineTokenizer lineTokenizer =
            new PatternMatchingCompositeLineTokenizer();

        lineTokenizer.setTokenizers(tokenizers);
        return lineTokenizer;
    }

    @Bean(name = "customerUpdateFieldSetMapper")
    public FieldSetMapper<CustomerUpdate> customerUpdateFieldSetMapper() {
        return fieldSet -> {
            switch (fieldSet.readInt("recordId")) {
                case 1:
                    return new CustomerUpdate.Name(
                        fieldSet.readLong("customerId"),
                        fieldSet.readString("firstName"),
                        fieldSet.readString("middleName"),
                        fieldSet.readString("lastName"));

                case 2:
                    return new CustomerUpdate.Address(
                        fieldSet.readLong("customerId"),
                        fieldSet.readString("address1"),
                        fieldSet.readString("address2"),
                        fieldSet.readString("city"),
                        fieldSet.readString("state"),
                        fieldSet.readString("postalCode")
                    );

                case 3:
                    String rawPreference = fieldSet.readString("notificationPreference");
                    Integer notificationPreference = null;

                    if (StringUtils.hasText(rawPreference)) {
                        notificationPreference = Integer.parseInt(rawPreference);
                    }

                    return new CustomerUpdate.Contact(
                        fieldSet.readLong("customerId"),
                        fieldSet.readString("email"),
                        fieldSet.readString("homePhone"),
                        fieldSet.readString("cellPhone"),
                        fieldSet.readString("workPhone"),
                        notificationPreference
                    );

                default: throw new IllegalArgumentException(
                    String.format("Invalid record type was found: %s", fieldSet.readInt("recordId"))
                );
            }
        };
    }
}
