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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import lombok.Value;
import lombok.extern.java.Log;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import org.qi4j.api.type.ValueType;
import org.qi4j.api.value.ValueSerializer.Options;
import org.qi4j.functional.Function;

/**
 *
 * @author alfio
 */
@Log
public class GsonValueSerializationTest {

    private final GsonValueSerialization valueSerialization = new GsonValueSerialization(new GsonProvider.DefaultGsonProviderMixin());

    @Value
    static class Data {

        String foo;

        String bar;

    }

    @Test
    public void testSerialize() {
        final Function<Data, String> fxn = valueSerialization.serialize();
        final Data data = new Data("foo", "bar");
        final String json = fxn.map(data);
        log.info(String.format("json: %s", json));
        final Data data2 = valueSerialization.deserialize(Data.class).map(json);
        log.info(String.format("data: %s", data));
        log.info(String.format("data2: %s", data2));
        assertThat(data, is(data2));
    }

    @Test
    public void testSerialize_ValueSerializerOptions() {
        final Function<Data, String> fxn = valueSerialization.serialize(new Options());
        final Data data = new Data("foo", "bar");
        final String json = fxn.map(data);
        log.info(String.format("json: %s", json));
        final Data data2 = valueSerialization.deserialize(Data.class).map(json);
        log.info(String.format("data: %s", data));
        log.info(String.format("data2: %s", data2));
        assertThat(data, is(data2));
    }

    @Test
    public void testSerialize_includeTypeInfo_true() {
        final Function<Data, String> fxn = valueSerialization.serialize(true);
        final Data data = new Data("foo", "bar");
        final String json = fxn.map(data);
        log.info(String.format("json: %s", json));
        final Data data2 = valueSerialization.deserialize(Data.class).map(json);
        log.info(String.format("data: %s", data));
        log.info(String.format("data2: %s", data2));
        assertThat(data, is(data2));
    }

    @Test
    public void testSerialize_includeTypeInfo_false() {
        final Function<Data, String> fxn = valueSerialization.serialize(false);
        final Data data = new Data("foo", "bar");
        final String json = fxn.map(data);
        log.info(String.format("json: %s", json));
        final Data data2 = valueSerialization.deserialize(Data.class).map(json);
        log.info(String.format("data: %s", data));
        log.info(String.format("data2: %s", data2));
        assertThat(data, is(data2));
    }

    /**
     * Test of serialize method, of class GsonValueSerialization.
     */
    @Test
    public void testSerialize_Object() {
        final Data data = new Data("foo", "bar");
        final String json = valueSerialization.serialize(data);
        log.info(String.format("json: %s", json));
        final Data data2 = valueSerialization.deserialize(Data.class).map(json);
        log.info(String.format("data: %s", data));
        log.info(String.format("data2: %s", data2));
        assertThat(data, is(data2));
    }

    /**
     * Test of serialize method, of class GsonValueSerialization.
     */
    @Test
    public void testSerialize_ValueSerializerOptions_Object() {
        final Data data = new Data("foo", "bar");
        final String json = valueSerialization.serialize(new Options(), data);
        log.info(String.format("json: %s", json));
        final Data data2 = valueSerialization.deserialize(Data.class).map(json);
        log.info(String.format("data: %s", data));
        log.info(String.format("data2: %s", data2));
        assertThat(data, is(data2));
    }

    @Test
    public void testSerialize_object_includeTypeInfo_true() {
        final Data data = new Data("foo", "bar");
        final String json = valueSerialization.serialize(data, true);
        log.info(String.format("json: %s", json));
        final Data data2 = valueSerialization.deserialize(Data.class).map(json);
        log.info(String.format("data: %s", data));
        log.info(String.format("data2: %s", data2));
        assertThat(data, is(data2));
    }

    @Test
    public void testSerialize_object_includeTypeInfo_false() {
        final Data data = new Data("foo", "bar");
        final String json = valueSerialization.serialize(data, false);
        log.info(String.format("json: %s", json));
        final Data data2 = valueSerialization.deserialize(Data.class).map(json);
        log.info(String.format("data: %s", data));
        log.info(String.format("data2: %s", data2));
        assertThat(data, is(data2));
    }

    @Test
    public void testSerialize_Object_OutputStream() throws UnsupportedEncodingException {
        final Data data = new Data("foo", "bar");
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        valueSerialization.serialize(data, bos);
        log.info(String.format("json: %s", bos.toString(StandardCharsets.UTF_8.name())));
        final Data data2 = valueSerialization.deserialize(Data.class, new ByteArrayInputStream(bos.toByteArray()));
        log.info(String.format("data: %s", data));
        log.info(String.format("data2: %s", data2));
        assertThat(data, is(data2));
    }

    @Test
    public void testSerialize_Object_OutputStream_includeTypeInfo_true() throws UnsupportedEncodingException {
        final Data data = new Data("foo", "bar");
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        valueSerialization.serialize(data, bos, true);
        log.info(String.format("json: %s", bos.toString(StandardCharsets.UTF_8.name())));
        final Data data2 = valueSerialization.deserialize(Data.class, new ByteArrayInputStream(bos.toByteArray()));
        log.info(String.format("data: %s", data));
        log.info(String.format("data2: %s", data2));
        assertThat(data, is(data2));
    }

    @Test
    public void testSerialize_Object_OutputStream_includeTypeInfo_false() throws UnsupportedEncodingException {
        final Data data = new Data("foo", "bar");
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        valueSerialization.serialize(data, bos, false);
        log.info(String.format("json: %s", bos.toString(StandardCharsets.UTF_8.name())));
        final Data data2 = valueSerialization.deserialize(Data.class, new ByteArrayInputStream(bos.toByteArray()));
        log.info(String.format("data: %s", data));
        log.info(String.format("data2: %s", data2));
        assertThat(data, is(data2));
    }

    /**
     * Test of serialize method, of class GsonValueSerialization.
     */
    @Test
    public void testSerialize_3args_2() throws UnsupportedEncodingException {
        final Data data = new Data("foo", "bar");
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        valueSerialization.serialize(new Options(), data, bos);
        log.info(String.format("json: %s", bos.toString(StandardCharsets.UTF_8.name())));
        final Data data2 = valueSerialization.deserialize(Data.class, new ByteArrayInputStream(bos.toByteArray()));
        log.info(String.format("data: %s", data));
        log.info(String.format("data2: %s", data2));
        assertThat(data, is(data2));
    }

    @Test
    public void testDeserialize_ValueType() {
        final Integer data = 10;
        final String json = valueSerialization.serialize(data);
        log.info(String.format("json: %s", json));
        final Function<String, Integer> fxn = valueSerialization.deserialize(new ValueType(Integer.class));
        final Integer data2 = fxn.map(json);
        log.info(String.format("data: %s", data));
        log.info(String.format("data2: %s", data2));
        assertThat(data, is(data2));
    }

    @Test
    public void testDeserialize_ValueType_String() {
        final Integer data = 10;
        final String json = valueSerialization.serialize(data);
        log.info(String.format("json: %s", json));
        final Integer data2 = valueSerialization.deserialize(new ValueType(Integer.class), json);
        log.info(String.format("data: %s", data));
        log.info(String.format("data2: %s", data2));
        assertThat(data, is(data2));
    }

    /**
     * Test of deserialize method, of class GsonValueSerialization.
     */
    @Test
    public void testDeserialize_ValueType_InputStream() throws UnsupportedEncodingException {
        final Integer data = 10;
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        valueSerialization.serialize(new Options(), data, bos);
        log.info(String.format("json: %s", bos.toString(StandardCharsets.UTF_8.name())));
        final Integer data2 = valueSerialization.deserialize(new ValueType(Integer.class), new ByteArrayInputStream(bos.toByteArray()));
        log.info(String.format("data: %s", data));
        log.info(String.format("data2: %s", data2));
        assertThat(data, is(data2));
    }

}
