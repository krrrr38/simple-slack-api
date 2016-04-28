package com.ullink.slack.simpleslackapi.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ullink.slack.simpleslackapi.replies.ParsedSlackReply;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.replies.SlackChannelReply;

class SlackJSONReplyParser
{
    static ParsedSlackReply decode(JsonObject obj, SlackSession session)
    {
        Boolean ok = obj.get("ok").getAsBoolean();
        String error = null;
        if (obj.get("error") != null)
        {
            error = obj.get("error").getAsString();
        }
        if (obj.get("presence") != null)
        {
            return new SlackUserPresenceReplyImpl(ok, error,"active".equals(obj.get("presence").getAsString()));
        }

        if (isMpim(obj) || isIm(obj) || isChannel(obj) || isGroup(obj)) {
            return buildSlackChannelReply(ok,error,obj,session);
        }

        if(isMessageReply(obj)) {
            String timestamp = obj.get("ts") != null ? obj.get("ts").getAsString() : null ;
            return new SlackMessageReplyImpl(ok, error, obj.get("reply_to") != null ? obj.get("reply_to").getAsLong() : -1, timestamp);
        }

        if (ok == null) {
            //smelly reply
            ok = Boolean.FALSE;
        }
        return new SlackReplyImpl(ok,error);
    }

    private static SlackChannelReply buildSlackChannelReply(Boolean ok, String error, JsonObject obj, SlackSession session)
    {
        if (obj.get("id") != null) {
            return new SlackChannelReplyImpl(ok,error, session.findChannelById(obj.get("id").getAsString()));
        }

        JsonElement channelObj = obj.get("channel");
        if (channelObj == null) {
            channelObj = obj.get("group");
        }

        String id = channelObj.getAsJsonObject().get("id").getAsString();
        return new SlackChannelReplyImpl(ok,error, session.findChannelById(id));
    }

    private static boolean isMessageReply(JsonObject obj)
    {
        return obj.get("ts") != null;
    }
    
    private static boolean isMpim(JsonObject obj)
    {
        JsonElement isMpim = obj.get("is_mpim");
        return isMpim != null && isMpim.getAsBoolean();
    }

    private static boolean isIm(JsonObject obj)
    {
        JsonElement isIm = obj.get("is_im");
        return isIm != null && isIm.getAsBoolean();
    }

    private static boolean isChannel(JsonObject obj)
    {
        JsonElement channel = obj.get("channel");
        return channel != null && channel.isJsonObject();
    }

    private static boolean isGroup(JsonObject obj)
    {
        if (obj.get("is_group") != null) {
            return obj.get("is_group").getAsBoolean();
        }

        JsonElement group = obj.get("group");
        return group != null && group.isJsonObject();
    }

}
