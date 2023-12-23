/*
 * Copyright 2023 Shaun Laurens
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.skua.configurationservice.gateway;


import io.skua.configurationservice.gateway.cluster.ClusterInteractionAgent;
import org.agrona.BufferUtil;
import org.agrona.concurrent.AgentRunner;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.SleepingMillisIdleStrategy;
import org.agrona.concurrent.UnsafeBuffer;
import org.agrona.concurrent.ringbuffer.OneToOneRingBuffer;

import static org.agrona.concurrent.ringbuffer.RingBufferDescriptor.TRAILER_LENGTH;

public class Gateway
{
    public static void main(final String[] args)
    {
        final IdleStrategy idleStrategy = new SleepingMillisIdleStrategy();
        final UnsafeBuffer soupClusterBuffer =
            new UnsafeBuffer(BufferUtil.allocateDirectAligned(16384 + TRAILER_LENGTH, 8));
        final OneToOneRingBuffer soupClusterChannel = new OneToOneRingBuffer(soupClusterBuffer);
        final ClusterInteractionAgent clusterInteractionAgent = new ClusterInteractionAgent(soupClusterChannel);
        final AgentRunner clusterInteractionAgentRunner = new AgentRunner(idleStrategy, Throwable::printStackTrace,
            null, clusterInteractionAgent);
        AgentRunner.startOnThread(clusterInteractionAgentRunner);
    }

}
