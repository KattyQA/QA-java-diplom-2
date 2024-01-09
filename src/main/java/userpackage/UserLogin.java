package userpackage;

public class UserLogin {

    private final String password;
    private String email;

    public UserLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserLogin fromUser(User user) {
        return new UserLogin(user.getEmail(), user.getPassword());
    }

}
