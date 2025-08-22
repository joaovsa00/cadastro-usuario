import br.com.dio.dao.UserDAO;
import br.com.dio.exception.CustomException;
import br.com.dio.exception.EmptyStorageException;
import br.com.dio.exception.UserNotFoundException;
import br.com.dio.exception.ValidatorException;
import br.com.dio.model.MenuOption;
import br.com.dio.model.UserModel;
import br.com.dio.validator.UserValidator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    private final static UserDAO dao = new UserDAO();
    private final static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while(true) {
            System.out.println("Bem vindo ao cadastro de usuários, selecione a operação desejada");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Atualizar");
            System.out.println("3 - Excluir");
            System.out.println("4 - Buscar por identificador");
            System.out.println("5 - Listar");
            System.out.println("6 - Sair");
            var userInput = scanner.nextInt();
            MenuOption selectedOption;
            try {
                selectedOption = MenuOption.values()[userInput - 1];
            }catch (Exception e){
                System.out.println("\n"+"Opção inválida, digite uma opção válida");
                System.out.println("=================================================================");
                System.out.println("Pressione Enter para continuar...");
                scanner.nextLine();
                scanner.nextLine();
                System.out.println("=================================================================");
                continue;
            }

            switch (selectedOption){
                case SAVE -> {
                    try {
                        var user = dao.save(requestToSave());
                        System.out.printf("""
                                
                                O usuário %s foi cadastrado com sucesso!
                                """, user.getName());
                        System.out.println("=================================================================");
                    }catch (ValidatorException e){
                        System.out.println("\n"+e.getMessage());
                        System.out.println("=================================================================");
                    }finally {
                        System.out.println("Pressione Enter para continuar...");
                        scanner.nextLine();
                        scanner.nextLine();
                        System.out.println("=================================================================");
                    }
                }
                case UPDATE -> {
                    try {
                        var user = dao.update(requestToUpdate());
                        System.out.printf("""
                                
                                O usuário %s foi alterado com sucesso!
                                """, user.getName());
                        System.out.println("=================================================================");
                    }catch (UserNotFoundException | EmptyStorageException e){
                        System.out.println("\n"+e.getMessage());
                        System.out.println("=================================================================");
                    } catch (CustomException e) {
                        e.printStackTrace();
                        System.out.println("=================================================================");
                    }finally {
                        System.out.println("Pressione Enter para continuar...");
                        scanner.nextLine();
                        scanner.nextLine();
                        System.out.println("=================================================================");
                    }
                }
                case DELETE -> {
                    try {
                        var user = dao.delete(requestID());
                        System.out.printf("""
                                
                                O usuário %s foi excluído com sucesso!
                                """, user);
                        System.out.println("=================================================================");
                    }catch (UserNotFoundException | EmptyStorageException e){
                        System.out.println("\n"+e.getMessage());
                        System.out.println("=================================================================");
                    }finally {
                        System.out.println("Pressione Enter para continuar...");
                        scanner.nextLine();
                        scanner.nextLine();
                        System.out.println("=================================================================");
                    }
                }
                case FIND_BY_ID -> {
                    try {
                        var user = dao.findById(requestID());
                        System.out.println("\n"+user);
                        System.out.println("=================================================================");
                    }catch (UserNotFoundException | EmptyStorageException e){
                        System.out.println("\n"+e.getMessage());
                        System.out.println("=================================================================");
                    }finally {
                        System.out.println("Pressione Enter para continuar...");
                        scanner.nextLine();
                        scanner.nextLine();
                        System.out.println("=================================================================");
                    }
                }
                case FIND_ALL -> {
                    try {
                        var user = dao.findAll();
                        //user.forEach(u -> System.out.println(u));
                        System.out.println("");
                        user.forEach(System.out::println);
                        System.out.println("=================================================================");
                    }catch (EmptyStorageException e){
                        System.out.println("\n"+e.getMessage());
                        System.out.println("=================================================================");
                    }finally {
                        System.out.println("Pressione Enter para continuar...");
                        scanner.nextLine();
                        scanner.nextLine();
                        System.out.println("=================================================================");
                    }
                }
                case EXIT -> {
                    System.exit(0);
                }
            }

        }
    }

    private static UserModel requestToSave() throws ValidatorException {
        System.out.println("Informe o nome do usuário");
        var name = scanner.next();
        System.out.println("Informe o e-mail do usuário");
        var email = scanner.next();
        System.out.println("Informe a data de nascimento do usuário (dd/MM/yyyy)");
        var birthdayString = scanner.next();
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        var birthday = LocalDate.parse(birthdayString, formatter);
        return validateInputs(0, name, email, birthday);
    }

    private static UserModel requestToUpdate() throws CustomException {
        System.out.println("Informe o identificador do usuário que deseja alterar");
        var id = scanner.nextLong();
        System.out.println("Informe o nome do usuário");
        var name = scanner.next();
        System.out.println("Informe o e-mail do usuário");
        var email = scanner.next();
        System.out.println("Informe a data de nascimento do usuário (dd/MM/yyyy)");
        var birthdayString = scanner.next();
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        var birthday = LocalDate.parse(birthdayString, formatter);
        return validateInputsCustomException(id, name, email, birthday);
    }

    private static long requestID(){
        System.out.println("Informe o identificador do usuário que deseja alterar");
        var id = scanner.nextLong();
        return id;
    }

    public static UserModel validateInputs(final long id, final String name, final String email, final LocalDate birthday) throws ValidatorException {
        var userModel = new UserModel(id, name, email, birthday);
        UserValidator.verifyModel(userModel);
        return userModel;
    }

    public static UserModel validateInputsCustomException(final long id, final String name, final String email, final LocalDate birthday) throws CustomException {
        var userModel = new UserModel(id, name, email, birthday);
        try {
            UserValidator.verifyModel(userModel);
        }catch (ValidatorException e){
            throw new CustomException("O seu usuário contém erros: "+e.getMessage(), e);
        }
        return userModel;
    }
}
