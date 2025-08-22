package br.com.dio.dao;

import br.com.dio.exception.EmptyStorageException;
import br.com.dio.exception.UserNotFoundException;
import br.com.dio.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private long nextId = 1L;

    private final List<UserModel> models = new ArrayList<>();

    public UserModel save(final UserModel model){
        model.setId(nextId++);
        models.add(model);
        return model;
    }

    public UserModel update(final UserModel model){
        var toUpdate = findById(model.getId());
        models.remove(toUpdate);
        models.add(model);
        return model;
    }

    public String delete(final long id){
        var toDelete = findById(id);
        var name = toDelete.getName();
        models.remove(toDelete);
        return name;
    }

    public UserModel findById(final long id){
        verifyStorage();
        var message = String.format("Não existe usuário com o ID %s cadastrado",id);
        return models.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(message));
    }

    public List<UserModel> findAll(){
        verifyStorage();
        return models;
    }

    private void verifyStorage() throws EmptyStorageException{
        if (models.isEmpty())
            throw new EmptyStorageException("Não há usuário cadastrado");
    }
}
