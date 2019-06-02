package hotel.oauth;

public class TokenData {

    private boolean isUser;

    public TokenData() {

    }

    public TokenData(final String role) {
        if ("USER".equals(role)) {
            this.isUser = true;
        }
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(final boolean user) {
        isUser = user;
    }
}
