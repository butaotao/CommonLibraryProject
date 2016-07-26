package com.cms.mylive.chat;

public class SysMessage extends AbsChatMessage {

	public long getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(long sendUserId) {
		this.sendUserId = sendUserId;
	}
}