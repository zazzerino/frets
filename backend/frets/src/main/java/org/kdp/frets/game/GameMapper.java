package org.kdp.frets.game;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.util.Arrays;

public class GameMapper implements RowMapper<Game>
{
    @Override
    public Game map(ResultSet rs, StatementContext ctx)
    {
        try {
            final var id = rs.getLong("id");
            final var createdAt = rs.getTimestamp("created_at").toInstant();
            final var hostId = rs.getLong("host_id");
            final var state = Game.State.valueOf(rs.getString("state"));

            final var game = new Game(id, createdAt, hostId, state);

            if (rs.getObject("player_ids") != null) {
                final var playerIds = (Long[]) rs.getArray("player_ids").getArray();
                game.setPlayerIds(Arrays.asList(playerIds));
            }

            return game;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

//        final var roundCount = rs.getInt("round_count");
//
//        final var stringsArray = (Integer[]) rs.getArray("strings_to_use").getArray();
//        final var stringsToUse = Arrays.stream(stringsArray).collect(Collectors.toSet());
//
//        final var accidentalsArray = (String[]) rs.getArray("accidentals_to_use").getArray();
//        final var accidentalsToUse = Arrays
//                .stream(accidentalsArray)
//                .map(Accidental::valueOf)
//                .collect(Collectors.toSet());
//
//        final var tuningArray = (String[]) rs.getArray("tuning").getArray();
//        final var tuning = Arrays.stream(tuningArray).toList();
//
//        final var startFret = rs.getInt("start_fret");
//        final var endFret = rs.getInt("end_fret");

//        final var playerArray = rs.getArray("player_ids");
//        Set<Long> playerIds = null;
//
//        if (playerArray != null) {
//            final var intArray = (Integer[]) playerArray.getArray();
//            playerIds = Arrays.stream(intArray)
//                    .map(Integer::longValue)
//                    .collect(Collectors.toSet());
//        }

//        game.setRoundCount(roundCount);
//        game.setStringsToUse(stringsToUse);
//        game.setAccidentalsToUse(accidentalsToUse);
//        game.setTuning(tuning);
//        game.setStartFret(startFret);
//        game.setEndFret(endFret);

//        if (playerIds != null) {
//            game.setPlayerIds(playerIds);
//        }
