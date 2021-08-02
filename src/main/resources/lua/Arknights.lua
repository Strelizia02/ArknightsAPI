local log = require("log")
local Api = require("coreApi")
local json = require("json")
local http = require("http")
-- 服务api接口地址
local url = "http://localhost:8086"

function ReceiveFriendMsg(CurrentQQ, data)
    local body =
    {
        text = data.Content,
        qq = data.FromUin
    }
    response, error_message =
    http.post("" .. url .. "/private/chat",
        {
            body = json.encode(body),
            headers =
            {
                ["Accept"] = "*/*",
                ["Content-Type"] = "application/json"
            }
        })
    return 1
end

-- 调用API接口返回数据
function ReceiveGroupMsg(CurrentQQ, data)
    local body =
    {
        text = data.Content,
        qq = data.FromUserId,
        name = data.FromNickName,
        groupId = data.FromGroupId
    }
    response, error_message =
    http.post("" .. url .. "/Arknights/receive",
        {
            body = json.encode(body),
            headers =
            {
                ["Accept"] = "*/*",
                ["Content-Type"] = "application/json"
            }
        })
    return 1
end

function ReceiveEvents(CurrentQQ, data, extData)
    local body =
    {
        msgType = data.MsgType,
        qq = data.ToUin,
        groupId = data.FromUin,
        content = data.Content,
        eventData = extData
    }
    log.info("%s", data.Content)
    response, error_message =
    http.post("" .. url .. "/events/group",
        {
            body = json.encode(body),
            headers =
            {
                ["Accept"] = "*/*",
                ["Content-Type"] = "application/json"
            }
        })
    return 1
end

