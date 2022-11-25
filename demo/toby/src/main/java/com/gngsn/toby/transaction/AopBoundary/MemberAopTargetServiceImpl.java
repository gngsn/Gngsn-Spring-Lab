package com.gngsn.toby.transaction.AopBoundary;

import com.gngsn.toby.transaction.Member;
import com.gngsn.toby.transaction.MemberDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberAopTargetServiceImpl implements MemberAopTargetService {
    @Autowired
    private MemberDAO memberDAO;

    @Override
    public void addMember(List<Member> members) {
        try {
            members.forEach(member -> memberDAO.insertMember(member));
        } catch (Exception e) {
            throw new RuntimeException("DB 저장 실패");
        }
    }

    @Override
    public void noTxAddMember(List<Member> members) {
        try {
            members.forEach(member -> memberDAO.insertMember(member));
        } catch (Exception e) {
            throw new RuntimeException("DB 저장 실패");
        }
    }
}
