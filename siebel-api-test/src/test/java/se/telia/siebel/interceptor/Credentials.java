package se.telia.siebel.interceptor;

public final class Credentials {
	
    private final String channel;
    private final String username;
    private final String password;

    public Credentials(String channel, String username, String password) {
        this.channel = channel;
        this.username = username;
        this.password = password;
    }

    public String getChannel() {
        return this.channel;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }
}
