local log = require("log")
local Api = require("coreApi")
local json = require("json")
local http = require("http")
-- 服务api接口地址
local url = "http://192.168.15.105:8086"

function ReceiveFriendMsg(CurrentQQ, data)
    return 1
end

-- 调用API接口返回数据
function ReceiveGroupMsg(CurrentQQ, data)
    local body =
    {
	    text = data.Content,
	    qq = data.FromUserId,
	    name = data.FromNickName
    }
    response, error_message =
        http.post(
            ""..url.."/Arknights/Group",
            {
                body = json.encode(body),
                headers =
                {
                    ["Accept"] = "*/*",
                    ["Content-Type"] = "application/json"
                }
            }
        )
    return 1
end

function ReceiveEvents(CurrentQQ, data, extData)
    return 1
end

