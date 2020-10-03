package com.mygdx.game.dataLayer.repositories;

import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

public interface IRepository<ID extends String,T extends Object> {
    public void add(ID id,T t);
    public void remove(ID id);
    public T getItem(ID id);
    public Map<ID,T> getAll();
}
