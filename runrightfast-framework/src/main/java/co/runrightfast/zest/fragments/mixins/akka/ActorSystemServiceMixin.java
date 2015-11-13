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
package co.runrightfast.zest.fragments.mixins.akka;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import co.runrightfast.akka.AkkaUtils;
import static co.runrightfast.akka.AkkaUtils.USER;
import static co.runrightfast.akka.AkkaUtils.WILDCARD;
import co.runrightfast.zest.composites.services.akka.ActorSystemService;
import org.qi4j.api.service.ServiceActivation;

/**
 *
 * @author alfio
 */
public class ActorSystemServiceMixin implements ActorSystemService, ServiceActivation {

    private ActorSystem actorSystem;

    @Override
    public ActorSystem actorSystem() {
        return actorSystem;
    }

    @Override
    public void activateService() throws Exception {
        this.actorSystem = ActorSystem.create();
    }

    @Override
    public void passivateService() throws Exception {
        final ActorSelection topLevelActors = this.actorSystem.actorSelection(AkkaUtils.actorPath(USER, WILDCARD));
        topLevelActors.tell(PoisonPill.getInstance(), actorSystem.deadLetters());
    }

}
