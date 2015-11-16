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

import akka.actor.ActorRef;
import co.runrightfast.exceptions.ApplicationException;
import co.runrightfast.exceptions.ApplicationExceptionSeverity;
import lombok.Getter;
import lombok.NonNull;

/**
 * Used to provide the supervisor with the message that failed and the failing actor. The supervisor can then take appropriate action. For example, the
 * supervisor may decide to drop the message or store it to be retired later.
 *
 * @author alfio
 */
public class MessageProcessingException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    @Getter
    private final Object failedMessage;

    @Getter
    private final ActorRef actor;

    public MessageProcessingException(@NonNull final ApplicationException cause, @NonNull final Object failedMessage, @NonNull final ActorRef actor) {
        this(cause.getSeverity(), cause, failedMessage, actor);
    }

    public MessageProcessingException(final ApplicationExceptionSeverity severity, @NonNull final Throwable cause, @NonNull final Object failedMessage, @NonNull final ActorRef actor) {
        super(severity, cause);
        this.failedMessage = failedMessage;
        this.actor = actor;
    }

}
