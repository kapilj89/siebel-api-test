package se.telia.siebel.interceptor;

public final class SiebelSession {
	
    public static final String TYPE_STATELESS = "Stateless";
    public static final String TYPE_STATEFULL = "Statefull";
    public static final String TYPE_NONE = "None";

    private final Credentials credentials;
    private String sessionType;
    private String sessionId;

    public SiebelSession(String sessionType, Credentials credentials) {
        this.sessionType = sessionType;
        this.credentials = credentials;
    }

    public Credentials getCredentials() {
    	return this.credentials;
    }
    
    public String getSessionId() {
        return this.sessionId;
    }

    public String getSessionType() {
        return this.sessionType;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setSessionType(String sessionType) {
		this.sessionType = sessionType;
	}
}