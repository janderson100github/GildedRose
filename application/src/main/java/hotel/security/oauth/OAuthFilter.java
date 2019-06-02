package hotel.security.oauth;

import hotel.oauth.AuthenticationService;
import hotel.oauth.TokenAuthentication;
import hotel.oauth.TokenData;
import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class OAuthFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AuthenticationService authenticationService;

    public OAuthFilter(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public void init(final FilterConfig filterConfig) {
        this.logger.debug("Initialized {}", this);
    }

    @Override
    public void destroy() {
        this.logger.info("Destroyed {}", this);
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        if (!(SecurityContextHolder.getContext()
                .getAuthentication() instanceof TokenAuthentication) && request instanceof HttpServletRequest) {
            try {
                authenticateUser((HttpServletRequest) request);
            } catch (final Exception any) {
                this.logger.trace("OAuth connection problem:", any);
                this.logger.info("Can't authenticate user: {}", any.toString());
                ((HttpServletResponse) response).sendError(HttpStatus.UNAUTHORIZED.value(),
                                                           "Use valid OAuth token to access this resource");
                return;
            }
        }

        try {
            if (!response.isCommitted()) {
                chain.doFilter(request, response);
            }
        } catch (final Exception any) {
            handleFilterException(any);
        }
    }

    private void handleFilterException(final Exception any) throws IOException, ServletException {
        if (any instanceof ClientAbortException) {
            this.logger.info("Client aborted: {}", any.toString());
        } else if (any instanceof IOException) {
            throw (IOException) any;
        } else if (any instanceof ServletException) {
            throw (ServletException) any;
        } else if (any instanceof RuntimeException) {
            throw (RuntimeException) any;
        } else {
            throw new RuntimeException(any);
        }
    }

    private void authenticateUser(final HttpServletRequest request) {
        final Optional<String> token = extractToken(request);

        final TokenAuthentication tokenAuthentication = buildTokenAuthentication(token);

        SecurityContextHolder.getContext()
                .setAuthentication(tokenAuthentication);
    }

    private TokenAuthentication buildTokenAuthentication(final Optional<String> token) {
        if (token.isPresent()) {
            return getAuthentication(token.get());
        }

        return TokenAuthentication.anonymous();
    }

    private TokenAuthentication getAuthentication(final String token) {
        TokenData tokenData = authenticationService.getTokenData(token);
        return TokenAuthentication.fromTokenData(tokenData);
    }

    private Optional<String> extractToken(final HttpServletRequest request) {
        final String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token != null) {
            return Optional.of(token.replace(OAuth2AccessToken.BEARER_TYPE, "")
                                       .trim());
        }
        return Optional.empty();
    }
}
