package com.telerikacademy.web.photocontest.helpers;

import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.entities.User;

public class PermissionHelper {


    public static void isSameUser(User authenticatedUser, User currentUser, String message) {
        if (!authenticatedUser.equals(currentUser)) {
            throw new UnauthorizedOperationException(message);
        }
    }

    public static void isOrganizer(User user, String message) {
        if (!"Organizer".equals(user.getRole().getName())) {
            throw new UnauthorizedOperationException(message);
        }
    }

    public static void isSameUserOrOrganizer(User authenticatedUser, User currentUser, String message) {
        if(!authenticatedUser.equals(currentUser) && !"Organizer".equals(authenticatedUser.getRole().getName())) {
            throw new UnauthorizedOperationException(message);
        }
    }
}
