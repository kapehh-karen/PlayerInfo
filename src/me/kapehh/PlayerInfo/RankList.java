package me.kapehh.PlayerInfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karen on 12.10.2014.
 */
public class RankList {

    public static class RankListItem {
        String rankName;
        int kills;
        int mobs;
        int playtime;

        public RankListItem(String rankName, int kills, int mobs, int playtime) {
            this.rankName = rankName;
            this.kills = kills;
            this.mobs = mobs;
            this.playtime = playtime;
        }

        public String getRankName() {
            return rankName;
        }

        public int getKills() {
            return kills;
        }

        public int getMobs() {
            return mobs;
        }

        public int getPlaytime() {
            return playtime;
        }

        @Override
        public String toString() {
            return "RankListItem{" +
                    "rankName='" + rankName + '\'' +
                    ", kills=" + kills +
                    ", mobs=" + mobs +
                    ", playtime=" + playtime +
                    '}';
        }
    }

    List<RankListItem> rankListItems = new ArrayList<RankListItem>();

    public RankList() {
        DBHelper dbHelper = Main.instance.getDbHelper();

        if (dbHelper == null) {
            return;
        }

        try {
            DBHelper.DBHelperResult helperResult = dbHelper.queryStart("SELECT * FROM ranklist");
            ResultSet result = helperResult.getResultSet();
            while (result.next()) {
                RankListItem item = new RankListItem(
                    result.getString("rankname"),
                    result.getInt("kills"),
                    result.getInt("mobs"),
                    result.getInt("playtime")
                );
                rankListItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public RankListItem getRankListItem(String rankName) {
        for (RankListItem item : rankListItems) {
            if (item.getRankName().equalsIgnoreCase(rankName)) {
                return item;
            }
        }
        return null;
    }

    public RankListItem getNextRankListItem(String rankName) {
        int r = rankListItems.size() - 1;
        for (int i = 0; i < (rankListItems.size() - 1); i++) {
            if (rankListItems.get(i).getRankName().equalsIgnoreCase(rankName)) {
                r = i + 1;
                break;
            }
        }
        return rankListItems.get(r);
    }

    public static int getLastKills(PlayerStat playerStat, RankListItem item) {
        int last = item.getKills() - playerStat.getPlayerKills();
        return (last < 0) ? 0 : last;
    }

    public static int getLastMobs(PlayerStat playerStat, RankListItem item) {
        int last = item.getMobs() - playerStat.getMobKills();
        return (last < 0) ? 0 : last;
    }

    public static int getLastTime(PlayerStat playerStat, RankListItem item) {
        int last = item.getPlaytime() - playerStat.getPlaytime();
        return (last < 0) ? 0 : last;
    }

    @Override
    public String toString() {
        return "RankList{" +
                "rankListItems=" + rankListItems +
                '}';
    }
}
