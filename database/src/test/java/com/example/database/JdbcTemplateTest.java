package com.example.database;

import com.example.database.jdbc.Member;
import com.example.database.jdbcTemplate.JdbcTemplateMemberRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@Transactional
@SpringBootTest
public class JdbcTemplateTest {
    @Autowired JdbcTemplateMemberRepository repository;

    @Test
    @DisplayName("저장")
    void accountTransfer(){
        //given
        Member memberA = new Member("memberA", 10000);
        Member memberB = new Member("memberB", 10000);
        repository.save(memberA);
        repository.save(memberB);

        Optional<Member> byId = repository.findById("memberB");
        Assertions.assertThat(byId.get().getMemberId().equals(memberB.getMemberId()));
    }

}
