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

import io.aeron.cluster.service.ClientSession;
import io.aeron.logbuffer.FragmentHandler;
import io.aeron.logbuffer.Header;
import org.agrona.DirectBuffer;
import org.agrona.ExpandableDirectByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ProtocolDemuxer implements FragmentHandler
{
    private final SkuaCore stateMachine;
    private final Logger logger = LoggerFactory.getLogger(ProtocolDemuxer.class);
    private ExpandableDirectByteBuffer returnBuffer;

    private ClientSession session;

    public ProtocolDemuxer(SkuaCore stateMachine)
    {
        this.stateMachine = stateMachine;
    }

    @Override
    public void onFragment(DirectBuffer buffer, int offset, int length, Header header)
    {
    }

    public void setSession(ClientSession session)
    {
        this.session = session;
    }

}
