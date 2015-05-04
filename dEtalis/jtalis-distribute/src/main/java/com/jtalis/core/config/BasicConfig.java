package com.jtalis.core.config;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * <p>This configuration tries to find etalis.P in the jar/class-path resources. First it tries to
 * find etalis.zip in the resources. If such doesn't exist, it tries to find in the
 * resources a directory "etalis" where are all ETALIS prolog sources. If it succeed in 
 * finding the prolog files in the sources it takes them and stores them in a temporary 
 * directory where the Prolog Environment could see them.</p> 
 * 
 * <p>
 * If created with a file, it uses it to return in <code>getEtalisSourceFile()</code> and
 * does not try to find any prolog files in the resources 
 * </p>
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class BasicConfig implements JtalisConfig {

	private static final Logger logger = Logger.getLogger(BasicConfig.class.getName());

	private static final String ETALIS_SOURCE_FILE = "etalis.P";

	private File tempDir;
	private File etalisSourceFile;

	/**
	 * Tries to find etalis.P in the resources. First it tries to find etalis.zip in the 
	 * resources. If such doesn't exist, it tries to find in the resources a directory "etalis" 
	 * where are all ETALIS prolog sources. If it succeed in finding the prolog files in the sources 
	 * it takes them and stores them in a temporary directory where the Prolog Environment could see them
	 */
	public BasicConfig() {
		// 
	}

	/**
	 * Uses the passed file as a location of etalis.P file.
	 * @param etalisSourceFile location of "etalis.P"
	 */
	public BasicConfig(String etalisSourceFile) {
		this(new File(etalisSourceFile));
	}

	/**
	 * Uses the passed file as a location of etalis.P file.
	 * @param etalisSourceFile location of "etalis.P"
	 */
	public BasicConfig(File etalisSourceFile) {
		this.etalisSourceFile = etalisSourceFile;
	}

	@Override
	public File getEtalisSourceFile() {
		if (etalisSourceFile == null) {
			tempDir = new File(System.getProperty("java.io.tmpdir"), "etalis-src");
			if (!tempDir.exists()) {
				tempDir.mkdirs();
			}
			tempDir.deleteOnExit();
	
			boolean status = false;
	
			InputStream in = null;
			try {
				// try find zip file in the resources and extract its files into the temp directory
				String zipName = "etalis.zip";
				in = BasicConfig.class.getResourceAsStream(zipName);
	
				if (in != null) {
					logger.info(zipName + " found in resources");
					status = unzip(in, "");
				}
	
				// try to find directory in the resources
				if (!status) {
					String folder = "etalis";
					URL url = BasicConfig.class.getResource(folder);
					
					if (url != null) {
						String urlStr = url.toString();
	
						if (urlStr.startsWith("file")) {
							// we are loaded from a class directory
							tempDir = new File((new URI(url.toString())).getPath());
							logger.info(folder  + " folder found in resources");
							status = true;
						}
						else if (urlStr.startsWith("jar")) {
							// we are loaded from a jar
							// remove the first protocol - "jar"
							String urlFile = new URL(url.getPath()).getPath(); 
							String jarFile = urlFile.substring(0, urlFile.indexOf('!'));
							String prefix = urlFile.substring(jarFile.length() + 2, urlFile.length());
	
							File jf = new File(jarFile);
							logger.info("Sources found in the jar file");
	
							in = new FileInputStream(jf);
	
							status = unzip(in, prefix);
						}
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				try {
					if (in != null) {
						in.close();
					}
				}
				catch (IOException e) {
				}
			}
	
			if (!status) {
				throw new RuntimeException("Could not load ETALIS sources");
			}
			findEtalisSourceFile(tempDir);
		}
		return etalisSourceFile;
	}

	private void findEtalisSourceFile(File file) {
		if (etalisSourceFile != null) {
			return;
		}

		if (file.isFile()) {
			if (file.getName().equals(ETALIS_SOURCE_FILE)) {
				etalisSourceFile = file;
			}
		}
		else {
			for (File f : file.listFiles()) {
				findEtalisSourceFile(f);
			}
		}
	}

	private boolean unzip(InputStream zipStream, String prefix) {
		logger.info("Extracting etalis source files");

		ZipInputStream zipin = null;

		try {
			zipin = new ZipInputStream(zipStream);
			byte data[] = new byte[1024];

			for (ZipEntry entry = zipin.getNextEntry(); entry != null; entry = zipin.getNextEntry()) {
				if (!entry.getName().startsWith(prefix)) {
					continue;
				}
				OutputStream out = null;
				try {
					File outFile = new File(tempDir, entry.getName());
					if (entry.isDirectory()) {
						if (!outFile.exists()) {
							outFile.mkdirs();
						}
					}
					else {
						File dst = new File(tempDir, entry.getName());
						out = new BufferedOutputStream(new FileOutputStream(dst), data.length);

						int len;
						while ((len = zipin.read(data, 0, data.length)) != -1) {
							out.write(data, 0, len);
						}
					}
				}
				catch (Exception e) {
					e.printStackTrace();
					logger.severe("Could not extract resource : " + entry.getName());
				}
				finally {
					if (out != null) {
						out.close();
					}
				}
			}
			return true;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (zipin != null) {
				try {
					zipin.close();
				}
				catch (IOException e) {
				}
			}
		}
		return false;
	}
}
