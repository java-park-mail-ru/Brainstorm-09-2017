package application.websocket;

public class Letter {
    // Id пользователя, кому отправляется или от кого пришло сообщение
    private Long addresserId;
    private Message message;


    public Letter(Long addresserId, Message message) {
        this.addresserId = addresserId;
        this.message = message;
    }


    public Long getAddresserId() {
        return addresserId;
    }

    public Message getMessage() {
        return message;
    }
}
