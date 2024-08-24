package com.telerikacademy.web.photocontest.helpers;

import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.models.User;

public class PermissionHelper {


    public static void isSameUser(User authenticatedUser, User currentUser, String message) {
        if (!authenticatedUser.equals(currentUser)) {
            throw new UnauthorizedOperationException(message);
        }
    }

    public static void isOrganizer(User user, String message) {
        if(!user.getRole().getName().equals("Organizer")){
            throw new UnauthorizedOperationException(message);
        }
    }
}
