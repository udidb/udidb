/*
 * Copyright (c) 2011-2015, Dan McNulty
 * All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package net.udidb.engine.ops.impls.help;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.Inject;

import net.udidb.engine.context.DebuggeeContext;
import net.udidb.engine.context.DebuggeeContextAware;
import net.udidb.engine.ops.annotations.GlobalOperation;
import net.udidb.engine.ops.impls.DisplayNameOperation;
import net.udidb.engine.ops.OperationException;
import net.udidb.engine.ops.annotations.DisplayName;
import net.udidb.engine.ops.annotations.HelpMessage;
import net.udidb.engine.ops.annotations.Operand;
import net.udidb.engine.ops.results.Result;
import net.udidb.engine.ops.results.ValueResult;

/**
 * Display help messages for operations
 *
 * @author mcnulty
 */
@HelpMessage("Display help for operations")
@DisplayName("help")
@GlobalOperation
public class Help extends DisplayNameOperation implements DebuggeeContextAware
{
    private final HelpMessageProvider provider;
    private DebuggeeContext debuggeeContext;

    @Operand(order=0, optional=true, restOfLine=true)
    @HelpMessage("the requested operations")
    private List<String> operationName;

    public List<String> getOperationName() {
        return operationName;
    }

    public void setOperationName(List<String> operationName) {
        this.operationName = operationName;
    }

    @Inject
    public Help(HelpMessageProvider provider) {
        this.provider = provider;
    }

    @Override
    public Result execute() throws OperationException {

        if (debuggeeContext != null) {
            if (operationName == null) {
                StringBuilder builder = new StringBuilder();
                provider.getAllShortMessages(builder);

                return new ValueResult(builder.toString());
            }

            String opName = StringUtils.join(operationName, " ");

            String longMessage = provider.getLongMessage(opName);
            if (longMessage == null) {
                throw new OperationException(String.format("No help available for operation '%s'", opName));
            }

            return new ValueResult(longMessage);
        }

        if (operationName == null) {
            StringBuilder builder = new StringBuilder();
            provider.getAllGlobalShortMessages(builder);

            return new ValueResult(builder.toString());
        }

        String opName = StringUtils.join(operationName, " ");

        String longMessage = provider.getGlobalLongMessage(opName);
        if (longMessage == null) {
            throw new OperationException(String.format("No help available for operation '%s'", opName));
        }

        return new ValueResult(longMessage);
    }

    @Override
    public void setDebuggeeContext(DebuggeeContext debuggeeContext)
    {
        this.debuggeeContext = debuggeeContext;
    }
}
