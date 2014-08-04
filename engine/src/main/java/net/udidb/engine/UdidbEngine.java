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

package net.udidb.engine;

import net.udidb.engine.events.EventDispatcher;
import net.udidb.engine.ops.Operation;
import net.udidb.engine.ops.OperationReader;
import net.udidb.engine.ops.results.OperationResultVisitor;
import net.udidb.engine.ops.results.Result;

/**
 * Engine for udidb, this class executes operations for the debugger
 *
 * @author mcnulty
 */
public class UdidbEngine {

    private final Config config;

    private final OperationReader reader;

    private final OperationResultVisitor visitor;

    private final EventDispatcher eventDispatcher;

    /**
     * Constructor.
     *
     * @param config the configuration
     * @param reader the reader
     * @param visitor the visitor
     * @param eventDispatcher the event dispatcher for handling events
     */
    public UdidbEngine(Config config, OperationReader reader, OperationResultVisitor visitor, EventDispatcher eventDispatcher) {
        this.config = config;
        this.reader = reader;
        this.visitor = visitor;
        this.eventDispatcher = eventDispatcher;
    }

    /**
     * Runs the engine, which executes operations and processes the results
     */
    public void run() {
        boolean shouldContinue = true;
        while (shouldContinue) {
            Operation op = null;
            try {
                op = reader.read();

                Result result = op.execute();

                shouldContinue = result.accept(op, visitor);
                if (shouldContinue) {
                    eventDispatcher.handleEvents(result);
                }
            }catch (Exception e) {
               if(!visitor.visit(op, e)) {
                   shouldContinue = false;
               }
            }
        }
    }
}