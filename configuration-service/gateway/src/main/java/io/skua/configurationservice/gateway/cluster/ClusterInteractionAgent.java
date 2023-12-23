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

package io.skua.configurationservice.gateway.cluster;

import io.aeron.cluster.client.AeronCluster;
import io.aeron.driver.MediaDriver;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.Agent;
import org.agrona.concurrent.MessageHandler;
import org.agrona.concurrent.SystemEpochClock;
import org.agrona.concurrent.ringbuffer.OneToOneRingBuffer;

/**
 * Agent to interact with the cluster
 */
public class ClusterInteractionAgent implements Agent, MessageHandler
{
    private static final long HEARTBEAT_INTERVAL = 250;
    private static final String INGRESS_CHANNEL = "aeron:udp?term-length=128k";
    private final OneToOneRingBuffer soupClusterComms;
    private long lastHeartbeatTime = Long.MIN_VALUE;
    private AeronCluster aeronCluster;
    private ConnectionState connectionState = ConnectionState.CLUSTER_NOT_CONNECTED;
    private MediaDriver mediaDriver;


    /**
     * Creates a new agent to interact with the cluster
     *
     * @param soupClusterChannel the channel to send messages to the cluster from soup
     */
    public ClusterInteractionAgent(final OneToOneRingBuffer soupClusterChannel)
    {
        this.soupClusterComms = soupClusterChannel;
    }

    @Override
    public int doWork()
    {
        final long now = SystemEpochClock.INSTANCE.time();
        if (now >= (lastHeartbeatTime + HEARTBEAT_INTERVAL))
        {
            lastHeartbeatTime = now;
            if (connectionState == ConnectionState.CLUSTER_CONNECTED)
            {
                aeronCluster.sendKeepAlive();
            }
        }

        int workCount = soupClusterComms.read(this);

        if (null != aeronCluster && !aeronCluster.isClosed())
        {
            workCount += aeronCluster.pollEgress();
        }

        return workCount;
    }

    @Override
    public String roleName()
    {
        return "cluster-connectivity";
    }

    @Override
    public void onMessage(final int msgTypeId, final MutableDirectBuffer buffer, final int offset, final int length)
    {
        //
    }

    @Override
    public void onClose()
    {
        if (aeronCluster != null)
        {
            aeronCluster.close();
        }
        if (mediaDriver != null)
        {
            mediaDriver.close();
        }
    }
}
