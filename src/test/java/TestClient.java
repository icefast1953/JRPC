import org.example.Client.proxy.ClientProxy;
import org.example.Common.pojo.User;
import org.example.Common.service.UserService;

public class TestClient {
    public static void main(String[] args) {
        ClientProxy proxy = new ClientProxy("127.0.0.1", 9999);
        UserService userService = proxy.getProxy(UserService.class);
        User byUserId = userService.getUserByUserId(1);
        System.out.println(byUserId);

        User user = User.builder()
                .id(100)
                .sex(true)
                .userName("wxx")
                .build();
        Integer i = userService.insertUserId(user);
        System.out.println(i);

    }
}
