package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.CreateShoutoutData;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class ShoutoutCreatedEvent extends TwitchEvent {
    String channelId;
    CreateShoutoutData data;
}
