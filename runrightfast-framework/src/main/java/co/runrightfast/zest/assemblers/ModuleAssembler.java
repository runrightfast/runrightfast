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
package co.runrightfast.zest.assemblers;

import java.util.function.Function;
import static org.apache.commons.lang3.Validate.noNullElements;
import static org.apache.commons.lang3.Validate.notEmpty;
import org.qi4j.bootstrap.ModuleAssembly;

/**
 *
 * @author alfio
 */
public interface ModuleAssembler {

    static Function<ModuleAssembly, ModuleAssembly> composeAssembler(final Function<ModuleAssembly, ModuleAssembly>... assemblers) {
        notEmpty(assemblers);
        noNullElements(assemblers);
        if (assemblers.length == 1) {
            return assemblers[0];
        }

        Function<ModuleAssembly, ModuleAssembly> chain = assemblers[0];
        for (int i = 1; i < assemblers.length; i++) {
            chain = chain.andThen(assemblers[i]);
        }
        return chain;
    }

}
