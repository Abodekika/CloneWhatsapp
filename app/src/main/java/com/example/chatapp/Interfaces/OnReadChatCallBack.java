package com.example.chatapp.Interfaces;

import com.example.chatapp.ModelClass.Message;

import java.util.List;

public interface OnReadChatCallBack {

    void onReadChatSuccess(List<Message> messageList );
    void onReadChatFailure();
}
