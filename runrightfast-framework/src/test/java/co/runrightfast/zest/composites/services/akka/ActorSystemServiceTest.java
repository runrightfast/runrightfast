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
package co.runrightfast.zest.composites.services.akka;

import static co.runrightfast.zest.assemblers.akka.AkkaAssemblers.assembleActorSystem;
import com.typesafe.config.ConfigFactory;
import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.test.AbstractQi4jTest;

/**
 *
 * @author alfio
 */
@Slf4j
public class ActorSystemServiceTest extends AbstractQi4jTest {

    static {
        ConfigFactory.invalidateCaches();
        System.setProperty("config.resource", String.format("/%s.conf", ActorSystemServiceTest.class.getSimpleName()));
    }

    @Test
    public void testActorSystem() {
        final ActorSystemService service = this.module.findService(ActorSystemService.class).get();
        assertThat(service, is(notNullValue()));
        assertThat(service.actorSystem(), is(notNullValue()));
    }

    @Override
    public void assemble(final ModuleAssembly assembly) throws AssemblyException {
        assembleActorSystem(assembly);
    }

}
