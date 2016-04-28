package com.ullink.slack.simpleslackapi.impl;

import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ullink.slack.simpleslackapi.SlackTeam;
import com.ullink.slack.simpleslackapi.SlackUser;

class SlackJSONParsingUtils
{

    private SlackJSONParsingUtils()
    {
        // Helper class
    }

    static final SlackUser buildSlackUser(JsonObject jsonUser)
    {
        String id = GsonHelper.getStringOrNull(jsonUser.get("id"));
        String name = GsonHelper.getStringOrNull(jsonUser.get("name"));
        String realName = GsonHelper.getStringOrNull(jsonUser.get("real_name"));
        String tz = GsonHelper.getStringOrNull(jsonUser.get("tz"));
        String tzLabel = GsonHelper.getStringOrNull(jsonUser.get("tz_label"));
        JsonElement element = jsonUser.get("tz_offset");
        Integer tzOffset = null;
        if (element != null)
        {
            tzOffset = element.getAsInt();
        }
        Boolean deleted = GsonHelper.ifNullFalse(jsonUser, "deleted");
        Boolean admin = GsonHelper.ifNullFalse(jsonUser, "is_admin");
        Boolean owner = GsonHelper.ifNullFalse(jsonUser, "is_owner");
        Boolean primaryOwner = GsonHelper.ifNullFalse(jsonUser, "is_primary_owner");
        Boolean restricted = GsonHelper.ifNullFalse(jsonUser, "is_restricted");
        Boolean ultraRestricted = GsonHelper.ifNullFalse(jsonUser, "is_ultra_restricted");
        Boolean bot = GsonHelper.ifNullFalse(jsonUser, "is_bot");
        JsonElement profileJSON = jsonUser.get("profile");
        String email = "";
        if (profileJSON !=null && !profileJSON.isJsonNull())
        {
            email = GsonHelper.getStringOrNull(profileJSON.getAsJsonObject().get("email"));
        }
        return new SlackUserImpl(id, name, realName, email, deleted, admin, owner, primaryOwner, restricted, ultraRestricted, bot, tz, tzLabel, tzOffset == null ? null : new Integer(tzOffset.intValue()));
    }

    static final SlackChannelImpl buildSlackChannel(JsonObject jsonChannel, Map<String, SlackUser> knownUsersById)
    {
        String id =  GsonHelper.getStringOrNull(jsonChannel.get("id"));
        String name = GsonHelper.getStringOrNull(jsonChannel.get("name"));
        String topic = null; // TODO
        String purpose = null; // TODO
        SlackChannelImpl toReturn = new SlackChannelImpl(id, name, topic, purpose, false);
        if (jsonChannel.get("members") != null)
        {
            JsonArray membersJson = jsonChannel.get("members").getAsJsonArray();
            for (JsonElement jsonMembersObject : membersJson)
            {
                String memberId = jsonMembersObject.getAsString();
                SlackUser user = knownUsersById.get(memberId);
                toReturn.addUser(user);
            }
        }
        return toReturn;
    }

    static final SlackChannelImpl buildSlackImChannel(JsonObject jsonChannel, Map<String, SlackUser> knownUsersById)
    {
        String id = GsonHelper.getStringOrNull(jsonChannel.get("id"));
        SlackChannelImpl toReturn = new SlackChannelImpl(id, null, null, null, true);
        String memberId = GsonHelper.getStringOrNull(jsonChannel.get("user"));
        SlackUser user = knownUsersById.get(memberId);
        toReturn.addUser(user);
        return toReturn;
    }

    static final SlackTeam buildSlackTeam(JsonObject jsonTeam)
    {
        String id = GsonHelper.getStringOrNull(jsonTeam.get("id"));
        String name = GsonHelper.getStringOrNull(jsonTeam.get("name"));
        String domain = GsonHelper.getStringOrNull(jsonTeam.get("domain"));
        return new SlackTeamImpl(id, name, domain);
    }
}
