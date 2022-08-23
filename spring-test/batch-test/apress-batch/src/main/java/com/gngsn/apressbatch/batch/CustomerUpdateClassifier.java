package com.gngsn.apressbatch.batch;

import com.gngsn.apressbatch.domain.CustomerUpdate;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.classify.Classifier;


/**
 * 3개의 ItemWriter를 등록했기 때문에 전달되는 아이템 유형에 띠라 올바른 Writer가 선택되도록 선택해야 함.
 * 주어진 아이템을 확인 후 적절한 ItemWriter를 반환하는 Classifier를 구현한다.
 */
//@RequiredArgsConstructor
@AllArgsConstructor
public class CustomerUpdateClassifier implements Classifier<CustomerUpdate, ItemWriter<? super CustomerUpdate>> {

    private final JdbcBatchItemWriter<CustomerUpdate> recordType1ItemWriter;
    private final JdbcBatchItemWriter<CustomerUpdate> recordType2ItemWriter;
    private final JdbcBatchItemWriter<CustomerUpdate> recordType3ItemWriter;

    @Override
    public ItemWriter<? super CustomerUpdate> classify(CustomerUpdate classifiable) {

        if (classifiable instanceof CustomerUpdate.Name) {
            return recordType1ItemWriter;
        } else if (classifiable instanceof CustomerUpdate.Address) {
            return recordType2ItemWriter;
        } else if (classifiable instanceof CustomerUpdate.Contact) {
            return recordType3ItemWriter;
        } else {
            throw new IllegalArgumentException(
                String.format("Invalid type: %s", classifiable.getClass().getCanonicalName())
            );
        }
    }
}
