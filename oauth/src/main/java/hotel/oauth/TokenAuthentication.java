package hotel.oauth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import javax.security.auth.Subject;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TokenAuthentication extends AbstractAuthenticationToken {

    private TokenAuthentication(final List<GrantedAuthority> authorities) {
        super(authorities);
    }

    public static TokenAuthentication anonymous() {
        return new TokenAuthentication(Arrays.asList(generateGrantedAuthority(Role.ANONYMOUS)));
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
        List<Role> roles = getRoles(tokenData);
        return new TokenAuthentication(generateGrantedAuthorities(roles));
    }

    private static List<GrantedAuthority> generateGrantedAuthorities(final List<Role> roles) {
        return roles.stream()
                .map(TokenAuthentication::generateGrantedAuthority)
                .collect(Collectors.toList());
    }

    private static GrantedAuthority generateGrantedAuthority(final Role role) {
        return new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return Role.USER.name();
            }
        };
    }

    private static List<Role> getRoles(final TokenData tokenData) {
        if (tokenData.isUser()) {
            return Arrays.asList(Role.ANONYMOUS, Role.USER);
        }
        return Arrays.asList(Role.ANONYMOUS);
    }
}
