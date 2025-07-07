package family.zambrana.starbound.punishment;

public class Punishment {
    public enum Type { BAN, MUTE }

    private final String target;
    private final String reason;
    private final long issuedAt;
    private final long expiresAt;
    private final String punisher;
    private final Type type;

    public Punishment(String target, String reason, long issuedAt, long expiresAt, String punisher, Type type) {
        this.target = target;
        this.reason = reason;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.punisher = punisher;
        this.type = type;
    }

    public String getTarget() { return target; }
    public String getReason() { return reason; }
    public long getIssuedAt() { return issuedAt; }
    public long getExpiresAt() { return expiresAt; }
    public String getPunisher() { return punisher; }
    public Type getType() { return type; }
    public boolean isExpired() { return expiresAt != -1 && System.currentTimeMillis() > expiresAt; }
}
