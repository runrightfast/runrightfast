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
package co.runrightfast.akka.actor;

import akka.actor.UntypedActor;
import co.runrightfast.exceptions.ApplicationException;
import co.runrightfast.exceptions.ApplicationExceptionSeverity;
import java.util.Optional;

/**
 *
 * @author alfio
 */
public abstract class WorkerActor extends UntypedActor {

    /**
     *
     * @param msg message
     * @throws MessageProcessingException all exceptions are wrapped within MessageProcessingException in order to capture the failed message and actor ref
     */
    @Override
    public final void onReceive(final Object msg) throws MessageProcessingException {
        try {
            processMessage(msg);
        } catch (final ApplicationException e) {
            throw new MessageProcessingException(e, msg, self());
        } catch (final Exception e) {
            throw new MessageProcessingException(severity(e).orElse(ApplicationExceptionSeverity.MAJOR), e, msg, self());
        }
    }

    protected abstract void processMessage(final Object msg) throws Exception;

    /**
     * This is used to assign a severity for exceptions that are not of type {@link ApplicationException}
     *
     * By default maps the following:
     *
     * <ul>
     * <li>{@link ApplicationExceptionSeverity#CRITICAL} : NullPointerException, IllegalArgumentException, IllegalStateException
     * </ul>
     *
     *
     * @param ex message processing exception
     * @return will be empty if no mapping is defined for the exception. In that case, {@link ApplicationExceptionSeverity#CRITICAL} will be used.
     */
    protected Optional<ApplicationExceptionSeverity> severity(final Exception ex) {
        if (ex instanceof NullPointerException
                || ex instanceof IllegalArgumentException
                || ex instanceof IllegalStateException) {
            return Optional.of(ApplicationExceptionSeverity.CRITICAL);
        }

        return Optional.empty();
    }

}
