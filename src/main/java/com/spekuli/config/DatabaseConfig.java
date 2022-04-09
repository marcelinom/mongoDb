package com.spekuli.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.util.ResourceUtils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
@ConfigurationProperties(prefix = "spring.database.mongodb")
public class DatabaseConfig {
	private static String PREFIX = "mongodb+srv://";

	/** Database user. */
	private String username;

	/** Database password. */
	private String password;

	/** Database host:port, separated by comma, in case of replicaset. */
	private String host;

	/** Database name. */
	private String database;
	
	public MongoClient mongoClient() throws IOException {
		String uri =
				new StringBuffer()
						.append(PREFIX)
						.append(username)
						.append(":")
						.append(password)
						.append("@")
						.append(host)
						.append("/")
						.append(database)
						.toString();

		System.out.println("uri mongo: " + uri);
		return MongoClients.create(uri);
	}

	@Bean
	public MongoDatabaseFactory mongoDbFactory() throws IOException {
		return new SimpleMongoClientDatabaseFactory(mongoClient(), database);
	}

	/**
	 * 
	 * Tests if the provided secret is an existing file. If it's a file, the
	 * contents are read and
	 * 
	 * assumed as the password. Otherwise, we assume the password was passed in
	 * plain text.
	 *
	 * 
	 * 
	 * @param secret
	 *            Path to a file containg the password or the password itself.
	 * 
	 * @return The password to be used for connection.
	 * 
	 */

	private static String extractSecretValue(String secret) throws IOException {
		Path secretPath = Path.of(secret);
		if (Files.exists(secretPath) && !Files.isDirectory(secretPath)) {
			return Files.readString(secretPath);
		} else {
			return new String(
					Files.readAllBytes(ResourceUtils.getFile("classpath:" + secret).toPath()),
					StandardCharsets.UTF_8);
		}
	}


	public DatabaseConfig setUsername(String username) {
		this.username = username;
		return this;
	}


	public DatabaseConfig setPassword(String password) {
		this.password = password;
		return this;
	}

	public DatabaseConfig setHost(String host) {
		this.host = host;
		return this;
	}

	public DatabaseConfig setDatabase(String database) {
		this.database = database;
		return this;
	}

}
