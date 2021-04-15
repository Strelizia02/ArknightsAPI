package com.strelizia.arknights.service.impl;

import com.strelizia.arknights.dao.*;
import com.strelizia.arknights.model.*;
import com.strelizia.arknights.service.UpdateDataService;
import com.strelizia.arknights.util.ImageUtil;
import com.strelizia.arknights.util.SendMsgUtil;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangzy
 * @Date 2020/12/19 15:46
 **/
@Service
@Slf4j
public class UpdateDataServiceImpl implements UpdateDataService {

    //地图ID信息
//    private final String mapIdurl = "https://andata.somedata.top/data-2020/map/exData/";

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

    @Override
    /**
     * checkUpdate是否检查版本更新
     *      —— 是，检查版本更新，版本不一致才进行更新
     *      —— 否，不进行版本检查，强制更新
     */
    public void updateAllData(boolean checkUpdate) {
        //获取kokodayo的Json数据Key
        String koKoDaYoKeyUrl = "https://api.kokodayo.fun/api/base/info";

        String jsonStr = getJsonStringFromUrl(koKoDaYoKeyUrl);
        JSONObject keyJsonObj = new JSONObject(jsonStr);
        String dataVersion = updateMapper.getVersion();
        String charKey = keyJsonObj.getJSONObject("result").getJSONObject("agent").getJSONObject("char").getString("key");
        String enemyKey = keyJsonObj.getJSONObject("result").getJSONObject("level").getJSONObject("enemy").getString("key");
        //版本不同才进行更新
        if (charKey.equals(dataVersion) && checkUpdate) {
            return;
        }
        updateMapper.updateVersion(charKey);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        updateMapper.clearOperatorData();
        List<Long> groups = userFoundMapper.selectAllGroups();

        for (Long groupId : groups) {
            String s = "游戏数据闪断更新中，更新期间存在无响应情况，请耐心等待更新完成。\n" +
                    "若十分钟后仍未收到更新完成信息，请联系开发者重新进行更新请求\n--" +
                    sdf.format(new Date());
            sendMsgUtil.CallOPQApiSendMsg(groupId, s, 2);
        }

        updateAllOperator(charKey);
        updateAllEnemy(enemyKey);
        updateMapAndItem();

        for (Long groupId : groups) {
            String s = "游戏数据更新完成\n--" + sdf.format(new Date());
            sendMsgUtil.CallOPQApiSendMsg(groupId, s, 2);
        }
    }

    public void updateAllOperator(String JsonId){
        //发送请求，封装所有的干员基础信息列表
        //干员列表
        String operatorListUrl = "https://andata.somedata.top/data-2020/char/list/";

        String allOperator = getJsonStringFromUrl(operatorListUrl + JsonId + ".json");

        JSONArray json = new JSONArray(allOperator);
        int length = json.length();
        for (int i = 0; i < length; i++){
            JSONObject operator = json.getJSONObject(i);
            updateOperatorTag(operator);
            String operatorId = operator.getString("No");
            //发送请求遍历干员详细信息
            //干员ID信息
            String operatorIdUrl = "https://andata.somedata.top/data-2020/char/data/";

            String operatorJson = getJsonStringFromUrl(operatorIdUrl + operatorId + ".json");
            Integer operatorNum = updateOperatorByJson(operatorJson);
            //近卫兔兔单独处理
            if (operatorId.equals("char_1001_amiya2")){
                operatorId = "char_002_amiya";
            }
            if (!operator.getString("class").equals("TOKEN")) {
                //只获取干员信息
                updateOperatorInfoById(operatorId, operatorNum);
            }
        }
        log.info("更新完成，共更新了{}个干员信息",length);
    }

    private void updateOperatorInfoById(String operatorId, Integer operatorNum) {
        //干员基础信息
        String infoUrl = "https://andata.somedata.top/data-2020/char/info/";

        String infoJson = getJsonStringFromUrl(infoUrl + operatorId + ".json");
        if (infoJson != null) {
            JSONObject infoJsonObj = new JSONObject(infoJson);

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
            }
            updateMapper.updateOperatorInfo(operatorBasicInfo);
        }
    }

    private void updateOperatorTag(JSONObject operator) {
        if (operator.getBoolean("gkzm")) {
            String name = operator.getString("name");
            JSONArray tags = operator.getJSONArray("tags");
            int rarity = tags.getInt(0) + 1;
            StringBuilder position = new StringBuilder(tags.getString(1).equals("MELEE") ? "近战位" : "远程位");
            for (int i = 2; i < tags.length(); i++){
                position.append(",").append(tags.getString(i));
            }
            if (rarity==5){
                position.append("," + "资深干员");
            }else if (rarity==6){
                position.append("," + "高级资深干员");
            }
            String profession = operator.getString("class");

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
    }

    public void updateAllEnemy(String enemyKey){
        log.info("开始更新敌人信息");
        //发送请求，封装所有的敌人面板信息列表
        //敌人列表
        String enemyListUrl = "https://andata.somedata.top/data-2020/lists/enemy/";

        String allEnemy = getJsonStringFromUrl(enemyListUrl + enemyKey + ".json");
        JSONObject enemyobj = new JSONObject(allEnemy);
        Set<String> enemyJson = enemyobj.keySet();
        int length = enemyJson.size();
        for(String enemyId:enemyJson) {
            //敌人ID信息
            String enemyIdUrl = "https://andata.somedata.top/data-2020/enemy/";

            String enemyStr = getJsonStringFromUrl(enemyIdUrl + enemyId + ".json");
            JSONArray enemyJsonObj = new JSONArray(enemyStr);
            String name = enemyobj.getJSONObject(enemyId).getString("name");
            for (int j = 0; j < enemyJsonObj.length(); j++) {
                //一个敌人可能有多个阶段，比如我老婆霜星
                JSONObject enemyData = enemyJsonObj.getJSONObject(j).getJSONObject("enemyData");
                JSONObject attributes = enemyData.getJSONObject("attributes");
                Integer atk = attributes.getJSONObject("atk").getInt("m_value");
                Integer baseAttackTime = attributes.getJSONObject("baseAttackTime").getInt("m_value");
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
            }
        }
        log.info("敌人信息更新完成，共更新了{}个敌人信息",length);
    }

    /**
     * 更新地图、材料基础信息
     */
    public void updateMapAndItem(){
        log.info("从企鹅物流中拉取地图、材料数据");
        //地图列表
        String mapListUrl = "https://penguin-stats.cn/PenguinStats/api/v2/stages?server=CN";

        MapJson[] maps = restTemplate
                .getForObject(mapListUrl, MapJson[].class);
        for (MapJson map : maps) {
            updateMapper.updateStageData(map);
        }

        //章节列表
        String zoneListUrl = "https://penguin-stats.cn/PenguinStats/api/v2/zones";

        ZoneJson[] zones = restTemplate.getForObject(zoneListUrl,ZoneJson[].class);
        for (ZoneJson zone : zones) {
            updateMapper.updateZoneData(zone);
        }

        updateItemAndFormula();

        //地图掉落关联表
        String matrixListUrl = "https://penguin-stats.cn/PenguinStats/api/v2/_private/result/matrix/CN/global";

        String matrixJsonStr = restTemplate.getForObject(matrixListUrl,String.class);
        JSONArray matrixJsons = new JSONObject(matrixJsonStr).getJSONArray("matrix");
        int length = matrixJsons.length();
        for (int i = 0; i < length; i++){
            JSONObject matrix = matrixJsons.getJSONObject(i);
            try {
                String stageId = matrix.getString("stageId");
                Integer itemId = Integer.parseInt(matrix.getString("itemId"));
                Integer quantity = matrix.getInt("quantity");
                Integer times = matrix.getInt("times");
                updateMapper.updateMatrixData(stageId, itemId, quantity, times);
            }catch (NumberFormatException e){
                //忽略家具材料
            }
        }
    }

    public void updateItemAndFormula(){
        //材料列表
        String itemListUrl = "https://cdn.jsdelivr.net/gh/Kengxxiao/ArknightsGameData@master/zh_CN/gamedata/excel/item_table.json";
        List<Integer> ids = materialMadeMapper.selectAllMaterId();

        JSONObject items = new JSONObject(getJsonStringFromUrl(itemListUrl)).getJSONObject("items");
        Pattern pattern = Pattern.compile("[0-9]*");
        Iterator<String> keys = items.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            //只更新数字id（字母id是一些抽奖券干员信物之类的）
            if (pattern.matcher(key).matches()) {
                JSONObject itemObj = items.getJSONObject(key);
                Integer id = Integer.parseInt(itemObj.getString("itemId"));
                //增量更新
                if (!ids.contains(id)) {
                    String name = itemObj.getString("name");
                    String icon = itemObj.getString("iconId");
                    updateMapper.updateItemData(id, name, icon);
                    //更新合成信息
                    updateItemFormula(id);
                }
            }
        }
        //企鹅物流数据缺失双芯片数据，单独插入
//        Integer[] DoubleId = {3213,3223,3233,3243,3253,3263,3273,3283};
//        String[] DoubleName = {"先锋双芯片", "近卫双芯片", "重装双芯片", "狙击双芯片", "术师双芯片", "医疗双芯片", "辅助双芯片", "特种双芯片"};
//        for(int i = 0; i < 8; i++){
//            if (!ids.contains(DoubleId[i])) {
//                updateMapper.updateItemData(DoubleId[i], DoubleName[i]);
//                updateItemFormula(DoubleId[i]);
//            }
//        }
        updateItemIcon();
    }

    public void updateItemFormula(Integer itemId){
        //根据材料id，更新材料合成公式

        String itemUrl = "https://andata.somedata.top/data-2020/item/" + itemId + ".json";
        JSONArray buildingProductList = new JSONObject(getJsonStringFromUrl(itemUrl)).getJSONArray("buildingProductList");
        if (buildingProductList != null && buildingProductList.length() > 0) {
            String roomType = buildingProductList.getJSONObject(0).getString("roomType");
            String formulaId = buildingProductList.getJSONObject(0).getString("formulaId");

            String materialMadeUrl = "https://cdn.jsdelivr.net/gh/Kengxxiao/ArknightsGameData@master/zh_CN/gamedata/excel/building_data.json";

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
     * 更新皮肤信息
     */
    public void updateSkin(){
        log.info("拉取皮肤数据");
        String skinListUrl = "https://andata.somedata.top/data-2020/char/extraSkins.json";
        String jsonStringFromUrl = getJsonStringFromUrl(skinListUrl);
        JSONArray skinJson = new JSONArray(jsonStringFromUrl);
        //皮肤只需要增量更新
        Integer maxId = skinInfoMapper.selectMaxId();
        int max = maxId==null?0: maxId;
        if (max < skinJson.length()){
            for (int i = max; i < skinJson.length(); i++){
                JSONObject skinObj = skinJson.getJSONObject(i);
                SkinInfo skinInfo = new SkinInfo();
                skinInfo.setSkinName(skinObj.getJSONObject("displaySkin").getString("skinName"));
                skinInfo.setDialog(skinObj.getJSONObject("displaySkin").getString("dialog"));
                skinInfo.setDrawerName(skinObj.getJSONObject("displaySkin").getString("drawerName"));
                skinInfo.setOperatorId(operatorInfoMapper.getOperatorIdByChar(skinObj.getString("charId")));
                skinInfo.setSkinGroupName(skinObj.getJSONObject("displaySkin").getString("skinGroupName"));

                String avatarId = skinObj.getString("avatarId");
                String[] split = avatarId.split("#");
                String skinImgUrl = "https://andata.somedata.top/dataX/char/halfPic/";
                skinInfo.setSkinBase64(imageUtil.getImageBase64ByUrl(skinImgUrl + split[0] + "%23" + split[1] + ".png"));

                skinInfoMapper.insertBySkinInfo(skinInfo);
            }
            log.info("原有时装{}个，当前时装{}个",max,skinJson.length());
        }
        //查找仍然是url的结果(上次更新url转base64失败的)
        List<Integer> ids = skinInfoMapper.selectBase64IsUrl();
        if (ids != null && ids.size() > 0){
            int i = 0;
            for (Integer id: ids) {
                skinInfoMapper.updateBaseStrById(id,
                        imageUtil.getImageBase64ByUrl(skinInfoMapper.selectSkinById(id)));
                i++;
            }
            if (i != 0)
                log.info("修复上次未获取的图片{}个",i);
        }
        updateItemIcon();
    }


    public void updateItemIcon(){
        log.info("开始拉取最新材料图标");
        List<Integer> maters = materialMadeMapper.selectAllMaterId();
        for (Integer id: maters){
            String picBase64 = materialMadeMapper.selectMaterialPicById(id);
            if (picBase64 == null || picBase64.startsWith("https://")){
                String iconId = new JSONObject(getJsonStringFromUrl("https://andata.somedata.top/data-2020/item/" + id + ".json")).getString("iconId");
                materialMadeMapper.updateBase64ById(imageUtil.getImageBase64ByUrl("https://andata.somedata.top/dataX/item/pic/" + iconId + ".png"), id);
            }
        }
    }

    //发送url的get请求获取结果json字符串
    public String getJsonStringFromUrl(String url){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("User-Agent","PostmanRuntime/7.26.8");
        httpHeaders.set("Authorization","2");
        httpHeaders.set("Host","andata.somedata.top");
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        String s = null;
        try {
            s = restTemplate
                    .exchange(url, HttpMethod.GET, httpEntity, String.class).getBody();
        }catch (Exception ignored){

        }

        return s;
    }

    @Override
    public Integer updateOperatorByJson(String json) {
        Map<String, Integer> operatorClass = new HashMap<>(8);
        operatorClass.put("PIONEER", 1);
        operatorClass.put("WARRIOR", 2);
        operatorClass.put("TANK", 3);
        operatorClass.put("SNIPER", 4);
        operatorClass.put("CASTER", 5);
        operatorClass.put("SUPPORT", 6);
        operatorClass.put("MEDIC", 7);
        operatorClass.put("SPECIAL", 8);

        JSONObject jsonObj = new JSONObject(json);
        String name = jsonObj.getString("name");
        //近卫兔兔改个名
        if (jsonObj.getJSONArray("phases").getJSONObject(0).getString("characterPrefabKey").equals("char_1001_amiya2")){
            name = "近卫阿米娅";
        }
        int rarity = jsonObj.getInt("rarity") + 1;
        jsonObj.getString("position");
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
            operatorData.setBaseAttackTime(panelMax.getInt("baseAttackTime"));
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
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.set("User-Agent", "PostmanRuntime/7.26.8");
                    httpHeaders.set("Authorization", "2");
                    httpHeaders.set("Host", "andata.somedata.top");
                    HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
                    //发送请求，封装结果数据
                    //技能ID信息
                    String skillIdUrl = "https://andata.somedata.top/data-2020/skills/";

                    JSONObject skillObj = new JSONObject(restTemplate
                            .exchange(skillIdUrl + skills.getJSONObject(i).getString("skillId") + ".json", HttpMethod.GET, httpEntity, String.class).getBody());

                    String skillName = skillObj
                            .getJSONArray("levels")
                            .getJSONObject(0)
                            .getString("name");

                    operatorSkillInfo.setSkillName(skillName);
                    updateMapper.insertOperatorSkill(operatorSkillInfo);
                    Integer skillId = updateMapper.selectSkillIdByName(skillName);

                    JSONArray levels = skillObj.getJSONArray("levels");
                    for (int level = 0; level < levels.length(); level++) {
                        JSONObject skillDescJson = levels.getJSONObject(level);
                        SkillDesc skillDesc = new SkillDesc();
                        skillDesc.setSkillId(skillId);
                        skillDesc.setSkillLevel(level + 1);
                        skillDesc.setSkillType(skillDescJson.getInt("skillType"));

                        //获取key-value列表
                        Map<String,Double> parameters = new HashMap<>();
                        JSONArray mapList = skillDescJson.getJSONArray("blackboard");
                        for (int keyId = 0; keyId < mapList.length(); keyId++){
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

                        while (m.find()){
                            String key = m.group(2).toLowerCase();
                            String title = m.group(3);
                            String percent = m.group(4);

                            Double val = parameters.get(key);
                            String value;

                            if (val != null) {
                                if (!percent.equals("")) {
                                    val = val * 100;
                                }
                                if (title.equals("0.0")) {
                                    value = new DecimalFormat("#.0").format(val) + percent;
                                } else {
                                    value = new DecimalFormat("#").format(val) + percent;
                                }

                            }else {
                                try {
                                    value = "" + skillDescJson.getInt(key);
                                }catch (Exception e){
                                    value = key;
                                }

                            }
                            m.appendReplacement(stringBuffer,value);
                        }

                        skillDesc.setDescription(m.appendTail(stringBuffer).toString().replace("--","-"));

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
            if (jsonObj.get("buildingData") instanceof JSONArray) {
                JSONArray buildingData = jsonObj.getJSONArray("buildingData");
                for (int i = 0; i< buildingData.length(); i++){
                    JSONArray build1 = buildingData.getJSONArray(i);
                    for (int j = 0; j < build1.length(); j++){
                        BuildingSkill buildingSkill = new BuildingSkill();
                        JSONObject buildObj = build1.getJSONObject(j);
                        buildingSkill.setOperatorId(operatorId);
                        buildingSkill.setPhase(buildObj.getJSONObject("cond").getInt("phase"));
                        buildingSkill.setLevel(buildObj.getJSONObject("cond").getInt("level"));
                        buildingSkill.setBuffName(buildObj.getJSONObject("data").getString("buffName"));
                        buildingSkill.setRoomType(buildObj.getJSONObject("data").getString("roomType"));
                        //正则表达式去除标签
                        Pattern pattern = Pattern.compile("<(.*?)>");
                        Matcher matcher = pattern.matcher(buildObj.getJSONObject("data").getString("description"));
                        buildingSkill.setDescription(matcher.replaceAll(""));
                        buildingSkillMapper.insertBuildingSkill(buildingSkill);
                    }
                }
            }
        }

        return operatorId;
    }

}
