package com.hamkelasi.bll;

import com.hamkelasi.dal.Base;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

// Enum equivalent to C# MessageStatus
public class Message {
    private int id;
    private int senderUser;
    private int receiverUser;
    private String subject;
    private String text;
    private LocalDateTime date;
    private MessageStatus status;



    // Getters and Setters
    public int getId() {
        return id;
    }

    public int getSenderUser() {
        return senderUser;
    }

    public void setSenderUser(int senderUser) {
        this.senderUser = senderUser;
    }

    public int getReceiverUser() {
        return receiverUser;
    }

    public void setReceiverUser(int receiverUser) {
        this.receiverUser = receiverUser;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public Message() {
    }

    public Message(int id) {
        com.hamkelasi.dal.Message dalMessage = new com.hamkelasi.dal.Message();
        List<Base.Row> dt = dalMessage.get(id);

        if (!dt.isEmpty()) {
            this.id = Integer.parseInt(dt.get(0).get("id").toString());
            this.senderUser = Integer.parseInt(dt.get(0).get("fromUser").toString());
            this.receiverUser = Integer.parseInt(dt.get(0).get("toUser").toString());
            this.subject = dt.get(0).get("subject").toString();
            this.text = dt.get(0).get("pmText").toString();
            this.date = LocalDateTime.parse(dt.get(0).get("SendDate").toString());
            this.status = MessageStatus.fromValue(Integer.parseInt(dt.get(0).get("Status").toString()));
        } else {
            this.id = -1;
        }
    }

    public List<Message> getInbox(int userId) {
        com.hamkelasi.dal.Message dalMessage = new com.hamkelasi.dal.Message();
        List<Base.Row> dt = dalMessage.getInboxListID(userId);
        List<Message> messageList = new ArrayList<>();

        for (int i = 0; i < dt.size(); i++) {
            int id = Integer.parseInt(dt.get(i).get("id").toString());
            Message temp = new Message(id);
            messageList.add(temp);
        }
        return messageList;
    }

    public List<Message> getOutbox(int userId) {
        com.hamkelasi.dal.Message dalMessage = new com.hamkelasi.dal.Message();
        List<Base.Row> dt = dalMessage.getOutboxListID(userId);
        List<Message> messageList = new ArrayList<>();

        for (int i = 0; i < dt.size(); i++) {
            int id = Integer.parseInt(dt.get(i).get("id").toString());
            Message temp = new Message(id);
            messageList.add(temp);
        }
        return messageList;
    }

    public void markAsRead() {
        markAsRead(this.id);
    }

    public void markAsRead(int messageId) {
        com.hamkelasi.dal.Message dalMessage = new com.hamkelasi.dal.Message();
        dalMessage.markAsRead(messageId);
    }

    public int add() {
        return add(this.receiverUser, this.senderUser, this.date, this.text, this.subject);
    }

    public int add(int receiverId, int senderId, LocalDateTime date, String text, String subject) {
        com.hamkelasi.dal.Message dalMessage = new com.hamkelasi.dal.Message();
        boolean isAdd = dalMessage.add(receiverId, senderId, date, text, subject);

        if (isAdd) {
            return 0; // ثبت اطلاعات با موفقیت انجام شد
        } else {
            return 9; // اشکال در ثبت اطلاعات
        }
    }

    public enum MessageStatus {
        READ(0),
        UNREAD(1);

        private final int value;

        MessageStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        // Convert integer to enum
        public static MessageStatus fromValue(int value) {
            for (MessageStatus status : MessageStatus.values()) {
                if (status.value == value) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Invalid MessageStatus value: " + value);
        }
    }
}