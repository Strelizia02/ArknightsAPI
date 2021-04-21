package com.strelizia.arknights.service.impl;

import com.strelizia.arknights.dao.UserFoundMapper;
import com.strelizia.arknights.util.AdminUtil;
import com.strelizia.arknights.dao.AdminUserMapper;
import com.strelizia.arknights.dao.ExecuteSqlMapper;
import com.strelizia.arknights.model.AdminUserInfo;
import com.strelizia.arknights.service.ExecuteSqlService;
import com.strelizia.arknights.util.SendMsgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

/**
 * @author wangzy
 * @Date 2021/2/20 11:02
 **/
@Service
public class ExecuteSqlServiceImpl implements ExecuteSqlService {

    @Autowired
    private ExecuteSqlMapper executeSqlMapper;

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Autowired
    private UserFoundMapper userFoundMapper;

    @Autowired
    private SendMsgUtil sendMsgUtil;

    @Override
    public String ExecuteSql(Long qq, String text) {
        List<AdminUserInfo> admins = adminUserMapper.selectAllAdmin();
        String qqMd5 = DigestUtils.md5DigestAsHex(qq.toString().getBytes());
        boolean b = AdminUtil.getSqlAdmin(qqMd5, admins);
        String s = "您没有sql权限";
        if (b) {
            String sql = text.substring(4).replace('~', ' ');
            s = executeSqlMapper.executeSql(sql).toString();
        }
        return s;
    }

    @Override
    public String sendGroupMessage(Long qq, String text) {
        List<AdminUserInfo> admins = adminUserMapper.selectAllAdmin();
        String qqMd5 = DigestUtils.md5DigestAsHex(qq.toString().getBytes());
        boolean b = AdminUtil.getSqlAdmin(qqMd5, admins);
        String s = "您没有群发消息权限";
        if (b) {
            List<Long> groupIds = userFoundMapper.selectAllGroups();
            for (Long groupId : groupIds) {
                sendMsgUtil.CallOPQApiSendMsg(groupId, text, 2);
            }
            return "";
        }
        return s;
    }
}
