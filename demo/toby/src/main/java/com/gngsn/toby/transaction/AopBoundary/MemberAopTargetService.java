package com.gngsn.toby.transaction.AopBoundary;

import com.gngsn.toby.transaction.Member;

import java.util.List;

public interface MemberAopTargetService {
    void addMember(List<Member> members);

    void noTxAddMember(List<Member> members);

}
