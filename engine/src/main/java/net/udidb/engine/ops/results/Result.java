/*
 * Copyright (c) 2011-2013, Dan McNulty
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *      * Neither the name of the UDI project nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS AND CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */

package net.udidb.engine.ops.results;

import net.udidb.engine.events.EventObserver;
import net.udidb.engine.ops.Operation;

/**
 * Encapsulates all new information made available by the execution of an operation.
 *
 * Note: all Result implementations should provide a non-default toString method. This will be used to obtain a
 * String representation of the the result.
 *
 * @author mcnulty
 */
public interface Result {

    /**
     * Accept a visitor to visit the result
     *
     * @param op the operation for which this is a result
     * @param visitor the visitor
     *
     * @return true, if the result indicates further operations should be executed; false otherwise
     */
    boolean accept(Operation op, OperationResultVisitor visitor);

    /**
     * @return true, if events are expected due to the execution of the operation
     */
    boolean isEventPending();

    /**
     * Operations can defer their completion until an event occurs in a debuggee. This method allows an Operation to
     * create a Result that encapsulates a visitor that can be used to complete the operation after an event has
     * occurred.
     *
     * @return the event observer for the operation, or null if the event has not been deferred
     */
    EventObserver getDeferredEventObserver();
}