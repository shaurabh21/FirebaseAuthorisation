package com.example.firebaseauthorisation.Model;

public class NewChatModel {
    String senderName,senderImage,receiverImage,sender_id,receiver_id,msg,timeStamp,receiver_name;

    public NewChatModel(String senderName, String senderImage, String receiverImage, String sender_id, String receiver_id, String msg, String timeStamp, String receiver_name) {
        this.senderName = senderName;
        this.senderImage = senderImage;
        this.receiverImage = receiverImage;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.msg = msg;
        this.timeStamp = timeStamp;
        this.receiver_name = receiver_name;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }

    public String getReceiverImage() {
        return receiverImage;
    }

    public void setReceiverImage(String receiverImage) {
        this.receiverImage = receiverImage;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
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

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }
}
