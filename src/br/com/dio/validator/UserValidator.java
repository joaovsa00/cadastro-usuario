package br.com.dio.validator;

import br.com.dio.exception.ValidatorException;
import br.com.dio.model.UserModel;

public class UserValidator {
    private UserValidator(){

    }

    public static void verifyModel(final UserModel model) throws ValidatorException {
        if (StringIsBlank(model.getName())) throw new ValidatorException("Informe um nome");
        if (model.getName().length() <= 1) throw new ValidatorException("O nome deve ter mais que 1 caractér");
        if (StringIsBlank(model.getEmail())) throw new ValidatorException("Informe um e-mail");
        if ((!model.getEmail().contains("@")) || (!model.getEmail().contains("."))) throw new ValidatorException("Informe um e-mail válido");
    }

    private static boolean StringIsBlank(final String value){
        return null == value || value.isBlank();
    }
}
