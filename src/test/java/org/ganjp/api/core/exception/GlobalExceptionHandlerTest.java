package org.ganjp.api.core.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GlobalExceptionHandlerTestController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_handleRuntimeException_with400() throws Exception {
        // When & Then
        mockMvc.perform(get("/test/runtime"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status.code").value(400))
                .andExpect(jsonPath("$.status.message").value("Request error"));
    }

    @Test
    void should_handleGeneralException_with500() throws Exception {
        // When & Then
        mockMvc.perform(get("/test/exception"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status.code").value(500))
                .andExpect(jsonPath("$.status.message").value("Internal server error"));
    }
}

@RestController
class GlobalExceptionHandlerTestController {
    @GetMapping("/test/runtime")
    public void throwRuntime() {
        throw new RuntimeException("Test runtime error");
    }

    @GetMapping("/test/exception")
    public void throwException() throws Exception {
        throw new Exception("Test general error");
    }
}
