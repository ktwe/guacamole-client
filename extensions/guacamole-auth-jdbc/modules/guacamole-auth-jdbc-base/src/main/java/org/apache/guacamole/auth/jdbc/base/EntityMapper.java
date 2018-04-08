/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.guacamole.auth.jdbc.base;

import java.util.Collection;
import java.util.Set;
import org.apache.ibatis.annotations.Param;

/**
 * Mapper for entities. An entity is the base concept behind a user or user
 * group, and serves as a common point for granting permissions and defining
 * group membership.
 */
public interface EntityMapper {

    /**
     * Inserts the given entity into the database. If the entity already
     * exists, this will result in an error.
     *
     * @param entity
     *     The entity to insert.
     *
     * @return
     *     The number of rows inserted.
     */
    int insert(@Param("entity") EntityModel entity);

    /**
     * Returns the set of all group identifiers of which the given entity is a
     * member, taking into account the given collection of known group
     * memberships which are not necessarily defined within the database.
     *
     * @param entity
     *     The entity whose effective groups should be returned.
     *
     * @param effectiveGroups
     *     The identifiers of any known effective groups that should be taken
     *     into account, such as those defined externally to the database.
     *
     * @return
     *     The set of identifiers of all groups that the given entity is a
     *     member of, including those where membership is inherited through
     *     membership in other groups.
     */
    Set<String> selectEffectiveGroupIdentifiers(@Param("entity") EntityModel entity,
            @Param("effectiveGroups") Collection<String> effectiveGroups);

}
