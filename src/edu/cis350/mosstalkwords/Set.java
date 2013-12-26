package edu.cis350.mosstalkwords;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/*
 * Set stores all of the information about the current set and performs
 * calculations such as calculation of streak, score, completeness, etc.
 * 
 */

public class Set implements Parcelable {

	private ArrayList<ImageStatistics> images;
	public final static int NUM_STARS = 5;
	final private double wordPenalty = 0.4;
	final private double soundPenalty = 0.6;

	public ArrayList<ImageStatistics> getImages() {
		return images;
	}

	public void resetImagesStatistics() {
		for (int i = 0; i < images.size(); i++)
			images.get(i).resetImageStatistics();
	}

	// BEGINNNING --- IMPLEMENT PARCELABLE INTERFACE
	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeTypedList(images);
	}

	public static final Parcelable.Creator<Set> CREATOR = new Parcelable.Creator<Set>() {
		public Set createFromParcel(Parcel in) {
			return new Set(in);
		}

		public Set[] newArray(int size) {
			return new Set[size];
		}
	};

	private Set(Parcel in) {
		images = new ArrayList<ImageStatistics>();
		in.readTypedList(images, ImageStatistics.CREATOR);
	}

	// END --- IMPLEMENT PARCELABLE INTERFACE

	public Set(List<ImageStatistics> imageList) {
		images = new ArrayList<ImageStatistics>();

		for (ImageStatistics i : imageList) {
			images.add(i);
		}
	}

	public String[] getWords() {
		String[] words = new String[images.size()];
		for (int i = 0; i < words.length; i++)
			words[i] = images.get(i).getImageName();
		return words;
	}

	public ImageStatistics get(int i) {
		return images.get(i);
	}

	public String getWord(int i) {
		return get(i).getImageName();
	}

	public int getSize() {
		return images.size();
	}

	public int getTotalScore() {
		return getTotalScore(getSize());
	}

	public int getScore(int imageIdx) {
		return getScore(imageIdx, false);
	}

	public int[] getScores() {
		int[] scores = new int[getSize()];
		for (int i = 0; i < scores.length; i++)
			scores[i] = getScore(i);
		return scores;
	}

	public int getStarScore() {
		double completeness = getCompleteness();
		double rangePerStar = 1.0 / (NUM_STARS + 1);
		int starScore = (int) (completeness / rangePerStar);
		return starScore;
	}

	public double getCompleteness() {
		int score = getTotalScore();
		int maxScore = getSize() * 100;
		double completeness = 1.0 * score / maxScore;
		return completeness;
	}

	public void incWordHint(int imageIdx) {
		ImageStatistics is = get(imageIdx);
		is.setWordHints(is.getWordHints() + 1);
	}

	public void setSolved(int imageIdx, boolean solved) {
		ImageStatistics is = get(imageIdx);
		is.setSolved(solved);
	}

	public void incAttempts(int imageIdx) {
		ImageStatistics is = get(imageIdx);
		is.setAttempts(is.getAttempts() + 1);
	}

	public void incSoundHint(int imageIdx) {
		ImageStatistics is = get(imageIdx);
		is.setSoundHints(is.getSoundHints() + 1);
	}

	public void setLastSeen(int imageIdx, long timeMillis) {
		ImageStatistics is = get(imageIdx);
		Date d = new Date(timeMillis);
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		is.setLastSeen(c);
	}

	public int getScore(int imageIdx, boolean ignoreSolvedStatus) {
		if (!ignoreSolvedStatus && !get(imageIdx).isSolved()) {
			if (get(imageIdx).getAttempts() > 0) { // if tries but failed,
													// return 20
				return 20;
			} else {
				return 0;
			}
		}

		int score = 100;
		if (get(imageIdx).isSeenToday()) {
			score = 80;
		}
		if (get(imageIdx).getWordHints() > 0)
			score = (int) (score * wordPenalty);
		else if (get(imageIdx).getSoundHints() > 0)
			score = (int) (score * soundPenalty);
		return score;
	}

	public int getTotalScore(int firstNImages) {
		int sum = 0;
		firstNImages = Math.min(firstNImages, getSize());
		for (int i = 0; i < firstNImages; i++)
			sum += getScore(i);
		return sum;
	}

	public int getLongestStreak() {
		int currentStreak = 0;
		int longestStreak = 0;
		int length = getSize();

		for (int i = 0; i < length; i++) {
			if (!images.get(i).isSolved()) {
				if (currentStreak > longestStreak) {
					longestStreak = currentStreak;
				}
				currentStreak = 0;
			} else {
				currentStreak++;
			}
		}
		if (longestStreak < currentStreak) {
			longestStreak = currentStreak;
		}
		return longestStreak;
	}

	public double getCompletenessPercent() {
		return Math.round(getCompleteness() * 1000) / 10;
	}

	public boolean equals(Object obj) {
		if (obj instanceof Set) {
			Set other = (Set) obj;
			return images.equals(other.images);
		} else
			return false;
	}

	public String generateSetReport(String[] images, String userName, int mode,
			int level) {
		String fullReport = "";
		fullReport += ("User: " + userName + "\n");
		if (mode == 35675)// cat
		{
			fullReport += ("Category: " + this.images.get(0).getCategory() + "\n");
		} else if (mode == 62230)// fav
		{
			fullReport += ("Favorites: " + "\n");
		} else {
			fullReport += ("WordQuest: Difficulty: " + level + "\n");
		}
		String efficiencyPercent = "" + getCompletenessPercent();
		fullReport += ("Completeness: " + efficiencyPercent + "%\n");
		fullReport += ("Longest Streak: " + getLongestStreak() + "\n");
		fullReport += ("\nImage By Image Statistics:\n\n");
		for (int i = 0; i < images.length; i++)
			fullReport += generateImageReport(images[i], i);

		fullReport += "\n----------------------------------------------------------------\n";
		return fullReport;
	}

	private String generateImageReport(String imageName, int index) {
		String imageReport = "";
		imageReport += imageName + ":\n";
		imageReport += "Score: " + getScore(index) + " ";
		imageReport += "Word Hint Used: " + images.get(index).getWordHints()
				+ " ";
		imageReport += "Syllable Hint Used: "
				+ images.get(index).getSoundHints() + " ";
		imageReport += "Attempts: " + images.get(index).getAttempts();
		imageReport += "\n";

		return imageReport;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (ImageStatistics img : images)
			sb.append(img).append(", ");
		sb.append("]");
		return sb.toString();
	}

}
