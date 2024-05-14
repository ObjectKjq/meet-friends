package com.kjq.project.service;

import com.kjq.project.model.dto.team.TeamJoinRequest;
import com.kjq.project.model.dto.team.TeamQuery;
import com.kjq.project.model.dto.team.TeamQuitRequest;
import com.kjq.project.model.dto.team.TeamUpdateRequest;
import com.kjq.project.model.entity.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kjq.project.model.entity.User;
import com.kjq.project.model.vo.TeamUserVO;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 86175
* @description 针对表【team】的数据库操作Service
* @createDate 2024-03-15 23:39:28
*/
public interface TeamService extends IService<Team> {

    @Transactional(rollbackFor = Exception.class)
    long addTeam(Team team, User loginUser);

    List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin);

    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser, HttpServletRequest request);

    List<TeamUserVO> countIsJoinTeam(List<TeamUserVO> teamList, HttpServletRequest request);

    @Transactional(rollbackFor = Exception.class)
    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    @Transactional(rollbackFor = Exception.class)
    boolean deleteTeam(long id, User loginUser);

    boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);
}
