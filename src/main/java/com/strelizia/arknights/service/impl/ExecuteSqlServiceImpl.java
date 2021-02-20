package com.strelizia.arknights.service.impl;

import com.strelizia.arknights.util.AdminUtil;
import com.strelizia.arknights.dao.AdminUserMapper;
import com.strelizia.arknights.dao.ExecuteSqlMapper;
import com.strelizia.arknights.model.AdminUserInfo;
import com.strelizia.arknights.service.ExecuteSqlService;
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

    @Override
    public String ExecuteSql(Long qq, String text) {
        List<AdminUserInfo> admins = adminUserMapper.selectAllAdmin();
        String qqMd5 = DigestUtils.md5DigestAsHex(qq.toString().getBytes());
        boolean b = AdminUtil.getSqlAdmin(qqMd5,admins);
        String s = "您没有sql权限";
        if (b) {
            String sql = text.substring(4).replace('~', ' ');
            s = executeSqlMapper.executeSql(sql).toString();
        }
        return s;
    }
}
