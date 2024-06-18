package com.brestats.model.dao;

import com.brestats.exceptions.IncorectConstructorArguments;
import com.brestats.model.data.User;

public class DBUser extends DBObject<User> {

    @Override
    public void insertQuery(User obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insertQuery'");
    }

    @Override
    public String getTable() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTable'");
    }

    @Override
    protected User constructor(String[] args) throws IncorectConstructorArguments {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'constructor'");
    }

    @Override
    protected String getWhereClause(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWhereClause'");
    }
    
}
