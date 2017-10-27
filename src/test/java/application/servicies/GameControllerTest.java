package application.servicies;

import application.models.User;
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

import static org.junit.Assert.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
public class GameControllerTest {
    @Autowired
    private UsersService usersService;
    @Autowired
    private MockMvc mockMvc;
    private User existingUser;

    @Before
    public void setup() {
        usersService.create(new User("ExistingUser", "password", "existing-user@mail.ru"));
        existingUser = usersService.findUserByLogin("ExistingUser");
        assertNotNull(existingUser);
        existingUser.setPassword("password");
    }


    @After
    public void after() {
        usersService.clearDB();
    }


    @Test
    public void testRecords() throws Exception {
        mockMvc.perform(get("/api/game/records"))
                .andExpect(status().isOk());
    }

    @Test
    public void testSuccessLocalRecord() throws Exception {
        mockMvc.perform(post("/api/game/local_record")
                .sessionAttr("userId", existingUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"localRecord\": 123 }"))
                .andExpect(status().isOk());
    }
}
