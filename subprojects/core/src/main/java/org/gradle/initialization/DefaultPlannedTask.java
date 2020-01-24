/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.initialization;

import org.gradle.api.internal.project.taskfactory.TaskIdentity;
import org.gradle.internal.taskgraph.PlannedTask;

import java.util.List;

public class DefaultPlannedTask implements PlannedTask{
    private final TaskIdentity<?> taskIdentity;
    private final List<TaskIdentity> dependencies;
    private List mustRunAfter;
    private List shouldRunAfter;
    private final List<TaskIdentity> finalizers;

    public DefaultPlannedTask(TaskIdentity<?> taskIdentity, List dependencies, List mustRunAfter, List shouldRunAfter, List finalizers) {
        this.taskIdentity = taskIdentity;
        this.dependencies = dependencies;
        this.mustRunAfter = mustRunAfter;
        this.shouldRunAfter = shouldRunAfter;
        this.finalizers = finalizers;
    }

    @Override
    public TaskIdentity getTask() {
        return taskIdentity;
    }

    @Override
    public List<TaskIdentity> getDependencies() {
        return dependencies;
    }

    @Override
    public List<TaskIdentity> getMustRunAfter() {
        return mustRunAfter;
    }

    @Override
    public List<TaskIdentity> getShouldRunAfter() {
        return shouldRunAfter;
    }

    @Override
    public List<TaskIdentity> getFinalizedBy() {
        return finalizers;
    }
}
