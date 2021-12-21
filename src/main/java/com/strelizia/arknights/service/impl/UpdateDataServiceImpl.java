package com.strelizia.arknights.service.impl;

import com.strelizia.arknights.dao.*;
import com.strelizia.arknights.model.*;
import com.strelizia.arknights.service.UpdateDataService;
import com.strelizia.arknights.util.FormatStringUtil;
import com.strelizia.arknights.util.ImageUtil;
import com.strelizia.arknights.util.SendMsgUtil;
import com.strelizia.arknights.util.XPathUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import io.github.mzdluo123.silk4j.*;

/**
 * @author wangzy
 * @Date 2020/12/19 15:46
 **/
@Service
@Slf4j
public class UpdateDataServiceImpl implements UpdateDataService {

    //地图ID信息
//    private final String mapIdurl = "https://andata.somedata.top/data-2020/map/exData/";

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private UserFoundMapper userFoundMapper;

    @Autowired
    private UpdateMapper updateMapper;

    @Autowired
    private SendMsgUtil sendMsgUtil;

    @Autowired
    protected RestTemplate restTemplate;

    @Autowired
    private BuildingSkillMapper buildingSkillMapper;

    @Autowired
    private SkinInfoMapper skinInfoMapper;

    @Autowired
    private OperatorInfoMapper operatorInfoMapper;

    @Autowired
    private ImageUtil imageUtil;

    @Autowired
    private MaterialMadeMapper materialMadeMapper;

    @Autowired
    private EnemyMapper enemyMapper;

    @Autowired
    private EquipMapper equipMapper;

    @Value("${userConfig.loginQq}")
    private Long loginQq;

//    private String url = "https://cdn.jsdelivr.net/gh/Kengxxiao/ArknightsGameData@master/zh_CN/gamedata/";
//    private String url = "https://raw.githubusercontent.com/Kengxxiao/ArknightsGameData/master/zh_CN/gamedata/";
    private String url = "http://vivien8261.gitee.io/arknights-bot-resource/gamedata/";

    @Override
    /**
     * checkUpdate是否检查版本更新
     *      —— 是，检查版本更新，版本不一致才进行更新
     *      —— 否，不进行版本检查，强制更新
     */
    public void updateAllData(boolean checkUpdate) {
        log.info("开始更新全部数据");
        //获取kokodayo的Json数据Key
        String koKoDaYoKeyUrl = "https://gitee.com/vivien8261/Arknights-Bot-Resource/raw/main/gamedata/excel/data_version.txt";

        String charKey = getJsonStringFromUrl(koKoDaYoKeyUrl);
//        JSONObject keyJsonObj = new JSONObject(jsonStr);
        String dataVersion = updateMapper.getVersion();
//        String charKey = keyJsonObj.getJSONObject("result").getJSONObject("agent").getJSONObject("char").getString("key");
        //版本不同才进行更新
        if (charKey.equals(dataVersion) && checkUpdate) {
            log.info("当前为最新版本无需更新");
            return;
        }
        log.info("新数据版本为：{}",charKey);
        updateMapper.updateVersion(charKey);

//        List<Long> groups = userFoundMapper.selectAllGroups();
//
//        for (Long groupId : groups) {
//            String s = "游戏数据闪断更新中，更新期间存在无响应情况，请耐心等待更新完成。\n" +
//                    "若十分钟后仍未收到更新完成信息，请联系开发者重新进行更新请求\n--" +
//                    sdf.format(new Date());
//            sendMsgUtil.CallOPQApiSendMsg(groupId, s, 2);
//        }
        sendMsgUtil.CallOPQApiSendMyself("游戏数据闪断更新中，更新期间存在无响应情况，请耐心等待更新完成。\n" +
                "若十分钟后仍未收到更新完成信息，请联系开发者重新进行更新请求\n--"
                + sdf.format(new Date()));

        updateAllOperator();
        updateAllEnemy();
        updateMapAndItem();

        sendMsgUtil.CallOPQApiSendMyself("游戏数据更新完成\n--" + sdf.format(new Date()));

//        for (Long groupId : groups) {
//            String s = "游戏数据更新完成\n--" + sdf.format(new Date());
//            sendMsgUtil.CallOPQApiSendMsg(groupId, s, 2);
//        }

    }

    /**
     * 全量更新干员相关信息
     */
    public void updateAllOperator() {
        //清理干员数据(因部分召唤物无char_id，不方便进行增量更新)
        log.info("清理干员数据");
        updateMapper.clearOperatorData();
        //获取全部干员基础数据
        String operatorListUrl = url + "excel/character_table.json";
        JSONObject operatorObj = new JSONObject(getJsonStringFromUrl(operatorListUrl));;
        //获取游戏公招描述数据
        log.info("更新公招描述数据");
        String gachaTableUrl = url + "excel/gacha_table.json";
        String recruit = new JSONObject(getJsonStringFromUrl(gachaTableUrl)).getString("recruitDetail");
        Pattern pattern = Pattern.compile("<(.*?)>");
        Matcher matcher = pattern.matcher(recruit);
        String replaceAll = matcher.replaceAll("").replace(" ","");
        String[] split = replaceAll.split("\n");
        //解析出全部的公招干员
        List<String> gachaCharList = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            if (split[i].startsWith("★")) {
                String[] chars = split[i].replace("★","").replace("\\n","").split("/");
                gachaCharList.addAll(Arrays.asList(chars));
            }
        }
        //获取全部干员技能数据
        log.info("更新全部干员技能数据");
        String skillIdUrl = url + "excel/skill_table.json";
        JSONObject skillObj = new JSONObject(getJsonStringFromUrl(skillIdUrl));
        //获取全部基建技能数据
        log.info("更新全部基建技能数据");
        String buildingUrl = url + "excel/building_data.json";
        JSONObject buildingObj = new JSONObject(getJsonStringFromUrl(buildingUrl));
        //获取全部干员档案数据
        log.info("更新全部干员档案数据");
        String infoTable = url + "excel/handbook_info_table.json";
        JSONObject infoTableObj = new JSONObject(getJsonStringFromUrl(infoTable)).getJSONObject("handbookDict");
        log.info("更新全部干员基础数据");
        Iterator<String> keys = operatorObj.keys();
        while (keys.hasNext()){
            String key = keys.next();
            JSONObject operator = operatorObj.getJSONObject(key);

            String name = operator.getString("name");
            // 判断干员名是否存在公招描述中
            if (gachaCharList.contains(name)) {
                updateOperatorTag(operator);
            }

            Integer operatorNum = updateOperatorByJson(key, operator, skillObj, buildingObj);

            if (infoTableObj.has(key)) {
                JSONObject jsonObject = infoTableObj.getJSONObject(key);
                updateOperatorInfoById(key, operatorNum, jsonObject);
            }
        }
        //更新模组信息
        updateOperatorEquipByJson();

        //近卫兔兔单独处理
        String amiyaSword = url + "excel/char_patch_table.json";
        JSONObject amiya2Json = new JSONObject(getJsonStringFromUrl(amiyaSword)).getJSONObject("patchChars").getJSONObject("char_1001_amiya2");
        Integer operatorNum = updateOperatorByJson("char_1001_amiya2", amiya2Json, skillObj, buildingObj);
        JSONObject amiyaInfo = infoTableObj.getJSONObject("char_002_amiya");
        updateOperatorInfoById("char_1001_amiya2", operatorNum, amiyaInfo);

        sendMsgUtil.CallOPQApiSendMyself("干员数据更新完成\n--"
                + sdf.format(new Date()));
        log.info("更新完成");
    }

    /**
     * 插入一条干员基础信息（档案、声优、画师）
     *
     * @param operatorId  干员char_id
     * @param operatorNum 数据库中的干员Id
     */
    private void updateOperatorInfoById(String operatorId, Integer operatorNum, JSONObject infoJsonObj) {
        OperatorBasicInfo operatorBasicInfo = new OperatorBasicInfo();
            operatorBasicInfo.setOperatorId(operatorNum);
            operatorBasicInfo.setCharId(operatorId);
            operatorBasicInfo.setDrawName(infoJsonObj.getString("drawName"));
            operatorBasicInfo.setInfoName(infoJsonObj.getString("infoName"));

            JSONArray storyTextAudio = infoJsonObj.getJSONArray("storyTextAudio");
            for (int i = 0; i < storyTextAudio.length(); i++) {
                JSONObject story = storyTextAudio.getJSONObject(i);
                String storyText = story.getJSONArray("stories").getJSONObject(0).getString("storyText");
                String storyTitle = story.getString("storyTitle");
                switch (storyTitle) {
                    case "基础档案":
                        String[] split = storyText.split("\n");
                        operatorBasicInfo.setInfection(split[split.length - 1]);
                        for (String s : split) {
                            if (s.length() < 1) {
                                break;
                            }
                            String[] basicText = s.substring(1).split("】");
                            switch (basicText[0]) {
                                case "代号":
                                    operatorBasicInfo.setCodeName(basicText[1]);
                                    break;
                                case "性别":
                                    operatorBasicInfo.setSex(basicText[1]);
                                    break;
                                case "出身地":
                                    operatorBasicInfo.setComeFrom(basicText[1]);
                                    break;
                                case "生日":
                                    operatorBasicInfo.setBirthday(basicText[1]);
                                    break;
                                case "种族":
                                    operatorBasicInfo.setRace(basicText[1]);
                                    break;
                                case "身高":
                                    String str = basicText[1];
                                    StringBuilder str2 = new StringBuilder();
                                    if (str != null && !"".equals(str)) {
                                        for (int j = 0; j < str.length(); j++) {
                                            if (str.charAt(j) >= 48 && str.charAt(j) <= 57) {
                                                str2.append(str.charAt(j));
                                            }
                                        }
                                    }
                                    operatorBasicInfo.setHeight(Integer.parseInt(str2.toString()));
                                    break;
                            }
                        }
                        break;
                    case "综合体检测试":
                        operatorBasicInfo.setComprehensiveTest(storyText);
                        break;
                    case "客观履历":
                        operatorBasicInfo.setObjectiveResume(storyText);
                        break;
                    case "临床诊断分析":
                        operatorBasicInfo.setClinicalDiagnosis(storyText);
                        break;
                    case "档案资料一":
                        operatorBasicInfo.setArchives1(storyText);
                        break;
                    case "档案资料二":
                        operatorBasicInfo.setArchives2(storyText);
                        break;
                    case "档案资料三":
                        operatorBasicInfo.setArchives3(storyText);
                        break;
                    case "档案资料四":
                        operatorBasicInfo.setArchives4(storyText);
                        break;
                    case "晋升记录":
                    case "晋升资料":
                        operatorBasicInfo.setPromotionInfo(storyText);
                        break;
                }
            updateMapper.updateOperatorInfo(operatorBasicInfo);
        }
    }

    /**
     * 获取干员的标签tag
     * @param operator 干员Json数据
     */
    private void updateOperatorTag(JSONObject operator) {
            String name = operator.getString("name");
            JSONArray tags = operator.getJSONArray("tagList");
            int rarity = operator.getInt("rarity") + 1;
            StringBuilder position = new StringBuilder(operator.getString("position").equals("MELEE") ? "近战位" : "远程位");

            for (int i = 0; i < tags.length(); i++) {
                position.append(",").append(tags.getString(i));
            }

            if (rarity == 5) {
                position.append(",资深干员");
            } else if (rarity == 6) {
                position.append(",高级资深干员");
            } else if (rarity == 1)
            {
                position.append(",支援机械");
            }

            String profession = operator.getString("profession");

            Map<String, String> operatorClass = new HashMap<>(8);
            operatorClass.put("PIONEER", "先锋干员");
            operatorClass.put("WARRIOR", "近卫干员");
            operatorClass.put("TANK", "重装干员");
            operatorClass.put("SNIPER", "狙击干员");
            operatorClass.put("CASTER", "术师干员");
            operatorClass.put("SUPPORT", "辅助干员");
            operatorClass.put("MEDIC", "医疗干员");
            operatorClass.put("SPECIAL", "特种干员");

            position.append(",").append(operatorClass.get(profession));

            updateMapper.updateTags(name, rarity, position.toString());

    }

    /**
     * 增量更新敌人面板信息
     */
    public void updateAllEnemy() {
        log.info("开始更新敌人信息");
        //获取全部敌人数据
        String enemyListUrl = url + "levels/enemydata/enemy_database.json";
        String allEnemy = getJsonStringFromUrl(enemyListUrl);
        JSONArray enemyObj = new JSONObject(allEnemy).getJSONArray("enemies");

        int length = 0;
        List<String> allEnemyId = enemyMapper.selectAllEnemyId();
        for (int i = 0; i < enemyObj.length(); i++) {
            String enemyId = enemyObj.getJSONObject(i).getString("Key");
            if (!allEnemyId.contains(enemyId)) {
                JSONObject oneEnemy = enemyObj.getJSONObject(i);
                JSONArray enemyJsonObj = oneEnemy.getJSONArray("Value");
                String name = enemyJsonObj.getJSONObject(0).getJSONObject("enemyData").getJSONObject("name").getString("m_value");
                for (int j = 0; j < enemyJsonObj.length(); j++) {
                    //一个敌人可能有多个阶段，比如我老婆霜星
                    JSONObject enemyData = enemyJsonObj.getJSONObject(j).getJSONObject("enemyData");
                    JSONObject attributes = enemyData.getJSONObject("attributes");
                    Integer atk = attributes.getJSONObject("atk").getInt("m_value");
                    Double baseAttackTime = attributes.getJSONObject("baseAttackTime").getDouble("m_value");
                    Integer def = attributes.getJSONObject("def").getInt("m_value");
                    Integer hpRecoveryPerSec = attributes.getJSONObject("hpRecoveryPerSec").getInt("m_value");
                    Integer magicResistance = attributes.getJSONObject("magicResistance").getInt("m_value");
                    Integer massLevel = attributes.getJSONObject("massLevel").getInt("m_value");
                    Integer maxHp = attributes.getJSONObject("maxHp").getInt("m_value");
                    Double moveSpeed = attributes.getJSONObject("moveSpeed").getDouble("m_value");
                    Double rangeRadius = enemyData.getJSONObject("rangeRadius").getDouble("m_value");
                    Integer silenceImmune = attributes.getJSONObject("silenceImmune").getBoolean("m_value") ? 0 : 1;
                    Integer sleepImmune = attributes.getJSONObject("sleepImmune").getBoolean("m_value") ? 0 : 1;
                    Integer stunImmune = attributes.getJSONObject("stunImmune").getBoolean("m_value") ? 0 : 1;

                    EnemyInfo enemyInfo = new EnemyInfo(enemyId, name, atk, baseAttackTime,
                            def, hpRecoveryPerSec, magicResistance, massLevel, maxHp,
                            moveSpeed, rangeRadius, silenceImmune, sleepImmune, stunImmune, j);

                    updateMapper.updateEnemy(enemyInfo);
                    length++;
                }
            }
        }


        log.info("敌人信息更新完成，共更新了{}个敌人信息", length);
    }

    /**
     * 更新地图、材料基础信息
     */
    public void updateMapAndItem() {

        log.info("从企鹅物流中拉取地图、材料数据");
        //地图列表
        String mapListUrl = "https://penguin-stats.cn/PenguinStats/api/v2/stages?server=CN";

        MapJson[] maps = restTemplate
                .getForObject(mapListUrl, MapJson[].class);
        int newMap = 0;
        for (MapJson map : maps) {
            List<String> mapIds = materialMadeMapper.selectAllMapId();
            if (!mapIds.contains(map.getStageId())) {
                updateMapper.updateStageData(map);
                newMap++;
            }
        }


        log.info("新增地图{}个", newMap);

        //章节列表
        String zoneListUrl = "https://penguin-stats.cn/PenguinStats/api/v2/zones";

        int newZone = 0;
        ZoneJson[] zones = restTemplate.getForObject(zoneListUrl, ZoneJson[].class);
        for (ZoneJson zone : zones) {
            List<String> zoneIds = materialMadeMapper.selectAllZoneId();
            if (!zoneIds.contains(zone.getZoneId())) {
                updateMapper.updateZoneData(zone);
                newZone++;
            }
        }


        log.info("新增章节{}个", newZone);

        updateItemAndFormula();

        //地图掉落关联表
        String matrixListUrl = "https://penguin-stats.cn/PenguinStats/api/v2/_private/result/matrix/CN/global";

        //全量更新所有掉落信息
        updateMapper.clearMatrixData();
        String matrixJsonStr = restTemplate.getForObject(matrixListUrl, String.class);
        JSONArray matrixJsons = new JSONObject(matrixJsonStr).getJSONArray("matrix");
        int length = matrixJsons.length();
        for (int i = 0; i < length; i++) {
            JSONObject matrix = matrixJsons.getJSONObject(i);
            try {
                String stageId = matrix.getString("stageId");
                Integer itemId = Integer.parseInt(matrix.getString("itemId"));
                Integer quantity = matrix.getInt("quantity");
                Integer times = matrix.getInt("times");
                updateMapper.updateMatrixData(stageId, itemId, quantity, times);
            } catch (NumberFormatException e) {
                //忽略家具材料
            }
        }
        sendMsgUtil.CallOPQApiSendMyself("企鹅物流数据更新完成\n--"
                + sdf.format(new Date()));
    }

    /**
     * 增量更新材料以及合成公式
     */
    public void updateItemAndFormula() {
        //材料列表
        String itemListUrl = url + "excel/item_table.json";
        List<String> ids = materialMadeMapper.selectAllMaterId();
        String jsonStringFromUrl = getJsonStringFromUrl(itemListUrl);
        if (jsonStringFromUrl != null) {
            JSONObject items = new JSONObject(jsonStringFromUrl).getJSONObject("items");
            Iterator<String> keys = items.keys();
            int newItem = 0;
            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject itemObj = items.getJSONObject(key);
                String id = itemObj.getString("itemId");
                //增量更新
                if (!ids.contains(id)) {
                    String name = itemObj.getString("name");
                    String icon = itemObj.getString("iconId");
                    updateMapper.updateItemData(id, name, icon);
                    //更新合成信息
                    updateItemFormula(id);
                    newItem++;
                }
            }
            sendMsgUtil.CallOPQApiSendMyself("材料合成数据更新完成\n--"
                    + sdf.format(new Date()));
            log.info("新增材料{}个", newItem);
            //企鹅物流数据缺失双芯片数据，单独插入
//        Integer[] DoubleId = {3213,3223,3233,3243,3253,3263,3273,3283};
//        String[] DoubleName = {"先锋双芯片", "近卫双芯片", "重装双芯片", "狙击双芯片", "术师双芯片", "医疗双芯片", "辅助双芯片", "特种双芯片"};
//        for(int i = 0; i < 8; i++){
//            if (!ids.contains(DoubleId[i])) {
//                updateMapper.updateItemData(DoubleId[i], DoubleName[i]);
//                updateItemFormula(DoubleId[i]);
//            }
//        }
        }
    }

    /**
     * 根据材料Id获取合成公式
     *
     * @param itemId 材料Id
     */
    public void updateItemFormula(String itemId) {
        //根据材料id，更新材料合成公式

        String itemUrl = url + "excel/item_table.json";
        JSONArray buildingProductList = new JSONObject(getJsonStringFromUrl(itemUrl)).getJSONObject("items").getJSONObject(itemId).getJSONArray("buildingProductList");
        if (buildingProductList != null && buildingProductList.length() > 0) {
            String roomType = buildingProductList.getJSONObject(0).getString("roomType");
            String formulaId = buildingProductList.getJSONObject(0).getString("formulaId");

            String materialMadeUrl = url + "excel/building_data.json";

            JSONArray formulaObj;
            if (roomType.equals("WORKSHOP")) {
                formulaObj = new JSONObject(getJsonStringFromUrl(materialMadeUrl)).getJSONObject("workshopFormulas").getJSONObject(formulaId).getJSONArray("costs");
                for (int i = 0; i < formulaObj.length(); i++) {
                    updateMapper.insertMaterialMade(itemId
                            , Integer.parseInt(formulaObj.getJSONObject(i).getString("id"))
                            , formulaObj.getJSONObject(i).getInt("count"));
                }
            } else if (roomType.equals("MANUFACTURE")) {
                formulaObj = new JSONObject(getJsonStringFromUrl(materialMadeUrl)).getJSONObject("manufactFormulas").getJSONObject(formulaId).getJSONArray("costs");
                for (int i = 0; i < formulaObj.length(); i++) {
                    updateMapper.insertMaterialMade(itemId
                            , Integer.parseInt(formulaObj.getJSONObject(i).getString("id"))
                            , formulaObj.getJSONObject(i).getInt("count"));
                }
            }
        }
    }

    /**
     * 增量更新皮肤信息
     */
    public void updateSkin() {
        log.info("拉取图标数据");
        String skinListUrl = url + "excel/skin_table.json";
        String jsonStringFromUrl = getJsonStringFromUrl(skinListUrl);
        JSONObject skinJson = new JSONObject(jsonStringFromUrl).getJSONObject("charSkins");
        //皮肤只需要增量更新
        List<String> skinNames = skinInfoMapper.selectAllNames();

        Iterator<String> keys = skinJson.keys();
        while (keys.hasNext()) {
            JSONObject skinObj = skinJson.getJSONObject(keys.next());
            if(skinObj.getJSONObject("displaySkin").get("skinName") instanceof String) {
                String name = skinObj.getJSONObject("displaySkin").getString("skinName");
                if (!skinNames.contains(name)) {
                    log.info("新增时装：" + name);
                    SkinInfo skinInfo = new SkinInfo();
                    skinInfo.setSkinName(name);
                    skinInfo.setDialog(skinObj.getJSONObject("displaySkin").getString("dialog"));
                    skinInfo.setDrawerName(skinObj.getJSONObject("displaySkin").getString("drawerName"));
                    skinInfo.setOperatorId(skinObj.getString("charId"));
                    skinInfo.setSkinGroupName(skinObj.getJSONObject("displaySkin").getString("skinGroupName"));
                    String avatarId = skinObj.getString("avatarId");
                    String[] split = avatarId.split("#");
                    String skinImgUrl = "http://vivien8261.gitee.io/arknights-bot-resource";
                    skinInfo.setSkinBase64(ImageUtil.getImageBase64ByUrl(skinImgUrl + split[0] + "_" + split[1] + ".png"));
                    skinInfoMapper.insertBySkinInfo(skinInfo);
                }
            }
        }
        log.info("原有时装{}个，当前时装{}个", skinNames.size(), skinJson.length());
        //查找仍然是url的结果(上次更新url转base64失败的)
        List<Integer> ids = skinInfoMapper.selectBase64IsUrl();
        if (ids != null && ids.size() > 0) {
            int i = 0;
            for (Integer id : ids) {
                skinInfoMapper.updateBaseStrById(id,
                        ImageUtil.getImageBase64ByUrl(skinInfoMapper.selectSkinById(id)));
                i++;
            }
            if (i != 0)
                log.info("修复上次未获取的图片{}个", i);
        }
        updateItemIcon();
        updateOperatorPng();
        sendMsgUtil.CallOPQApiSendMyself("时装数据更新完成\n--"
                + sdf.format(new Date()));
    }

    /**
     * 更新材料图标，以材料表为基础update，只更新非base64的字段
     */
    public void updateItemIcon() {
        log.info("开始拉取最新材料图标");
        List<String> maters = materialMadeMapper.selectAllMaterId();
        for (String id : maters) {
            String picBase64 = materialMadeMapper.selectMaterialPicById(id);
            if (picBase64 == null || picBase64.startsWith("https://")) {
                String iconId = materialMadeMapper.selectAllMaterIconId(id);
                materialMadeMapper.updateBase64ById(ImageUtil.getImageBase64ByUrl("http://vivien8261.gitee.io/arknights-bot-resource/item/" + iconId + ".png"), id);
            }
        }
        sendMsgUtil.CallOPQApiSendMyself("材料图标更新完成\n--"
                + sdf.format(new Date()));
    }

    /**
     * 更新干员半身照，增量更新
     */
    public void updateOperatorPng() {
        log.info("开始更新干员半身照");
        List<String> allOperatorId = operatorInfoMapper.getAllOperatorId();
        for (String id : allOperatorId) {
            String base = operatorInfoMapper.selectOperatorPngById(id);
            if (base == null || base.startsWith("http")) {
                log.info(id + "半身照正在更新");
                operatorInfoMapper.insertOperatorPngById(id, ImageUtil.getImageBase64ByUrl("http://vivien8261.gitee.io/arknights-bot-resource/portrait/" + id + "_1.png"));
            }
        }
        sendMsgUtil.CallOPQApiSendMyself("干员半身照更新完成\n--"
                + sdf.format(new Date()));
    }

    /**
     * 更新干员语音，增量更新
     */
    public void updateOperatorVoice() {
        log.info("开始更新干员语音");
        List<OperatorName> allOperatorId = operatorInfoMapper.getAllOperatorIdAndName();
        for (OperatorName name : allOperatorId) {
            String html = XPathUtil.getHtmlByUrl("http://prts.wiki/w/" + name.getOperatorName() + "/语音记录");
            Document document = Jsoup.parse(html);
            Elements as = document.select("a[download]");
            for (Element a: as){
                String url = a.attr("href");
                String[] split = url.split("/");
                String fileName = XPathUtil.decodeUnicode(split[split.length - 1].substring(0,split[split.length - 1].length() - 4));
                String path = "voice/" + name.getCharId() + "/" + fileName + ".wav";
                try {
                    XPathUtil.downloadNet(url, path);
//                    SilkCoder.encode();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("干员语音完成");
        sendMsgUtil.CallOPQApiSendMyself("干员语音完成\n--"
                + sdf.format(new Date()));
    }

    /**
     * 发送url的get请求获取结果json字符串
     *
     * @param url url
     * @return 返回结果String
     */
    public String getJsonStringFromUrl(String url) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("User-Agent", "PostmanRuntime/7.26.8");
        httpHeaders.set("Authorization", "2");
        httpHeaders.set("Host", "andata.somedata.top");
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        String s = null;
        try {
            s = restTemplate
                    .exchange(url, HttpMethod.GET, httpEntity, String.class).getBody();
        } catch (Exception ignored) {

        }
        return s;
    }

    /**
     * 更新单个干员详细信息。包括技能天赋
     *
     * @param jsonObj 单个干员详细json
     * @return 返回更新数量
     */
    public Integer updateOperatorByJson(String charId, JSONObject jsonObj, JSONObject skillObj, JSONObject buildingObj) {
        Map<String, Integer> operatorClass = new HashMap<>(8);
        operatorClass.put("PIONEER", 1);
        operatorClass.put("WARRIOR", 2);
        operatorClass.put("TANK", 3);
        operatorClass.put("SNIPER", 4);
        operatorClass.put("CASTER", 5);
        operatorClass.put("SUPPORT", 6);
        operatorClass.put("MEDIC", 7);
        operatorClass.put("SPECIAL", 8);

        String name = jsonObj.getString("name");
        //近卫兔兔改个名
        if (jsonObj.getJSONArray("phases").getJSONObject(0).getString("characterPrefabKey").equals("char_1001_amiya2")) {
            name = "近卫阿米娅";
        }
        int rarity = jsonObj.getInt("rarity") + 1;
        boolean isNotObtainable = jsonObj.getBoolean("isNotObtainable");

        //封装干员信息
        OperatorInfo operatorInfo = new OperatorInfo();
        operatorInfo.setOperator_name(name);
        operatorInfo.setOperator_rarity(rarity);
        if (!isNotObtainable) {
            operatorInfo.setAvailable(1);
        } else {
            operatorInfo.setAvailable(0);
        }
        operatorInfo.setIn_limit(0);
        operatorInfo.setOperator_class(operatorClass.get(jsonObj.getString("profession")));

        updateMapper.insertOperator(operatorInfo);
        Integer operatorId = updateMapper.selectOperatorIdByName(name);

        JSONArray phases = jsonObj.getJSONArray("phases");
        if (operatorId != null) {
            int length = phases.length();
            //封装干员面板信息（满级无潜能无信赖）
            JSONArray operatorPanel = phases.getJSONObject(length - 1).getJSONArray("attributesKeyFrames");
            JSONObject panelMax = operatorPanel.getJSONObject(operatorPanel.length() - 1).getJSONObject("data");
            OperatorData operatorData = new OperatorData();
            operatorData.setId(operatorId);
            operatorData.setAtk(panelMax.getInt("atk"));
            operatorData.setDef(panelMax.getInt("def"));
            operatorData.setMagicResistance(panelMax.getInt("magicResistance"));
            operatorData.setMaxHp(panelMax.getInt("maxHp"));
            operatorData.setBlockCnt(panelMax.getInt("blockCnt"));
            operatorData.setCost(panelMax.getInt("cost"));
            operatorData.setBaseAttackTime(panelMax.getDouble("baseAttackTime"));
            operatorData.setRespawnTime(panelMax.getInt("respawnTime"));
            updateMapper.updateOperatorData(operatorData);

            //封装干员精英化花费
            for (int i = 1; i < length; i++) {
                JSONObject array = phases.getJSONObject(i);
                if (array.get("evolveCost") instanceof JSONArray) {
                    JSONArray evolveJson = array.getJSONArray("evolveCost");
                    for (int j = 0; j < evolveJson.length(); j++) {
                        JSONObject evolve = evolveJson.getJSONObject(j);
                        //精英i花费
                        OperatorEvolveInfo operatorEvolveInfo = new OperatorEvolveInfo();
                        operatorEvolveInfo.setOperatorId(operatorId);
                        operatorEvolveInfo.setEvolveLevel(i);
                        operatorEvolveInfo.setUseMaterialId(evolve.getInt("id"));
                        operatorEvolveInfo.setUseNumber(evolve.getInt("count"));
                        updateMapper.insertOperatorEvolve(operatorEvolveInfo);
                    }
                }
            }

            //封装干员天赋
            if (jsonObj.get("talents") instanceof JSONArray) {
                JSONArray talents = jsonObj.getJSONArray("talents");
                for (int i = 0; i < talents.length(); i++) {
                    JSONArray candidates = talents.getJSONObject(i).getJSONArray("candidates");
                    for (int j = 0; j < candidates.length(); j++) {
                        TalentInfo talentInfo = new TalentInfo();
                        JSONObject candidate = candidates.getJSONObject(j);
                        talentInfo.setTalentName(candidate.getString("name"));
                        Pattern pattern = Pattern.compile("<(.*?)>");
                        Matcher matcher = pattern.matcher(candidate.getString("description"));
                        talentInfo.setDescription(matcher.replaceAll(""));
                        talentInfo.setLevel(candidate.getJSONObject("unlockCondition").getInt("level"));
                        talentInfo.setPhase(candidate.getJSONObject("unlockCondition").getInt("phase"));
                        talentInfo.setPotential(candidate.getInt("requiredPotentialRank"));
                        talentInfo.setOperatorId(operatorId);
                        updateMapper.insertOperatorTalent(talentInfo);
                    }
                }
            }

            //封装干员技能
            JSONArray skills = jsonObj.getJSONArray("skills");
            for (int i = 0; i < skills.length(); i++) {
                OperatorSkillInfo operatorSkillInfo = new OperatorSkillInfo();
                operatorSkillInfo.setOperatorId(operatorId);
                operatorSkillInfo.setSkillIndex(i + 1);
                if (skills.getJSONObject(i).get("skillId") instanceof String) {
                    String skillName = skillObj
                            .getJSONObject(skills.getJSONObject(i).getString("skillId"))
                            .getJSONArray("levels").getJSONObject(0).getString("name");

                    operatorSkillInfo.setSkillName(skillName);
                    updateMapper.insertOperatorSkill(operatorSkillInfo);
                    Integer skillId = updateMapper.selectSkillIdByName(skillName);

                    JSONArray levels = skillObj.getJSONObject(skills.getJSONObject(i).getString("skillId")).getJSONArray("levels");
                    for (int level = 0; level < levels.length(); level++) {
                        JSONObject skillDescJson = levels.getJSONObject(level);
                        SkillDesc skillDesc = new SkillDesc();
                        skillDesc.setSkillId(skillId);
                        skillDesc.setSkillLevel(level + 1);
                        skillDesc.setSkillType(skillDescJson.getInt("skillType"));

                        //获取key-value列表
                        Map<String, Double> parameters = new HashMap<>();
                        JSONArray mapList = skillDescJson.getJSONArray("blackboard");
                        for (int keyId = 0; keyId < mapList.length(); keyId++) {
                            parameters.put(mapList.getJSONObject(keyId).getString("key").toLowerCase(),
                                    mapList.getJSONObject(keyId).getDouble("value"));
                        }

                        Pattern pattern = Pattern.compile("<(.*?)>");
                        Matcher matcher = pattern.matcher(skillDescJson.getString("description"));

                        //使用正则表达式替换参数
                        Pattern p = Pattern.compile("(\\{-?([a-zA-Z/.\\]\\[0-9_@]+):?([0-9.]*)(%?)\\})");
                        //代码可以运行不要乱改.jpg
                        //这个正则已经不断进化成我看不懂的形式了
                        Matcher m = p.matcher(matcher.replaceAll(""));
                        StringBuffer stringBuffer = new StringBuffer();

                        while (m.find()) {
                            String key = m.group(2).toLowerCase();
                            String percent = m.group(4);

                            Double val = parameters.get(key);
                            String value;

                            if (val != null) {
                                if (!percent.equals("")) {
                                    val = val * 100;
                                }
                                value = FormatStringUtil.FormatDouble2String(val) + percent;
                            } else {
                                try {
                                    value = "" + skillDescJson.getInt(key);
                                } catch (Exception e) {
                                    value = key;
                                }

                            }
                            m.appendReplacement(stringBuffer, value);
                        }

                        skillDesc.setDescription(m.appendTail(stringBuffer).toString().replace("--", "-"));

                        skillDesc.setSpType(skillDescJson.getJSONObject("spData").getInt("spType"));
                        skillDesc.setMaxCharge(skillDescJson.getJSONObject("spData").getInt("maxChargeTime"));
                        skillDesc.setSpCost(skillDescJson.getJSONObject("spData").getInt("spCost"));
                        skillDesc.setSpInit(skillDescJson.getJSONObject("spData").getInt("initSp"));
                        skillDesc.setDuration(skillDescJson.getInt("duration"));

                        updateMapper.updateSkillDecs(skillDesc);

                    }

                    //获取技能等级列表(专一专二专三)
                    JSONArray levelUpCostCond = skills.getJSONObject(i).getJSONArray("levelUpCostCond");
                    //该技能专j+1的花费
                    for (int j = 0; j < levelUpCostCond.length(); j++) {
                        JSONObject skillCostObj = levelUpCostCond.getJSONObject(j);
                        if (skillCostObj.get("levelUpCost") instanceof JSONArray) {
                            JSONArray levelUpCost = skillCostObj.getJSONArray("levelUpCost");
                            for (int k = 0; k < levelUpCost.length(); k++) {
                                SkillMaterInfo skillMaterInfo = new SkillMaterInfo();
                                skillMaterInfo.setSkillId(skillId);
                                skillMaterInfo.setMaterLevel(j + 1);
                                skillMaterInfo.setUseMaterialId(levelUpCost.getJSONObject(k).getInt("id"));
                                skillMaterInfo.setUseNumber(levelUpCost.getJSONObject(k).getInt("count"));
                                updateMapper.insertSkillMater(skillMaterInfo);
                            }
                        }
                    }
                }
            }

            //封装干员基建技能
            if (buildingObj.getJSONObject("chars").has(charId)) {
                JSONObject chars = buildingObj.getJSONObject("chars").getJSONObject(charId);
                JSONObject buffs = buildingObj.getJSONObject("buffs");
                if (chars.get("buffChar") instanceof JSONArray) {
                    JSONArray buildingData = chars.getJSONArray("buffChar");
                    for (int i = 0; i < buildingData.length(); i++) {
                        if (buildingData.getJSONObject(i).get("buffData") instanceof JSONArray) {
                            JSONArray build1 = buildingData.getJSONObject(i).getJSONArray("buffData");
                            for (int j = 0; j < build1.length(); j++) {
                                BuildingSkill buildingSkill = new BuildingSkill();
                                JSONObject buildObj = build1.getJSONObject(j);
                                String buffId = buildObj.getString("buffId");
                                buildingSkill.setOperatorId(operatorId);
                                buildingSkill.setPhase(buildObj.getJSONObject("cond").getInt("phase"));
                                buildingSkill.setLevel(buildObj.getJSONObject("cond").getInt("level"));
                                buildingSkill.setBuffName(buffs.getJSONObject(buffId).getString("buffName"));
                                buildingSkill.setRoomType(buffs.getJSONObject(buffId).getString("roomType"));
                                //正则表达式去除标签
                                Pattern pattern = Pattern.compile("<(.*?)>");
                                Matcher matcher = pattern.matcher(buffs.getJSONObject(buffId).getString("description"));
                                buildingSkill.setDescription(matcher.replaceAll(""));
                                buildingSkillMapper.insertBuildingSkill(buildingSkill);
                            }
                        }
                    }
                }
            }
        }

        return operatorId;
    }

    public void updateOperatorEquipByJson(){
        log.info("开始更新模组数据");
        String equipUrl = url + "excel/battle_equip_table.json";
        String equipUnlockUrl = url + "excel/uniequip_table.json";
        JSONObject equip = new JSONObject(getJsonStringFromUrl(equipUrl));
        JSONObject equipUnlock = new JSONObject(getJsonStringFromUrl(equipUnlockUrl));

        Iterator<String> keys = equip.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            EquipInfo equipInfo = new EquipInfo();

            JSONObject equipDict = equipUnlock.getJSONObject("equipDict").getJSONObject(key);
            equipInfo.setEquipId(equipDict.getString("uniEquipId"));
            equipInfo.setEquipName(equipDict.getString("uniEquipName"));
            equipInfo.setCharId(equipDict.getString("charId"));

            JSONArray phases = equip.getJSONObject(key).getJSONArray("phases");
            JSONArray candidates = phases.getJSONObject(0).getJSONArray("parts").getJSONObject(0).
                    getJSONObject("overrideTraitDataBundle").getJSONArray("candidates");

            //天赋变化
            StringBuilder additionalDescription = new StringBuilder("");
            StringBuilder overrideDescripton = new StringBuilder("");
            for(int i = 0; i < candidates.length(); i++) {
                JSONObject candidate = candidates.getJSONObject(i);
                //获取key-value列表
                Map<String, Double> parameters = new HashMap<>();
                JSONArray mapList = candidate.getJSONArray("blackboard");
                for (int keyId = 0; keyId < mapList.length(); keyId++) {
                    parameters.put(mapList.getJSONObject(keyId).getString("key").toLowerCase(),
                            mapList.getJSONObject(keyId).getDouble("value"));
                }
                if (candidate.get("additionalDescription") instanceof String) {
                    String additional = candidate.getString("additionalDescription");
                    additionalDescription.append(getValueByKeysFormatString(additional, parameters));
                }
                if (candidate.get("overrideDescripton") instanceof String) {
                    String override = candidate.getString("overrideDescripton");
                    overrideDescripton.append(getValueByKeysFormatString(override, parameters));
                }
            }
            String addStr = additionalDescription.toString();
            String overStr = overrideDescripton.toString();

            if (addStr.equals("")) {
                addStr = "\n\t新增天赋：无";
            }else {
                addStr = "\n\t新增天赋：" + addStr;
            }
            if (overStr.equals(""))
            {
                overStr = "\n\t天赋变化：无";
            }else {
                overStr = "\n\t天赋变化：" + overStr;
            }
            String talentDesc = addStr + overStr;
            equipInfo.setDesc(talentDesc);
            equipInfo.setLevel(candidates.getJSONObject(0).getJSONObject("unlockCondition").getInt("level"));
            equipInfo.setPhase(candidates.getJSONObject(0).getJSONObject("unlockCondition").getInt("phase"));
            equipMapper.insertEquipInfo(equipInfo);

            for (int i = 0; i < phases.length(); i++)
            {
                JSONArray buffs = phases.getJSONObject(i).getJSONArray("attributeBlackboard");
                for(int j = 0; j < buffs.length(); j++){
                    String buffKey = buffs.getJSONObject(j).getString("key");
                    Double value = buffs.getJSONObject(j).getDouble("value");
                    equipMapper.insertEquipBuff(key, buffKey, value);
                }
            }

            JSONArray itemCost = equipDict.getJSONArray("itemCost");
            for (int i = 0; i < itemCost.length(); i++){
                String materialId = itemCost.getJSONObject(i).getString("id");
                Integer useNumber = itemCost.getJSONObject(i).getInt("count");
                equipMapper.insertEquipcost(key, materialId, useNumber);
            }

            JSONArray missionList = equipDict.getJSONArray("missionList");
            for (int i = 0; i < missionList.length(); i++) {
                String missionId = missionList.getString(i);
                String desc = equipUnlock.getJSONObject("missionList").getJSONObject(missionId).getString("desc");
                equipMapper.insertEquipMission(key, missionId, desc);
            }
        }
    }

    public String getValueByKeysFormatString(String s, Map<String, Double> parameters){
        Pattern pattern = Pattern.compile("<(.*?)>");
        Matcher matcher = pattern.matcher(s);
        Pattern p = Pattern.compile("(\\{-?([a-zA-Z/.\\]\\[0-9_@]+):?([0-9.]*)(%?)\\})");
        Matcher m = p.matcher(matcher.replaceAll(""));
        StringBuffer stringBuffer = new StringBuffer();

        while (m.find()) {
            String buffKey = m.group(2).toLowerCase();
            String percent = m.group(4);

            Double val = parameters.get(buffKey);
            String value;
            if (!percent.equals("")) {
                val = val * 100;
            }
            value = FormatStringUtil.FormatDouble2String(val) + percent;
            m.appendReplacement(stringBuffer, value);
        }
        return m.appendTail(stringBuffer).toString().replace("--", "-");
    }
}
