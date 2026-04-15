package model;
import java.util.Stack;

public class Scoreboard {
  private String HomeTeam;
  private int HomeScore;

  private String AwayTeam;
  private int AwayScore;


  private String LastActionDescription;
  private Stack<ScoringAction> GameHistory;

  // Helper to represent a scoring action
  private static class ScoringAction {
    String team;
    int points;

    ScoringAction(String team, int points) {
      this.team = team;
      this.points = points;
    }
  }

  // Default constructor
  public Scoreboard() {
    HomeTeam = "";
    AwayTeam = "";
    HomeScore = 0;
    AwayScore = 0;
    LastActionDescription = "Start of the game";
    GameHistory = new Stack<>();
  }

  //Setters and Getters
  //------ [Setting the name of both teams] ------
  public void setTeamNames(String _HomeTeam, String _AwayTeam) {
    //checking to see if either of the names are invalid
    if (_HomeTeam == null || _HomeTeam.trim().isEmpty()) {throw new IllegalAccessError("Home team name cannot be blank!");}
    if (_AwayTeam == null || _AwayTeam.trim().isEmpty()) {throw new IllegalAccessError("Away team name cannot be blank!");}

    //otherwise
    HomeTeam = _HomeTeam;
    AwayTeam = _AwayTeam;
  }

  //------ [Point Adders] ------
  public void addPointsToHome(int points) {
    //checking to make sure the team is set and valid amount of points
    requireTeamNames();
    requirePositivePoints(points);

    GameHistory.push(new ScoringAction("Home", points));
    HomeScore += points;
    LastActionDescription = HomeTeam + " +" + points;
  }

  public void addPointsToAway(int points) {
    //checking to make sure the team is set and valid amount of points
    requireTeamNames();
    requirePositivePoints(points);

    GameHistory.push(new ScoringAction("Away", points));
    AwayScore += points;
    LastActionDescription = AwayTeam + " +" + points;
  }

  //------ [Clear the game and restart] ------
  public void restart() {
    HomeScore = 0;
    AwayScore = 0;
    GameHistory.clear();
    LastActionDescription = "Game has been restarted!";
  }

  public void undoLast() {
    //Check to see if there is an action to undo
    if (GameHistory.isEmpty()) throw new IllegalStateException("There is no previous action!");

    //else
    ScoringAction last = GameHistory.pop();
    if (last.team.equals("Home")) HomeScore -= last.points;
    else AwayScore -= last.points;
    //updating the last action
    LastActionDescription = "Undo: removed +" + last.points + " from " + last.team;
  }


   //------ [Getters] ------
   public String getHomeName() {return HomeTeam;}
   public String getAwayName() {return AwayTeam;}
   public int getHomeScore() {return HomeScore;}
   public int getAwayScore() {return AwayScore;}
   public String getLastActionDescription() {return LastActionDescription;}
   public boolean hasTeamName() {return !HomeTeam.isEmpty() && !AwayTeam.isEmpty();}
   public boolean hasHistory() {return !GameHistory.isEmpty();}

   //------ [Private helpers] ------
   private void requireTeamNames() {
    if (!hasTeamName()) throw new IllegalStateException("Team names must be set before marking either team for points!");
   }
   private void requirePositivePoints(int points) {
    if (points <= 0) throw new IllegalArgumentException("Number of piitns must be greater than 0!");
   }  


}
