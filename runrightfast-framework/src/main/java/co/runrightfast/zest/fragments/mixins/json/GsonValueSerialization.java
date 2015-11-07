/*
 Copyright 2015 Alfio Zappala

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package co.runrightfast.zest.fragments.mixins.json;

import co.runrightfast.zest.composites.services.json.GsonProvider;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import lombok.NonNull;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.type.ValueType;
import org.qi4j.api.value.ValueSerialization;
import org.qi4j.api.value.ValueSerializationException;
import org.qi4j.functional.Function;
import org.qi4j.functional.Function2;

/**
 *
 * @author alfio
 */
public class GsonValueSerialization implements ValueSerialization {

    private final Gson gson;

    public GsonValueSerialization(@NonNull @Service final GsonProvider gsonProvider) {
        this.gson = gsonProvider.gson();
    }

    @Override
    public <T> Function<T, String> serialize() {
        return gson::toJson;
    }

    @Override
    public <T> Function<T, String> serialize(final Options options) {
        return gson::toJson;
    }

    @Override
    public <T> Function<T, String> serialize(final boolean includeTypeInfo) {
        return gson::toJson;
    }

    @Override
    public String serialize(@NonNull final Object object) throws ValueSerializationException {
        return gson.toJson(object);
    }

    @Override
    public String serialize(final Options options, final Object object) throws ValueSerializationException {
        return serialize(object);
    }

    @Override
    public String serialize(@NonNull final Object object, final boolean includeTypeInfo) throws ValueSerializationException {
        return serialize(object);
    }

    @Override
    public void serialize(@NonNull final Object object, @NonNull final OutputStream out) throws ValueSerializationException {
        try (final JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8))) {
            gson.toJson(object, object.getClass(), writer);
        } catch (final IOException ex) {
            throw new ValueSerializationException(ex);
        }
    }

    @Override
    public void serialize(final Options options, @NonNull final Object object, @NonNull final OutputStream out) throws ValueSerializationException {
        serialize(object, out);
    }

    @Override
    public void serialize(@NonNull final Object object, @NonNull final OutputStream out, final boolean includeTypeInfo) throws ValueSerializationException {
        serialize(object, out);
    }

    @Override
    public <T> Function<String, T> deserialize(@NonNull final Class<T> type) {
        return json -> gson.fromJson(json, type);
    }

    @Override
    public <T> Function<String, T> deserialize(@NonNull final ValueType valueType) {
        return json -> (T) gson.fromJson(json, valueType.mainType());
    }

    @Override
    public <T> Function2<ValueType, String, T> deserialize() {
        return (valueType, json) -> (T) gson.fromJson(json, valueType.mainType());
    }

    @Override
    public <T> T deserialize(final Class<?> type, final String json) throws ValueSerializationException {
        return (T) gson.fromJson(json, type);
    }

    @Override
    public <T> T deserialize(final ValueType valueType, final String json) throws ValueSerializationException {
        return (T) gson.fromJson(json, valueType.mainType());
    }

    @Override
    public <T> T deserialize(final Class<?> type, final InputStream in) throws ValueSerializationException {
        try (final JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return gson.fromJson(reader, type);
        } catch (final IOException e) {
            throw new ValueSerializationException(e);
        }
    }

    @Override
    public <T> T deserialize(final ValueType valueType, InputStream in) throws ValueSerializationException {
        try (final JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return gson.fromJson(reader, valueType.mainType());
        } catch (final IOException e) {
            throw new ValueSerializationException(e);
        }
    }

}
