package application.websocket;

import application.game.messages.BurstingBubbles;
import application.game.messages.NewBubbles;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import application.game.messages.ClientSnap;
import application.game.messages.ServerSnap;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "class")
@JsonSubTypes({
        @Type(ClientSnap.class),
        @Type(ServerSnap.class),
        @Type(NewBubbles.class),
        @Type(BurstingBubbles.class),
        })
public abstract class Message {
}
