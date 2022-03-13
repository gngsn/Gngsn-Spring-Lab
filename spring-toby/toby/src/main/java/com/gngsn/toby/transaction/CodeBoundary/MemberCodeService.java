package com.gngsn.toby.transaction.CodeBoundary;

import com.gngsn.toby.transaction.Member;

import java.util.List;

public interface MemberCodeService {
    void addMember(List<Member> members);

    void noTxAddMember(List<Member> members);
}
