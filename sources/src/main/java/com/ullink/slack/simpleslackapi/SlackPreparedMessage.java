package com.ullink.slack.simpleslackapi;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SlackPreparedMessage {
    private final String message;
    private final boolean unfurl;
    private final SlackAttachment[] attachments;

    private SlackPreparedMessage(String message, boolean unfurl, SlackAttachment[] attachments) {
        this.message = message;
        this.unfurl = unfurl;
        this.attachments = attachments;
    }

    public String getMessage() {
        return message;
    }

    public boolean isUnfurl() {
        return unfurl;
    }

    public SlackAttachment[] getAttachments() {
        return attachments;
    }

    public static class Builder {
        String message;
        boolean unfurl;
        List<SlackAttachment> attachments;

        public Builder() {
            this.attachments = new ArrayList<>();
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withUnfurl(boolean unfurl) {
            this.unfurl = unfurl;
            return this;
        }

        public Builder addAttachment(SlackAttachment attachment) {
            this.attachments.add(attachment);
            return this;
        }

        public Builder addAttachments(List<SlackAttachment> attachments) {
            this.attachments.addAll(attachments);
            return this;
        }

        public Builder withAttachments(List<SlackAttachment> attachments) {
            this.attachments = attachments;
            return this;
        }

        public SlackPreparedMessage build() {
            return new SlackPreparedMessage(
                    message,
                    unfurl,
                    attachments.toArray(new SlackAttachment[]{}));
        }
    }

    @Override
    public String toString() {
        return "SlackPreparedMessage{" +
                "message='" + message + '\'' +
                ", unfurl=" + unfurl +
                ", attachments=" + Arrays.toString(attachments) +
                '}';
    }
}
