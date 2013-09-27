/*
 * Copyright 2009 Red Hat, Inc.
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.hornetq.core.remoting.impl.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.hornetq.core.buffers.impl.ChannelBufferWrapper;
import org.hornetq.spi.core.remoting.BufferDecoder;

import java.util.List;

/**
 * A Netty FrameDecoder used to decode messages.
 *
 * @author <a href="mailto:tim.fox@jboss.com">Tim Fox</a>
 * @author <a href="ataylor@redhat.com">Andy Taylor</a>
 * @author <a href="tlee@redhat.com">Trustin Lee</a>
 * @author <a href="nmaurer@redhat.com">Norman Maurer</a>
 *
 * @version $Revision$, $Date$
 */
public class HornetQFrameDecoder extends ByteToMessageDecoder
{
   private final BufferDecoder decoder;

   public HornetQFrameDecoder(final BufferDecoder decoder)
   {
      this.decoder = decoder;
   }

   // ByteToMessageDecoder overrides
   // -------------------------------------------------------------------------------------

   @Override
   protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, List<Object> out) throws Exception
   {
      for (;;)
      {
         int start = in.readerIndex();

         int length = decoder.isReadyToHandle(new ChannelBufferWrapper(in));

         in.readerIndex(start);

         if (length == -1)
         {
             return;
         }
         out.add(in.readBytes(length));
      }

   }
}
