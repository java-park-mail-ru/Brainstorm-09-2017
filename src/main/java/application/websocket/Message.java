package application.websocket;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import application.mechanics.base.ClientSnap;
import application.mechanics.base.ServerSnap;
import application.mechanics.requests.InitGame;

@JsonTypeInfo(use= JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "class")
@JsonSubTypes({
        @Type(InitGame.Request.class),
        @Type(ClientSnap.class),
        @Type(ServerSnap.class),
        })
public abstract class Message {
}
