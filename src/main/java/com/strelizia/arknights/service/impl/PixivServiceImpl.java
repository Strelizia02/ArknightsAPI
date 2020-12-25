package com.strelizia.arknights.service.impl;

//import com.scienjus.client.PixivParserClient;
//import com.scienjus.model.RankWork;
//import com.scienjus.model.Work;
//import com.strelizia.arknights.dao.SeTuMapper;
import com.strelizia.arknights.service.PixivService;
//import com.strelizia.arknights.util.SendMsgUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.util.DigestUtils;
//
//import java.util.List;
//import java.util.Random;
//
/**
 * @author wangzy
 * @Date 2020/12/22 14:14
 **/
//@Service
public class PixivServiceImpl implements PixivService {
//
//    @Value("${pixiv.username}")
//    private String pixivUsername;
//
//    @Value("${pixiv.password}")
//    private String pixivPassword;
//
//    @Value("${pixiv.count}")
//    private Integer count;
//
//    @Autowired
//    private SeTuMapper seTuMapper;
//
//    @Autowired
//    private SendMsgUtil sendMsgUtil;
//
//    public String getSeTuUrlByName(Long qq, Long groupId, String name, String s){
//        String qqMd5 = DigestUtils.md5DigestAsHex(qq.toString().getBytes());
//        Integer pixiv = seTuMapper.selectTodaySeTuByQQ(qqMd5);
//        //根据关键字返回随机涩图url
//        String url = "";
//        String result = "";
//        if (pixiv < count) {
//            //创建一个客户端实例
//            PixivParserClient client = new PixivParserClient();
////            //设置用户名和对应的密码
////            client.setUsername(pixivUsername);
////            client.setPassword(pixivPassword);
////            //登录
////            if (client.login()) {
////                //进行获取图片任务..
//            if (s == null){
//                s = "kancolle";
//            }
//                List<Work> works = client.search(s);
//                int r = new Random().nextInt(works.size());
//                url = works.get(r).getImageUrls().getLarge();
////            }
//            //关闭客户端
//            client.close();
//            seTuMapper.updateTodaySeTu(qqMd5,name,groupId);
//            result = "";
//            sendMsgUtil.CallOPQApiSendImg(groupId,null,SendMsgUtil.picUrl, url,2);
//        }else {
//            result = name + "别冲了，一天就"+ count +"张涩图";
//        }
//        return result;
//    }
//
//    public String getSeTuUrlByRank(Long qq, Long groupId, String name, int rank) {
//        String qqMd5 = DigestUtils.md5DigestAsHex(qq.toString().getBytes());
//        Integer pixiv = seTuMapper.selectTodaySeTuByQQ(qqMd5);
//        //根据关键字返回随机涩图url
//        String url = "";
//        String result = "";
//        if (pixiv < count) {
//            //获取排行榜某张图，最多取前100
//            rank = rank % 100;
//            PixivParserClient client = new PixivParserClient();
//            client.setUsername(pixivUsername);
//            client.setPassword(pixivPassword);
//            if (client.login()) {
//                List<RankWork> works = client.ranking().getWorks();
//                url = works.get(rank).getWork().getImageUrls().getLarge();
//            }
//            client.close();
//            result = "";
//            seTuMapper.updateTodaySeTu(qqMd5,name,groupId);
//            sendMsgUtil.CallOPQApiSendImg(groupId,null,SendMsgUtil.picUrl,url,2);
//        } else {
//            result = name + "别冲了，一天就" + count + "张涩图";
//        }
//        return result;
//    }
}
