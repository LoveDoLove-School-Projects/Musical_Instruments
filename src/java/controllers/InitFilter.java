package controllers;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

public class InitFilter implements Filter {

    /**
     * Filters the incoming request and response objects. If the request URI
     * contains ".jsp", forwards the request to the 4xxErrorPage.jsp. Otherwise,
     * passes the request and response objects to the next filter in the chain.
     *
     * @param request the ServletRequest object representing the client's
     * request
     * @param response the ServletResponse object representing the response to
     * be sent to the client
     * @param chain the FilterChain object used to invoke the next filter in the
     * chain
     * @throws ServletException if the servlet encounters difficulty
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        if (req.getRequestURI().contains(".jsp")) {
            req.getRequestDispatcher("/pages/errors/4xxErrorPage.jsp").forward(request, response);
            return;
        }
        chain.doFilter(request, response);
    }
}
