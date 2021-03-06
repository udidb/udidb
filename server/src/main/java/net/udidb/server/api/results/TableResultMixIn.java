/*
 * Copyright (c) 2011-2015, Dan McNulty
 * All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package net.udidb.server.api.results;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.udidb.engine.events.EventObserver;
import net.udidb.engine.ops.results.TableResult;
import net.udidb.engine.ops.results.TableRow;

/**
 * @author mcnulty
 */
public class TableResultMixIn
{
    private boolean eventPending = false;

    @JsonIgnore
    private EventObserver deferredEventObserver = null;

    private List<String> columnHeaders;

    private List<TableRow> rows;

    private String typeName = TableResult.class.getSimpleName();

}
