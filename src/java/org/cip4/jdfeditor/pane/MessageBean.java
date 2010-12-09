package org.cip4.jdfeditor.pane;

public class MessageBean {
	private String filePathName;
	private String senderId;
	private String messageType;
	private String timeReceived;
	private String size;
	
	
	public MessageBean() {
	}
	
	public String getFilePathName()
	{
		return filePathName;
	}
	
	public void setFilePathName(String filePathName)
	{
		this.filePathName = filePathName;
	}
	
	public String getSenderId() {
		return senderId;
	}
	
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	
	public String getMessageType() {
		return messageType;
	}
	
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	
	public String getTimeReceived() {
		return timeReceived;
	}
	
	public void setTimeReceived(String timeReceived) {
		this.timeReceived = timeReceived;
	}
	
	public String getSize() {
		return size;
	}
	
	public void setSize(String size) {
		this.size = size;
	}
}
