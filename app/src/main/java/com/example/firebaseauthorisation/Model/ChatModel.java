package com.example.firebaseauthorisation.Model;

public class ChatModel {
    String receiver_id,sender_id,msg, timeStamp,receiver_name;

    public ChatModel(String receiver_id, String sender_id, String msg, String timeSpan,String receiver_name) {
        this.receiver_id = receiver_id;
        this.sender_id = sender_id;
        this.msg = msg;
        this.timeStamp = timeSpan;
        this.receiver_name = receiver_name;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
