local log = require("log")
local Api = require("coreApi")
local json = require("json")
local http = require("http")
-- 服务api接口地址
local url = "http://192.168.15.105:8086"

-- 这个文件直接复制到已部署好的机器人OPQBot/Plugin文件夹中，无需重启自动加载
-- 字符串切分方法，copy百度的
function Split(szFullString, szSeparator)
    local nFindStartIndex = 1
    local nSplitIndex = 1
    local nSplitArray = {}
    while true do
       local nFindLastIndex = string.find(szFullString, szSeparator, nFindStartIndex)
       if not nFindLastIndex then
        nSplitArray[nSplitIndex] = string.sub(szFullString, nFindStartIndex, string.len(szFullString))
        break
       end
       nSplitArray[nSplitIndex] = string.sub(szFullString, nFindStartIndex, nFindLastIndex - 1)
       nFindStartIndex = nFindLastIndex + string.len(szSeparator)
       nSplitIndex = nSplitIndex + 1
    end
    return nSplitArray
end

function ReceiveFriendMsg(CurrentQQ, data)
    return 1
end

-- 调用API接口返回数据
function ReceiveGroupMsg(CurrentQQ, data)
    local list = Split(data.Content," ")
    local body =
    {
	    pool = list[2],
	    qq = data.FromUserId,
	    name = data.FromNickName
    }
    if (list[1] == "菜单") then
        luaRes =
        Api.Api_SendMsg(
            CurrentQQ,
            {
                toUser = data.FromGroupId,
                sendToType = 2,
                sendMsgType = "TextMsg",
                groupid = 0,
                content = "抽卡/十连/卡池",
                atUser = 0
            }
        )
    elseif (list[1] == "抽卡") then
        response, error_message =
            http.post(
                ""..url.."/Arknights/chouKa",
                {
                    body = json.encode(body),
                    headers =
                    {
                        ["Accept"] = "*/*",
                        ["Content-Type"] = "application/json"
                    }
                }
            )
            local html = response.body
            log.notice("From Lua 抽卡 %s", html)
            luaRes =
                Api.Api_SendMsg(
                    CurrentQQ,
                    {
                        toUser = data.FromGroupId,
                        sendToType = 2,
                        sendMsgType = "TextMsg",
                        groupid = 0,
                        content = html,
                        atUser = 0
                    }
                )
    elseif (list[1] == "十连") then
        response, error_message =
            http.post(
                ""..url.."/Arknights/shiLian",
                {
                    body = json.encode(body),
                    headers =
                    {
                        ["Accept"] = "*/*",
                        ["Content-Type"] = "application/json"
                    }
                }
            )
            local html = response.body
            log.notice("From Lua 十连 %s", html)
            luaRes =
                Api.Api_SendMsg(
                CurrentQQ,
                {
                    toUser = data.FromGroupId,
                    sendToType = 2,
                    sendMsgType = "TextMsg",
                    groupid = 0,
                    content = html,
                    atUser = 0
                }
            )
    elseif (list[1] == "卡池") then
        response, error_message =
        http.request(
            "GET",
            ""..url.."/Arknights/getPoolName",
            {
                query = "type=text",
                headers =
                {
                    ["Accept"] = "*/*",
                    ["Content-Type"] = "application/json",
                    ["User-Agent"] = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36"
                }
            }
        )
        local html = response.body
        log.notice("From Lua 卡池 %s", html)
        luaRes =
        Api.Api_SendMsg(
            CurrentQQ,
            {
                toUser = data.FromGroupId,
                sendToType = 2,
                sendMsgType = "TextMsg",
                groupid = 0,
                content = html,
                atUser = 0
            }
        )
	end
    return 1
end

function ReceiveEvents(CurrentQQ, data, extData)
    return 1
end

