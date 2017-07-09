package services;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import db.DbManager;
import db.LevelSolutionData;
/**
 * Sokoban services.
 * handle GET and POST.
 * 
 */
@Path("/SokobanServices")
public class SokobanServices {
	private DbManager db;
	private Gson gson;

	public SokobanServices() {
		this.db = DbManager.getInstance();
		this.gson = new GsonBuilder().create();
	}

	/**
	 * Checking if the solution exists in the DB by the levelId.
	 * @param levelId
	 * @return the solution or null 
	 */
	@GET
	@Path("/get/{levelId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getUser(@PathParam("levelId") String levelId) {

		// get LevelSolutionData from DB
		LevelSolutionData levelSolData = db.getLevelSolution(levelId);
		
		String jsonLevelSolutionData = gson.toJson(levelSolData);
		
		// return Json to client
		return jsonLevelSolutionData;
	}

	/**
	 * Adding the solution to the DB
	 * @param jsonLevelSolutionData The Solution wrapped by json
	 */
	@POST
	@Path("/add")
	public void addName(String jsonLevelSolutionData) {


		// from Json
		LevelSolutionData levelSolData = gson.fromJson(jsonLevelSolutionData, LevelSolutionData.class);

		//System.out.println("levelSolData: " + levelSolData.toString());
		// add to DB
		db.add(levelSolData);

	}

}
