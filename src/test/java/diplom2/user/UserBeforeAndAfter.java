package diplom2.user;

import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;

import static diplom2.constants.Constants.BASE_URL;

public class UserBeforeAndAfter {

    UserClient userClient = new UserClient();
    protected String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @After
    public void tearDown(){
        if(accessToken != null) {
            UserClient.userDelete(accessToken);
        }
    }
}
