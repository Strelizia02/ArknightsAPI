local log = require("log")
local Api = require("coreApi")
local json = require("json")
local http = require("http")
-- 服务api接口地址
local url = "http://localhost:8086"

function ReceiveFriendMsg(CurrentQQ, data)
    return 1
end

-- 调用API接口返回数据
function ReceiveGroupMsg(CurrentQQ, data)
    if(string.find(data.Content, "{\"Content\":\"##") == 1) then
    keyWord = "公招截图 "..data.Content
        local body =
        {
            text = keyWord,
            qq = data.FromUserId,
            name = data.FromNickName,
            groupId = data.FromGroupId
        }
        response, error_message =
        http.post(
            ""..url.."/Arknights/receive",
            {
                body = json.encode(body),
                headers =
                {
                    ["Accept"] = "*/*",
                    ["Content-Type"] = "application/json"
                }
            }
        )
        elseif (string.find(data.Content, "##") == 1) then
        keyWord = data.Content:gsub("##", "")
        local body =
        {
        text = keyWord,
        qq = data.FromUserId,
        name = data.FromNickName,
        groupId = data.FromGroupId
        }
        response, error_message =
        http.post(
        ""..url.."/Arknights/receive",
        {
        body = json.encode(body),
        headers =
        {
        ["Accept"] = "*/*",
        ["Content-Type"] = "application/json"
        }
        }
        )
    end
    return 1
end

function ReceiveEvents(CurrentQQ, data, extData)
    return 1
end

