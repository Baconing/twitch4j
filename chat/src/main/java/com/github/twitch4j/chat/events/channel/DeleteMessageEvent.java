package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.ApiStatus;

/**
 * Fired when a single message was deleted over IRC by a moderator via {@code /delete <target-msg-id>}
 */
@Value
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DeleteMessageEvent extends AbstractChannelEvent {

    /**
     * The raw message event.
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    IRCMessageEvent messageEvent;

    /**
     * The login name of the user whose message was deleted.
     */
    String userName;

    /**
     * UUID of the message that was deleted.
     */
    String msgId;

    /**
     * The message that was deleted.
     */
    String message;

    /**
     * Whether the deleted message was an action event (/me).
     */
    @Accessors(fluent = true)
    boolean wasActionMessage;

    @ApiStatus.Internal
    public DeleteMessageEvent(IRCMessageEvent messageEvent, EventChannel channel, String userName, String msgId, String message, boolean wasActionMessage) {
        super(channel);
        this.messageEvent = messageEvent;
        this.userName = userName;
        this.msgId = msgId;
        this.message = message;
        this.wasActionMessage = wasActionMessage;
    }

}
