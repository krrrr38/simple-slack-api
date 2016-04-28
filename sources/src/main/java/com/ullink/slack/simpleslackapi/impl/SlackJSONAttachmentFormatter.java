package com.ullink.slack.simpleslackapi.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackField;

class SlackJSONAttachmentFormatter
{
    public static List<JsonObject> encodeAttachments(SlackAttachment... attachments)
    {
        List<JsonObject> toReturn = new ArrayList<>();
        for (int i = 0; i < attachments.length; i++)
        {
            JsonObject attachmentJSON = new JsonObject();
            toReturn.add(attachmentJSON);
            if (attachments[i].title != null)
            {
                attachmentJSON.addProperty("title", attachments[i].title);
            }
            if (attachments[i].thumb_url != null)
            {
                attachmentJSON.addProperty("thumb_url", attachments[i].thumb_url);
            }
            if (attachments[i].titleLink != null)
            {
                attachmentJSON.addProperty("title_link", attachments[i].titleLink);
            }
            if (attachments[i].text != null)
            {
                attachmentJSON.addProperty("text", attachments[i].text);
            }
            if (attachments[i].color != null)
            {
                attachmentJSON.addProperty("color", attachments[i].color);
            }
            if (attachments[i].pretext != null)
            {
                attachmentJSON.addProperty("pretext", attachments[i].pretext);
            }
            if (attachments[i].fallback != null)
            {
                attachmentJSON.addProperty("fallback", attachments[i].fallback);
            }
            if (attachments[i].miscRootFields != null)
            {
                for (Map.Entry<String, String> entry : attachments[i].miscRootFields.entrySet())
                {
                    attachmentJSON.addProperty(entry.getKey(), entry.getValue());
                }
            }
            if (attachments[i].markdown_in != null && !attachments[i].markdown_in.isEmpty())
            {
                JsonArray array = new JsonArray();
                for (String markdown : attachments[i].markdown_in) {
                    array.add(markdown);
                }
                attachmentJSON.add("mrkdwn_in", array);
            }
            if (attachments[i].fields != null && !attachments[i].fields.isEmpty())
            {
                attachmentJSON.add("fields", encodeAttachmentFields(attachments[i].fields));
            }

        }
        return toReturn;
    }

    private static JsonArray encodeAttachmentFields(List<SlackField> fields)
    {
        JsonArray toReturn = new JsonArray();
        for (SlackField field : fields)
        {
            JsonObject fieldJSON = new JsonObject();
            if (field.title != null)
            {
                fieldJSON.addProperty("title", field.title);
            }
            if (field.value != null)
            {
                fieldJSON.addProperty("value", field.value);
            }
            fieldJSON.addProperty("short", field.isShort);
            toReturn.add(fieldJSON);
        }
        return toReturn;
    }

    public static void main(String[] args) throws UnsupportedEncodingException
    {
        System.out.println(new String("Loïc Herve".getBytes(), "UTF-8"));
    }
}
