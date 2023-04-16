package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * This event gets called when a user joins a channel.
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class ChannelJoinEvent extends AbstractChannelEvent {

    /**
     * User
     */
    EventUser user;

    /**
     * Event Constructor
     *
     * @param channel The channel that this event originates from.
     * @param user    The user triggering the event.
     */
    public ChannelJoinEvent(EventChannel channel, EventUser user) {
        super(channel);
        this.user = user;
    }
}
