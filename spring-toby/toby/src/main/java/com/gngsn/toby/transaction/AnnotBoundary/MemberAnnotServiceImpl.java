package com.gngsn.toby.transaction.AnnotBoundary;

import com.gngsn.toby.transaction.Member;
import com.gngsn.toby.transaction.MemberDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberAnnotServiceImpl implements MemberAnnotService {
    private final MemberDAO memberDAO;

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
