/*
 * Copyright (c) 2011-2015, Dan McNulty
 * All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package net.udidb.server.api.models;

import java.util.Map;

/**
 * @author mcnulty
 */
public class DebuggeeConfigModel
{
    private String execPath;

    private String[] args;

    private Map<String, String> env;

    public String[] getArgs()
    {
        return args;
    }

    public void setArgs(String[] args)
    {
        this.args = args;
    }

    public Map<String, String> getEnv()
    {
        return env;
    }

    public void setEnv(Map<String, String> env)
    {
        this.env = env;
    }

    public String getExecPath()
    {
        return execPath;
    }

    public void setExecPath(String execPath)
    {
        this.execPath = execPath;
    }
}
