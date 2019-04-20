package com.github.ocraft.s2client.protocol.unit;

/*-
 * #%L
 * ocraft-s2client-protocol
 * %%
 * Copyright (C) 2017 - 2018 Ocraft Project
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import SC2APIProtocol.Raw;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public enum CloakState {
    CLOAKED,
    NOT_CLOAKED,
    CLOAKED_DETECTED,
    /** Under the fog, so unknown whether it's cloaked or not. **/
    CLOAKED_UNKNOWN,
    CLOAKED_ALLIED;

    public static CloakState from(Raw.CloakState sc2ApiCloakState) {
        require("sc2api cloak state", sc2ApiCloakState);
        switch (sc2ApiCloakState) {
            case Cloaked:
                return CLOAKED;
            case NotCloaked:
                return NOT_CLOAKED;
            case CloakedDetected:
                return CLOAKED_DETECTED;
            default:
                throw new AssertionError("unknown sc2api cloak state: " + sc2ApiCloakState);
        }
    }
}
