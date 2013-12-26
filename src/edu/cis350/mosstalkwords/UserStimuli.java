package edu.cis350.mosstalkwords;

import java.util.Calendar;

public class UserStimuli {
    private String imageName;
    private String Category;
    private int isFavorite;
    private int attempts;
    private int correctAttempts;
    private int soundHints;
    private int playwordHints;
    private int noHint;
    private Calendar lastSeen;
    private double difficulty;
    private String url;
    
    public UserStimuli(){
    	
    }
    
    public UserStimuli(String imageName, String category, int isFavorite,
			int attempts, int correctAttempts, int soundHints,
			int playwordHints, int noHint, Calendar lastSeen,
			double difficulty, String url) {
		super();
		this.imageName = imageName;
		Category = category;
		this.isFavorite = isFavorite;
		this.attempts = attempts;
		this.correctAttempts = correctAttempts;
		this.soundHints = soundHints;
		this.playwordHints = playwordHints;
		this.noHint = noHint;
		this.lastSeen = lastSeen;
		this.difficulty = difficulty;
		this.url = url;
	}
        
        public String getImageName() {
                return imageName;
        }
        public void setImageName(String imageName) {
                this.imageName = imageName;
        }
        public int getIsFavorite() {
                return isFavorite;
        }
        public void setIsFavorite(int isFavorite) {
                this.isFavorite = isFavorite;
        }
        public int getAttempts() {
                return attempts;
        }
        public void setAttempts(int attempts) {
                this.attempts = attempts;
        }
        public int getCorrectAttempts() {
                return correctAttempts;
        }
        public void setCorrectAttempts(int correctAttempts) {
                this.correctAttempts = correctAttempts;
        }
        public int getSoundHints() {
                return soundHints;
        }
        public void setSoundHints(int soundHints) {
                this.soundHints = soundHints;
        }
        public int getPlaywordHints() {
                return playwordHints;
        }
        public void setPlaywordHints(int playwordHints) {
                this.playwordHints = playwordHints;
        }
        public int getNoHint() {
                return noHint;
        }
        public void setNoHint(int noHint) {
                this.noHint = noHint;
        }
        public Calendar getLastSeen() {
                return lastSeen;
        }
        public void setLastSeen(Calendar lastSeen) {
                //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

                //Calendar cd = Calendar.getInstance();
                this.lastSeen = lastSeen;
        }
        public double getDifficulty() {
                return difficulty;
        }
        public void setDifficulty(double difficulty) {
                this.difficulty = difficulty;
        }
        public String getUrl() {
                return url;
        }
        public void setUrl(String url) {
                this.url = url;
        }
        public String getCategory() {
                return Category;
        }
        public void setCategory(String category) {
                Category = category;
        }
    
}