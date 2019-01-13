package com.projects.bigdata.data_streaming.utility;

import static java.util.concurrent.ThreadLocalRandom.current;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.bigdata.utility.Utility;
import com.projects.bigdata.utility.trade.Direction;
import com.projects.bigdata.utility.trade.Exchange;
import com.projects.bigdata.utility.trade.Trade;

/**
 * {@code Supplier} implementations to be passed in to {@code DataStreamingTCPServer}, when it comes to building a string to be sent over the network through {@code Socket}s.
 * 
 * */
public final class StreamingLineSupplier {
	private static final Logger logger = LoggerFactory.getLogger(StreamingLineSupplier.class);
	private static final ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * It generates a phrase made up by randomly generated words.
	 * 
	 * @return Randomly generated phrase. 
	 * */
	public static String randomPhrase() {
		final int maxNrWordsPerPhrase = 100;
		final int maxNrWordPostfix = 999;
		final var phrase = IntStream.range(1, ThreadLocalRandom.current().nextInt(1, maxNrWordsPerPhrase) + 1).
		mapToObj(i -> "Word"+ThreadLocalRandom.current().nextInt(1, maxNrWordPostfix)).collect(Collectors.joining(" "));
		return phrase;
	}
	
	/**
	 * It randomly generates a {@code Trade} obejct and converts it to JSON.
	 * 
	 * @return JSON representations of Trade object.
	 * */
	public static String randomTrade() {
		try {
			return mapper.writeValueAsString(new Trade(Utility.Symbols.get(current().nextInt(Utility.Symbols.size())), getRandomEnumValue(Direction.class), current().nextInt(1, 100), 
					new BigDecimal(current().nextDouble(1.0, 999.0)), getRandomEnumValue(Exchange.class))).toString();
		} catch (final JsonProcessingException e) {
			logger.warn("Unable to build JSON string.", e);
			return "";
		}
	}
	
	private static <T extends Enum<?>> T getRandomEnumValue(final Class<T> clazz){
        return clazz.getEnumConstants()[current().nextInt(clazz.getEnumConstants().length)];
    }
}