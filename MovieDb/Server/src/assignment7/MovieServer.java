package assignment7;

import java.io.BufferedReader;
import assignment7.Movie;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

/*A server that collects information about movies and returns them to a client!
 * 
 * This was just an assignment that practiced using APIs and parsing JSON.
 * 
 * After the server connects to the client, it asks the client for a year and then
 * number of movies.  Then it collects the top x movies from that year and returns
 * them as one giant string, separated by newlines.  Then, the server repeats.
 */

public class MovieServer{
	// :(
	static String API_KEY = "NIL";

	public static void main(String[] args) {
		System.out.println("Echo Server ...");
		Socket sSocket = null;
		try {
			//attempts to connect to client
			ServerSocket serverSocket = new ServerSocket(6000);
			System.out.println("Waiting for connection.....");
			sSocket = serverSocket.accept();
			System.out.println("Connected to client");
			serverSocket.close();
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}	
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(sSocket.getInputStream()));
			PrintWriter out = new PrintWriter(sSocket.getOutputStream(), true);
			String inputLine;
			out.println("Get information about the top movies!  Type 'quit' to quit.");
			while (true) {
				//collects year
				out.println("What year would you like a movie from?");
				inputLine = br.readLine();
				int year = Integer.parseInt(inputLine);
				System.out.println("Client request: " + inputLine);
				
				//collects number
				out.println("How many movies?");
				inputLine = br.readLine();
				int numMovies = Integer.parseInt(inputLine);
				System.out.println("Client request: " + inputLine);

				//generates movie output
				String json = fetchData(year);
				System.out.println(json);
				Movie[] movies = parseData(json, numMovies);
				for (int i = 0; i < numMovies; i++) {
					out.println(movies[i].toString());
				}
			}
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

	/*
	 * 'json' needs to be a valid response from themoviedb.com
	 * numMovies needs to be an integer greater than or equal to zero.
	 * 
	 * The function will return the top x movies contained in the json string.
	 */
	public static Movie[] parseData(String json, int numMovies) {
		if(numMovies < 0)
			return null;
		Movie[] movies = new Movie[numMovies];
		try {
			JSONObject page = new JSONObject(json);
			JSONArray JSONmovies = page.getJSONArray("results");
			if (JSONmovies.isNull(0))
				return null;
			for (int i = 0; i < numMovies; i++) {
				JSONObject tmpJson = JSONmovies.getJSONObject(i);
				String title = tmpJson.getString("title");
				String year = tmpJson.getString("release_date");
				String overview = tmpJson.getString("overview");
				movies[i] = new Movie(title, year, overview);;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return movies;
	}

	/*
	 * Collects a json response from themoviedb.com for a given year.
	 * Movies are sorted in order of popularity, descending.
	 */
	public static String fetchData(int year) {
		String jsonStr = "";
		HttpURLConnection urlConnection = null;
		BufferedReader reader = null;
		try {
			// build url
			String sUrl = "http://api.themoviedb.org/3/discover/movie?" 
					+ "api_key=" + API_KEY + "&year=" + year 
					+ "&sort_by=popularity.desc";
			URL url = new URL(sUrl);
			//make connection
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();
			InputStream inputStream = urlConnection.getInputStream();
			// Place input stream into a buffered reader
			reader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			StringBuilder buffer = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				buffer.append(line).append("\n");
			}
			jsonStr = buffer.toString();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return jsonStr;
	}
}