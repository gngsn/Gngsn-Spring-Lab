package com.gngsn.apressbatch.valid;


import com.gngsn.apressbatch.dao.CustomerUpdateDAO;
import com.gngsn.apressbatch.domain.CustomerUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CustomerItemValidator implements Validator<CustomerUpdate> {

    private final CustomerUpdateDAO customerUpdateDAO;

    @Override
    public void validate(CustomerUpdate customer) throws ValidationException {
        int count = customerUpdateDAO.selectCnt(customer.getCustomerId());

        if (count == 0) {
            throw new ValidationException(
                String.format("Customer id %s was not able to be found", customer.getCustomerId())
            );
        }
    }
}
