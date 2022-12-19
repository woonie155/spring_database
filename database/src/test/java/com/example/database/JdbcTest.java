package com.example.database;

import com.example.database.jdbc.DBConnectionUtil;
import com.example.database.jdbc.JdbcMemberService;
import com.example.database.jdbc.Member;
import com.example.database.jdbc.JdbcMemberRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.NoSuchElementException;

@Slf4j
@Transactional
@SpringBootTest
public class JdbcTest {

    @Autowired JdbcMemberRepository repository;
    @Autowired JdbcMemberService service;

    @Test
    void connection() throws SQLException {
        Connection connection = DBConnectionUtil.getConnection();
        Assertions.assertThat(connection).isNotNull();

        Connection datasourceConnection = DBConnectionUtil.getDataSourceConnection().getConnection();
        Assertions.assertThat(datasourceConnection).isNotNull();
    }

//    @Test
//    void crud() throws SQLException {
//        Member member = new Member("member1", 10000);
//        repository.save(member);
//
//        Member findMember = repository.findById(member.getMemberId());
//        Assertions.assertThat(findMember).isEqualTo(member); //롬복으로 동등성 보장
//
//        repository.update(member.getMemberId(), 20000);
//        Member updateMember = repository.findById(member.getMemberId());
//        Assertions.assertThat(updateMember.getMoney()).isEqualTo(20000);
//
//        repository.delete(member.getMemberId());
//        Assertions.assertThatThrownBy(() -> repository.findById(member.getMemberId()))
//                .isInstanceOf(NoSuchElementException.class);
//    }


    @Test
    @DisplayName("정상 이체")
    void accountTransfer() throws SQLException {
        //given
        Member memberA = new Member("memberA", 10000);
        Member memberB = new Member("memberB", 10000);
        repository.save(memberA);
        repository.save(memberB);

        //when
        service.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);

        //then
        Member findMemberA = repository.findById(memberA.getMemberId());
        Member findMemberB = repository.findById(memberB.getMemberId());
        Assertions.assertThat(findMemberA.getMoney()).isEqualTo(8000);
        Assertions.assertThat(findMemberB.getMoney()).isEqualTo(12000);
    }
    @Test
    @DisplayName("이체중 예외 발생")
    void accountTransferEx() throws SQLException {
        //given
        Member memberA = new Member("memberA", 10000);
        Member memberEx = new Member("ex", 10000);
        repository.save(memberA);
        repository.save(memberEx);

        //when
        Assertions.assertThatThrownBy(() ->
                service.accountTransfer(memberA.getMemberId(), memberEx.getMemberId(),
                        2000)).isInstanceOf(IllegalStateException.class);

        //then
        Member findMemberA = repository.findById(memberA.getMemberId());
        Member findMemberEx = repository.findById(memberEx.getMemberId());

        Assertions.assertThat(findMemberA.getMoney()).isEqualTo(10000);
        Assertions.assertThat(findMemberEx.getMoney()).isEqualTo(10000);
    }
}
