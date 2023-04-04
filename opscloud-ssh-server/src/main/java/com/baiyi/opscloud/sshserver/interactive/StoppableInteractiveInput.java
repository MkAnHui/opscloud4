/*
 * Copyright (c) 2020 François Onimus
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baiyi.opscloud.sshserver.interactive;

import org.jline.terminal.Size;
import org.jline.utils.AttributedString;

import java.util.List;

/**
 * Interface to give to interactive command to provide lines
 */
@FunctionalInterface
public interface StoppableInteractiveInput extends InteractiveInput {

    /**
     * Get lines to write
     *
     * @param size         terminal size
     * @param currentDelay current refresh delay
     * @return bean containing lines
     */
    InteractiveInputIO getIO(Size size, long currentDelay);

    @Override
    default List<AttributedString> getLines(Size size, long currentDelay) {
        return getIO(size, currentDelay).getLines();
    }
}
