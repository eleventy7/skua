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

package io.skua.configurationservice.infra;

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


public class ConfigurationClusteredService implements ClusteredService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationClusteredService.class);

    @Override
    public void onStart(final Cluster cluster, final Image snapshotImage)
    {
        LOGGER.info("Start");
    }

    @Override
    public void onSessionOpen(final ClientSession session, final long timestamp)
    {
        LOGGER.info("Client session opened");
    }

    @Override
    public void onSessionClose(final ClientSession session, final long timestamp, final CloseReason closeReason)
    {
        LOGGER.info("Session Close");
    }

    @Override
    public void onSessionMessage(
        final ClientSession session,
        final long timestamp,
        final DirectBuffer buffer,
        final int offset,
        final int length,
        final Header header)
    {
        LOGGER.info("Session Message");
    }

    @Override
    public void onTimerEvent(final long correlationId, final long timestamp)
    {
        LOGGER.info("Timer Event");
    }

    @Override
    public void onTakeSnapshot(final ExclusivePublication snapshotPublication)
    {
        LOGGER.info("Take Snapshot");
    }

    @Override
    public void onRoleChange(final Cluster.Role newRole)
    {
        LOGGER.info("Role change: {}", newRole);
    }

    @Override
    public void onTerminate(final Cluster cluster)
    {
        LOGGER.info("Terminating");
    }
}
