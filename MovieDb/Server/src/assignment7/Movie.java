package assignment7;

public class Movie {
	String movieName, year, overview;
	
	public Movie(String movieName, String year, String overview) {
		super();
		this.movieName = movieName;
		this.year = year;
		this.overview = overview;
	}
	
	@Override
	public String toString(){
		String toReturn = year + "\n"
				+ movieName + "\n"
				+ overview + "\n";
		return toReturn;
	}
	
	public String getOverview(){
		return overview;
	}
	public void setOverview(String overview){
		this.overview = overview;
	}
	public String getMovieName() {
		return movieName;
	}
	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
}
