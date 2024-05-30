package com.brestats.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;

import com.brestats.exceptions.IncorectConstructorArguments;
import com.brestats.model.Model;

public abstract class DBRawObject<T extends Model> extends DBObject<T> {
    @Override
    protected T constructor(String[] args, DBObject<? extends Model>[] db) throws IncorectConstructorArguments {
        return this.constructor(args);
    }

    public ArrayList<T> selectQuery(String query) throws SQLException {
        return this.selectQuery(query, null);
    }

    public T getItem(String id) {
        return this.getItem(id, null);
    }

    protected abstract T constructor(String[] args) throws IncorectConstructorArguments;
}
