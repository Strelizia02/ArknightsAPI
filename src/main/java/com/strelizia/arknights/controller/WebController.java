package com.strelizia.arknights.controller;

import com.strelizia.arknights.dao.AdminUserMapper;
import com.strelizia.arknights.dao.AgentMapper;
import com.strelizia.arknights.dao.GroupAdminInfoMapper;
import com.strelizia.arknights.dao.ModelCountMapper;
import com.strelizia.arknights.model.AdminUserInfo;
import com.strelizia.arknights.model.AgentInfo;
import com.strelizia.arknights.model.EventsMessage;
import com.strelizia.arknights.model.GroupAdminInfo;
import com.strelizia.arknights.service.*;
import com.strelizia.arknights.util.SendMsgUtil;
import com.strelizia.arknights.vo.JsonResult;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangzy
 * @Date 2020/11/20 17:02
 * 通用的群聊回复接口
 **/
@RequestMapping("web")
@RestController
@Slf4j
public class WebController {

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Autowired
    private GroupAdminInfoMapper groupAdminInfoMapper;

    @Autowired
    private AgentMapper agentMapper;


    /**
     * 用户权限管理接口
     * @param messages 全部用户配置
     * @return 修改人数
     */
    @PostMapping("setUserAdmin")
    public Integer setUserAdmin(
            @RequestBody List<AdminUserInfo> messages
    ) {
        adminUserMapper.truncateUserAdmin();
        int count = 0;
        for (AdminUserInfo userAdmin : messages){
            count += adminUserMapper.updateUserAdmin(userAdmin);
        }
        return count;
    }

    /**
     * 用户权限搜索接口
     * @return
     */
    @GetMapping("searchUserAdmin")
    public JsonResult<List<AdminUserInfo>> searchUserAdmin() {
        List<AdminUserInfo> adminUserInfos = adminUserMapper.selectAllAdmin();
        return JsonResult.success(adminUserInfos);
    }

    /**
     * 群权限管理接口
     * @param message
     * @return
     */
    @PostMapping("setGroupAdmin")
    public Boolean setGroupAdmin(
            @RequestBody GroupAdminInfo message
    ) {
        Integer integer = groupAdminInfoMapper.updateGroupAdmin(message);
        return integer == 1;
    }

    /**
     * 群权限搜索接口
     * @return
     */
    @GetMapping("searchGroupAdmin")
    public JsonResult<List<GroupAdminInfo>> searchGroupAdmin() {
        List<GroupAdminInfo> allGroupAdmin = groupAdminInfoMapper.getAllGroupAdmin();
        return JsonResult.success(allGroupAdmin);
    }


    /**
     * 卡池搜索接口
     * @return
     */
    @GetMapping("searchPool")
    public JsonResult<List<AgentInfo>> searchPool() {
        List<String> poolNames = agentMapper.selectPool();
        List<AgentInfo> pools = new ArrayList<>(poolNames.size());
        for(String poolName : poolNames){
            Integer limit = agentMapper.selectPoolLimit(poolName);
            AgentInfo poolInfo = new AgentInfo();
            poolInfo.setLimit(limit);
            poolInfo.setName(poolName);
            pools.add(poolInfo);
        }
        return JsonResult.success(pools);
    }


    /**
     * 卡池up干员搜索接口
     * @param pool
     * @return
     */
    @GetMapping("searchAgent")
    public JsonResult<List<AgentInfo>> searchAgent(@RequestParam String pool) {
        List<AgentInfo> agentInfos = agentMapper.selectPoolAgent(pool);
        return JsonResult.success(agentInfos);
    }

//    /**
//     * 新增卡池up干员
//     * @param message
//     * @return
//     */
//    @PostMapping("insertAgentPool")
//    public JsonResult<Boolean> insertAgentPool(
//            @RequestBody List<AgentInfo> message
//    ) {
//        for (AgentInfo agent : message){
//            agentMapper.insertAgentPool(agent);
//        }
//        return null;
//    }

    /**
     * 设置卡池up干员
     * @param message
     * @return
     */
    @PostMapping("setAgentPool")
    public JsonResult<Boolean> setAgentPool(
            @RequestBody List<AgentInfo> message
    ) {
        for (AgentInfo agent : message){
            agentMapper.deleteAgentPool(agent);
            agentMapper.insertAgentPool(agent);
        }
        return null;
    }

}
