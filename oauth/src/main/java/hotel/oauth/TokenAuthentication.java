package hotel.oauth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import javax.security.auth.Subject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TokenAuthentication extends AbstractAuthenticationToken {

    private TokenAuthentication(final List<GrantedAuthority> authorities) {
        super(authorities);
    }

    public static TokenAuthentication anonymous() {
        return new TokenAuthentication(Collections.emptyList());
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public String getPrincipal() {
        if (this.getCredentials() == null) {
            return null;
        }
        return this.getCredentials()
                .toString();
    }

    @Override
    public String toString() {
        return String.format("[%s %s]", getClass().getSimpleName(), getPrincipal());
    }

    @Override
    public boolean implies(final Subject subject) {
        return false;
    }

    public static TokenAuthentication fromTokenData(final TokenData tokenData) {
        if (tokenData.isUser()) {
            return new TokenAuthentication(Arrays.asList(new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return "USER";
                }
            }));
        }
        return new TokenAuthentication(Collections.emptyList());
    }
}
