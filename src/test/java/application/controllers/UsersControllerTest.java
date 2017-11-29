package application.controllers;

import application.models.User;
import application.servicies.UsersService;
import org.junit.After;
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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import static org.junit.Assert.assertNotNull;
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

    @Autowired
    private PlatformTransactionManager transactionManager;
    private TransactionStatus transaction;

    private User existingUser;


    @Before
    public void setup() {
        transaction = transactionManager.getTransaction(
                new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW)
        );

        usersService.create(new User("ExistingUser", "password", "existing-user@mail.ru"));
        existingUser = usersService.findUserByLogin("ExistingUser");
        assertNotNull(existingUser);
        existingUser.setPassword("password");
    }

    @After
    public void after() {
        transactionManager.rollback(transaction);
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
    public void testSuccessChangeEmail() throws Exception {
        mockMvc.perform(patch("/api/users/me")
                .sessionAttr("userId", existingUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"new-email@mail.ru\"}"))
                .andExpect(status().isOk());
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
    public void testSuccessChangePassword() throws Exception {
        mockMvc.perform(patch("/api/users/me")
                .sessionAttr("userId", existingUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"password\":\"newPassword\"}"))
                .andExpect(status().isOk());
    }


    @Test
    public void testUnsuccessChangeInvalidPassword() throws Exception {
        mockMvc.perform(patch("/api/users/me")
                .sessionAttr("userId", existingUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"password\":\"pw\"}"))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testSuccessSignin() throws Exception {
        mockMvc.perform(post("/api/users/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"" + existingUser.getLogin() + "\", "
                        + "\"password\":\"" + existingUser.getPassword() + "\"}"))
                .andExpect(status().isOk());
    }


    @Test
    public void testUnsuccessSignin() throws Exception {
        mockMvc.perform(post("/api/users/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"" + existingUser.getLogin() + "\", "
                        + "\"password\":\"BadPassword\"}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/users/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"BadLogin\", "
                        + "\"password\":\"password\"}"))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testLogout() throws Exception {
        mockMvc.perform(get("/api/users/me")
                .sessionAttr("userId", existingUser.getId()))
                .andExpect(status().isOk());
    }


    @Test
    public void testSetTheme() throws Exception {
        mockMvc.perform(patch("/api/users/theme")
                .sessionAttr("userId", existingUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"theme\": 1 }"))
                .andExpect(status().isOk());
    }


    @Test
    public void testUnsuccesSetThemeUnauthorized() throws Exception {
        mockMvc.perform(patch("/api/users/theme")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"theme\": 1 }"))
                .andExpect(status().isUnauthorized());
    }
}
