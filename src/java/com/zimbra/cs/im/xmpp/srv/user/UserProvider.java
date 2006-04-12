/*
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 ("License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.zimbra.com/license
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and limitations
 * under the License.
 * 
 * Part of the Zimbra Collaboration Suite Server.
 *
 * The Original Code is Copyright (C) Jive Software. Used with permission
 * Portions created by Zimbra are Copyright (C) 2006 Zimbra, Inc.
 * All Rights Reserved.
 *
 * Contributor(s):
 *
 * ***** END LICENSE BLOCK *****
 */

package com.zimbra.cs.im.xmpp.srv.user;

import java.util.Date;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Provider interface for the user system.
 *
 * @author Matt Tucker
 */
interface UserProvider {

    /**
     * Loads the specified user by username.
     *
     * @param username the username
     * @return the User.
     * @throws UserNotFoundException if the User could not be loaded.
     */
    public User loadUser(String username) throws UserNotFoundException;

    /**
     * Creates a new user. This method should throw an
     * UnsupportedOperationException if this operation is not
     * supporte by the backend user store.
     *
     * @param username the username.
     * @param password the plain-text password.
     * @param name the user's name, which can be <tt>null</tt>.
     * @param email the user's email address, which can be <tt>null</tt>.
     * @return a new User.
     * @throws UserAlreadyExistsException if the username is already in use.
     */
    public User createUser(String username, String password, String name, String email)
            throws UserAlreadyExistsException;

    /**
     * Delets a user. This method should throw an
     * UnsupportedOperationException if this operation is not
     * supported by the backend user store.
     *
     * @param username the username to delete.
     */
    public void deleteUser(String username);

//    /**
//     * Returns the number of users in the system.
//     *
//     * @return the total number of users.
//     */
//    public int getUserCount();

    /**
     * Returns an unmodifiable Collections of all users in the system. The
     * {@link UserCollection} class can be used to assist in the implementation
     * of this method. It takes a String [] of usernames and presents it as a
     * Collection of User objects (obtained with calls to
     * {@link UserManager#getUser(String)}.
     *
     * @return an unmodifiable Collection of all users.
     */
    public Collection<User> getUsers();

    /**
     * Returns an unmodifiable Collections of users in the system within the
     * specified range. The {@link UserCollection} class can be used to assist
     * in the implementation of this method. It takes a String [] of usernames
     * and presents it as a  Collection of User objects (obtained with calls to
     * {@link UserManager#getUser(String)}.<p>
     *
     * It is possible that the number of results returned will be less than that
     * specified by <tt>numResults</tt> if <tt>numResults</tt> is greater than the
     * number of records left to display.
     *
     * @param startIndex the beginning index to start the results at.
     * @param numResults the total number of results to return.
     * @return an unmodifiable Collection of users within the specified range.
     */
    public Collection<User> getUsers(int startIndex, int numResults);

    /**
     * Returns the user's password. This method should throw an UnsupportedOperationException
     * if this operation is not supported by the backend user store.
     *
     * @param username the username of the user.
     * @return the user's password.
     * @throws UserNotFoundException if the given user could not be loaded.
     * @throws UnsupportedOperationException if the provider does not
     *      support the operation (this is an optional operation).
     */
    public String getPassword(String username) throws UserNotFoundException,
            UnsupportedOperationException;

    /**
     * Sets the users's password. This method should throw an UnsupportedOperationException
     * if this operation is not supported by the backend user store.
     *
     * @param username the username of the user.
     * @param password the new plaintext password for the user.
     * @throws UserNotFoundException if the given user could not be loaded.
     * @throws UnsupportedOperationException if the provider does not
     *      support the operation (this is an optional operation).
     */
    public void setPassword(String username, String password)
            throws UserNotFoundException, UnsupportedOperationException;

    /**
     * Sets the user's name. This method should throw an UnsupportedOperationException
     * if this operation is not supported by the backend user store.
     *
     * @param username the username.
     * @param name the name.
     * @throws UserNotFoundException if the user could not be found.
     */
    public void setName(String username, String name) throws UserNotFoundException;

    /**
     * Sets the user's email address. This method should throw an
     * UnsupportedOperationException if this operation is not supported
     * by the backend user store.
     *
     * @param username the username.
     * @param email the email address.
     * @throws UserNotFoundException if the user could not be found.
     */
    public void setEmail(String username, String email) throws UserNotFoundException;

    /**
     * Sets the date the user was created. This method should throw an
     * UnsupportedOperationException if this operation is not supported
     * by the backend user store.
     *
     * @param username the username.
     * @param creationDate the date the user was created.
     * @throws UserNotFoundException if the user could not be found.
     */
    public void setCreationDate(String username, Date creationDate) throws UserNotFoundException;

    /**
     * Sets the date the user was last modified. This method should throw an
     * UnsupportedOperationException if this operation is not supported
     * by the backend user store.
     *
     * @param username the username.
     * @param modificationDate the date the user was last modified.
     * @throws UserNotFoundException if the user could not be found.
     */
    public void setModificationDate(String username, Date modificationDate)
            throws UserNotFoundException;

//    /**
//     * Returns the set of fields that can be used for searching for users. Each field
//     * returned must support wild-card and keyword searching. For example, an
//     * implementation might send back the set {"Username", "Name", "Email"}. Any of
//     * those three fields can then be used in a search with the
//     * {@link #findUsers(Set,String)} method.<p>
//     *
//     * This method should throw an UnsupportedOperationException if this
//     * operation is not supported by the backend user store.
//     *
//     * @return the valid search fields.
//     * @throws UnsupportedOperationException if the provider does not
//     *      support the operation (this is an optional operation).
//     */
//    public Set<String> getSearchFields() throws UnsupportedOperationException;
//
//    /**
//     * Searches for users based on a set of fields and a query string. The fields must
//     * be taken from the values returned by {@link #getSearchFields()}. The query can
//     * include wildcards. For example, a search on the field "Name" with a query of "Ma*"
//     * might return user's with the name "Matt", "Martha" and "Madeline".<p>
//     *
//     * This method should throw an UnsupportedOperationException if this
//     * operation is not supported by the backend user store. 
//     *
//     * @param fields the fields to search on.
//     * @param query the query string.
//     * @return a Collection of users that match the search.
//     * @throws UnsupportedOperationException if the provider does not
//     *      support the operation (this is an optional operation).
//     */
//    public Collection<User> findUsers(Set<String> fields, String query)
//            throws UnsupportedOperationException;
//
//    /**
//     * Searches for users based on a set of fields and a query string. The fields must
//     * be taken from the values returned by {@link #getSearchFields()}. The query can
//     * include wildcards. For example, a search on the field "Name" with a query of "Ma*"
//     * might return user's with the name "Matt", "Martha" and "Madeline".<p>
//     *
//     * The startIndex and numResults parameters are used to page through search
//     * results. For example, if the startIndex is 0 and numResults is 10, the first
//     * 10 search results will be returned. Note that numResults is a request for the
//     * number of results to return and that the actual number of results returned
//     * may be fewer.<p>
//     *
//     * This method should throw an UnsupportedOperationException if this
//     * operation is not supported by the backend user store.
//     *
//     * @param fields the fields to search on.
//     * @param query the query string.
//     * @return a Collection of users that match the search.
//     * @return startIndex the starting index in the search result to return.
//     * @return numResults the number of users to return in the search result.
//     * @throws UnsupportedOperationException if the provider does not
//     *      support the operation (this is an optional operation).
//     */
//    public Collection<User> findUsers(Set<String> fields, String query, int startIndex,
//            int numResults) throws UnsupportedOperationException;

    /**
     * Returns true if this UserProvider is read-only. When read-only,
     * users can not be created, deleted, or modified.
     *
     * @return true if the user provider is read-only.
     */
    public boolean isReadOnly();

    /**
     * Returns true if this UserProvider is able to retrieve user passwords from
     * the backend user store. If this operation is not supported then {@link #getPassword(String)}
     * will throw an {@link UnsupportedOperationException} if invoked.
     *
     * @return true if this UserProvider is able to retrieve user passwords from the
     *         backend user store.
     */
    public boolean supportsPasswordRetrieval();

    public Map<String, String> loadProperties(String username);
    
    public void insertProperty(String username, String propName, String propValue);
    
    public void updateProperty(String username, String propName, String propValue);
    
    public void deleteProperty(String username, String propName);
    
    /**
     * Returns the value of the specified property for the given username. This method is
     * an optimization to avoid loading a user to get a specific property.
     *
     * @param username the username of the user to get a specific property value.
     * @param propertyName the name of the property to return its value.
     * @return the value of the specified property for the given username.
     */
    public String getPropertyValue(String username, String propertyName);
    
    
    
}