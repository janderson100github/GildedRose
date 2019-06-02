package hotel.oauth;

import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    // TODO cache this
    public TokenData getTokenData(String token) {

        // TODO go to OAuth API and get info and convert to TokenData
        if ("123".equals(token)) {
            return new TokenData("USER");
        }
        else return new TokenData();
    }
}
