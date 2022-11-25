package com.gngsn.toby.transaction.AnnotBoundary;

import com.gngsn.toby.transaction.Member;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemberAnnotService {
    @Transactional
    void addMember(List<Member> members);

    void noTxAddMember(List<Member> members);
}
