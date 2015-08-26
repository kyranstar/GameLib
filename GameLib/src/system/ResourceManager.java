package system;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import draw.GraphicsUtils;

public final class ResourceManager {
	private static final Logger logger = LoggerFactory.getLogger(ResourceManager.class);

	private final Map<String, Resource<?>> resources = new HashMap<>();

	@SuppressWarnings("unchecked")
	public <T> Resource<T> loadResource(final String filepath) {
		if (resources.containsKey(filepath)) {
			return (Resource<T>) resources.get(filepath);
		}
		final Resource<T> res = loadResourceFromFile(filepath);
		resources.put(filepath, res);
		return res;
	}

	@SuppressWarnings("unchecked")
	private <T> Resource<T> loadResourceFromFile(final String filepath) {
		final String extension = getFileExtension(filepath);
		switch (extension) {
		case "png":
		case "gif":
		case "bmp":
		case "jpg":
		case "jpeg":
			return (Resource<T>) new Resource<BufferedImage>(loadImage('/' + filepath), filepath);
		case "txt":
		case "cnf":
		case "conf":
		case "cfg":
		case "log":
		case "csv":
		case "yaml":
		case "xml":
		case "json":
		case "html":
			return (Resource<T>) new Resource<String>(loadText('/' + filepath), filepath);
		}
		throw new IllegalArgumentException("Unrecognized file extension: " + extension);
	}

	private String loadText(final String filename) {
		final InputStream in = ResourceManager.class.getResourceAsStream(filename);
		return convertStreamToString(in);
	}

	private static String convertStreamToString(final InputStream is) {
		try (Scanner s = new Scanner(is, "UTF-8")) {
			s.useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
		}
	}

	private BufferedImage loadImage(final String file) {
		try {
			return GraphicsUtils.loadImage(file);
		} catch (final IOException e) {
			logger.error("IOException reading file: " + file, e);
			return null;
		}
	}

	private String getFileExtension(final String name) {
		try {
			return name.substring(name.lastIndexOf('.') + 1);
		} catch (final Exception e) {
			logger.error("Error finding file extension of file " + name, e);
			return "";
		}

	}

	public void unloadAll() {
		resources.clear();
	}

	public static final class Resource<T> {
		public final T data;
		public final String name;
		public final String fullName;

		public Resource(final T data, final String path) {
			this.data = data;
			this.fullName = path;
			name = extractFilename(path);
		}

		private String extractFilename(final String path) {
			final Path fileName = Paths.get(path).getFileName();
			if (fileName == null) {
				logger.error("Filename nonexistant (filename == null)");
				return null;
			}
			return fileName.toString();
		}
	}
}
