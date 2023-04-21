package com.example.accesskeybackend;

import com.example.accesskeybackend.template.controller.AccessKeyIPSupportController;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccessKeyIPSupportController.class)
@WithMockUser(username = "test-user", password = "test-password")
public class AccessKeyIPSupportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String siteUrl = "siteUrl";
    private static final String urlTemplate = "/api/web/checkIPv6Support";

    @Test
    void isOk_when_check_IPv6_support() throws Exception {
        mockMvc.perform(get(urlTemplate)
                        .queryParam(siteUrl, "www.google.com"))
                .andExpect(jsonPath("$", Is.is(true)))
                .andExpect(status().isOk());

        mockMvc.perform(get(urlTemplate)
                        .queryParam(siteUrl, "google.com"))
                .andExpect(jsonPath("$", Is.is(true)))
                .andExpect(status().isOk());

        mockMvc.perform(get(urlTemplate)
                        .queryParam(siteUrl, "https://www.google.com/search?q=example"))
                .andExpect(jsonPath("$", Is.is(true)))
                .andExpect(status().isOk());

        mockMvc.perform(get(urlTemplate)
                        .queryParam(siteUrl, "www.school45.mogilev.by"))
                .andExpect(jsonPath("$", Is.is(false)))
                .andExpect(status().isOk());

        mockMvc.perform(get(urlTemplate)
                        .queryParam(siteUrl, "school45.mogilev.by"))
                .andExpect(jsonPath("$", Is.is(false)))
                .andExpect(status().isOk());

        mockMvc.perform(get(urlTemplate)
                        .queryParam(siteUrl, "https://www.school45.mogilev.by"))
                .andExpect(jsonPath("$", Is.is(false)))
                .andExpect(status().isOk());
    }

    @Test
    void fail_when_invalid_url() throws Exception {
        mockMvc.perform(get(urlTemplate)
                        .queryParam(siteUrl, "http path"))
                .andExpect(jsonPath("$.errors[0]", Is.is("Invalid site URL: http path")))
                .andExpect(status().isBadRequest());
    }
}
