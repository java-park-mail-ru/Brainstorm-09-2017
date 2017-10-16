package application.controllers;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
public class UsersControllerTest {
    @Autowired
    private UsersService usersService;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setup(){
        usersService.clearDB();
    }


    @Test
    public void testMeRequiresLogin() throws Exception {
        mockMvc
                .perform(get("/api/users/me"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    public void testSignup() throws Exception {
        mockMvc
                .perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"login\", \"password\":\"password\", \"email\":\"user@mail.ru\"}"))
                .andExpect(status().isOk());
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
