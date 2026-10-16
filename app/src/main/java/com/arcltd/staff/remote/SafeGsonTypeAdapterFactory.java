package com.arcltd.staff.remote;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

public class SafeGsonTypeAdapterFactory implements TypeAdapterFactory {

    private static final ThreadLocal<Integer> DEPTH = ThreadLocal.withInitial(() -> 0);

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        final Class<? super T> rawType = type.getRawType();

        // Only intercept POJO / Model classes (non-primitive, non-string, non-collection, non-map)
        if (rawType.isPrimitive()
                || String.class.isAssignableFrom(rawType)
                || Number.class.isAssignableFrom(rawType)
                || Boolean.class.isAssignableFrom(rawType)
                || Character.class.isAssignableFrom(rawType)
                || Collection.class.isAssignableFrom(rawType)
                || Map.class.isAssignableFrom(rawType)) {
            return null;
        }

        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);

        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            @Override
            public T read(JsonReader in) throws IOException {
                in.setLenient(true);
                Integer depthVal = DEPTH.get();
                int currentDepth = depthVal != null ? depthVal : 0;
                DEPTH.set(currentDepth + 1);

                T result;
                try {
                    JsonToken peek;
                    try {
                        peek = in.peek();
                    } catch (EOFException e) {
                        return createFallbackObject(rawType, "Empty Response");
                    } catch (Exception e) {
                        return createFallbackObject(rawType, e.getMessage());
                    }

                    if (peek == JsonToken.NULL) {
                        in.nextNull();
                        result = null;
                    } else if (peek == JsonToken.BEGIN_OBJECT) {
                        try {
                            result = delegate.read(in);
                        } catch (Exception e) {
                            result = createFallbackObject(rawType, e.getMessage());
                        }
                    } else if (peek == JsonToken.STRING) {
                        String str = in.nextString();
                        if (str == null || str.trim().isEmpty()) {
                            result = createFallbackObject(rawType, "No Data Found");
                        } else {
                            str = str.trim();
                            if (str.startsWith("{") && str.endsWith("}")) {
                                try {
                                    result = gson.fromJson(str, type.getType());
                                } catch (Exception e) {
                                    result = createFallbackObject(rawType, str);
                                }
                            } else {
                                result = createFallbackObject(rawType, str);
                            }
                        }
                    } else {
                        // If BEGIN_ARRAY, NUMBER, BOOLEAN, etc. received when expecting OBJECT
                        try {
                            in.skipValue();
                        } catch (Exception ignored) {
                        }
                        result = createFallbackObject(rawType, "No Data Found");
                    }

                    // Only drain reader at the top-level (root object) call
                    if (currentDepth == 0) {
                        drainJsonReader(in);
                    }
                } finally {
                    DEPTH.set(currentDepth);
                }

                return result;
            }
        };
    }

    private void drainJsonReader(JsonReader in) {
        try {
            in.setLenient(true);
            while (in.peek() != JsonToken.END_DOCUMENT) {
                in.skipValue();
            }
        } catch (Exception ignored) {
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T createFallbackObject(Class<? super T> rawType, String message) {
        try {
            T instance = (T) rawType.getDeclaredConstructor().newInstance();
            setFieldOrMethod(instance, rawType, "responseCode", "404");
            setFieldOrMethod(instance, rawType, "ResponseCode", "404");
            setFieldOrMethod(instance, rawType, "responsecode", "404");
            setFieldOrMethod(instance, rawType, "message", message != null ? message : "No Data Found");
            setFieldOrMethod(instance, rawType, "Message", message != null ? message : "No Data Found");
            return instance;
        } catch (Exception e) {
            return null;
        }
    }

    private void setFieldOrMethod(Object instance, Class<?> rawType, String name, String value) {
        Class<?> current = rawType;
        while (current != null && current != Object.class) {
            try {
                Field field = current.getDeclaredField(name);
                field.setAccessible(true);
                field.set(instance, value);
                return;
            } catch (NoSuchFieldException ignored) {
            } catch (Exception e) {
                break;
            }

            String setterName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
            try {
                Method method = current.getDeclaredMethod(setterName, String.class);
                method.setAccessible(true);
                method.invoke(instance, value);
                return;
            } catch (NoSuchMethodException ignored) {
            } catch (Exception e) {
                break;
            }

            current = current.getSuperclass();
        }
    }
}
