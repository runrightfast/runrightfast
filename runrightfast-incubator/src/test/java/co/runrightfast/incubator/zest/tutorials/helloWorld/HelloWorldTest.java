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
package co.runrightfast.incubator.zest.tutorials.helloWorld;

import lombok.extern.java.Log;
import org.junit.Test;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.bootstrap.SingletonAssembler;

/**
 *
 * @author alfio
 */
@Log
public class HelloWorldTest {

    @Test
    public void test() throws Exception {
        final SingletonAssembler assembler = new SingletonAssembler() {
            @Override
            public void assemble(final ModuleAssembly assembly) throws AssemblyException {
                assembly.transients(Speaker.class);
            }
        };

        final Speaker speaker = assembler.module().newTransient(Speaker.class); // <3>
        log.info(speaker.sayHello());
    }

}
