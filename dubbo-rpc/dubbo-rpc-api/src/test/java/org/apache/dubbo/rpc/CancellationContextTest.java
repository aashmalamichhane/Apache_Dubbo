/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.rpc;

import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link CancellationContext}
 */
class CancellationContextTest {

    @Test
    void test() throws Exception {
        CancellationContext cancellationContext = new CancellationContext();

        CountDownLatch latch = new CountDownLatch(2);
        cancellationContext.addListener(rpcServiceContext -> {
            latch.countDown();
        });
        cancellationContext.addListener(rpcServiceContext -> {
            latch.countDown();
        });
        RuntimeException runtimeException = new RuntimeException();
        cancellationContext.cancel(runtimeException);
        latch.await();
        Assertions.assertNull(cancellationContext.getListeners());
        Assertions.assertTrue(cancellationContext.isCancelled());
        Assertions.assertEquals(cancellationContext.getCancellationCause(), runtimeException);
    }

    @Test
    void testAddListenerAfterCancel() throws Exception {
        CancellationContext cancellationContext = new CancellationContext();

        cancellationContext.cancel(null);
        CountDownLatch latch = new CountDownLatch(1);
        cancellationContext.addListener(rpcServiceContext -> {
            latch.countDown();
        });
        latch.await();
    }
}
