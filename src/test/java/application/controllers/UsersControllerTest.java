package application.controllers;

import application.models.User;
import application.servicies.UsersService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SuppressWarnings({"InstanceMethodNamingConvention"})
@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
public class UsersControllerTest {
    @Autowired
    private UsersService usersService;
    @Autowired
    private MockMvc mockMvc;
    private User existingUser;


    @Before
    public void setup() {
        usersService.clearDB();
        usersService.create(new User("ExistingUser", "password", "existing-user@mail.ru"));
        existingUser = usersService.findUserByLogin("ExistingUser");
        assert existingUser != null;
        existingUser.setPassword("password");
    }


    @Test
    public void testSignup() throws Exception {
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"login\", \"password\":\"password\", \"email\":\"user@mail.ru\"}"))
                .andExpect(status().isOk());
    }


    @Test
    public void testUnsuccesSignupLoginAlreadyInUse() throws Exception {
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"" + existingUser.getLogin() + "\", \"password\":\"Password\", \"email\":\"user@mail.ru\"}"))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testSuccessGetCurrentUser() throws Exception {
        mockMvc.perform(get("/api/users/me")
                .sessionAttr("userId", existingUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("login").value(existingUser.getLogin()))
                .andExpect(jsonPath("email").value(existingUser.getEmail()));
    }


    @Test
    public void testUnsuccessGetCurrentUserRequiresLogin() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    public void testUnsuccessUpdateUserUnauthorized() throws Exception {
        mockMvc.perform(patch("/api/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@mail.ru\"}"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    public void testUnsuccessChangeInvalidEmail() throws Exception {
        mockMvc.perform(patch("/api/users/me")
                .sessionAttr("userId", existingUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"invalid@email\"}"))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testUnsuccessChangeInvalidPassword() throws Exception {
        mockMvc.perform(patch("/api/users/me")
                .sessionAttr("userId", existingUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"password\":\"pw\"}"))
                .andExpect(status().isBadRequest());
    }
//
//
//    @Test
//    public void testSignin() throws IOException {
//        login();
//    }


//    private void login() throws IOException {
//        testSignup();
//
//        final Response<SuccessResponse> response = app.signin(credentials).execute();
//        assertEquals(HttpStatus.OK.value(), response.code());
//
//        final String coockie = response.headers().get("Set-Cookie");
//        assertNotNull(coockie);
//    }
}
