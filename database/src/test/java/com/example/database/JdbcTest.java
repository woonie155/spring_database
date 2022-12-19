package com.example.database;

import com.example.database.jdbc.DBConnectionUtil;
import com.example.database.jdbc.Member;
import com.example.database.jdbc.JdbcMemberRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
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

    @Test
    void connection() throws SQLException {
        Connection connection = DBConnectionUtil.getConnection();
        Assertions.assertThat(connection).isNotNull();

        Connection datasourceConnection = DBConnectionUtil.getDataSourceConnection().getConnection();
        Assertions.assertThat(datasourceConnection).isNotNull();
    }

    @Test
    void crud() throws SQLException {
        Member member = new Member("member1", 10000);
        repository.save(member);

        Member findMember = repository.findById(member.getMemberId());
        Assertions.assertThat(findMember).isEqualTo(member); //롬복으로 동등성 보장

        repository.update(member.getMemberId(), 20000);
        Member updateMember = repository.findById(member.getMemberId());
        Assertions.assertThat(updateMember.getMoney()).isEqualTo(20000);

        repository.delete(member.getMemberId());
        Assertions.assertThatThrownBy(() -> repository.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);
    }
}
