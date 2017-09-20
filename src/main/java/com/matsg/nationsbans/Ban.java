package com.matsg.nationsbans;

import java.sql.Timestamp;
import java.util.UUID;

public class Ban {

    private String playerName, reason, staffName;
    private Timestamp banDate, pardonDate;
    private UUID playerUUID;

    public Ban(UUID playerUUID, String playerName, String staffName, String reason, Timestamp pardonDate) {
        this.playerUUID = playerUUID;
        this.playerName = playerName;
        this.staffName = staffName;
        this.reason = reason;
        this.banDate = new Timestamp(System.currentTimeMillis());
        this.pardonDate = pardonDate;
    }

    public Ban(UUID playerUUID, String playerName, String staffName, String reason, Timestamp banDate, Timestamp pardonDate) {
        this(playerUUID, playerName, staffName, reason, pardonDate);
        this.banDate = banDate;
    }

    public Timestamp getBanDate() {
        return banDate;
    }

    public Timestamp getPardonDate() {
        return pardonDate;
    }

    public String getPlayerName() {
        return playerName;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String getReason() {
        return reason;
    }

    public String getStaffName() {
        return staffName;
    }
}