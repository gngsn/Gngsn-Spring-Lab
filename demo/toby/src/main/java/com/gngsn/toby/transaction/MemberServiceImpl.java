package com.gngsn.toby.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberDAO memberDAO;

    @Override
    public int getMembersCnt() {
        return memberDAO.selectMemberCnt();
    }
}
