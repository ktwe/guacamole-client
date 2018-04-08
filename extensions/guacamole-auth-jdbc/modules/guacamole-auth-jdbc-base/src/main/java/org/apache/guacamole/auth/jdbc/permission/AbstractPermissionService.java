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

package org.apache.guacamole.auth.jdbc.permission;

import org.apache.guacamole.auth.jdbc.user.ModeledAuthenticatedUser;
import org.apache.guacamole.auth.jdbc.user.ModeledUser;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.auth.permission.ObjectPermission;
import org.apache.guacamole.net.auth.permission.ObjectPermissionSet;
import org.apache.guacamole.net.auth.permission.Permission;
import org.apache.guacamole.net.auth.permission.PermissionSet;

/**
 * Abstract PermissionService implementation which provides additional
 * convenience methods for enforcing the permission model.
 *
 * @param <PermissionSetType>
 *     The type of permission sets this service provides access to.
 *
 * @param <PermissionType>
 *     The type of permission this service provides access to.
 */
public abstract class AbstractPermissionService<PermissionSetType extends PermissionSet<PermissionType>,
        PermissionType extends Permission>
    implements PermissionService<PermissionSetType, PermissionType> {

    /**
     * Determines whether the given user can read the permissions currently
     * granted to the given target user. If the reading user and the target
     * user are not the same, then explicit READ or SYSTEM_ADMINISTER access is
     * required. Permission inheritance via user groups is taken into account.
     *
     * @param user
     *     The user attempting to read permissions.
     *
     * @param targetUser
     *     The user whose permissions are being read.
     *
     * @return
     *     true if permission is granted, false otherwise.
     *
     * @throws GuacamoleException 
     *     If an error occurs while checking permission status, or if
     *     permission is denied to read the current user's permissions.
     */
    protected boolean canReadPermissions(ModeledAuthenticatedUser user,
            ModeledUser targetUser) throws GuacamoleException {

        // A user can always read their own permissions
        if (user.getUser().getIdentifier().equals(targetUser.getIdentifier()))
            return true;
        
        // A system adminstrator can do anything
        if (user.getUser().isAdministrator())
            return true;

        // Can read permissions on target user if explicit READ is granted
        ObjectPermissionSet userPermissionSet = user.getUser().getEffectivePermissions().getUserPermissions();
        return userPermissionSet.hasPermission(ObjectPermission.Type.READ, targetUser.getIdentifier());

    }

}
