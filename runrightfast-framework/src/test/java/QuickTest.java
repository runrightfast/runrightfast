
import co.runrightfast.app.events.Event;
import co.runrightfast.app.events.EventLevel;
import static co.runrightfast.app.events.EventLevel.ERROR;
import static co.runrightfast.app.events.EventLevel.INFO;
import com.google.common.collect.ImmutableSet;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import lombok.extern.java.Log;
import org.apache.commons.lang3.reflect.TypeLiteral;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.apache.commons.lang3.reflect.Typed;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

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
/**
 *
 * @author alfio
 */
@Log
public class QuickTest {

    static enum AppEvents implements Event {

        STARTED(INFO),
        STOPPED(INFO),
        FAILED(ERROR);

        private final EventLevel level;

        private AppEvents(EventLevel level) {
            this.level = level;
        }

        @Override
        public EventLevel eventLevel() {
            return level;
        }
    }

    @Test
    public void testURI() throws URISyntaxException {
        final URI uri = new URI("/runrightfast.co/heartbeat/1/0");

        log.log(Level.INFO, "uri: {0}", uri);

        assertThat(uri, is(new URI("/runrightfast.co/heartbeat//1/0").normalize()));
        assertThat(uri, is(not(new URI("/runrightfast.co/heartbeat/1/1"))));

        log.info(String.format("reolved URI : %s", new URI("/runrightfast.co/").resolve("event-logging-service/").resolve(new URI("1/0"))));
    }

    @Test
    public void testTyped() {
        final Typed stringList = new TypeLiteral<List<String>>() {
        };

        final Typed stringArrayList = new TypeLiteral<ArrayList<String>>() {
        };

        log.info(String.format("stringList : %s", stringList));
        log.info(String.format("stringList.getType() : %s", stringList.getType()));

        assertThat(TypeUtils.isAssignable(stringArrayList.getType(), stringList.getType()), is(true));
        assertThat(TypeUtils.isAssignable(stringList.getType(), stringArrayList.getType()), is(false));
        assertThat(TypeUtils.isAssignable(stringList.getType(), stringArrayList.getType()), is(false));
    }

    @Test
    public void testAppEvents() {
        final Set<Event> events = ImmutableSet.copyOf(AppEvents.values());
        events.stream().forEach(event -> log.info(String.format("%s -> %s", event.name(), event.eventLevel())));
    }

}
