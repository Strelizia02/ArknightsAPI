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
    if data.MsgType == "ON_EVENT_GROUP_JOIN" then
        myData = {
            InviteUin = extData.InviteUin,
            UserID = extData.UserID,
            UserName = extData.UserName
        }
    end
    if data.MsgType == "ON_EVENT_GROUP_REVOKE" then
        myData = {
            AdminUserID = extData.AdminUserID,
            GroupID = extData.GroupID,
            MsgRandom = extData.MsgRandom,
            MsgSeq = extData.MsgSeq,
            UserID = extData.UserID
        }
    end

    log.notice("%s", data.Content)
    log.notice("%s", json.encode(myData))

    local body =
    {
        msgType = data.MsgType,
        qq = data.ToUin,
        groupId = data.FromUin,
        content = data.Content,
		eventData = json.encode(myData)
    }
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

