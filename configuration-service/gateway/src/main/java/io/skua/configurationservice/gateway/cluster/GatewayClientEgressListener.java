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

import io.aeron.cluster.client.EgressListener;
import io.aeron.cluster.codecs.EventCode;
import io.aeron.logbuffer.Header;
import org.agrona.DirectBuffer;


/**
 * Cluster egress listener
 */
public class GatewayClientEgressListener implements EgressListener
{

    /**
     * The onMessage method is called when a message is received by the GatewayClientEgressListener.
     * It handles the message and performs the necessary processing.
     *
     * @param clusterSessionId the session ID associated with the cluster
     * @param timestamp        the timestamp of the message
     * @param buffer           the DirectBuffer containing the message data
     * @param offset           the offset within the buffer where the message starts
     * @param length           the length of the message
     * @param header           the Header object associated with the message
     */
    @Override
    public void onMessage(
        final long clusterSessionId,
        final long timestamp,
        final DirectBuffer buffer,
        final int offset,
        final int length,
        final Header header)
    {
    }


    /**
     * This method is called when a session event occurs in the cluster.
     *
     * @param correlationId      the correlation ID associated with the event
     * @param clusterSessionId   the session ID associated with the cluster
     * @param leadershipTermId   the leadership term ID associated with the event
     * @param leaderMemberId     the member ID of the leader associated with the event
     * @param code               the code representing the event type
     * @param detail             the detail message associated with the event
     */
    @Override
    public void onSessionEvent(
        final long correlationId,
        final long clusterSessionId,
        final long leadershipTermId,
        final int leaderMemberId,
        final EventCode code,
        final String detail)
    {
        if (code != EventCode.OK)
        {
        }
    }

    /**
     * The onNewLeader method is called when a new leader is elected in the cluster.
     * It provides information about the new leader and its associated details.
     *
     * @param clusterSessionId   the session ID associated with the cluster
     * @param leadershipTermId   the leadership term ID of the new leader
     * @param leaderMemberId     the member ID of the new leader
     * @param ingressEndpoints   the endpoints of the new leader for incoming messages
     */
    @Override
    public void onNewLeader(
        final long clusterSessionId,
        final long leadershipTermId,
        final int leaderMemberId,
        final String ingressEndpoints)
    {
    }

}
