package me.philippheuer.twitch4j.endpoints;

import com.jcabi.log.Logger;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.exceptions.RestException;
import me.philippheuer.twitch4j.model.*;
import me.philippheuer.util.annotation.Unofficial;
import me.philippheuer.util.rest.QueryRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * All api methods related to a stream.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Getter
@Setter
public class StreamEndpoint extends AbstractTwitchEndpoint {

	/**
	 * Stream Endpoint
	 *
	 * @param twitchClient The Twitch Client.
	 */
	public StreamEndpoint(TwitchClient twitchClient) {
		super(twitchClient);
	}

	/**
	 * Get Stream by Channel
	 * <p>
	 * Gets stream information (the stream object) for a specified channel.
	 * Requires Scope: none
	 *
	 * @param channel Get stream object of Channel Entity
	 * @return Optional of type Stream is only Present if Stream is Online, returns Optional.empty for Offline streams
	 */
	public Optional<Stream> getByChannel(Channel channel) {
		// Endpoint
		String requestUrl = String.format("%s/streams/%s", getTwitchClient().getTwitchEndpoint(), channel.getId());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// REST Request
		try {
			StreamSingle responseObject = restTemplate.getForObject(requestUrl, StreamSingle.class);

			// Stream Offline
			if (responseObject.getStream() == null) {
				// Stream Offline
				return Optional.empty();
			} else {
				// Stream Online
				return Optional.ofNullable(responseObject.getStream());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * Get All Streams (ordered by current viewers, desc)
	 * <p>
	 * Gets the list of all live streams.
	 * Requires Scope: none
	 *
	 * @param limit       Maximum number of most-recent objects to return. Default: 25. Maximum: 100.
	 * @param offset      Object offset for pagination of results. Default: 0.
	 * @param language    Restricts the returned streams to the specified language. Permitted values are locale ID strings, e.g. en, fi, es-mx.
	 * @param game        Restricts the returned streams to the specified game.
	 * @param channelIds  Receives the streams from a comma-separated list of channel IDs.
	 * @param stream_type Restricts the returned streams to a certain stream type. Valid values: live, playlist, all. Playlists are offline streams of VODs (Video on Demand) that appear live. Default: live.
	 * @return Returns all streams that match with the provided filtering.
	 */
	public List<Stream> getAll(Optional<Long> limit, Optional<Long> offset, Optional<String> language, Optional<Game> game, Optional<String> channelIds, Optional<String> stream_type) {
		// Endpoint
		String requestUrl = String.format("%s/streams", getTwitchClient().getTwitchEndpoint());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", limit.orElse(25l).toString()));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("offset", offset.orElse(0l).toString()));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("language", language.orElse(null)));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("game", game.map(Game::getName).orElse(null)));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("channel", channelIds.isPresent() ? channelIds.get() : null));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("stream_type", stream_type.orElse("all")));

		// REST Request
		try {
			StreamList responseObject = restTemplate.getForObject(requestUrl, StreamList.class);

			return responseObject.getStreams();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}

		return null;
	}

	/**
	 * Get Followed Streams
	 * <p>
	 * Gets the list of online streams a user follows based on the OAuthTwitch token provided.
	 * Requires Scope: user_read
	 *
	 * @param credential The user.
	 * @return All streams as user follows.
	 */
	public List<Stream> getFollowed(OAuthCredential credential) {
		// Endpoint
		String requestUrl = String.format("%s/streams/followed", getTwitchClient().getTwitchEndpoint());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getPrivilegedRestTemplate(credential);

		// REST Request
		try {
			StreamList responseObject = restTemplate.getForObject(requestUrl, StreamList.class);

			return responseObject.getStreams();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return new ArrayList<Stream>();
	}

	/**
	 * Get Featured Streams
	 * <p>
	 * Gets a list of all featured live streams.
	 * Requires Scope: none
	 *
	 * @param limit  Maximum number of most-recent objects to return. Default: 25. Maximum: 100.
	 * @param offset Object offset for pagination of results. Default: 0.
	 * @return The requested range/amount of featured streams.
	 */
	public List<StreamFeatured> getFeatured(Optional<Long> limit, Optional<Long> offset) {
		// Endpoint
		String requestUrl = String.format("%s/streams/featured", getTwitchClient().getTwitchEndpoint());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("limit", limit.orElse(25l).toString()));
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("offset", offset.orElse(0l).toString()));

		// REST Request
		try {
			StreamFeaturedList responseObject = restTemplate.getForObject(requestUrl, StreamFeaturedList.class);

			return responseObject.getFeatured();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return new ArrayList<StreamFeatured>();
	}

	/**
	 * Get Streams Summary
	 * <p>
	 * Gets a summary of all live streams.
	 * Requires Scope: none
	 *
	 * @param game Restricts the summary stats to the specified game.
	 * @return A <code>StreamSummary</code> object, that contains the total number of live streams and viewers.
	 */
	public StreamSummary getSummary(Optional<Game> game) {
		// Endpoint
		String requestUrl = String.format("%s/streams/summary", getTwitchClient().getTwitchEndpoint());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("game", game.map(Game::getName).orElse("")));

		// REST Request
		try {
			Logger.trace(this, "Rest Request to [%s]", requestUrl);
			StreamSummary responseObject = restTemplate.getForObject(requestUrl, StreamSummary.class);

			return responseObject;
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * Get recommended streams for User (Unofficial)
	 * Gets a list of recommended streams for a user.
	 * Requires Scope: none
	 *
	 * @param credential OAuthCredential of the user, you want to request recommendations for.
	 * @return StreamList of random Streams.
	 */
	@Unofficial
	public List<Recommendation> getRecommendations(OAuthCredential credential) {
		// Endpoint
		String requestUrl = String.format("%s/streams/recommended", getTwitchClient().getTwitchEndpoint());
		RestTemplate restTemplate = getTwitchClient().getRestClient().getRestTemplate();

		// Parameters
		restTemplate.getInterceptors().add(new QueryRequestInterceptor("oauth_token", credential.getToken()));

		// REST Request
		try {
			Logger.trace(this, "Rest Request to [%s]", requestUrl);
			RecommendationList responseObject = restTemplate.getForObject(requestUrl, RecommendationList.class);

			return responseObject.getRecommendedStreams();
		} catch (RestException restException) {
			Logger.error(this, "RestException: " + restException.getRestError().toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}
}
