package edu.cis350.mosstalkwords;

import android.os.Parcel;
import android.os.Parcelable;

public class Image implements Parcelable {
	
	private String category;
	private String word;
	private String url;
	private int frequency;
	private int length;
	private int imageability;
	private boolean favourite;
	private int level;
	
	// BEGINNNING --- IMPLEMENT PARCELABLE INTERFACE
	public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(category);
        out.writeString(word);
        out.writeString(url);
        out.writeInt(frequency);
        out.writeInt(length);
        out.writeInt(imageability);
        out.writeInt(favourite ? 1 : 0);
    }
    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
    private Image(Parcel in) {
    	category = in.readString();
    	word = in.readString();
    	url = in.readString();
    	frequency = in.readInt();
    	length = in.readInt();
    	imageability = in.readInt();
    	favourite = in.readInt() == 1;
    }
	// END --- IMPLEMENT PARCELABLE INTERFACE
    
	public Image(String word, String category, String imageability, String length, String level, String frequency, String url) {
		try {
			this.setCategory(category);
			this.setWord(word);
			this.setUrl(url);
			//this.setFrequency(Integer.parseInt(frequency));
			//this.setLength(Integer.parseInt(length));
			//this.setImageability(Integer.parseInt(imageability));
			this.setFavourite(false);
			this.setLevel(level);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public int getImageability() {
		return imageability;
	}

	public void setImageability(int imageability) {
		this.imageability = imageability;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public boolean isFavourite(){
		return favourite;
	}
	
	public void setFavourite(boolean favourite){
		this.favourite = favourite;
	}
	
	public void toggleFavourite(){
		favourite = !favourite;
	}
	
	public boolean equals(Object obj){
		if(obj instanceof Image) {
			Image other = (Image) obj;
			return     category.equals(other.category) 
					&& word.equals(other.word)
					&& url.equals(other.url)
					&& frequency == other.frequency
					&& length == other.length
					&& imageability == other.imageability
					&& favourite == other.favourite;
		}
		else
			return false;
	}
	
	public String toString(){
		return    category + " / " 
				+ word + " / "
				+ url + " / "
				+ frequency + " / "
				+ length + " / "
				+ imageability + " / "
				+ favourite;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = Integer.parseInt(level);
	}
}
