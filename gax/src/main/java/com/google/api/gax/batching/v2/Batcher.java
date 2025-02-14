/*
 * Copyright 2019 Google LLC
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *     * Neither the name of Google LLC nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.google.api.gax.batching.v2;

import com.google.api.core.ApiFuture;
import com.google.api.core.BetaApi;

/**
 * Represents a batching context where individual elements will be accumulated and flushed in a
 * large batch request at some point in the future. The buffered elements can be flushed manually or
 * when triggered by an internal threshold. This is intended to be used for high throughput
 * scenarios at the cost of latency.
 *
 * @param <ElementT> The type of each individual element to be batched.
 * @param <ElementResultT> The type of the result for each individual element.
 */
@BetaApi("The surface for batching is not stable yet and may change in the future.")
public interface Batcher<ElementT, ElementResultT> extends AutoCloseable {

  /**
   * Queues the passed in element to be sent at some point in the future.
   *
   * <p>The element will be sent as part of a larger batch request at some point in the future. The
   * returned {@link ApiFuture} will be resolved once the result for the element has been extracted
   * from the batch response.
   *
   * <p>Note: Cancelling returned result simply marks the future cancelled, It would not stop the
   * batch request.
   */
  ApiFuture<ElementResultT> add(ElementT entry);

  /**
   * Synchronously sends any pending elements as a batch and waits for all outstanding batches to be
   * complete.
   */
  void flush() throws InterruptedException;

  /**
   * Closes this Batcher by preventing new elements from being added and flushing the existing
   * elements.
   */
  @Override
  void close() throws InterruptedException;
}
