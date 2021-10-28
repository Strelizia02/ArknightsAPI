package com.strelizia.arknights.service.impl;

import com.strelizia.arknights.dao.AdminUserMapper;
import com.strelizia.arknights.dao.BiliMapper;
import com.strelizia.arknights.dao.GroupAdminInfoMapper;
import com.strelizia.arknights.dao.UserFoundMapper;
import com.strelizia.arknights.model.AdminUserInfo;
import com.strelizia.arknights.model.BiliCount;
import com.strelizia.arknights.model.DynamicDetail;
import com.strelizia.arknights.model.GroupAdminInfo;
import com.strelizia.arknights.service.BiliListeningService;
import com.strelizia.arknights.util.AdminUtil;
import com.strelizia.arknights.util.ImageUtil;
import com.strelizia.arknights.util.SendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangzy
 * @Date 2021/1/12 17:00
 **/
@Service
@Slf4j
public class BiliListeningServiceImpl implements BiliListeningService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserFoundMapper userFoundMapper;

    @Autowired
    private GroupAdminInfoMapper groupAdminInfoMapper;

    @Autowired
    private SendMsgUtil sendMsgUtil;

    @Autowired
    private BiliMapper biliMapper;

    @Autowired
    private ImageUtil imageUtil;

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Override
    public boolean getDynamicList() {
        List<BiliCount> biliCountList = biliMapper.getBiliCountList();
        boolean b = false;
        for (BiliCount bili : biliCountList) {
            try {
                String biliSpace = "https://space.bilibili.com/" + bili.getUid() + "/dynamic";
                String dynamicList = "https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/space_history?host_uid=";
                String dynamicListUrl = "&offset_dynamic_id=0&need_top=";
                String topDynamic = restTemplate
                        .exchange(dynamicList + bili.getUid() + dynamicListUrl + 1, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class).getBody();//1 -> 抓取置顶动态
                //解析动态列表json
                Long top = new JSONObject(topDynamic).getJSONObject("data").getJSONArray("cards").getJSONObject(0).getJSONObject("desc").getLong("dynamic_id");
                bili.setTop(top);
                //循环遍历每个被监听的账号
                String result;
                String url = dynamicList + bili.getUid() + dynamicListUrl + 0;//0 -> 不抓取置顶动态
                HttpEntity<String> httpEntity = new HttpEntity<>(new HttpHeaders());
                String s = restTemplate
                        .exchange(url, HttpMethod.GET, httpEntity, String.class).getBody();
                JSONObject dynamicJson = new JSONObject(s);
                //解析动态列表json
                JSONArray dynamics = dynamicJson.getJSONObject("data").getJSONArray("cards");
                //获取当前的最新5动态
                List<Long> newList = new ArrayList<>(5);
                for (int i = 0; i < 5; i++) {
                    newList.add(dynamics.getJSONObject(i).getJSONObject("desc").getLong("dynamic_id"));
                }
                //对比第一条动态
                Long newId = newList.get(0);
                Long first = bili.getFirst();
                if (!first.equals(newId)) {
                    bili.setFirst(newId);
                    bili.setSecond(newList.get(1));
                    bili.setThird(newList.get(2));
                    bili.setFourth(newList.get(3));
                    bili.setFifth(newList.get(4));
                    //获取最新动态详情
                    DynamicDetail newDetail = getDynamicDetail(newId);
                    String name = newDetail.getName();
                    bili.setName(name);
                    biliMapper.updateNewDynamic(bili);
                    result = name + "更新了一条" + newDetail.getType() + "动态\n" +
                            newDetail.getTitle() + "\n" +
                            newDetail.getText() + "\n" + biliSpace;
                    log.info("{}有新动态", name);
                    b = true;
                    List<Long> groups = userFoundMapper.selectCakeGroups(bili.getUid());
                    String pic = newDetail.getPicUrl();
                    if (pic == null) {
                        for (Long groupId : groups) {
                            sendMsgUtil.CallOPQApiSendMsg(groupId, result, 2);
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException ignored) {

                            }
                        }
                    } else {
//                    String picBase64 = imageUtil.getImageBase64ByUrl(pic);
                        for (Long groupId : groups) {
                            sendMsgUtil.CallOPQApiSendImg(groupId, result, SendMsgUtil.picUrl, pic, 2);
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException ignored) {

                            }
                        }
                    }
                }
            }catch(Exception e){

            }
        }
        return b;
    }

    @Override
    public DynamicDetail getDynamicDetail(Long DynamicId) {
        DynamicDetail dynamicDetail = new DynamicDetail();
        //获取动态的Json消息
        HttpEntity<String> httpEntity = new HttpEntity<>(new HttpHeaders());
        String dynamicDetailUrl = "https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/get_dynamic_detail?dynamic_id=";
        String s = restTemplate
                .exchange(dynamicDetailUrl + DynamicId, HttpMethod.GET, httpEntity, String.class).getBody();
        JSONObject detailJson = new JSONObject(s);
        int type = detailJson.getJSONObject("data").getJSONObject("card").getJSONObject("desc").getInt("type");
        String cardStr = detailJson.getJSONObject("data").getJSONObject("card").getString("card");
        JSONObject cardJson = new JSONObject(cardStr);
        String text = "";
        String name = detailJson.getJSONObject("data").getJSONObject("card").getJSONObject("desc").getJSONObject("user_profile").getJSONObject("info").getString("uname");
        String dType = "";
        String title = "";
        String pic = null;
        switch (type) {
            case 1:
                dType = "转发";
                title = "请点击链接查看转发动态详情";
                text = cardJson.getJSONObject("item").getString("content");
                break;
            case 2://普通动态有图
                dType = "图文";
                text = cardJson.getJSONObject("item").getString("description");
                pic = cardJson.getJSONObject("item").getJSONArray("pictures").getJSONObject(0).getString("img_src");
                break;
            case 64://专栏动态
                dType = "专栏";
                title = cardJson.getString("title");
                text = "https://www.bilibili.com/read/cv" + cardJson.getLong("id");
                pic = cardJson.getJSONArray("image_urls").getString(0);
                break;
            case 4://普通动态无图
                dType = "文字";
                text = cardJson.getJSONObject("item").getString("content");
                break;
            case 8://视频动态
                dType = "视频";
                title = cardJson.getString("title");
                pic = cardJson.getString("pic");
                text = "https://www.bilibili.com/video/" + detailJson.getJSONObject("data").getJSONObject("card").getJSONObject("desc").getString("bvid");
                break;
            default:
                title = "请点击链接查看最新动态";
                break;
        }
        dynamicDetail.setName(name);
        dynamicDetail.setTitle(title);
        dynamicDetail.setType(dType);
        dynamicDetail.setPicUrl(pic);
        dynamicDetail.setText(text);

        return dynamicDetail;
    }

    @Override
    public String getVideo(Long qq, String name) {
        BiliCount bili = biliMapper.getOneDynamicByName(name);
        String videoUrl = "&pn=1&ps=1&jsonp=jsonp";
        String videoHead = "https://api.bilibili.com/x/space/arc/search?mid=";
        String newBvstr = restTemplate
                .exchange(videoHead + bili.getUid() + videoUrl, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class).getBody();
        JSONObject newBvJson = new JSONObject(newBvstr);
        String newBv = newBvJson.getJSONObject("data").getJSONObject("list").getJSONArray("vlist").getJSONObject(0).getString("bvid");
        return "[ATUSER(" + qq + ")]https://www.bilibili.com/video/" + newBv;
    }

    @Override
    public String getDynamic(Long qq, Long groupId, String name, int index) {
        BiliCount dynamics = biliMapper.getOneDynamicByName(name);
        if (dynamics == null) {
            return "[ATUSER(" + qq + ")]机器人尚未监听该用户，请联系管理员监听";
        }
        DynamicDetail d;
        switch (index) {
            case 1:
                d = getDynamicDetail(dynamics.getFirst());
                break;
            case 2:
                d = getDynamicDetail(dynamics.getSecond());
                break;
            case 3:
                d = getDynamicDetail(dynamics.getThird());
                break;
            case 4:
                d = getDynamicDetail(dynamics.getFourth());
                break;
            case 5:
                d = getDynamicDetail(dynamics.getFifth());
                break;
            default:
                d = getDynamicDetail(dynamics.getTop());
        }
        String result = name + "的" + d.getType() + "动态\n" +
                d.getTitle() + "\n" +
                d.getText();
        sendMsgUtil.CallOPQApiSendImg(groupId, "[ATUSER(" + qq + ")]" + result, SendMsgUtil.picUrl, d.getPicUrl(), 2);
        return "";
    }

    @Override
    public String getBiliList(Long groupId) {
        List<BiliCount> bilis = biliMapper.getBiliCountListByGroupId(groupId);
        if (bilis.size() > 0) {
            StringBuilder s = new StringBuilder("");
            for (BiliCount bili : bilis) {
                s.append("\n用户：").append(bili.getName()).append("\tUid:").append(bili.getUid());
            }
            return s.substring(1);
        }else {
            return "本群暂时还没有关注up哦~";
        }
    }

    @Override
    public String setGroupBiliRel(Long qq, Long groupId, String biliId) {
        List<AdminUserInfo> admins = adminUserMapper.selectAllAdmin();
        String qqMd5 = DigestUtils.md5DigestAsHex(qq.toString().getBytes());
        boolean b = AdminUtil.getupLoadAdmin(qqMd5, admins);
        if (b) {
            Integer integer = groupAdminInfoMapper.existGroupId(groupId);
            if (integer == 0) {
                groupAdminInfoMapper.insertGroupId(groupId);
            }
            Long uid = Long.parseLong(biliId);
            Integer integer1 = biliMapper.existBiliUid(uid);
            if (integer1 == 0) {
                biliMapper.insertBiliUid(uid);
            }

            Integer relation = biliMapper.selectGroupBiliRel(groupId, uid);
            if (relation == 0) {
                biliMapper.insertGroupBiliRel(groupId, uid);
                return "关注成功";
            } else {
                return "本群已经关注了这个uid";
            }
        }else {
            return "您无权进行关注，请联系开发者";
        }
    }

    @Override
    public String removeGroupBiliRel(Long qq, Long groupId, String biliId) {
        List<AdminUserInfo> admins = adminUserMapper.selectAllAdmin();
        String qqMd5 = DigestUtils.md5DigestAsHex(qq.toString().getBytes());
        boolean b = AdminUtil.getupLoadAdmin(qqMd5, admins);
        if (b) {
            Long uid = Long.parseLong(biliId);
            biliMapper.deleteGroupBiliRel(groupId, uid);
            return "取消关注成功";
        }else {
            return "您无权进行关注更改";
        }
    }
}
