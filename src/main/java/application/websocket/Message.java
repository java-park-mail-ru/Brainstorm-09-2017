package application.websocket;

import application.game.messages.*;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "class")
@JsonSubTypes({
        @Type(ClientSnap.class),
        @Type(ServerSnap.class),
        @Type(NewBubbles.class),
        @Type(BurstingBubbles.class),
        @Type(Surrender.class),
        })
public abstract class Message {
}
