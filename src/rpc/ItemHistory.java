package rpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Item;

/**
 * Servlet implementation class ItemHistory
 */
@WebServlet("/history")
public class ItemHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DBConnection conn = DBConnectionFactory.getDBConnection();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ItemHistory() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String userId = request.getParameter("user_id");
		Set<Item> items = conn.getFavoriteItems(userId);
		RpcHelper.writeJsonArray(response, RpcHelper.getJsonArray(items));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			JSONObject input = RpcHelper.readJsonObject(request);
			if (input.has("user_id") && input.has("favorite")) {
				String userId = (String) input.get("user_id");
				JSONArray array = (JSONArray) input.get("favorite");
				List<String> visitedEvents = new ArrayList<>();
				for (int i = 0; i < array.length(); i++) {
					String eventId = (String) array.get(i);
					visitedEvents.add(eventId);
				}
				conn.setFavoriteItems(userId, visitedEvents);
				RpcHelper.writeJsonObject(response, new JSONObject().put("status", "OK"));
			} else {
				RpcHelper.writeJsonObject(response, new JSONObject().put("status", "InvalidParameter"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			JSONObject input = RpcHelper.readJsonObject(request);
			if (input.has("user_id") && input.has("unfavorite")) {
				String userId = (String) input.get("user_id");
				JSONArray array = (JSONArray) input.get("unfavorite");
				List<String> unfavoredEvents = new ArrayList<>();
				for (int i = 0; i < array.length(); i++) {
					String eventId = (String) array.get(i);
					unfavoredEvents.add(eventId);
				}
				conn.unsetFavoriteItems(userId, unfavoredEvents);
				RpcHelper.writeJsonObject(response, new JSONObject().put("Unfavored", input));
			} else {
				RpcHelper.writeJsonObject(response, new JSONObject().put("status", "InvalidParameter"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
