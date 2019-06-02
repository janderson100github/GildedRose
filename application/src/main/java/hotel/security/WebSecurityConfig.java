package hotel.security;

import hotel.security.oauth.OAuthFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ALL = "/**";

    private final OAuthFilter oauthFilter;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public WebSecurityConfig(final OAuthFilter oauthFilter) {
        this.oauthFilter = oauthFilter;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        logger.info("Setting security configuration");

        http.addFilterBefore(this.oauthFilter, RequestHeaderAuthenticationFilter.class)
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .authorizeRequests()
                .antMatchers(getPermittedPaths())
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(ALL)
                .denyAll();

        http.headers()
                .frameOptions()
                .sameOrigin();

        http.csrf()
                .disable();

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER);
    }

    private String[] getPermittedPaths() {
        return new String[]{};
    }

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
    }
}
