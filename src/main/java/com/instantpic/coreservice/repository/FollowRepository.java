package com.instantpic.coreservice.repository;
import com.instantpic.coreservice.dto.follow.FollowDto;
import com.instantpic.coreservice.dto.follow.FollowList;
import com.instantpic.coreservice.dto.follow.NeighborDto;
import com.instantpic.coreservice.dto.follow.NeighborList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class FollowRepository {

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public FollowRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private RowMapper<FollowDto> followDtoRowMapper() {
        return (rs, rowNum) -> {
            FollowDto follow = new FollowDto();
            follow.setUserId(rs.getString("user_id"));
            follow.setFollowId(rs.getString("following_id"));
          return follow;
        };
    }

    private RowMapper<NeighborDto> neighborDtoRowMapper() {
        return (rs, rowNum) -> {
            NeighborDto neighbor = new NeighborDto();
            neighbor.setNeighborId(rs.getString("following_id"));
            return neighbor;
        };
    }

    public Optional<FollowDto> follow(String userId, String followId) {
        jdbcTemplate.update("INSERT INTO instapic.follows (user_id, following_id) VALUES (?, ?)", userId, followId);
        List<FollowDto> result = jdbcTemplate.query("SELECT * FROM instapic.follows WHERE user_id = ? AND following_id = ?;", followDtoRowMapper(), userId, followId);
        return result.stream().findAny();
    }

    public Optional<FollowDto> unfollow(String userId, String followId) {
        List<FollowDto> result = jdbcTemplate.query("SELECT * FROM instapic.follows WHERE user_id = ? AND following_id = ?;", followDtoRowMapper(), userId, followId);
        jdbcTemplate.update("DELETE FROM instapic.follows WHERE user_id = ? AND follow_id = ?", userId, followId);
        return result.stream().findAny();
    }

    public FollowList getFollowers(String followingId) {
        FollowList result = new FollowList();
        result.setFollowList(jdbcTemplate.query("SELECT * FROM instapic.follows WHERE following_id = ?;", followDtoRowMapper(), followingId));
        return result;
    }

    public FollowList getFollowing(String userId) {
        FollowList result = new FollowList();
        result.setFollowList(jdbcTemplate.query("SELECT * FROM instapic.follows WHERE user_id = ?;", followDtoRowMapper(), userId));
        return result;
    }

    public NeighborList getNeighbors(String userId, String followingId) {
        NeighborList result = new NeighborList();
        result.setNeighbors(jdbcTemplate.query("SELECT a.following_id FROM instapic.follows AS a INNER JOIN instapic.follows AS b ON a.following_id = b.user_id WHERE a.user_id = ? AND b.following_id = ?;", neighborDtoRowMapper(), userId, followingId));
        return result;
    }

}