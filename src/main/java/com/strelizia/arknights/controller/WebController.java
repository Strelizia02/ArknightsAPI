package com.strelizia.arknights.controller;

import com.strelizia.arknights.annotation.Token;
import com.strelizia.arknights.dao.*;
import com.strelizia.arknights.model.*;
import com.strelizia.arknights.vo.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @Autowired
    private ModelCountMapper modelCountMapper;

    @Autowired
    private LoginMapper loginMapper;

    @Autowired
    private BiliMapper biliMapper;

    @Autowired
    private ActivityMapper activityMapper;


    /**
     * 用户权限单行修改
     * @param messages 用户权限
     * @return 修改人数
     */
    @Token
    @PostMapping("setUserAdmin")
    public JsonResult<Integer> setUserAdmin(
            @RequestBody AdminUserInfo messages
    ) {
        return JsonResult.success(adminUserMapper.updateUserAdmin(messages));
    }

    /**
     * 用户权限单行添加
     * @param messages 用户权限
     * @return 修改人数
     */
    @Token
    @PostMapping("addUserAdmin")
    public JsonResult<Integer> addUserAdmin(
            @RequestBody AdminUserInfo messages
    ) {
        return JsonResult.success(adminUserMapper.insertUserAdmin(messages));
    }

    /**
     * 用户权限单行删除
     * @return
     */
    @Token
    @DeleteMapping("deleteUserAdmin")
    public JsonResult<Integer> deleteUserAdmin(@RequestParam String qq) {
        return JsonResult.success(adminUserMapper.daleteUserAdmin(qq));
    }

    /**
     * 用户权限搜索接口
     * @return
     */
    @Token
    @GetMapping("getUserAdmin")
    public JsonResult<List<AdminUserInfo>> getUserAdmin(@RequestParam Integer current) {
        List<AdminUserInfo> adminUserInfos = adminUserMapper.selectAllAdminByPage(10 * (current - 1));
        Integer count = adminUserMapper.selectAllAdminCount();
        return JsonResult.success(adminUserInfos, count);
    }

    /**
     * 群权限管理接口
     * @param message
     * @return
     */
    @Token
    @PostMapping("setGroupAdmin")
    public JsonResult<Integer> setGroupAdmin(
            @RequestBody GroupAdminInfo message
    ) {
        return JsonResult.success(groupAdminInfoMapper.updateGroupAdmin(message));
    }

    /**
     * 群权限搜索接口
     * @return
     */
    @Token
    @GetMapping("getGroupAdmin")
    public JsonResult<List<GroupAdminInfo>> getGroupAdmin(@RequestParam Integer current) {
        List<GroupAdminInfo> allGroupAdmin = groupAdminInfoMapper.getAllGroupAdmin(10 * (current - 1));
        Integer count = groupAdminInfoMapper.getAllGroupAdminCount();
        return JsonResult.success(allGroupAdmin, count);
    }


    /**
     * 卡池搜索接口
     * @return
     */
    @Token
    @GetMapping("getPool")
    public JsonResult<List<AgentInfo>> getPool(@RequestParam Integer current, @RequestParam String pool) {
        List<String> poolNames = agentMapper.selectPoolByPage(pool, 10 * (current - 1));
        List<AgentInfo> pools = new ArrayList<>(poolNames.size());
        for(String poolName : poolNames){
            Integer limit = agentMapper.selectPoolLimit(poolName);
            AgentInfo poolInfo = new AgentInfo();
            poolInfo.setLimit(limit);
            poolInfo.setName(poolName);
            pools.add(poolInfo);
        }
        Integer count = agentMapper.selectPoolCount(pool);
        return JsonResult.success(pools, count);
    }


    /**
     * 卡池up干员搜索接口
     * @param pool
     * @return
     */
    @Token
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
    @Token
    @DeleteMapping("deleteAgentPool")
    public void deleteAgentPool(@RequestParam String pool) {
        agentMapper.deleteAgentPool(pool);
    }


    /**
     * 设置卡池up干员
     * @param message
     * @return
     */
    @Token
    @PostMapping("setAgentPool")
    public JsonResult<Boolean> setAgentPool(
            @RequestBody List<AgentInfo> message
    ) {
        deleteAgentPool(message.get(0).getPool());
        for (AgentInfo agent : message){
            agentMapper.insertAgentPool(agent);
        }
        return JsonResult.success(true);
    }


    /**
     * 新增干员外号
     * @param message
     * @return
     */
    @Token
    @PostMapping("addNickName")
    public JsonResult<Integer> addNickName(
            @RequestBody NickName message
    ) {
        return JsonResult.success(nickNameMapper.insertNickName(message));
    }


    /**
     * 删除干员外号
     * @return
     */
    @Token
    @DeleteMapping("deleteNickName")
    public JsonResult<Integer> deleteNickName(@RequestParam String nickName) {
        return JsonResult.success(nickNameMapper.deleteNickName(nickName));
    }


    /**
     * 查询干员外号
     * @return
     */
    @Token
    @GetMapping("getNickName")
    public JsonResult<List<NickName>> getNickName(@RequestParam Integer current) {
        List<NickName> nickNames = nickNameMapper.selectAllNickName(10 * (current - 1));
        Integer count = nickNameMapper.selectAllNickNameCount();
        return JsonResult.success(nickNames, count);
    }


    /**
     * 查询功能调用次数
     * @return
     */
    @GetMapping("getNumberOfCalls")
    public JsonResult<List<ModelCountInfo>> getNumberOfCalls(){
        List<ModelCountInfo> modelCountInfos = modelCountMapper.selectModelCount();
        return JsonResult.success(modelCountInfos);
    }

    /**
     * 登录接口
     * @param message
     * @return
     */
    @PostMapping("login")
    public JsonResult<String> login(
            @RequestBody LoginUser message
    ) {
        LoginUser inPwd = loginMapper.getloginUser(message.getUserName());
        if(inPwd != null && inPwd.getPassWord().equals(DigestUtils.md5DigestAsHex(message.getPassWord().getBytes()))){
            String token = DigestUtils.md5DigestAsHex(((System.currentTimeMillis() + new Random().nextInt(999999999)) + "").getBytes());
            loginMapper.setToken(inPwd.getUserName(), token);
            return JsonResult.success(token);
        }else {
            return JsonResult.failureWithCode("300", "用户名或密码无效");
        }
    }

    /**
     * UID监听搜索接口
     * @return
     */
    @Token
    @GetMapping("getUid")
    public JsonResult<List<BiliCount>> getUid(@RequestParam Integer current, @RequestParam String name) {
        List<BiliCount> biliCountListByPage = biliMapper.getBiliCountListByPage(name, 10 * (current - 1));
        Integer count = biliMapper.getBiliCountListCount(name);
        return JsonResult.success(biliCountListByPage, count);
    }

    /**
     * 查询某Uid有哪些群关注
     * @return
     */
    @Token
    @GetMapping("getGroupsByUid")
    public JsonResult<List<Long>> getGroupsByUid(@RequestParam Long uid) {
        List<Long> groups = biliMapper.selectGroupByUid(uid);
        return JsonResult.success(groups);
    }

    /**
     * 查询某Uid没有被哪些群关注
     * @return
     */
    @Token
    @GetMapping("getGroupByNotListenUid")
    public JsonResult<List<Long>> getGroupByNotListenUid(@RequestParam Long uid, @RequestParam String groupId) {
        List<Long> groups = biliMapper.selectGroupByNotListenUid(uid, groupId);
        return JsonResult.success(groups);
    }

    /**
     * 监听某uid
     * @return
     */
    @Token
    @GetMapping("listenUid")
    public JsonResult<Integer> listenUid(@RequestParam Long uid) {
        return JsonResult.success(biliMapper.insertBiliUid(uid));
    }

    /**
     * 取消监听某uid
     * @return
     */
    @Token
    @DeleteMapping("deleteUid")
    public JsonResult<Integer> deleteUid(@RequestParam Long uid) {
        return JsonResult.success(biliMapper.deleteUid(uid));
    }

    /**
     * 某群关注某uid
     * @return
     */
    @Token
    @GetMapping("addGroupUid")
    public JsonResult<Integer> addGroupUid(@RequestParam Long groupId, @RequestParam Long uid) {
        return JsonResult.success(biliMapper.insertGroupBiliRel(groupId, uid));
    }


    /**
     * 某群取消关注某uid
     * @return
     */
    @Token
    @DeleteMapping("deleteGroupUid")
    public JsonResult<Integer> deleteGroupUid(@RequestParam Long groupId, @RequestParam Long uid) {
        return JsonResult.success(biliMapper.deleteGroupBiliRel(groupId, uid));
    }

    /**
     * 获取某群未关注的列表
     * @return
     */
    @Token
    @GetMapping("getUidNotListen")
    public JsonResult<List<BiliCount>> getUidNotListen(@RequestParam Long groupId, @RequestParam String name) {
        return JsonResult.success(biliMapper.getNotListenListByGroupId(groupId, name));
    }

    /**
     * 获取某群关注的列表
     * @return
     */
    @Token
    @GetMapping("getUidByGroup")
    public JsonResult<List<BiliCount>> getUidByGroup(@RequestParam Long groupId) {
        return JsonResult.success(biliMapper.getBiliCountListByGroupId(groupId));
    }

    /**
     * 获取每日活跃度
     * @return
     */
    @GetMapping("getActivity")
    public JsonResult<Map<String, List<ActivityInfo>>> getActivity() {
        List<ActivityInfo> SendMsg = new ArrayList<>();
        List<ActivityInfo> SendPic =  new ArrayList<>();
        List<ActivityInfo> Getmsg =  new ArrayList<>();
        List<ActivityInfo> activityInfos = activityMapper.selectActivity();
        for (ActivityInfo actInfo :activityInfos){
            if (actInfo.getType() == 0) {
                Getmsg.add(actInfo);
            } else if (actInfo.getType() == 1) {
                SendMsg.add(actInfo);
            } else if (actInfo.getType() == 2) {
                SendPic.add(actInfo);
            }
        }
        Map<String, List<ActivityInfo>> result = new HashMap<>(3);
        result.put("接收消息", Getmsg);
        result.put("发送文字", SendMsg);
        result.put("发送图片", SendPic);
        return JsonResult.success(result);
    }

}
