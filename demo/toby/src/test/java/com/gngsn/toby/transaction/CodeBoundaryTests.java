package com.gngsn.toby.transaction;

import com.gngsn.toby.transaction.CodeBoundary.MemberCodeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/* Code 트랜잭션 Test
 */
@SpringBootTest
public class CodeBoundaryTests {
    @Autowired
    MemberService memberService;

    @Autowired
    MemberCodeService memberCodeService;

    @Test
    void successTest() {
        final List<Member> members = new ArrayList<>();

        members.add(new Member("success1", "gngsn1@mail.com", "qwerty"));
        members.add(new Member("success2", "gngsn2@mail.com", "qwerty"));
        members.add(new Member("success3", "gngsn3@mail.com", "qwerty"));
        members.add(new Member("success4", "gngsn4@mail.com", "qwerty"));

        int beforeCnt = memberService.getMembersCnt();
        memberCodeService.addMember(members);
        int afterCnt = memberService.getMembersCnt();

        System.out.println("before : " + beforeCnt + ", afterCnt : " + afterCnt);
        Assertions.assertTrue(afterCnt-beforeCnt == members.size());
    }

    /* 코드를 통한 트랜잭션 설정 - Fail Case
     *
     * transactionTemplate을 사용한 트랜잭션
     * 실제로 이렇게는 잘사용하지 않음
     *
     * 실행 전과 후의 DB 데이터의 수를 비교 후
     * 트랜잭션이 정상 작동 됐는지 확인
     * */
    @Test
    void failTestNameTooLong() {
        List<Member> members = setErrorMemberList();

        int beforeCnt = memberService.getMembersCnt();
        memberCodeService.addMember(members);
        int afterCnt = memberService.getMembersCnt();

        System.out.println("before : " + beforeCnt + ", afterCnt : " + afterCnt);
        Assertions.assertTrue(beforeCnt == afterCnt);
    }


    /* 코드를 통한 트랜잭션 설정
     *
     * transactionTemplate을 사용하지 않았을 때
     *
     * 실행 전과 후의 DB 데이터의 수를 비교 후
     * 트랜잭션이 정상 작동 됐는지 확인
     * */
    @Test
    void failNoTxTestNameTooLong() {
        List<Member> members = setErrorMemberList();

        int beforeCnt = memberService.getMembersCnt();
        memberCodeService.noTxAddMember(members);
        int afterCnt = memberService.getMembersCnt();

        System.out.println("before : " + beforeCnt + ", afterCnt : " + afterCnt);
        Assertions.assertTrue(beforeCnt != afterCnt);
    }


    private List<Member> setErrorMemberList() {
        final List<Member> members = new ArrayList<>();

        members.add(new Member("txtest1", "gngsn1@mail.com", "qwerty"));
        members.add(new Member("txtest2", "gngsn2@mail.com", "qwerty"));
        members.add(new Member("너무길어서오류가난다음트랜잭션걸리겠지?", "gngsn3@mail.com", "qwerty"));
        members.add(new Member("txtest4", "gngsn4@mail.com", "qwerty"));

        return members;
    }
}
