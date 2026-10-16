package com.arcltd.staff.activity.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.otherAndMain.PdfViewerReaderActivity;
import com.arcltd.staff.web_view.VideoWebViewActivity;
import com.bumptech.glide.Glide;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;
    private static final int VIEW_TYPE_DATE_HEADER = 3;

    private final List<ChatMessage> chatMessages;
    private final OnMessageLongClickListener longClickListener;

    private static MediaPlayer activeMediaPlayer = null;
    private static ImageButton activePlayButton = null;
    private static Handler audioHandler = new Handler(Looper.getMainLooper());
    private static Runnable audioProgressRunnable = null;

    private final SimpleDateFormat timeFormat =
            new SimpleDateFormat("hh:mm a", Locale.getDefault());

    public interface OnMessageLongClickListener {
        void onMessageLongClick(ChatMessage message, int position);
    }

    public ChatAdapter(
            List<ChatMessage> chatMessages,
            OnMessageLongClickListener longClickListener) {

        this.chatMessages = chatMessages;
        this.longClickListener = longClickListener;
    }

    @Override
    public int getItemViewType(int position) {

        ChatMessage message = chatMessages.get(position);

        if (message.getId() != null &&
                message.getId().startsWith("date_header_")) {

            return VIEW_TYPE_DATE_HEADER;
        }

        return message.isSent()
                ? VIEW_TYPE_SENT
                : VIEW_TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        LayoutInflater inflater =
                LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_DATE_HEADER) {

            View view = inflater.inflate(
                    R.layout.item_chat_date_header,
                    parent,
                    false);

            return new DateHeaderHolder(view);

        } else if (viewType == VIEW_TYPE_SENT) {

            View view = inflater.inflate(
                    R.layout.item_chat_sent,
                    parent,
                    false);

            return new SentMessageHolder(view);

        } else {

            View view = inflater.inflate(
                    R.layout.item_chat_received,
                    parent,
                    false);

            return new ReceivedMessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder holder,
            int position) {

        ChatMessage message = chatMessages.get(position);

        if (holder instanceof SentMessageHolder) {

            ((SentMessageHolder) holder).bind(
                    message,
                    longClickListener,
                    timeFormat);

        } else if (holder instanceof ReceivedMessageHolder) {

            ((ReceivedMessageHolder) holder).bind(
                    message,
                    longClickListener,
                    timeFormat);

        } else if (holder instanceof DateHeaderHolder) {

            ((DateHeaderHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages == null ? 0 : chatMessages.size();
    }

    static class DateHeaderHolder
            extends RecyclerView.ViewHolder {

        TextView textDate;

        DateHeaderHolder(View itemView) {
            super(itemView);
            textDate = itemView.findViewById(R.id.text_date);
        }

        void bind(ChatMessage message) {
            textDate.setText(message.getMessage());
        }
    }

    static class SentMessageHolder
            extends RecyclerView.ViewHolder {

        TextView messageText;
        TextView timeText;
        ImageView imgStatus;

        FrameLayout layoutImageContainer;
        FrameLayout layoutUploadOverlay;
        FrameLayout layoutDocContainer;
        ProgressBar progressDocUpload;
        ProgressBar progressVideoUpload;
        ProgressBar progressVoiceUpload;

        ImageView imgChatImage;
        LinearLayout layoutDoc;
        TextView tvDocName;
        FrameLayout layoutVideo;
        LinearLayout layoutVoice;
        ImageButton btnVoicePlay;
        SeekBar seekVoice;
        TextView tvVoiceDuration;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.text_message);
            timeText = itemView.findViewById(R.id.text_timestamp);
            imgStatus = itemView.findViewById(R.id.img_status);

            layoutImageContainer = itemView.findViewById(R.id.layout_image_container);
            layoutUploadOverlay = itemView.findViewById(R.id.layout_upload_overlay);
            layoutDocContainer = itemView.findViewById(R.id.layout_doc_container);
            progressDocUpload = itemView.findViewById(R.id.progress_doc_upload);
            progressVideoUpload = itemView.findViewById(R.id.progress_video_upload);
            progressVoiceUpload = itemView.findViewById(R.id.progress_voice_upload);

            imgChatImage = itemView.findViewById(R.id.img_chat_image);
            layoutDoc = itemView.findViewById(R.id.layout_doc);
            tvDocName = itemView.findViewById(R.id.tv_doc_name);
            layoutVideo = itemView.findViewById(R.id.layout_video);
            layoutVoice = itemView.findViewById(R.id.layout_voice);
            btnVoicePlay = itemView.findViewById(R.id.btn_voice_play);
            seekVoice = itemView.findViewById(R.id.seek_voice);
            tvVoiceDuration = itemView.findViewById(R.id.tv_voice_duration);
        }

        void bind(
                ChatMessage message,
                OnMessageLongClickListener listener,
                SimpleDateFormat timeFormat) {

            bindMediaContent(message, messageText, layoutImageContainer, imgChatImage, layoutDocContainer, layoutDoc, tvDocName, layoutVideo, layoutVoice, btnVoicePlay, seekVoice, tvVoiceDuration);

            boolean isUploading = (message.getId() != null && message.getId().startsWith("temp_")) || message.getStatus() == 0;

            if (layoutUploadOverlay != null) {
                layoutUploadOverlay.setVisibility(isUploading && message.isImage() ? View.VISIBLE : View.GONE);
            }
            if (progressDocUpload != null) {
                progressDocUpload.setVisibility(isUploading && message.isDocument() ? View.VISIBLE : View.GONE);
            }
            if (progressVideoUpload != null) {
                progressVideoUpload.setVisibility(isUploading && message.isVideo() ? View.VISIBLE : View.GONE);
            }
            if (progressVoiceUpload != null) {
                progressVoiceUpload.setVisibility(isUploading && message.isVoice() ? View.VISIBLE : View.GONE);
            }

            timeText.setText(
                    timeFormat.format(
                            new Date(message.getTimestamp())
                    )
            );

            updateStatus(message.getStatus());

            itemView.setOnLongClickListener(v -> {

                if (listener != null) {
                    listener.onMessageLongClick(
                            message,
                            getBindingAdapterPosition());
                }

                return true;
            });
        }

        private void updateStatus(int status) {

            if (imgStatus == null) return;

            imgStatus.clearColorFilter();

            switch (status) {

                case 2:
                    // Seen (Blue Double Tick)
                    imgStatus.setImageResource(
                            R.drawable.ic_double_tick);

                    imgStatus.setColorFilter(
                            Color.parseColor("#34B7F1"));
                    break;

                case 1:
                    // Delivered / Sent to Server (Grey Double Tick)
                    imgStatus.setImageResource(
                            R.drawable.ic_double_tick);

                    imgStatus.setColorFilter(
                            Color.parseColor("#757575"));
                    break;

                default:
                    // Pending / Uploading (Clock Icon)
                    imgStatus.setImageResource(
                            R.drawable.ic_clock);

                    imgStatus.setColorFilter(
                            Color.parseColor("#757575"));
                    break;
            }
        }
    }

    static class ReceivedMessageHolder
            extends RecyclerView.ViewHolder {

        TextView messageText;
        TextView timeText;

        ImageView imgChatImage;
        LinearLayout layoutDoc;
        TextView tvDocName;
        FrameLayout layoutVideo;
        LinearLayout layoutVoice;
        ImageButton btnVoicePlay;
        SeekBar seekVoice;
        TextView tvVoiceDuration;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.text_message);
            timeText = itemView.findViewById(R.id.text_timestamp);

            imgChatImage = itemView.findViewById(R.id.img_chat_image);
            layoutDoc = itemView.findViewById(R.id.layout_doc);
            tvDocName = itemView.findViewById(R.id.tv_doc_name);
            layoutVideo = itemView.findViewById(R.id.layout_video);
            layoutVoice = itemView.findViewById(R.id.layout_voice);
            btnVoicePlay = itemView.findViewById(R.id.btn_voice_play);
            seekVoice = itemView.findViewById(R.id.seek_voice);
            tvVoiceDuration = itemView.findViewById(R.id.tv_voice_duration);
        }

        void bind(
                ChatMessage message,
                OnMessageLongClickListener listener,
                SimpleDateFormat timeFormat) {

            bindMediaContent(message, messageText, null, imgChatImage, null, layoutDoc, tvDocName, layoutVideo, layoutVoice, btnVoicePlay, seekVoice, tvVoiceDuration);

            timeText.setText(
                    timeFormat.format(
                            new Date(message.getTimestamp())
                    )
            );

            itemView.setOnLongClickListener(v -> {

                if (listener != null) {
                    listener.onMessageLongClick(
                            message,
                            getBindingAdapterPosition());
                }

                return true;
            });
        }
    }

    private static void bindMediaContent(
            ChatMessage message,
            TextView messageText,
            FrameLayout layoutImageContainer,
            ImageView imgChatImage,
            FrameLayout layoutDocContainer,
            LinearLayout layoutDoc,
            TextView tvDocName,
            FrameLayout layoutVideo,
            LinearLayout layoutVoice,
            ImageButton btnVoicePlay,
            SeekBar seekVoice,
            TextView tvVoiceDuration) {

        Context context = messageText.getContext();

        if (message.isImage()) {

            messageText.setVisibility(View.GONE);
            if (layoutDocContainer != null) layoutDocContainer.setVisibility(View.GONE);
            else if (layoutDoc != null) layoutDoc.setVisibility(View.GONE);
            if (layoutVideo != null) layoutVideo.setVisibility(View.GONE);
            if (layoutVoice != null) layoutVoice.setVisibility(View.GONE);

            if (layoutImageContainer != null) layoutImageContainer.setVisibility(View.VISIBLE);
            if (imgChatImage != null) {
                imgChatImage.setVisibility(View.VISIBLE);

                String mediaUrl = message.getMediaUrl();
                Object loadTarget = mediaUrl;
                if (!TextUtils.isEmpty(mediaUrl)) {
                    if (mediaUrl.startsWith("/")) {
                        File localFile = new File(mediaUrl);
                        if (localFile.exists()) {
                            loadTarget = localFile;
                        }
                    } else if (mediaUrl.startsWith("content://") || mediaUrl.startsWith("file://")) {
                        loadTarget = Uri.parse(mediaUrl);
                    }
                }

                Glide.with(context)
                        .load(loadTarget)
                        .placeholder(R.drawable.arcround)
                        .error(R.drawable.arcround)
                        .into(imgChatImage);

                imgChatImage.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(context, FullScreenImageActivity.class);
                        intent.putExtra("imageUrl", message.getMediaUrl());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(context, "Cannot open image", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } else if (message.isDocument()) {

            messageText.setVisibility(View.GONE);
            if (layoutImageContainer != null) layoutImageContainer.setVisibility(View.GONE);
            else if (imgChatImage != null) imgChatImage.setVisibility(View.GONE);
            if (layoutVideo != null) layoutVideo.setVisibility(View.GONE);
            if (layoutVoice != null) layoutVoice.setVisibility(View.GONE);

            if (layoutDocContainer != null) layoutDocContainer.setVisibility(View.VISIBLE);
            if (layoutDoc != null) {
                layoutDoc.setVisibility(View.VISIBLE);
                if (tvDocName != null) {
                    tvDocName.setText(message.getDocumentName());
                }
                layoutDoc.setOnClickListener(v -> openDocumentInInstalledApp(context, message.getMediaUrl()));
            }

        } else if (message.isVideo()) {

            messageText.setVisibility(View.GONE);
            if (layoutImageContainer != null) layoutImageContainer.setVisibility(View.GONE);
            else if (imgChatImage != null) imgChatImage.setVisibility(View.GONE);
            if (layoutDocContainer != null) layoutDocContainer.setVisibility(View.GONE);
            else if (layoutDoc != null) layoutDoc.setVisibility(View.GONE);
            if (layoutVoice != null) layoutVoice.setVisibility(View.GONE);

            if (layoutVideo != null) {
                layoutVideo.setVisibility(View.VISIBLE);
                layoutVideo.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(context, VideoWebViewActivity.class);
                        intent.putExtra("url", message.getMediaUrl());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(context, "Cannot open video", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } else if (message.isVoice()) {

            messageText.setVisibility(View.GONE);
            if (layoutImageContainer != null) layoutImageContainer.setVisibility(View.GONE);
            else if (imgChatImage != null) imgChatImage.setVisibility(View.GONE);
            if (layoutDocContainer != null) layoutDocContainer.setVisibility(View.GONE);
            else if (layoutDoc != null) layoutDoc.setVisibility(View.GONE);
            if (layoutVideo != null) layoutVideo.setVisibility(View.GONE);

            if (layoutVoice != null) {
                layoutVoice.setVisibility(View.VISIBLE);
                if (btnVoicePlay != null) {
                    btnVoicePlay.setOnClickListener(v -> playVoiceNote(context, message.getMediaUrl(), btnVoicePlay, seekVoice, tvVoiceDuration));
                }
            }

        } else {

            if (layoutImageContainer != null) layoutImageContainer.setVisibility(View.GONE);
            else if (imgChatImage != null) imgChatImage.setVisibility(View.GONE);
            if (layoutDocContainer != null) layoutDocContainer.setVisibility(View.GONE);
            else if (layoutDoc != null) layoutDoc.setVisibility(View.GONE);
            if (layoutVideo != null) layoutVideo.setVisibility(View.GONE);
            if (layoutVoice != null) layoutVoice.setVisibility(View.GONE);

            messageText.setVisibility(View.VISIBLE);
            messageText.setText(message.getMessage());
        }
    }

    private static void openDocumentInInstalledApp(Context context, String mediaUrl) {
        if (context == null || TextUtils.isEmpty(mediaUrl)) {
            Toast.makeText(context, "Invalid document link", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String url = mediaUrl.trim();
            Uri uri = Uri.parse(url);

            Intent intent = new Intent(Intent.ACTION_VIEW);

            if ("file".equalsIgnoreCase(uri.getScheme()) && uri.getPath() != null) {
                File file = new File(uri.getPath());
                if (file.exists()) {
                    uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
                }
            }

            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);

            Intent chooser = Intent.createChooser(intent, "Open PDF with");
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(chooser);

        } catch (Exception e) {
            try {
                String url = mediaUrl.trim();
                if (url.startsWith("http://") || url.startsWith("https://") || url.toLowerCase().endsWith(".pdf")) {
                    Intent pdfIntent = new Intent(context, PdfViewerReaderActivity.class);
                    pdfIntent.putExtra("link", url);
                    pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(pdfIntent);
                } else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(browserIntent);
                }
            } catch (Exception ex) {
                Toast.makeText(context, "Cannot open PDF file", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static void playVoiceNote(Context context, String mediaUrl, ImageButton btnPlay, SeekBar seekVoice, TextView tvVoiceDuration) {
        try {
            if (activeMediaPlayer != null) {
                activeMediaPlayer.stop();
                activeMediaPlayer.release();
                activeMediaPlayer = null;
                if (audioProgressRunnable != null) {
                    audioHandler.removeCallbacks(audioProgressRunnable);
                    audioProgressRunnable = null;
                }
                if (activePlayButton != null) {
                    activePlayButton.setImageResource(android.R.drawable.ic_media_play);
                }
            }

            activeMediaPlayer = new MediaPlayer();
            activeMediaPlayer.setDataSource(context, Uri.parse(mediaUrl));
            activeMediaPlayer.prepare();
            activeMediaPlayer.start();
            activePlayButton = btnPlay;
            btnPlay.setImageResource(android.R.drawable.ic_media_pause);

            if (seekVoice != null) {
                seekVoice.setMax(activeMediaPlayer.getDuration());
                seekVoice.setProgress(0);
                seekVoice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser && activeMediaPlayer != null) {
                            activeMediaPlayer.seekTo(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                });
            }

            audioProgressRunnable = new Runnable() {
                @Override
                public void run() {
                    if (activeMediaPlayer != null && activeMediaPlayer.isPlaying()) {
                        int current = activeMediaPlayer.getCurrentPosition();
                        int total = activeMediaPlayer.getDuration();
                        if (seekVoice != null) seekVoice.setProgress(current);
                        if (tvVoiceDuration != null) tvVoiceDuration.setText(formatTime(current) + " / " + formatTime(total));
                        audioHandler.postDelayed(this, 100);
                    }
                }
            };
            audioHandler.post(audioProgressRunnable);

            activeMediaPlayer.setOnCompletionListener(mp -> {
                btnPlay.setImageResource(android.R.drawable.ic_media_play);
                if (seekVoice != null) seekVoice.setProgress(0);
                if (tvVoiceDuration != null && activeMediaPlayer != null) {
                    tvVoiceDuration.setText(formatTime(activeMediaPlayer.getDuration()));
                }
                if (audioProgressRunnable != null) {
                    audioHandler.removeCallbacks(audioProgressRunnable);
                    audioProgressRunnable = null;
                }
                if (activeMediaPlayer != null) {
                    activeMediaPlayer.release();
                    activeMediaPlayer = null;
                }
                activePlayButton = null;
            });

        } catch (Exception e) {
            Toast.makeText(context, "Cannot play audio", Toast.LENGTH_SHORT).show();
            if (btnPlay != null) {
                btnPlay.setImageResource(android.R.drawable.ic_media_play);
            }
        }
    }

    private static String formatTime(int millis) {
        int seconds = (millis / 1000) % 60;
        int minutes = (millis / (1000 * 60)) % 60;
        return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds);
    }
}