package com.arcltd.staff.activity.chat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.firebase.MyMessagingService;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.GetChatResponse;
import com.arcltd.staff.sqlliteDatabase.ChatLocalDBHelper;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.Infrastructure;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity
        extends BaseActivity
        implements ChatAdapter.OnMessageLongClickListener {

    private static final String TAG = "ChatActivity";
    private static final long POLLING_INTERVAL = 3000L;

    private static final int REQUEST_IMAGE_PICK = 1001;
    private static final int REQUEST_CAMERA_CAPTURE = 1002;
    private static final int REQUEST_DOCUMENT_PICK = 1003;
    private static final int REQUEST_VIDEO_PICK = 1004;
    private static final int REQUEST_PERMISSIONS = 2001;

    private RecyclerView recyclerView;
    private ChatAdapter adapter;

    private List<ChatMessage> messageList;

    private EditText editMessage;
    private ImageButton btnSend;
    private ImageButton btnAttach;
    private ImageButton btnMic;

    private LinearLayout layoutChatSend;
    private LinearLayout layoutRecording;
    private TextView tvRecordingTimer;
    private ImageButton btnRecordCancel;
    private ImageButton btnRecordSend;

    private String targetEmpCode;
    private String targetEmpName;
    private String myEmpCode;

    private ChatLocalDBHelper dbHelper;

    private Uri photoUri;
    private MediaRecorder voiceRecorder = null;
    private File voiceOutputFile = null;

    private final Handler recordTimerHandler = new Handler(Looper.getMainLooper());
    private long recordStartTime = 0;

    private final Runnable recordTimerRunnable = new Runnable() {
        @Override
        public void run() {
            if (voiceRecorder != null) {
                long elapsedSeconds = (System.currentTimeMillis() - recordStartTime) / 1000;
                long minutes = elapsedSeconds / 60;
                long seconds = elapsedSeconds % 60;
                if (tvRecordingTimer != null) {
                    tvRecordingTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
                }
                recordTimerHandler.postDelayed(this, 500);
            }
        }
    };

    private final Handler pollingHandler = new Handler(Looper.getMainLooper());
    private boolean receiverRegistered = false;

    // =========================================================
    // POLLING
    // =========================================================

    private final Runnable pollingRunnable = new Runnable() {
        @Override
        public void run() {
            fetchChatFromServer();
            pollingHandler.postDelayed(this, POLLING_INTERVAL);
        }
    };

    // =========================================================
    // FCM BROADCAST RECEIVER
    // =========================================================

    private final BroadcastReceiver chatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String senderCode = intent.getStringExtra(MyMessagingService.EXTRA_SENDER_CODE);

            if (TextUtils.isEmpty(senderCode) || TextUtils.isEmpty(targetEmpCode)) {
                return;
            }

            if (targetEmpCode.trim().equalsIgnoreCase(senderCode.trim())) {
                Log.d(TAG, "FCM chat received from " + senderCode);
                loadMessages();
                fetchChatFromServer();
                markMessagesAsRead();
            }
        }
    };

    // =========================================================
    // CREATE
    // =========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        dbHelper = ChatLocalDBHelper.getInstance(this);

        myEmpCode = RetrofitClient.getStringUserPreference(this, Constants.EMP_CODE);
        targetEmpCode = getIntent().getStringExtra("emp_code");
        targetEmpName = getIntent().getStringExtra("userName");

        if (TextUtils.isEmpty(myEmpCode) || TextUtils.isEmpty(targetEmpCode)) {
            Toast.makeText(this, "Employee code is missing.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        initUI();
        loadMessages(true);
        fetchChatFromServer();
        markMessagesAsRead();
    }

    // =========================================================
    // UI
    // =========================================================

    private void initUI() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(
                    TextUtils.isEmpty(targetEmpName) ? "Chat" : targetEmpName
            );
        }

        recyclerView = findViewById(R.id.recyclerViewChat);
        editMessage = findViewById(R.id.edit_chat_message);
        btnSend = findViewById(R.id.btn_chat_send);
        btnAttach = findViewById(R.id.btn_chat_attach);
        btnMic = findViewById(R.id.btn_chat_mic);

        layoutChatSend = findViewById(R.id.layout_chat_send);
        layoutRecording = findViewById(R.id.layout_recording);
        tvRecordingTimer = findViewById(R.id.tv_recording_timer);
        btnRecordCancel = findViewById(R.id.btn_record_cancel);
        btnRecordSend = findViewById(R.id.btn_record_send);

        messageList = new ArrayList<>();
        adapter = new ChatAdapter(messageList, this);
        recyclerView.setAdapter(adapter);

        btnSend.setOnClickListener(v -> sendMessage());

        if (btnAttach != null) {
            btnAttach.setOnClickListener(v -> showAttachmentOptions());
        }

        if (btnMic != null) {
            btnMic.setOnClickListener(v -> startWhatsAppVoiceRecording());
        }

        if (btnRecordCancel != null) {
            btnRecordCancel.setOnClickListener(v -> cancelVoiceRecording());
        }

        if (btnRecordSend != null) {
            btnRecordSend.setOnClickListener(v -> stopAndSendVoiceNote());
        }

        editMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    if (btnMic != null) btnMic.setVisibility(View.GONE);
                    if (btnSend != null) btnSend.setVisibility(View.VISIBLE);
                } else {
                    if (btnSend != null) btnSend.setVisibility(View.GONE);
                    if (btnMic != null) btnMic.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // =========================================================
    // ATTACHMENTS
    // =========================================================

    private void showAttachmentOptions() {
        String[] options = {"📷 Image (Gallery)", "📸 Camera Photo", "📄 Document (PDF/File)", "🎥 Video", "🎙️ Voice Note"};
        new AlertDialog.Builder(this)
                .setTitle("Select Attachment")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            openGallery();
                            break;
                        case 1:
                            openCamera();
                            break;
                        case 2:
                            openDocumentPicker();
                            break;
                        case 3:
                            openVideoPicker();
                            break;
                        case 4:
                            startWhatsAppVoiceRecording();
                            break;
                    }
                })
                .show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    private void openCamera() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSIONS);
                return;
            }
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File photoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "chat_" + System.currentTimeMillis() + ".jpg");
            photoUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(takePictureIntent, REQUEST_CAMERA_CAPTURE);
        } catch (Exception e) {
            Toast.makeText(this, "Cannot open camera", Toast.LENGTH_SHORT).show();
        }
    }

    private void openDocumentPicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select Document"), REQUEST_DOCUMENT_PICK);
    }

    private void openVideoPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_VIDEO_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_IMAGE_PICK && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            sendMediaMessage(selectedImage, "[IMAGE] ", "Image Attachment");
        } else if (requestCode == REQUEST_CAMERA_CAPTURE && photoUri != null) {
            sendMediaMessage(photoUri, "[IMAGE] ", "Image Attachment");
        } else if (requestCode == REQUEST_DOCUMENT_PICK && data != null && data.getData() != null) {
            Uri docUri = data.getData();
            String fileName = getFileNameFromUri(docUri);
            sendMediaMessage(docUri, "[DOCUMENT] ", fileName);
        } else if (requestCode == REQUEST_VIDEO_PICK && data != null && data.getData() != null) {
            Uri videoUri = data.getData();
            sendMediaMessage(videoUri, "[VIDEO] ", "Video Attachment");
        }
    }

    private String getFileNameFromUri(Uri uri) {
        String name = "Document";
        if (uri == null) return name;
        try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (nameIndex != -1) {
                    name = cursor.getString(nameIndex);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Filename query error", e);
        }
        return name;
    }

    private File getFileFromUri(Uri uri) {
        if (uri == null) return null;

        if ("file".equalsIgnoreCase(uri.getScheme()) && uri.getPath() != null) {
            File directFile = new File(uri.getPath());
            if (directFile.exists()) return directFile;
        }

        try {
            String fileName = getFileNameFromUri(uri);
            File cacheFile = new File(getExternalCacheDir(), "upload_" + System.currentTimeMillis() + "_" + fileName);
            try (InputStream inputStream = getContentResolver().openInputStream(uri);
                 FileOutputStream outputStream = new FileOutputStream(cacheFile)) {

                if (inputStream == null) return null;

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            }
            return cacheFile;
        } catch (Exception e) {
            Log.e(TAG, "Error converting Uri to File", e);
            return null;
        }
    }

    // =========================================================
    // VOICE NOTE RECORDING (WHATSAPP STYLE)
    // =========================================================

    private void startWhatsAppVoiceRecording() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_PERMISSIONS);
            return;
        }

        try {
            voiceOutputFile = new File(getExternalCacheDir(), "voice_" + System.currentTimeMillis() + ".mp3");
            voiceRecorder = new MediaRecorder();
            voiceRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            voiceRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            voiceRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            voiceRecorder.setOutputFile(voiceOutputFile.getAbsolutePath());
            voiceRecorder.prepare();
            voiceRecorder.start();

            recordStartTime = System.currentTimeMillis();
            if (tvRecordingTimer != null) {
                tvRecordingTimer.setText("00:00");
            }
            recordTimerHandler.post(recordTimerRunnable);

            if (layoutChatSend != null) layoutChatSend.setVisibility(View.GONE);
            if (layoutRecording != null) layoutRecording.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            Toast.makeText(this, "Audio recording failed", Toast.LENGTH_SHORT).show();
            cancelVoiceRecording();
        }
    }

    private void stopAndSendVoiceNote() {
        recordTimerHandler.removeCallbacks(recordTimerRunnable);
        try {
            if (voiceRecorder != null) {
                voiceRecorder.stop();
                voiceRecorder.release();
                voiceRecorder = null;

                if (voiceOutputFile != null && voiceOutputFile.exists()) {
                    Uri voiceUri = Uri.fromFile(voiceOutputFile);
                    sendMediaMessage(voiceUri, "[VOICE] ", "Voice Note");
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Voice stop error", e);
        } finally {
            if (layoutRecording != null) layoutRecording.setVisibility(View.GONE);
            if (layoutChatSend != null) layoutChatSend.setVisibility(View.VISIBLE);
        }
    }

    private void cancelVoiceRecording() {
        recordTimerHandler.removeCallbacks(recordTimerRunnable);
        try {
            if (voiceRecorder != null) {
                voiceRecorder.stop();
                voiceRecorder.release();
                voiceRecorder = null;
            }
            if (voiceOutputFile != null && voiceOutputFile.exists()) {
                voiceOutputFile.delete();
            }
        } catch (Exception ignored) {
        } finally {
            if (layoutRecording != null) layoutRecording.setVisibility(View.GONE);
            if (layoutChatSend != null) layoutChatSend.setVisibility(View.VISIBLE);
        }
    }

    // =========================================================
    // SEND MEDIA MESSAGE
    // =========================================================

    private void sendMediaMessage(Uri uri, String formattedPrefix, String fallbackText) {
        if (TextUtils.isEmpty(myEmpCode) || TextUtils.isEmpty(targetEmpCode)) {
            Toast.makeText(this, "Employee code is missing.", Toast.LENGTH_SHORT).show();
            return;
        }

        long timestamp = System.currentTimeMillis();
        String tempId = "temp_" + timestamp;

        File uploadFile = getFileFromUri(uri);
        String localPathOrUri = (uploadFile != null && uploadFile.exists()) ? uploadFile.getAbsolutePath() : (uri != null ? uri.toString() : fallbackText);
        String localMessageText = formattedPrefix + localPathOrUri;

        dbHelper.addMessage(
                myEmpCode,
                targetEmpCode,
                tempId,
                localMessageText,
                true,
                timestamp,
                true,
                0
        );

        loadMessages(true);

        if (Infrastructure.isInternetPresent()) {
            if (uploadFile != null && uploadFile.exists()) {
                apiPresenter.sendChatWithFile(
                        disposable,
                        Constants.ApiRequestCode.SEND_CHAT,
                        myEmpCode,
                        targetEmpCode,
                        fallbackText,
                        uploadFile
                );
            } else {
                apiPresenter.sendChat(
                        disposable,
                        Constants.ApiRequestCode.SEND_CHAT,
                        myEmpCode,
                        targetEmpCode,
                        localMessageText
                );
            }
        } else {
            Toast.makeText(this, "No internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    // =========================================================
    // GET SERVER CHAT
    // =========================================================

    private void fetchChatFromServer() {
        if (!Infrastructure.isInternetPresent()) return;
        if (TextUtils.isEmpty(myEmpCode) || TextUtils.isEmpty(targetEmpCode)) return;

        apiPresenter.getChat(
                disposable,
                Constants.ApiRequestCode.GET_CHAT,
                myEmpCode,
                targetEmpCode
        );
    }

    // =========================================================
    // LOAD SQLITE
    // =========================================================

    private void loadMessages(boolean forceScrollToBottom) {
        if (TextUtils.isEmpty(myEmpCode) || TextUtils.isEmpty(targetEmpCode)) return;

        List<ChatMessage> raw = dbHelper.getMessages(myEmpCode, targetEmpCode);
        List<ChatMessage> processed = processMessagesWithDateHeaders(raw);

        int oldSize = messageList.size();
        boolean isNearBottom = false;

        if (recyclerView != null && recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int lastVisible = layoutManager.findLastCompletelyVisibleItemPosition();
            isNearBottom = (lastVisible >= oldSize - 3);
        }

        messageList.clear();
        messageList.addAll(processed);
        adapter.notifyDataSetChanged();

        if (adapter.getItemCount() > 0 && (forceScrollToBottom || isNearBottom || oldSize == 0)) {
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        }
    }

    private void loadMessages() {
        loadMessages(false);
    }

    // =========================================================
    // DATE HEADERS
    // =========================================================

    private List<ChatMessage> processMessagesWithDateHeaders(List<ChatMessage> rawMessages) {
        List<ChatMessage> result = new ArrayList<>();
        String lastDate = "";
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

        for (ChatMessage message : rawMessages) {
            String messageDate = format.format(new Date(message.getTimestamp()));

            if (!messageDate.equals(lastDate)) {
                String header = Infrastructure.isToday(message.getTimestamp()) ? "Today" : messageDate;
                result.add(new ChatMessage("date_header_" + message.getTimestamp(), header, false, message.getTimestamp()));
                lastDate = messageDate;
            }
            result.add(message);
        }
        return result;
    }

    // =========================================================
    // SEND MESSAGE
    // =========================================================

    private void sendMessage() {
        String text = editMessage.getText().toString().trim();

        if (TextUtils.isEmpty(text)) return;
        if (TextUtils.isEmpty(myEmpCode) || TextUtils.isEmpty(targetEmpCode)) {
            Toast.makeText(this, "Employee code is missing.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Infrastructure.isInternetPresent()) {
            Toast.makeText(this, "No internet connection.", Toast.LENGTH_SHORT).show();
            return;
        }

        long timestamp = System.currentTimeMillis();
        String tempId = "temp_" + timestamp;

        dbHelper.addMessage(
                myEmpCode,
                targetEmpCode,
                tempId,
                text,
                true,
                timestamp,
                true,
                0
        );

        editMessage.setText("");
        loadMessages(true);

        apiPresenter.sendChat(
                disposable,
                Constants.ApiRequestCode.SEND_CHAT,
                myEmpCode,
                targetEmpCode,
                text
        );
    }

    // =========================================================
    // MARK SEEN
    // =========================================================

    private void markMessagesAsRead() {
        if (TextUtils.isEmpty(myEmpCode) || TextUtils.isEmpty(targetEmpCode)) return;

        dbHelper.markAsRead(myEmpCode, targetEmpCode);
        loadMessages();

        if (!Infrastructure.isInternetPresent()) return;

        apiPresenter.markChatSeen(
                disposable,
                Constants.ApiRequestCode.MARK_CHAT_SEEN,
                targetEmpCode,
                myEmpCode
        );
    }

    // =========================================================
    // LONG CLICK (DELETE / SHARE)
    // =========================================================

    @Override
    public void onMessageLongClick(ChatMessage message, int position) {
        if (message == null || TextUtils.isEmpty(message.getId())) return;
        if (message.getId().startsWith("date_header_")) return;

        String[] options = {"Share", "Delete"};

        new AlertDialog.Builder(this)
                .setTitle("Message Options")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        shareMessage(message);
                    } else if (which == 1) {
                        confirmDeleteMessage(message);
                    }
                })
                .show();
    }

    private void confirmDeleteMessage(ChatMessage message) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Message")
                .setMessage("Are you sure you want to delete this message?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deleteMessage(message.getId());
                    if (message.getId() != null && !message.getId().startsWith("temp_") && Infrastructure.isInternetPresent()) {
                        apiPresenter.deleteChat(disposable, Constants.ApiRequestCode.DELETE_CHAT, message.getId());
                    }
                    Toast.makeText(this, "Message deleted", Toast.LENGTH_SHORT).show();
                    loadMessages();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void shareMessage(ChatMessage message) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);

            if (message.isImage()) {
                shareIntent.setType("image/*");
                Uri uri = Uri.parse(message.getMediaUrl());
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else if (message.isDocument()) {
                shareIntent.setType("*/*");
                Uri uri = Uri.parse(message.getMediaUrl());
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else if (message.isVoice()) {
                shareIntent.setType("audio/*");
                Uri uri = Uri.parse(message.getMediaUrl());
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else if (message.isVideo()) {
                shareIntent.setType("video/*");
                Uri uri = Uri.parse(message.getMediaUrl());
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, message.getMessage());
            }

            startActivity(Intent.createChooser(shareIntent, "Share message via"));
        } catch (Exception e) {
            Toast.makeText(this, "Cannot share message", Toast.LENGTH_SHORT).show();
        }
    }

    // =========================================================
    // RESUME / PAUSE / POLLING
    // =========================================================

    @Override
    protected void onResume() {
        super.onResume();

        if (!receiverRegistered) {
            IntentFilter filter = new IntentFilter(MyMessagingService.CHAT_BROADCAST);
            ContextCompat.registerReceiver(
                    this,
                    chatReceiver,
                    filter,
                    ContextCompat.RECEIVER_NOT_EXPORTED
            );
            receiverRegistered = true;
        }

        MyMessagingService.setActiveChatEmployee(targetEmpCode);
        fetchChatFromServer();
        loadMessages();
        markMessagesAsRead();
        startPolling();
    }

    @Override
    protected void onPause() {
        stopPolling();

        if (receiverRegistered) {
            try {
                unregisterReceiver(chatReceiver);
            } catch (Exception e) {
                Log.e(TAG, "Receiver unregister error", e);
            }
            receiverRegistered = false;
        }

        MyMessagingService.clearActiveChatEmployee(targetEmpCode);
        super.onPause();
    }

    private void startPolling() {
        pollingHandler.removeCallbacks(pollingRunnable);
        pollingHandler.postDelayed(pollingRunnable, POLLING_INTERVAL);
    }

    private void stopPolling() {
        pollingHandler.removeCallbacks(pollingRunnable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_clear_chat) {
            confirmClearChat();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmClearChat() {
        new AlertDialog.Builder(this)
                .setTitle("Clear Chat")
                .setMessage("Are you sure you want to clear all messages in this conversation?")
                .setPositiveButton("Clear", (dialog, which) -> {
                    dbHelper.clearChat(myEmpCode, targetEmpCode);
                    if (Infrastructure.isInternetPresent()) {
                        apiPresenter.clearChat(disposable, Constants.ApiRequestCode.CLEAR_CHAT, myEmpCode, targetEmpCode);
                    }
                    Toast.makeText(this, "Chat cleared", Toast.LENGTH_SHORT).show();
                    loadMessages();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        if (callAPIId == Constants.ApiRequestCode.GET_CHAT) {
            Log.e(TAG, "GET_CHAT error: " + message);
            return;
        }

        super.onDisplayMessage(message, callAPIId, errorCode);

        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showResult(Object object, int callAPIId) {

        if (callAPIId == Constants.ApiRequestCode.GET_CHAT) {
            if (!(object instanceof GetChatResponse)) return;

            GetChatResponse response = (GetChatResponse) object;
            if (response.getChat() == null) return;

            boolean serverHasSentMessages = false;
            boolean incomingUnread = false;
            List<String> activeServerIds = new ArrayList<>();

            for (GetChatResponse.ChatItem item : response.getChat()) {
                if (item == null) continue;

                String serverId = item.getId();
                String sender = item.getSenderEmpCode();
                String receiver = item.getReceiverEmpCode();

                if (TextUtils.isEmpty(serverId)) {
                    serverId = "msg_" + (item.getCreatedAt() != null ? item.getCreatedAt().hashCode() : System.currentTimeMillis());
                }

                if (TextUtils.isEmpty(sender) && !TextUtils.isEmpty(receiver)) {
                    if (myEmpCode != null && receiver.trim().equalsIgnoreCase(myEmpCode.trim())) {
                        sender = targetEmpCode;
                    } else {
                        sender = myEmpCode;
                    }
                }

                if (TextUtils.isEmpty(receiver) && !TextUtils.isEmpty(sender)) {
                    if (myEmpCode != null && sender.trim().equalsIgnoreCase(myEmpCode.trim())) {
                        receiver = targetEmpCode;
                    } else {
                        receiver = myEmpCode;
                    }
                }

                if (TextUtils.isEmpty(sender)) sender = targetEmpCode;
                if (TextUtils.isEmpty(receiver)) receiver = myEmpCode;

                activeServerIds.add(serverId);

                boolean isSent = myEmpCode.trim().equalsIgnoreCase(sender.trim());
                int status = parseStatus(item.getMessageStatus());
                long timestamp = parseTimestamp(item.getCreatedAt());

                String imageUrl = item.getImageUrl();
                String messageText = item.getMessage();

                if (!TextUtils.isEmpty(imageUrl)) {
                    String lowerUrl = imageUrl.trim().toLowerCase();
                    if (lowerUrl.endsWith(".pdf") || lowerUrl.endsWith(".doc") || lowerUrl.endsWith(".docx") || lowerUrl.endsWith(".xlsx") || lowerUrl.endsWith(".xls") || lowerUrl.endsWith(".txt")) {
                        if (messageText == null || !messageText.startsWith("[DOCUMENT]")) {
                            String docName = (!TextUtils.isEmpty(messageText) && !messageText.equalsIgnoreCase("Image Attachment")) ? messageText : "Document.pdf";
                            messageText = "[DOCUMENT] " + imageUrl + "|" + docName;
                        }
                    } else if (lowerUrl.endsWith(".mp4") || lowerUrl.endsWith(".3gp") || lowerUrl.endsWith(".mkv")) {
                        if (messageText == null || !messageText.startsWith("[VIDEO]")) {
                            messageText = "[VIDEO] " + imageUrl;
                        }
                    } else if (lowerUrl.endsWith(".mp3") || lowerUrl.endsWith(".m4a") || lowerUrl.endsWith(".aac") || lowerUrl.endsWith(".wav")) {
                        if (messageText == null || !messageText.startsWith("[VOICE]")) {
                            messageText = "[VOICE] " + imageUrl;
                        }
                    } else {
                        if (messageText == null || !messageText.startsWith("[IMAGE]")) {
                            messageText = "[IMAGE] " + imageUrl;
                        }
                    }
                }

                if (!dbHelper.messageExists(serverId)) {
                    dbHelper.addMessage(
                            sender,
                            receiver,
                            serverId,
                            messageText,
                            isSent,
                            timestamp,
                            isSent || "1".equals(item.getReadUnread()),
                            status
                    );
                } else {
                    dbHelper.updateMessageStatus(serverId, status);
                }

                if (isSent) serverHasSentMessages = true;
                if (!isSent && !"1".equals(item.getReadUnread())) incomingUnread = true;
            }

            dbHelper.syncServerMessages(myEmpCode, targetEmpCode, activeServerIds);

            if (serverHasSentMessages) {
                dbHelper.clearTempMessages(myEmpCode, targetEmpCode);
            }

            loadMessages();

            if (incomingUnread) {
                markMessagesAsRead();
            }

            return;
        }

        if (callAPIId == Constants.ApiRequestCode.SEND_CHAT) {
            Infrastructure.dismissProgressDialog();
            dbHelper.clearTempMessages(myEmpCode, targetEmpCode);
            loadMessages();
            fetchChatFromServer();
            return;
        }

        if (callAPIId == Constants.ApiRequestCode.MARK_CHAT_SEEN) {
            return;
        }

        super.showResult(object, callAPIId);
    }

    private int parseStatus(String value) {
        try {
            int status = Integer.parseInt(value);
            if (status < 0) return 0;
            if (status > 2) return 2;
            return status;
        } catch (Exception e) {
            return 0;
        }
    }

    private long parseTimestamp(String createdAt) {
        if (TextUtils.isEmpty(createdAt)) return System.currentTimeMillis();

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = format.parse(createdAt);
            if (date != null) return date.getTime();
        } catch (Exception e) {
            Log.e(TAG, "Date parse error: " + createdAt, e);
        }

        return System.currentTimeMillis();
    }
}