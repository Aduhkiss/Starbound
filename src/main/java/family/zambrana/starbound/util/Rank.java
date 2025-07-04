package family.zambrana.starbound.util;

public enum Rank {
    PLAYER("Player", "§7", 0),
    VIP("VIP", "§a[VIP] ", 1),
    VIP_PLUS("VIP+", "§a[VIP§6+§a] ", 2),
    MVP("MVP", "§b[MVP] ", 3),
    MVP_PLUS("MVP+", "§b[MVP§c+§b] ", 4),
    MVP_PLUS_PLUS("MVP++", "§6[MVP§c++§6] ", 5),

    // Special
    YOUTUBE("Youtuber", "§c[§fYOUTUBE§c] ", 6),
    MOJANG("Mojang", "§6[MOJANG] ", 6),
    EVENTS("Events", "§e[EVENTS] ", 7),
    MCP("MasterControl", "§4[MCP] ", 99),
    PIG("Pig", "§d[PIG] ", 6),
    PIG_PLUS("Pig+", "§d[PIG§c+§d] ", 6),
    PIG_PLUS_PLUS("Pig++", "§d[PIG§c++§d] ", 6),
    PIG_PLUS_PLUS_PLUS("Pig+++", "§d[PIG§c+++§d] ", 6),

    // Staff
    GM("Game Master", "§2[GM] ", 8),
    ADMIN("Admin", "§c[ADMIN] ", 9),
    OWNER("Owner", "§c[OWNER] ", 10),

    // April Fool's
    LOL("LOL", "§b[LOL] ", 1),
    LOL_PLUS("LOL+", "§b[LOL§d+§b] ", 2),
    WAT("WAT", "§b[WAT] ", 3),
    WAT_PLUS("WAT+", "§b[WAT§d+§b] ", 4),

    // Removed Staff
    HELPER("Helper", "§9[HELPER] ", 7),
    JR_HELPER("Jr Helper", "§9[Jr HELPER] ", 7),
    MOD("Moderator", "§2[MOD] ", 8),
    BUILD_TEAM("Build Team", "§3[BUILD TEAM] ", 7),

    // Removed Special
    SPECIAL("Special", "§7[SPECIAL] ", 6),
    RETIRED("Retired", "§7[RETIRED] ", 6),
    BETA_TESTER("Beta Tester", "§d[BETA TESTER] ", 6),
    GOD("God", "§6[GOD] ", 6),
    ABOVE_THE_RULES("Above The Rules", "§c[ABOVE THE RULES] ", 6),
    MCPROHOSTING("MCProHosting", "§2[MCProHosting] ", 6),
    APPLE("Apple", "§c[APPLE] ", 6),
    MIXER("Mixer", "§9[MIXER] ", 6),
    BEAM("Beam", "§b[BEAM] ", 6),
    ANGUS("Angus", "§6[ANGUS] ", 6),
    SLOTH("Sloth", "§d[SLOTH] ", 6),
    CRINGE("Cringe", "§e[CRINGE] ", 6),
    SALMON("Salmon", "§d[SALMON] ", 6),

    // SkyBlock Exclusives
    MINISTER("Minister", "§c[MINISTER] ", 6),
    MAYOR("Mayor", "§d[MAYOR] ", 6);

    private final String name;
    private final String prefix;
    private int level;

    Rank(String name, String prefix, int level) {
        this.name = name;
        this.prefix = prefix;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }
    public int getLevel() {
        return level;
    }
}
