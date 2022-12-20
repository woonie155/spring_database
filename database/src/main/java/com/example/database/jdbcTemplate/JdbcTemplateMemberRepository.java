package com.example.database.jdbcTemplate;

import com.example.database.jdbc.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcTemplateMemberRepository {

    private final JdbcTemplate template;

    public Member save(Member member){
        String sql = "insert into member(member_id, money) values (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
//            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"member_id"});
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, member.getMemberId());
            ps.setInt(2, member.getMoney());
            return ps;
        }, keyHolder);

//        String key = keyHolder.getKey();
        return member;
    }

    public Optional<Member> findById(String name) {
        String sql = "select member_id, money from member where member_id = ?";
        try {
            Member member = template.queryForObject(sql, memberRowMapper(), name);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setMemberId(rs.getString("member_id"));
            member.setMoney(rs.getInt("money"));
            return member;
        };
    }
}
