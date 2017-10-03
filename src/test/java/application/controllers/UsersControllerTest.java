package application.controllers;

import application.models.User;
import application.servicies.UsersService;
import application.views.SuccessResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class UsersControllerTest {
    private UsersService usersService;

    @LocalServerPort
    private int localPort;
    private User credentials;
    private static final AtomicLong ID_GENERATOR = new AtomicLong();
    private Application app;

    @Before
    public void setup(){
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:" + localPort)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        app = retrofit.create(Application.class);
        credentials = new User(null, "loginUCS" + ID_GENERATOR.getAndIncrement(), "password", "user@mail.ru");
    }


    @Test
    public void testMeRequiresLogin() throws IOException {
        final Response<User> response = app.currentUser().execute();
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.code());
    }


    @Test
    public void testSignup() throws IOException {
        final Response<SuccessResponse> response = app.signup(credentials).execute();
        assertEquals(HttpStatus.OK.value(), response.code());
    }


    @Test
    public void testSignin() throws IOException {
        login();
    }


    private void login() throws IOException {
        testSignup();

        final Response<SuccessResponse> response = app.signin(credentials).execute();
        assertEquals(HttpStatus.OK.value(), response.code());

        final String coockie = response.headers().get("Set-Cookie");
        assertNotNull(coockie);
    }


    public interface Application {
        @GET("/api/users/me")
        Call<User> currentUser();

        @GET("/api/users/me")
        Call<User> currentUser(@Header("Cookie") String cookie);

        @POST("/api/users")
        Call<SuccessResponse> signup(@Body User user);

        @POST("/api/users/signin")
        Call<SuccessResponse> signin(@Body User user);
    }
}
