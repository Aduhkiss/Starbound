package family.zambrana.starbound.util;

public enum Rank {
    PLAYER("Player", "§7"),
    VIP("VIP", "§a[VIP] "),
    VIP_PLUS("VIP+", "§a[VIP§6+§a] "),
    MVP("MVP", "§b[MVP] "),
    MVP_PLUS("MVP+", "§b[MVP§c+§b] "),
    MVP_PLUS_PLUS("MVP++", "§6[MVP§c++§6] "),

    // Special
    YOUTUBE("Youtuber", "§c[§fYOUTUBE§c] "),
    MOJANG("Mojang", "§6[MOJANG] "),
    EVENTS("Events", "§e[EVENTS] "),
    MCP("MasterControl", "§4[MCP] "),
    PIG("Pig", "§d[PIG] "),
    PIG_PLUS("Pig+", "§d[PIG§c+§d] "),
    PIG_PLUS_PLUS("Pig++", "§d[PIG§c++§d] "),
    PIG_PLUS_PLUS_PLUS("Pig+++", "§d[PIG§c+++§d] "),

    // Staff
    GM("Game Master", "§2[GM] "),
    ADMIN("Admin", "§c[ADMIN] "),
    OWNER("Owner", "§c[OWNER] "),

    // April Fool's
    LOL("LOL", "§b[LOL] "),
    LOL_PLUS("LOL+", "§b[LOL§d+§b] "),
    WAT("WAT", "§b[WAT] "),
    WAT_PLUS("WAT+", "§b[WAT§d+§b] "),

    JER("JERRY", "§a[JER§r] "),
    JER_PLUS("JERRY+", "§a[JER§c+§a] "),
    JAY("JAY", "§b[JAY] "),
    JAY_PLUS("JAY+", "§b[JAY§d+§b] "),
    JAY_PLUS_PLUS("JAY++", "§b[JAY§d++§b] "),
    JERAY("JERAY", "§9[JERAY] "),
    JERAY_PLUS("JERAY+", "§a[JERAY§c+§a] "),
    SA_JERAY("SA JERAY", "§c[SA JERAY] "),
    YERAY("YERAY", "§c[YERAY] "),
    JERAY_PLUS_PLUS_PLUS("JERAY+++", "§d[JERAY§5+++§d] "),

    // Removed Staff
    HELPER("Helper", "§9[HELPER] "),
    JR_HELPER("Jr Helper", "§9[Jr HELPER] "),
    MOD("Moderator", "§2[MOD] "),
    BUILD_TEAM("Build Team", "§3[BUILD TEAM] "),

    // Removed Special
    SPECIAL("Special", "§7[SPECIAL] "),
    RETIRED("Retired", "§7[RETIRED] "),
    BETA_TESTER("Beta Tester", "§d[BETA TESTER] "),
    GOD("God", "§6[GOD] "),
    ABOVE_THE_RULES("Above The Rules", "§c[ABOVE THE RULES] "),
    MCPROHOSTING("MCProHosting", "§2[MCProHosting] "),
    APPLE("Apple", "§c[APPLE] "),
    MIXER("Mixer", "§9[MIXER] "),
    BEAM("Beam", "§b[BEAM] "),
    ANGUS("Angus", "§6[ANGUS] "),
    SLOTH("Sloth", "§d[SLOTH] "),
    CRINGE("Cringe", "§e[CRINGE] "),
    SALMON("Salmon", "§d[SALMON] "),

    // SkyBlock Exclusives
    MINISTER("Minister", "§c[MINISTER] "),
    MAYOR("Mayor", "§5[MAYOR] ");

    private final String name;
    private final String prefix;

    Rank(String name, String prefix) {
        this.name = name;
        this.prefix = prefix;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }
}
