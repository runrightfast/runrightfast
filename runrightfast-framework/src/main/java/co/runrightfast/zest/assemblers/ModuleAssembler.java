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
import lombok.NonNull;
import org.apache.commons.lang3.ArrayUtils;
import static org.apache.commons.lang3.Validate.noNullElements;
import org.qi4j.bootstrap.ModuleAssembly;

/**
 *
 * @author alfio
 */
public interface ModuleAssembler {

    static Function<ModuleAssembly, ModuleAssembly> composeAssembler(
            @NonNull final Function<ModuleAssembly, ModuleAssembly> assembler1,
            @NonNull final Function<ModuleAssembly, ModuleAssembly> assembler2,
            final Function<ModuleAssembly, ModuleAssembly>... moreAssemblers) {
        if (ArrayUtils.isNotEmpty(moreAssemblers)) {
            noNullElements(moreAssemblers);

            Function<ModuleAssembly, ModuleAssembly> chain = assembler1.andThen(assembler2);

            for (final Function<ModuleAssembly, ModuleAssembly> assembler : moreAssemblers) {
                chain = chain.andThen(assembler);
            }
            return chain;

        } else {
            return assembler1.andThen(assembler2);
        }

    }

}
