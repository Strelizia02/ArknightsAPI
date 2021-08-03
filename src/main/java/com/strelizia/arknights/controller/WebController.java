package com.strelizia.arknights.controller;

import com.strelizia.arknights.dao.AdminUserMapper;
import com.strelizia.arknights.dao.AgentMapper;
import com.strelizia.arknights.dao.GroupAdminInfoMapper;
import com.strelizia.arknights.dao.NickNameMapper;
import com.strelizia.arknights.model.AdminUserInfo;
import com.strelizia.arknights.model.AgentInfo;
import com.strelizia.arknights.model.GroupAdminInfo;
import com.strelizia.arknights.model.NickName;
import com.strelizia.arknights.vo.JsonResult;
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

    @Autowired
    private NickNameMapper nickNameMapper;


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
    @GetMapping("getUserAdmin")
    public JsonResult<List<AdminUserInfo>> getUserAdmin() {
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
    @GetMapping("getGroupAdmin")
    public JsonResult<List<GroupAdminInfo>> getGroupAdmin() {
        List<GroupAdminInfo> allGroupAdmin = groupAdminInfoMapper.getAllGroupAdmin();
        return JsonResult.success(allGroupAdmin);
    }


    /**
     * 卡池搜索接口
     * @return
     */
    @GetMapping("getPool")
    public JsonResult<List<AgentInfo>> getPool() {
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
    @GetMapping("getAgent")
    public JsonResult<List<AgentInfo>> getAgent(@RequestParam String pool) {
        List<AgentInfo> agentInfos = agentMapper.selectPoolAgent(pool);
        return JsonResult.success(agentInfos);
    }

    /**
     * 删除卡池up干员
     * @param pool
     * @return
     */
    @DeleteMapping("deleteAgentPool")
    public JsonResult<Boolean> deleteAgentPool(@RequestParam String pool) {
        agentMapper.deleteAgentPool(pool);
        return null;
    }

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
            agentMapper.insertAgentPool(agent);
        }
        return null;
    }

    /**
     * 修改干员外号
     * @param message
     * @return
     */
    @PostMapping("setNickName")
    public Integer setNickName(
            @RequestBody List<NickName> message
    ) {
        int count = 0;
        nickNameMapper.deleteNickName();
        for (NickName nickName : message){
            count += nickNameMapper.insertNickName(nickName);
        }
        return count;
    }

    /**
     * 查询干员外号
     * @return
     */
    @GetMapping("getNickName")
    public JsonResult<List<NickName>> getNickName() {
        List<NickName> nickNames = nickNameMapper.selectAllNickName();
        return JsonResult.success(nickNames);
    }

}
