package org.nusco.narjillos.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.experiment.Stat;

public class PersistentHistory extends PersistentStorage implements History {

	public PersistentHistory(String name) {
		super(name);
		createStatsTable();
	}

	@Override
	public void saveStats(Experiment experiment) {
		Stat stat = new Stat(experiment);
		if (contains(stat))
			return;
	    try {
	    	Statement stmt = getConnection().createStatement();
	    	String sql = "INSERT INTO STATS (TICKS, RUNNING_TIME, " +
	    				 "NUMBER_OF_NARJILLOS, NUMBER_OF_FOOD_PELLETS, " +
	    				 "CURRENT_POOL_SIZE, HISTORYCAL_POOL_SIZE, AVERAGE_GENERATION, " +
	    				 "OXYGEN, HYDROGEN, NITROGEN, " + 
	    				 "O2H, O2N, H2O, H2N, N2O, N2H, Z2O, Z2H, Z2N) VALUES (" + 
	    				 stat.ticks + ", " +
	    				 stat.runningTime + ", " +
	    				 stat.numberOfNarjillos + ", " +
	    				 stat.numberOfFoodPellets + ", " +
	    				 stat.currentPoolSize + ", " +
	    				 stat.historicalPoolSize + ", " +
	    				 stat.averageGeneration + ", " +
	    				 stat.oxygen + ", " +
	    				 stat.hydrogen + ", " +
	    				 stat.nitrogen + ", " +
	    				 stat.o2h + ", " +
	    				 stat.o2n + ", " +
	    				 stat.h2o + ", " +
	    				 stat.h2n + ", " +
	    				 stat.n2o + ", " +
	    				 stat.n2h + ", " +
	    				 stat.z2o + ", " +
	    				 stat.z2h + ", " +
	    				 stat.z2n + ");";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Stat getLatestStats() {
		try {
			Statement statement = getConnection().createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM STATS WHERE TICKS = (SELECT MAX(TICKS) FROM STATS);");
			if (!rs.next())
				return null;

			return toStat(rs);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Stat> getStats() {
		try {
			List<Stat> result = new LinkedList<>();
			Statement statement = getConnection().createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM STATS ORDER BY TICKS;");
			while (rs.next())
				result.add(toStat(rs));
			return result;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void createStatsTable() {
		try {
			Statement stmt = getConnection().createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS STATS "
					+ "(TICKS                   INT PRIMARY KEY     NOT NULL,"
					+ " RUNNING_TIME            INT                 NOT NULL,"
					+ " NUMBER_OF_NARJILLOS     INT                 NOT NULL,"
					+ " NUMBER_OF_FOOD_PELLETS  INT                 NOT NULL,"
					+ " CURRENT_POOL_SIZE       INT                 NOT NULL,"
					+ " HISTORYCAL_POOL_SIZE    INT                 NOT NULL,"
					+ " AVERAGE_GENERATION      DOUBLE              NOT NULL,"
					+ " OXYGEN                  DOUBLE              NOT NULL,"
					+ " HYDROGEN                DOUBLE              NOT NULL,"
					+ " NITROGEN                DOUBLE              NOT NULL,"
					+ " O2H                     INT                 NOT NULL,"
					+ " O2N                     INT                 NOT NULL,"
					+ " H2O                     INT                 NOT NULL,"
					+ " H2N                     INT                 NOT NULL,"
					+ " N2O                     INT                 NOT NULL,"
					+ " N2H                     INT                 NOT NULL,"
					+ " Z2O                     INT                 NOT NULL,"
					+ " Z2H                     INT                 NOT NULL,"
					+ " Z2N                     INT                 NOT NULL)";
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean contains(Stat stat) {
		try {
			Statement statement = getConnection().createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM STATS WHERE TICKS = " + stat.ticks + ";");
			return rs.next();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private Stat toStat(ResultSet rs) throws SQLException {
		return new Stat(rs.getInt("TICKS"),
						rs.getInt("RUNNING_TIME"),
						rs.getInt("NUMBER_OF_NARJILLOS"),
						rs.getInt("NUMBER_OF_FOOD_PELLETS"),
						rs.getInt("CURRENT_POOL_SIZE"),
						rs.getInt("HISTORYCAL_POOL_SIZE"),
						rs.getDouble("AVERAGE_GENERATION"),
						rs.getDouble("OXYGEN"),
						rs.getDouble("HYDROGEN"),
						rs.getDouble("NITROGEN"),
						rs.getInt("O2H"),
						rs.getInt("O2N"),
						rs.getInt("H2O"),
						rs.getInt("H2N"),
						rs.getInt("N2O"),
						rs.getInt("N2H"),
						rs.getInt("Z2O"),
						rs.getInt("Z2H"),
						rs.getInt("Z2N"));
	}
}
