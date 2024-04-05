package com.orange.lo.sample.lo2pubsub.liveobjects;

import java.util.Objects;

public class LoMessage {

    private final int messageId;
    private final String message;

    public LoMessage(int messageId, String message) {
        this.messageId = messageId;
        this.message = message;
    }

    public int getMessageId() {
        return messageId;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoMessage)) return false;
        LoMessage loMessage = (LoMessage) o;
        return messageId == loMessage.messageId && Objects.equals(message, loMessage.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId, message);
    }
}
