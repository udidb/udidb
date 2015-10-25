/*
 * Copyright (c) 2011-2015, Dan McNulty
 * All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package net.udidb.server.test;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import net.libudi.api.event.EventType;
import net.udidb.server.api.models.DebuggeeConfigModel;
import net.udidb.server.api.models.DebuggeeContextModel;
import net.udidb.server.api.models.OperationModel;
import net.udidb.server.api.models.UdiEventModel;

/**
 * @author mcnulty
 */
public class CreateDebuggeeTest extends BaseServerTest
{
    public CreateDebuggeeTest()
    {
        super("linux/clang/3.2-11/basic-64bit-dynamic");
    }

    @Test
    public void createDebuggee() throws Exception
    {
        DebuggeeConfigModel configModel = new DebuggeeConfigModel();
        configModel.setExecPath(getBinaryPath().toString());

        DebuggeeContextModel contextModel = given()
                .contentType("application/json")
                .body(configModel)
                .when()
                .post(getUri("/debuggeeContexts"))
                .as(DebuggeeContextModel.class);

        assertFalse(StringUtils.isEmpty(contextModel.getId()));
        assertTrue(contextModel.getPid() > 0);
        assertEquals(configModel.getExecPath(), contextModel.getExecPath());

        continueContext(contextModel);

        // Wait for the exit event
        UdiEventModel event = waitForEvent();
        assertNotNull(event);
        assertEquals(contextModel.getId(), event.getContextId());
        assertEquals(contextModel.getPid(), event.getPid());
        assertEquals(EventType.PROCESS_EXIT, event.getEventType());
        assertEquals(event.getEventData(), ImmutableMap.of("exitCode", 0));

        continueContext(contextModel);

        event = waitForEvent();
        assertNotNull(event);
        assertEquals(EventType.PROCESS_CLEANUP, event.getEventType());
    }

    private void continueContext(DebuggeeContextModel contextModel)
    {
        OperationModel continueOp = new OperationModel();
        continueOp.setName("continue");

        given()
                .contentType("application/json")
                .body(continueOp)
                .when()
                .post(getUri(String.format("/debuggeeContexts/%s/process/operation", contextModel.getId())))
                .then()
                .body("name", equalTo("continue"));
    }
}