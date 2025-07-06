package family.zambrana.starbound.util;

public enum Rank {
    PLAYER("Player", "§7", 0, "Default rank with no special privileges."),
    VIP("VIP", "§a[VIP] ", 1, "Supporter rank with light perks."),
    VIP_PLUS("VIP+", "§a[VIP§6+§a] ", 2, "Upgraded VIP with more cosmetics."),
    MVP("MVP", "§b[MVP] ", 3, "Premium rank with exclusive features."),
    MVP_PLUS("MVP+", "§b[MVP§c+§b] ", 4, "Enhanced MVP access with extra perks."),
    MVP_PLUS_PLUS("MVP++", "§6[MVP§c++§6] ", 5, "Top-tier donator with access to private features."),

    // Special
    YOUTUBE("Youtuber", "§c[§fYOUTUBE§c] ", 6, "Granted to recognized YouTube content creators."),
    MOJANG("Mojang", "§6[MOJANG] ", 6, "Mojang employees. Treat them well."),
    EVENTS("Events", "§e[EVENTS] ", 7, "Hosts and coordinates server events."),
    MCP("MasterControl", "§4[MCP] ", 99, "Server overseer with ultimate authority."),
    PIG("Pig", "§d[PIG] ", 6, "A porky token of appreciation."),
    PIG_PLUS("Pig+", "§d[PIG§c+§d] ", 6, "Enhanced pork privileges."),
    PIG_PLUS_PLUS("Pig++", "§d[PIG§c++§d] ", 6, "Double bacon. Double power."),
    PIG_PLUS_PLUS_PLUS("Pig+++", "§d[PIG§c+++§d] ", 6, "Triple pork overload."),

    // Staff
    GAME_MASTER("Game Master", "§2[GM] ", 8, "Manages in-game balance and mechanics."),
    ADMIN("Admin", "§c[ADMIN] ", 9, "Has full administrative permissions."),
    OWNER("Owner", "§c[OWNER] ", 10, "Server owner with absolute control."),

    // Removed Staff
    HELPER("Helper", "§9[HELPER] ", 7, "Legacy support role."),
    JR_HELPER("Jr Helper", "§9[Jr HELPER] ", 7, "Trial helper rank."),
    MODERATOR("Moderator", "§2[MOD] ", 8, "Handled chat moderation and rule enforcement."),
    BUILD_TEAM("Build Team", "§3[BUILD TEAM] ", 7, "Helped build the world you're in."),

    // Removed Special
    SPECIAL("Special", "§7[SPECIAL] ", 6, "Honorable mention."),
    RETIRED("Retired", "§7[RETIRED] ", 6, "Former staff with legacy status."),
    BETA_TESTER("Beta Tester", "§d[BETA TESTER] ", 6, "Assisted in testing the server before release."),
    GOD("God", "§6[GOD] ", 6, "All-powerful. Probably."),
    ABOVE_THE_RULES("Above The Rules", "§c[ABOVE THE RULES] ", 6, "You probably shouldn’t be seeing this."),
    MCPROHOSTING("MCProHosting", "§2[MCProHosting] ", 6, "Sponsored us once upon a time."),
    APPLE("Apple", "§c[APPLE] ", 6, "It just works."),
    MIXER("Mixer", "§9[MIXER] ", 6, "Gone but never forgotten."),
    BEAM("Beam", "§b[BEAM] ", 6, "Old-school Mixer status."),
    ANGUS("Angus", "§6[ANGUS] ", 6, "Premium beef."),
    SLOTH("Sloth", "§d[SLOTH] ", 6, "Slow and steady wins the race."),
    CRINGE("Cringe", "§e[CRINGE] ", 6, "Awarded for maximum embarrassment."),
    SALMON("Salmon", "§d[SALMON] ", 6, "Fresh and flopping."),

    // SkyBlock Exclusives
    MINISTER("Minister", "§c[MINISTER] ", 6, "Helps govern SkyBlock activities."),
    MAYOR("Mayor", "§d[MAYOR] ", 6, "Elected leader.");

    private final String name;
    private final String prefix;
    private final int level;
    private final String description;

    Rank(String name, String prefix, int level, String description) {
        this.name = name;
        this.prefix = prefix;
        this.level = level;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public static Rank safeValueOf(String input) {
        try {
            return Rank.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return Rank.PLAYER;
        }
    }
}
