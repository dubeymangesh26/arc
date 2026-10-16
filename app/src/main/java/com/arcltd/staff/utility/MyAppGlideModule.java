package com.arcltd.staff.utility;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

@GlideModule
public final class MyAppGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);
        builder.setLogLevel(Log.ERROR);
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        // We don't want to decode videos for profile pictures, 
        // and sometimes VideoDecoder crashes with setDataSourceCallback failed: status = 0x80000000
        // on some devices when it misidentifies data.
        super.registerComponents(context, glide, registry);
    }
}
