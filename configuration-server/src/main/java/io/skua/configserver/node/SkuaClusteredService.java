/*
 * Copyright 2021 Shaun Laurens
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.skua.configserver.node;

import io.aeron.ExclusivePublication;
import io.aeron.Image;
import io.aeron.cluster.codecs.CloseReason;
import io.aeron.cluster.service.ClientSession;
import io.aeron.cluster.service.Cluster;
import io.aeron.cluster.service.ClusteredService;
import io.aeron.logbuffer.Header;
import org.agrona.DirectBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkuaClusteredService implements ClusteredService
{
    private final ProtocolDemuxer demuxer;
    private final SkuaCore stateMachine;
    private final Logger log = LoggerFactory.getLogger(SkuaClusteredService.class);

    public SkuaClusteredService()
    {
        stateMachine = new SkuaCore();
        demuxer = new ProtocolDemuxer(stateMachine);
    }

    @Override
    public void onStart(Cluster cluster, Image snapshotImage)
    {
        if (snapshotImage != null)
        {
            log.info("loading snapshot");
            snapshotImage.poll(demuxer, 1);
        }
    }

    @Override
    public void onSessionOpen(ClientSession session, long timestamp)
    {
        log.info("Cluster Client Session opened");
    }

    @Override
    public void onSessionClose(ClientSession session, long timestamp, CloseReason closeReason)
    {
        log.info("Cluster Client Session closed");
    }

    @Override
    public void onSessionMessage(ClientSession session, long timestamp, DirectBuffer buffer, int offset,
                                 int length, Header header)
    {
        demuxer.setSession(session);
        demuxer.onFragment(buffer, offset, length, header);
    }

    @Override
    public void onTimerEvent(long correlationId, long timestamp)
    {
        log.info("Cluster Node timer firing");
    }

    @Override
    public void onTakeSnapshot(ExclusivePublication snapshotPublication)
    {
        log.info("taking snapshot");
    }

    @Override
    public void onRoleChange(Cluster.Role newRole)
    {
        log.info("Cluster Node is in role {}", newRole.name());
    }

    @Override
    public void onTerminate(Cluster cluster)
    {
        log.info("Cluster Node is terminating");
    }
}
