package application.servicies;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
public class GameControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRecords() throws Exception {
        mockMvc.perform(get("/api/game/records"))
                .andExpect(status().isOk());
    }

    @Test
    public void testSuccessLocalRecord() throws Exception {
        mockMvc.perform(get("/api/game/local_record"))
                .andExpect(status().isOk());
    }
}
