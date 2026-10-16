package com.arcltd.staff.activity.chat;

import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.adapter.EmployeeListAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.firebase.MyMessagingService;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.EmployeeListResponse;
import com.arcltd.staff.response.GetChatHistoryUsersResponse;
import com.arcltd.staff.response.GetChatResponse;
import com.arcltd.staff.response.UserChatListResponse;
import com.arcltd.staff.sqlliteDatabase.ChatLocalDBHelper;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatUserListActivity extends BaseActivity {
    private static final String TAG = "ChatUserListActivity";

    RecyclerView list;
    SwipeRefreshLayout swiptoRefresh;
    AutoCompleteTextView atSearch;
    ProgressBar searchProgressBar;
    SpeechRecognizer speechRecognizer;
    ImageView micButton;
    private ChatLocalDBHelper dbHelper;
    private EmployeeListResponse lastLoadedEmpResponse;

    private final BroadcastReceiver chatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            fetchChatData();
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_chat_user_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Chats");
        }

        list = findViewById(R.id.list);
        swiptoRefresh = findViewById(R.id.swiptoRefresh);
        searchProgressBar = findViewById(R.id.searchProgressBar);
        atSearch = findViewById(R.id.atSearch);
        micButton = findViewById(R.id.ivEmpMic);

        dbHelper = ChatLocalDBHelper.getInstance(this);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {}

            @Override
            public void onBeginningOfSpeech() {
                atSearch.setText("");
                atSearch.setHint("Listening...");
            }

            @Override
            public void onRmsChanged(float v) {}

            @Override
            public void onBufferReceived(byte[] bytes) {}

            @Override
            public void onEndOfSpeech() {}

            @Override
            public void onError(int i) {}

            @Override
            public void onResults(Bundle bundle) {
                micButton.setImageResource(R.drawable.ic_mic_24);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (data != null && !data.isEmpty()) {
                    atSearch.setText(data.get(0));
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {}

            @Override
            public void onEvent(int i, Bundle bundle) {}
        });

        micButton.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                speechRecognizer.stopListening();
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                speechRecognizer.startListening(speechRecognizerIntent);
            }
            return false;
        });

        atSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                fetchChatData();
            }
        });

        swiptoRefresh.setOnRefreshListener(() -> {
            fetchChatData();
            swiptoRefresh.setRefreshing(false);
        });

        fetchChatData();
        updateChatUserList(null);
    }

    private void fetchChatData() {
        try {
            if (Infrastructure.isInternetPresent()) {
                searchProgressBar.setVisibility(View.VISIBLE);

                String searchQuery = atSearch.getText().toString().trim();
                String prefEmpCode = RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.EMP_CODE);
                String myEmpCode = !TextUtils.isEmpty(prefEmpCode) ? prefEmpCode : RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.ID);

                if (searchQuery.isEmpty()) {
                    // 1) NO SEARCH QUERY: Fetch user chat history
                    apiPresenter.getChatHistoryUsers(disposable, Constants.ApiRequestCode.DIVISION_LIST, myEmpCode, "", "", "");
                } else {
                    // 2) SEARCH QUERY ACTIVE: Search employee directory
                    String loginType = RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.LOGIN_TYPE);
                    String divName = RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.DIVISION_NAME);
                    apiPresenter.list_employee(disposable, Constants.ApiRequestCode.DIVISION_LIST, "", searchQuery, divName, loginType);
                    apiPresenter.list_chat_employee(disposable, Constants.ApiRequestCode.DIVISION_LIST, searchQuery);

                    // Instant local DB search
                    List<EmployeeListResponse.EmployeeList> localSearch = dbHelper.searchUsers(searchQuery);
                    if (!localSearch.isEmpty()) {
                        list.setLayoutManager(new LinearLayoutManager(this));
                        ChatHistoryAdapter localAdapter = new ChatHistoryAdapter(this, localSearch);
                        list.setAdapter(localAdapter);
                        list.setVisibility(View.VISIBLE);
                    }
                }

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in fetchChatData", e);
        }
    }

    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        searchProgressBar.setVisibility(View.GONE);
        if (swiptoRefresh != null) {
            swiptoRefresh.setRefreshing(false);
        }
        Infrastructure.dismissProgressDialog();

        if (callAPIId == Constants.ApiRequestCode.DIVISION_LIST || callAPIId == Constants.ApiRequestCode.GET_CHAT) {
            Log.e(TAG, "API " + callAPIId + " message/error: " + message);
            updateChatUserList(lastLoadedEmpResponse);
            return;
        }

        super.onDisplayMessage(message, callAPIId, errorCode);
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d(TAG, "showResult: " + object);
        searchProgressBar.setVisibility(View.GONE);
        if (swiptoRefresh != null) {
            swiptoRefresh.setRefreshing(false);
        }
        Infrastructure.dismissProgressDialog();

        if (callAPIId == Constants.ApiRequestCode.DIVISION_LIST) {
            if (object instanceof GetChatHistoryUsersResponse) {
                GetChatHistoryUsersResponse historyResp = (GetChatHistoryUsersResponse) object;
                if (historyResp.getEmployeeList() != null) {
                    dbHelper.saveChatHistoryUsers(historyResp.getEmployeeList());

                    EmployeeListResponse empResp = new EmployeeListResponse();
                    empResp.setResponseCode(historyResp.getResponseCode());
                    empResp.setMessage(historyResp.getMessage());

                    List<EmployeeListResponse.EmployeeList> empList = new ArrayList<>();
                    for (GetChatHistoryUsersResponse.UserItem item : historyResp.getEmployeeList()) {
                        EmployeeListResponse.EmployeeList emp = new EmployeeListResponse.EmployeeList();
                        emp.setEmp_code(item.getEmpCode());
                        emp.setEmp_name(item.getEmpName());
                        emp.setProfilepic(item.getProfilePic());
                        emp.setBranch_code(item.getBranchCode());
                        emp.setBranch_name(item.getBranchName());
                        emp.setDivision_name(item.getDivisionName());
                        emp.setRegion_name(item.getRegionName());
                        emp.setToken(item.getToken());
                        emp.setLast_message(item.getLastMessage());
                        emp.setLast_seen(item.getLastSeen());
                        emp.setUnread_count(item.getUnreadCount());
                        empList.add(emp);
                    }
                    empResp.setEmployee_list(empList);
                    updateChatUserList(empResp);
                } else {
                    updateChatUserList(lastLoadedEmpResponse);
                }
            } else if (object instanceof EmployeeListResponse) {
                EmployeeListResponse resp = (EmployeeListResponse) object;
                if (resp.getEmployee_list() != null) {
                    dbHelper.saveUsers(resp.getEmployee_list());
                }
                updateChatUserList(resp);
            } else if (object instanceof UserChatListResponse) {
                UserChatListResponse chatResp = (UserChatListResponse) object;
                if (chatResp.getEmployee_list() != null) {
                    dbHelper.saveUserChatList(chatResp.getEmployee_list());
                }
                EmployeeListResponse empResp = new EmployeeListResponse();
                empResp.setResponseCode(chatResp.getResponseCode());
                empResp.setMessage(chatResp.getMessage());

                List<EmployeeListResponse.EmployeeList> empList = new ArrayList<>();
                if (chatResp.getEmployee_list() != null) {
                    for (UserChatListResponse.EmployeeList item : chatResp.getEmployee_list()) {
                        EmployeeListResponse.EmployeeList emp = new EmployeeListResponse.EmployeeList();
                        emp.setEmp_code(item.getEmp_code());
                        emp.setEmp_name(item.getName());
                        emp.setProfilepic(item.getProfilepic());
                        emp.setBranch_code(item.getBranch_code());
                        emp.setBranch_name(item.getBranch_name());
                        emp.setDivision_name(item.getDivision_name());
                        emp.setRegion_name(item.getRegion_name());
                        empList.add(emp);
                    }
                }
                empResp.setEmployee_list(empList);
                updateChatUserList(empResp);
            }
        }

        if (callAPIId == Constants.ApiRequestCode.GET_CHAT) {
            if (object instanceof GetChatResponse) {
                GetChatResponse response = (GetChatResponse) object;
                if (response.getChat() != null) {
                    String myEmpCode = RetrofitClient.getStringUserPreference(this, Constants.EMP_CODE);
                    List<String> activeServerIds = new ArrayList<>();

                    for (GetChatResponse.ChatItem item : response.getChat()) {
                        if (item == null) continue;
                        String serverId = !TextUtils.isEmpty(item.getId()) ? item.getId() : "msg_" + (item.getCreatedAt() != null ? item.getCreatedAt().hashCode() : System.currentTimeMillis());
                        String rawSender = item.getSenderEmpCode();
                        String rawReceiver = item.getReceiverEmpCode();

                        String sender = !TextUtils.isEmpty(rawSender) ? rawSender : (!TextUtils.isEmpty(rawReceiver) ? ((myEmpCode != null && rawReceiver.trim().equalsIgnoreCase(myEmpCode.trim())) ? "EMPLOYEE" : myEmpCode) : "");
                        String receiver = !TextUtils.isEmpty(rawReceiver) ? rawReceiver : (!TextUtils.isEmpty(rawSender) ? ((myEmpCode != null && rawSender.trim().equalsIgnoreCase(myEmpCode.trim())) ? "EMPLOYEE" : myEmpCode) : "");

                        if (TextUtils.isEmpty(sender) || TextUtils.isEmpty(receiver)) {
                            continue;
                        }

                        activeServerIds.add(serverId);

                        boolean isSent = myEmpCode != null && myEmpCode.trim().equalsIgnoreCase(sender.trim());
                        int status = parseStatus(item.getMessageStatus());
                        long timestamp = parseTimestamp(item.getCreatedAt());
                        String formattedMessage = formatMediaMessage(item.getMessage(), item.getImageUrl());

                        if (!dbHelper.messageExists(serverId)) {
                            dbHelper.addMessage(
                                    sender,
                                    receiver,
                                    serverId,
                                    formattedMessage,
                                    isSent,
                                    timestamp,
                                    isSent || "1".equals(item.getReadUnread()),
                                    status
                            );
                        } else {
                            dbHelper.updateMessageStatus(serverId, status);
                        }

                        // Auto-register communication partner (sender or receiver) in SQLite chat_users if missing
                        String partnerCode = (myEmpCode != null && myEmpCode.trim().equalsIgnoreCase(sender.trim())) ? receiver : sender;
                        if (!TextUtils.isEmpty(partnerCode) && !partnerCode.trim().equalsIgnoreCase(myEmpCode != null ? myEmpCode.trim() : "")) {
                            if (dbHelper.getUser(partnerCode) == null) {
                                dbHelper.saveUser(partnerCode, "Employee " + partnerCode, "", "", "", "", "", "");
                            }
                        }
                    }

                    dbHelper.syncServerMessages(myEmpCode, "", activeServerIds);
                    updateChatUserList(lastLoadedEmpResponse);
                }
            }
        }

        Infrastructure.dismissProgressDialog();
    }

    private void updateChatUserList(EmployeeListResponse employeeListResponse) {
        try {
            Log.e(TAG, "activeOrderResponse: " + new GsonBuilder().create().toJson(employeeListResponse));
            searchProgressBar.setVisibility(View.GONE);

            if (employeeListResponse != null && employeeListResponse.getEmployee_list() != null && !employeeListResponse.getEmployee_list().isEmpty()) {
                this.lastLoadedEmpResponse = employeeListResponse;
            }

            String searchQuery = atSearch.getText().toString().trim();
            String myEmpCode = RetrofitClient.getStringUserPreference(this, Constants.EMP_CODE);

            if (searchQuery.isEmpty()) {
                // 1) NO SEARCH QUERY: Show chat history / users with message response
                List<EmployeeListResponse.EmployeeList> finalList = new ArrayList<>();
                List<String> addedCodes = new ArrayList<>();

                // A) Add all chat partners returned from server API (getChatHistoryUsers)
                List<EmployeeListResponse.EmployeeList> serverList = new ArrayList<>();
                if (lastLoadedEmpResponse != null && lastLoadedEmpResponse.getEmployee_list() != null) {
                    serverList.addAll(lastLoadedEmpResponse.getEmployee_list());
                } else if (employeeListResponse != null && employeeListResponse.getEmployee_list() != null) {
                    serverList.addAll(employeeListResponse.getEmployee_list());
                }

                for (EmployeeListResponse.EmployeeList emp : serverList) {
                    if (emp != null && emp.getEmp_code() != null) {
                        String codeUpper = emp.getEmp_code().trim().toUpperCase();
                        if (myEmpCode != null && codeUpper.equalsIgnoreCase(myEmpCode.trim())) {
                            continue;
                        }
                        if (!addedCodes.contains(codeUpper)) {
                            finalList.add(emp);
                            addedCodes.add(codeUpper);
                        }
                    }
                }

                // B) Add any additional communication partners from local SQLite database
                List<String> chatUserCodes = dbHelper.getChatUserCodes(myEmpCode);
                for (String code : chatUserCodes) {
                    if (code != null) {
                        String codeUpper = code.trim().toUpperCase();
                        if (myEmpCode != null && codeUpper.equalsIgnoreCase(myEmpCode.trim())) {
                            continue;
                        }
                        if (!addedCodes.contains(codeUpper)) {
                            EmployeeListResponse.EmployeeList localEmp = dbHelper.getUser(code);
                            if (localEmp != null && localEmp.getEmp_name() != null && !localEmp.getEmp_name().trim().isEmpty()) {
                                finalList.add(localEmp);
                            } else {
                                EmployeeListResponse.EmployeeList missingEmp = new EmployeeListResponse.EmployeeList();
                                missingEmp.setEmp_code(code);
                                missingEmp.setEmp_name("Employee " + code);
                                finalList.add(missingEmp);
                            }
                            addedCodes.add(codeUpper);
                        }
                    }
                }

                // C) Sort chat users by last message timestamp descending (most recent first)
                Collections.sort(finalList, (e1, e2) -> {
                    ChatMessage msg1 = dbHelper.getLastMessage(myEmpCode, e1.getEmp_code());
                    ChatMessage msg2 = dbHelper.getLastMessage(myEmpCode, e2.getEmp_code());
                    long t1 = msg1 != null ? msg1.getTimestamp() : parseTimestamp(e1.getLast_seen());
                    long t2 = msg2 != null ? msg2.getTimestamp() : parseTimestamp(e2.getLast_seen());
                    return Long.compare(t2, t1); // descending
                });

                if (finalList.isEmpty()) {
                    Toast.makeText(this, "No recent chats. Use search to find someone.", Toast.LENGTH_SHORT).show();
                }

                list.setLayoutManager(new LinearLayoutManager(this));
                ChatHistoryAdapter chatHistoryAdapter = new ChatHistoryAdapter(this, finalList);
                list.setAdapter(chatHistoryAdapter);
                list.setVisibility(View.VISIBLE);

            } else {
                // 2) SEARCH QUERY ACTIVE: Show employee list matching search query to start a new chat
                List<EmployeeListResponse.EmployeeList> searchResults =
                        (employeeListResponse != null && employeeListResponse.getEmployee_list() != null && !employeeListResponse.getEmployee_list().isEmpty())
                                ? employeeListResponse.getEmployee_list()
                                : dbHelper.searchUsers(searchQuery);

                list.setLayoutManager(new LinearLayoutManager(this));
                ChatHistoryAdapter searchAdapter = new ChatHistoryAdapter(this, searchResults);
                list.setAdapter(searchAdapter);
                list.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            ELog.e(TAG, "Exception in updateChatUserList", e);
        }
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

    private String formatMediaMessage(String messageText, String imageUrl) {
        String safeText = messageText != null ? messageText : "";
        if (TextUtils.isEmpty(imageUrl)) {
            return safeText;
        }
        String lowerUrl = imageUrl.trim().toLowerCase();
        if (lowerUrl.endsWith(".pdf") || lowerUrl.endsWith(".doc") || lowerUrl.endsWith(".docx") || lowerUrl.endsWith(".xlsx") || lowerUrl.endsWith(".xls") || lowerUrl.endsWith(".txt")) {
            if (!safeText.startsWith("[DOCUMENT]")) {
                String docName = (!safeText.isEmpty() && !safeText.equalsIgnoreCase("Image Attachment")) ? safeText : "Document.pdf";
                return "[DOCUMENT] " + imageUrl + "|" + docName;
            }
        } else if (lowerUrl.endsWith(".mp4") || lowerUrl.endsWith(".3gp") || lowerUrl.endsWith(".mkv")) {
            if (!safeText.startsWith("[VIDEO]")) {
                return "[VIDEO] " + imageUrl;
            }
        } else if (lowerUrl.endsWith(".mp3") || lowerUrl.endsWith(".m4a") || lowerUrl.endsWith(".aac") || lowerUrl.endsWith(".wav")) {
            if (!safeText.startsWith("[VOICE]")) {
                return "[VOICE] " + imageUrl;
            }
        } else {
            if (!safeText.startsWith("[IMAGE]")) {
                return "[IMAGE] " + imageUrl;
            }
        }
        return safeText;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContextCompat.registerReceiver(this, chatReceiver,
                new IntentFilter(MyMessagingService.CHAT_BROADCAST),
                ContextCompat.RECEIVER_NOT_EXPORTED);
        fetchChatData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        searchProgressBar.setVisibility(View.GONE);
        if (swiptoRefresh != null) {
            swiptoRefresh.setRefreshing(false);
        }
        Infrastructure.dismissProgressDialog();
        try {
            unregisterReceiver(chatReceiver);
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchProgressBar.setVisibility(View.GONE);
        if (swiptoRefresh != null) {
            swiptoRefresh.setRefreshing(false);
        }
        Infrastructure.dismissProgressDialog();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }
}
