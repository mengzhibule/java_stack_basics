package com.shawn.geektime.homework.user.db.repository;

/**
 * Interface for generic CRUD operations on a repository for a specific type.
 *
 * @param <T>
 * @param <ID>
 * @author Shawn
 * @since 1.0
 */
public interface CommonRepository<T, ID>
    extends InsertRepository<T, ID>,
        DeleteRepository<T, ID>,
        UpdateRepository<T, ID>,
        ReadRepository<T, ID> {}
