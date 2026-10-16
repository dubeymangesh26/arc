package com.arcltd.staff.activity.chat;

public class ChatMessage {

    private String id;
    private String message;
    private boolean isSent;
    private long timestamp;

    // 0 = Sent
    // 1 = Delivered
    // 2 = Seen
    private int status;

    public ChatMessage(String id, String message,
                       boolean isSent, long timestamp) {
        this(id, message, isSent, timestamp, 0);
    }

    public ChatMessage(String id, String message,
                       boolean isSent, long timestamp, int status) {

        this.id = id;
        this.message = message;
        this.isSent = isSent;
        this.timestamp = timestamp;
        setStatus(status);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        if (status < 0) status = 0;
        if (status > 2) status = 2;
        this.status = status;
    }

    public boolean isDocument() {
        if (message == null) return false;
        String msg = message.trim().toLowerCase();
        if (msg.startsWith("[document]")) return true;
        String url = getMediaUrl().toLowerCase();
        return url.endsWith(".pdf") || url.endsWith(".doc") || url.endsWith(".docx") || url.endsWith(".xlsx") || url.endsWith(".xls") || url.endsWith(".txt");
    }

    public boolean isImage() {
        if (message == null) return false;
        if (isDocument()) return false;
        String msg = message.trim().toLowerCase();
        if (msg.startsWith("[image]")) return true;
        String url = getMediaUrl().toLowerCase();
        return url.endsWith(".jpg") || url.endsWith(".png") || url.endsWith(".jpeg") || url.endsWith(".webp") || url.endsWith(".gif");
    }

    public boolean isVideo() {
        if (message == null) return false;
        if (isDocument() || isImage()) return false;
        String msg = message.trim().toLowerCase();
        if (msg.startsWith("[video]")) return true;
        String url = getMediaUrl().toLowerCase();
        return url.endsWith(".mp4") || url.endsWith(".3gp") || url.endsWith(".mkv");
    }

    public boolean isVoice() {
        if (message == null) return false;
        if (isDocument()) return false;
        String msg = message.trim().toLowerCase();
        if (msg.startsWith("[voice]")) return true;
        String url = getMediaUrl().toLowerCase();
        return url.endsWith(".mp3") || url.endsWith(".m4a") || url.endsWith(".aac") || url.endsWith(".wav");
    }

    public String getMediaUrl() {
        if (message == null) return "";
        String msg = message.trim();
        if (msg.startsWith("[IMAGE]")) {
            return msg.substring("[IMAGE]".length()).trim();
        } else if (msg.startsWith("[DOCUMENT]")) {
            String content = msg.substring("[DOCUMENT]".length()).trim();
            if (content.contains("|")) {
                return content.split("\\|")[0].trim();
            }
            return content;
        } else if (msg.startsWith("[VOICE]")) {
            return msg.substring("[VOICE]".length()).trim();
        } else if (msg.startsWith("[VIDEO]")) {
            return msg.substring("[VIDEO]".length()).trim();
        }
        return msg;
    }

    public String getDocumentName() {
        if (message == null) return "Document.pdf";
        String msg = message.trim();
        if (msg.startsWith("[DOCUMENT]")) {
            String content = msg.substring("[DOCUMENT]".length()).trim();
            if (content.contains("|")) {
                String[] parts = content.split("\\|");
                if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                    return parts[1].trim();
                }
            }
        }
        String url = getMediaUrl();
        if (url.contains("/")) {
            String filename = url.substring(url.lastIndexOf("/") + 1);
            if (!filename.isEmpty()) {
                return filename;
            }
        }
        return "Document.pdf";
    }

    public String getDisplayMessage() {
        if (isDocument()) {
            return "📄 " + getDocumentName();
        } else if (isImage()) {
            return "📷 Photo";
        } else if (isVideo()) {
            return "🎥 Video";
        } else if (isVoice()) {
            return "🎙️ Voice note";
        }
        return message != null ? message : "";
    }
}