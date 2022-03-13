package com.gngsn.toby.transaction.CodeBoundary;

import com.gngsn.toby.transaction.Member;
import com.gngsn.toby.transaction.MemberDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberCodeServiceImpl implements MemberCodeService {
    private final PlatformTransactionManager transactionManager;
    private final MemberDAO memberDAO;

    @Override
    public void addMember(List<Member> members) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        transactionTemplate.execute(status -> {
                    members.forEach(member -> memberDAO.insertMember(member));
                    return members;
                }
        );
    }

    @Override
    public void noTxAddMember(List<Member> members) {
        members.forEach(member -> memberDAO.insertMember(member));
    }
}
