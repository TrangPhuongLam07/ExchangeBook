package com.exchangeBook.ExchangeBook.authConfig;

import com.exchangeBook.ExchangeBook.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/*
Create By : ANHTUAN
*/
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, IOException {
//        String targetUrl = determineTargetUrl(authentication);
//        getRedirectStrategy().sendRedirect(request, response, targetUrl);

        // Customize your JSON response here
        String jsonResponse = "{\"message\": \"Login successful.\", \"username\": \"" + authentication.getName() + "\"}";

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }


    private String determineTargetUrl(Authentication authentication) {
        // Assuming your User object is stored in the authentication principal
        // Adjust this line based on where the user ID is located in your User object
        Long userId = ((User) authentication.getPrincipal()).getId();

        // Replace "/admin/{id}" with the actual URL pattern for your admin pages
        return "/admin/user-all";
    }
}