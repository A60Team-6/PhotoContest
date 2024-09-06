package com.telerikacademy.web.photocontest.helpers;

import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.exceptions.UnauthorizedOperationException;

public class PermissionHelper {


    public static void isSameUser(User authenticatedUser, User currentUser, String message) {
        if (!authenticatedUser.equals(currentUser)) {
            throw new UnauthorizedOperationException(message);
        }
    }

    public static void isOrganizer(User user, String message) {
        /*ToDo Switchh the places of constant and user.getRole*/
        if (!user.getRole().getName().equals("Organizer")) {
            throw new UnauthorizedOperationException(message);
        }
    }
}
