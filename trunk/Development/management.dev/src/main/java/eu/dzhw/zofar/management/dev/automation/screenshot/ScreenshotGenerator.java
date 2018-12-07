package eu.dzhw.zofar.management.dev.automation.screenshot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dzhw.zofar.management.utils.files.DirectoryClient;
import eu.dzhw.zofar.management.utils.files.FileClient;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class ScreenshotGenerator {

	private static final ScreenshotGenerator INSTANCE = new ScreenshotGenerator();

	private static final Logger LOGGER = LoggerFactory.getLogger(ScreenshotGenerator.class);

	private ScreenshotGenerator() {
		super();
	}

	public static ScreenshotGenerator getInstance() {
		return INSTANCE;
	}

	public void spiderSurvey(final String url, final File screenshotDir, final ArrayList<java.awt.Dimension> dimensions,
			boolean maximizeHeight, final List<String> languages) throws Exception {
		if (url == null)
			throw new Exception("URL is null");
		if (screenshotDir == null)
			throw new Exception("Screenshot directory is null");
		if (!screenshotDir.exists())
			throw new Exception("Screenshot directory does not exist");
		if (!screenshotDir.canWrite())
			throw new Exception("cannot write to Screenshot directory");

		final DirectoryClient directoryClient = DirectoryClient.getInstance();
		final File cache = directoryClient.createDir(directoryClient.getTemp(), "screenshotFileCache");
		directoryClient.cleanDirectory(cache);

		spiderSurveyHelperAsFiles(url, cache, screenshotDir, dimensions, maximizeHeight, languages);
		directoryClient.cleanDirectory(cache);
	}

	public void spiderSurveyForMDM(final String url, final File screenshotDir,
			final ArrayList<java.awt.Dimension> dimensions, boolean maximizeHeight, final List<String> languages)
			throws Exception {
		if (url == null)
			throw new Exception("URL is null");
		if (screenshotDir == null)
			throw new Exception("Screenshot directory is null");
		if (!screenshotDir.exists())
			throw new Exception("Screenshot directory does not exist");
		if (!screenshotDir.canWrite())
			throw new Exception("cannot write to Screenshot directory");

		final DirectoryClient directoryClient = DirectoryClient.getInstance();
		final File cache = directoryClient.createDir(directoryClient.getTemp(), "screenshotFileCache");
		directoryClient.cleanDirectory(cache);

		spiderSurveyHelperAsFilesForMDM(url, cache, screenshotDir, dimensions, maximizeHeight, languages);
		directoryClient.cleanDirectory(cache);
	}

	private void spiderSurveyHelperAsFiles(String url, File cache, File screenshotDir,
			final ArrayList<java.awt.Dimension> dimensions, boolean maximizeHeight, final List<String> languages)
			throws Exception {
		if (url == null)
			throw new Exception("URL is null");
		if (cache == null)
			throw new Exception("cache directory is null");
		if (!cache.exists())
			throw new Exception("cache directory does not exist");
		if (!cache.canWrite())
			throw new Exception("cannot write to cache directory");

		System.out.println("URL : " + url);

		System.setProperty("webdriver.chrome.driver", "/.../Browser/chromedriver");
		final WebDriver driver = new ChromeDriver();

		// System.setProperty("webdriver.gecko.driver",
		// "xxxx/Downloads/geckodriver-v0.10.0-linux64/geckodriver");
		// System.setProperty("webdriver.firefox.bin", "/usr/bin/firefox");
		// System.setProperty("webdriver.firefox.marionette",
		// "xxxx/Downloads/geckodriver-v0.10.0-linux64/geckodriver");

		// System.setProperty("webdriver.opera.driver","/.../Browser/operadriver_linux64/operadriver");
		// OperaOptions options = new OperaOptions();
		// options.setBinary(new File("/usr/bin/opera"));
		// final WebDriver driver = new OperaDriver(options);

		// driver.manage().window().setSize(new Dimension(480, 320));

		driver.get(url);
		String currentUrl = driver.getCurrentUrl();

		System.out.println("Page : " + currentUrl);

		ArrayList<Dimension> windowDimensions = new ArrayList<Dimension>();

		driver.manage().window().maximize();
		int maxHeight = driver.manage().window().getSize().getHeight();
		int maxWidth = driver.manage().window().getSize().getWidth();

		LOGGER.info("Max Dimension : {} x {}", maxWidth, maxHeight);

		if ((dimensions == null) || (dimensions.isEmpty())) {
			windowDimensions.add(new Dimension(1280, 720));
		} else {
			for (final java.awt.Dimension dim : dimensions) {
				final int width = (int) dim.getWidth();
				int height = (int) dim.getHeight();
				if (maximizeHeight)
					height = (int) Math.max(height, maxHeight);
				windowDimensions.add(new Dimension(width, height));
			}
		}

		while (currentUrl != null) {
			System.out.println("currentUrl : " + currentUrl);
			final Map<String, String> variantURLS = new LinkedHashMap<String, String>();

			if ((languages == null) || languages.isEmpty()) {
				variantURLS.put(currentUrl, "");
			} else {
				final boolean hasParameter = currentUrl.contains("?");
				for (final String language : languages) {
					if (!hasParameter)
						variantURLS.put(currentUrl + "?zofar_lang=" + language, language);
					else
						variantURLS.put(currentUrl + "&zofar_lang=" + language, language);
				}
			}

			for (final Map.Entry<String, String> variantURL : variantURLS.entrySet()) {
				if (!driver.getCurrentUrl().equals(variantURL.getKey())) {
					driver.get(variantURL.getKey());
				}

				String filename = variantURL.getKey().substring(variantURL.getKey().lastIndexOf('/') + 1,
						variantURL.getKey().lastIndexOf('.'));
				if (filename.contains(";jsessionid=")) {
					filename = filename.substring(0, filename.indexOf(';'));
					filename = filename.replaceAll(Pattern.quote(".html"), "");
				}
				System.out.println("filename : "+filename);
//				// Fullscreen
//				File fullscreenshotFile = null;
//				try {
//					// ((JavascriptExecutor)driver).executeScript("document.body.style.zoom=(top.window.screen.height-70)/Math.max(document.body.scrollHeight,
//					// document.body.offsetHeight, document.documentElement.clientHeight,
//					// document.documentElement.scrollHeight,
//					// document.documentElement.offsetHeight);");
//					// final File scrFile = ((TakesScreenshot)
//					// driver).getScreenshotAs(OutputType.FILE);
//					//
//					String tmp = filename + "##fullscreen";
//					if (!variantURL.getValue().equals(""))
//						tmp = tmp + "##" + variantURL.getValue();
//
//					final File renamedFile = FileClient.getInstance().createOrGetFile(tmp, ".png", cache);
//					// FileClient.getInstance().copyFile(scrFile, renamedFile);
//					fullscreenshotFile = renamedFile;
//					//
//					final Screenshot fullPageScreenshot = new AShot()
//							.shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driver);
//
//					final BufferedImage image = fullPageScreenshot.getImage();
//					ImageIO.write(image, "png", fullscreenshotFile);
//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//				if (fullscreenshotFile != null) {
//					File destFile = FileClient.getInstance().move(fullscreenshotFile, screenshotDir);
//				}

				for (final Dimension dim : windowDimensions) {
					driver.manage().window().setSize(dim);

					File screenshot = null;

					try {
//						final File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

						String tmp = filename + "##" + dim.getWidth() + "x" + dim.getHeight();
						if (!variantURL.getValue().equals(""))
							tmp = tmp + "##" + variantURL.getValue();

						final File renamedFile = FileClient.getInstance().createOrGetFile(tmp, ".png", cache);
						
						final Screenshot tmpScreenshot = new AShot()
								.shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driver);

						final BufferedImage image = tmpScreenshot.getImage();
						ImageIO.write(image, "png", renamedFile);
						
						
						
//						FileClient.getInstance().copyFile(scrFile, renamedFile);
						screenshot = renamedFile;

					} catch (Exception e) {
						e.printStackTrace();
					}

					if (screenshot != null) {
						File destFile = FileClient.getInstance().move(screenshot, screenshotDir);
					}
					
					// Iterate through carousel

					List<WebElement> carousels = driver.findElements(By.cssSelector(".carousel"));

					if (carousels != null) {
						for (WebElement carousel : carousels) {
							String carouselId = carousel.getAttribute("id");
//							System.out.println("Carousel : " + carouselId);

							boolean carouselMode = false;
							try {
								WebElement carouselInner = driver
										.findElement(By.cssSelector("#" + carouselId + " .carousel-inner"));
								if (carouselInner != null) {
									final String overflow = carouselInner.getCssValue("overflow");
//									System.out.println("overflow : " + overflow);
									carouselMode = (overflow.equals("visible"));
								}
							} catch (NoSuchElementException e) {
								System.out.println("Carousel " + carouselId + " : no inner container found");
							}

							if (!carouselMode)
								continue;

							WebElement carouselBtn = driver
									.findElement(By.cssSelector(".carousel-control-next[hRef='#" + carouselId + "']"));
//							System.out.println("carouselNext : " + carouselBtn);

							List<WebElement> carouselSlides = null;
							try {
								carouselSlides = driver
										.findElements(By.cssSelector("#" + carouselId + " .carousel-item"));
							} catch (NoSuchElementException e) {
							}

							if (carouselSlides != null) {
								WebElement activeSlide = null;
								try {
									activeSlide = driver.findElement(By.cssSelector("#" + carouselId + " .active"));
								} catch (NoSuchElementException e) {
									System.out.println("Carousel " + carouselId + " : no active slide found");
								}
								final int slideCount = carouselSlides.size();
								int lft = 0;
								while ((activeSlide != null)&&(lft < slideCount)) {
									System.out.println("Carousel " + carouselId + " slide : "
											+ lft);

									// Screenshot slide
									
									File carouselScreenshot = null;

									try {
										String tmp = filename + "##" + dim.getWidth() + "x" + dim.getHeight();
										if (!variantURL.getValue().equals(""))
											tmp = tmp + "##" + variantURL.getValue();
										
										tmp = tmp+"carousel"+carouselId+"_slide"+lft;

										final File renamedFile = FileClient.getInstance().createOrGetFile(tmp, ".png", cache);
										// FileClient.getInstance().copyFile(scrFile, renamedFile);
										carouselScreenshot = renamedFile;
										//
										final Screenshot fullPageScreenshot = new AShot()
												.shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driver);

										final BufferedImage image = fullPageScreenshot.getImage();
										ImageIO.write(image, "png", carouselScreenshot);

									} catch (Exception e) {
										e.printStackTrace();
									}

									if (carouselScreenshot != null) {
										File destFile = FileClient.getInstance().move(carouselScreenshot, screenshotDir);
									}

									if (!carouselBtn.isDisplayed())
										break;
									carouselBtn.click();
									
									//wait
									Thread.sleep(5*1000);
									
									lft = lft + 1;
									try {
										activeSlide = driver.findElement(By.cssSelector("#" + carouselId + " .active"));
									} catch (NoSuchElementException e) {
										System.out.println(
												"Carousel " + carouselId + " : no active slide found after click");
										break;
									}
								}
							}
						}
					}
				}
			}

			if (currentUrl.endsWith("end.html")) {
				System.out.println("End page reached");
				break;
			}
			WebElement forwardBtn = null;
			try {
				forwardBtn = driver.findElement(By.cssSelector(".zo-forward"));
			} catch (NoSuchElementException e) {
			}

			if (forwardBtn == null) {
				try {
					forwardBtn = driver.findElement(By.cssSelector(".zofar-btn-forward"));
				} catch (NoSuchElementException e) {
				}
			}

			if (forwardBtn != null) {
				if (!forwardBtn.isDisplayed()) {
					JavascriptExecutor js = (JavascriptExecutor) driver;
					js.executeScript("arguments[0].click();", forwardBtn);
				} else
					forwardBtn.click();
			} else {
				System.err.println("No forward Button found");
				break;
			}

			if (driver.getCurrentUrl().equals(currentUrl)) {
				LOGGER.error("loop : " + currentUrl);
				break;
			}
			currentUrl = driver.getCurrentUrl();
		}
		driver.quit();
	}

	private void spiderSurveyHelperAsFilesForMDM(String url, File cache, File screenshotDir,
			final ArrayList<java.awt.Dimension> dimensions, boolean maximizeHeight, final List<String> languages)
			throws Exception {
		if (url == null)
			throw new Exception("URL is null");
		if (cache == null)
			throw new Exception("cache directory is null");
		if (!cache.exists())
			throw new Exception("cache directory does not exist");
		if (!cache.canWrite())
			throw new Exception("cannot write to cache directory");

		System.out.println("URL : " + url);

		System.setProperty("webdriver.chrome.driver", "/xxx/Browser/chromedriver");
		final WebDriver driver = new ChromeDriver();

		// System.setProperty("webdriver.gecko.driver",
		// "xxxx/Downloads/geckodriver-v0.10.0-linux64/geckodriver");
		// System.setProperty("webdriver.firefox.bin", "/usr/bin/firefox");
		// System.setProperty("webdriver.firefox.marionette",
		// "xxxx/Downloads/geckodriver-v0.10.0-linux64/geckodriver");

		// System.setProperty("webdriver.opera.driver","/xxx/Browser/operadriver_linux64/operadriver");
		// OperaOptions options = new OperaOptions();
		// options.setBinary(new File("/usr/bin/opera"));
		// final WebDriver driver = new OperaDriver(options);

		// driver.manage().window().setSize(new Dimension(480, 320));

		driver.get(url);
		String currentUrl = driver.getCurrentUrl();

		System.out.println("Page : " + currentUrl);

		ArrayList<Dimension> windowDimensions = new ArrayList<Dimension>();

		driver.manage().window().maximize();
		int maxHeight = driver.manage().window().getSize().getHeight();
		int maxWidth = driver.manage().window().getSize().getWidth();

		if ((dimensions == null) || (dimensions.isEmpty())) {
			windowDimensions.add(new Dimension(1280, 720));
		} else {
			for (final java.awt.Dimension dim : dimensions) {
				final int width = (int) dim.getWidth();
				int height = (int) dim.getHeight();
				if (maximizeHeight)
					height = (int) Math.max(height, maxHeight);
				windowDimensions.add(new Dimension(width, height));
			}
		}

		while (currentUrl != null) {
			System.out.println("currentUrl : " + currentUrl);
			final Map<String, String> variantURLS = new LinkedHashMap<String, String>();

			if ((languages == null) || languages.isEmpty()) {
				variantURLS.put(currentUrl, "");
			} else {
				final boolean hasParameter = currentUrl.contains("?");
				for (final String language : languages) {
					if (!hasParameter)
						variantURLS.put(currentUrl + "?zofar_lang=" + language, language);
					else
						variantURLS.put(currentUrl + "&zofar_lang=" + language, language);
				}
			}

			for (final Map.Entry<String, String> variantURL : variantURLS.entrySet()) {
				if (!driver.getCurrentUrl().equals(variantURL.getKey())) {
					driver.get(variantURL.getKey());
				}

				String filename = variantURL.getKey().substring(variantURL.getKey().lastIndexOf('/') + 1,
						variantURL.getKey().lastIndexOf('.'));
				if (filename.contains(";jsessionid=")) {
					filename = filename.substring(0, filename.indexOf(';'));
					filename = filename.replaceAll(Pattern.quote(".html"), "");
				}

				for (final Dimension dim : windowDimensions) {
					driver.manage().window().setSize(dim);

					File screenshot = null;

					try {
						String tmp = filename + "##" + dim.getWidth() + "x" + dim.getHeight();
						if (!variantURL.getValue().equals(""))
							tmp = tmp + "##" + variantURL.getValue();

						final File renamedFile = FileClient.getInstance().createOrGetFile(tmp, ".png", cache);
						// FileClient.getInstance().copyFile(scrFile, renamedFile);
						screenshot = renamedFile;
						//
						final Screenshot fullPageScreenshot = new AShot()
								.shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driver);

						final BufferedImage image = fullPageScreenshot.getImage();
						ImageIO.write(image, "png", screenshot);

					} catch (Exception e) {
						e.printStackTrace();
					}

					if (screenshot != null) {
						File destFile = FileClient.getInstance().move(screenshot, screenshotDir);
					}

					// Iterate through carousel

					List<WebElement> carousels = driver.findElements(By.cssSelector(".carousel"));

					if (carousels != null) {
						for (WebElement carousel : carousels) {
							String carouselId = carousel.getAttribute("id");
//							System.out.println("Carousel : " + carouselId);

							boolean carouselMode = false;
							try {
								WebElement carouselInner = driver
										.findElement(By.cssSelector("#" + carouselId + " .carousel-inner"));
								if (carouselInner != null) {
									final String overflow = carouselInner.getCssValue("overflow");
//									System.out.println("overflow : " + overflow);
									carouselMode = (overflow.equals("visible"));
								}
							} catch (NoSuchElementException e) {
								System.out.println("Carousel " + carouselId + " : no inner container found");
							}

							if (!carouselMode)
								continue;

							WebElement carouselBtn = driver
									.findElement(By.cssSelector(".carousel-control-next[hRef='#" + carouselId + "']"));
//							System.out.println("carouselNext : " + carouselBtn);

							List<WebElement> carouselSlides = null;
							try {
								carouselSlides = driver
										.findElements(By.cssSelector("#" + carouselId + " .carousel-item"));
							} catch (NoSuchElementException e) {
							}

							if (carouselSlides != null) {
								WebElement activeSlide = null;
								try {
									activeSlide = driver.findElement(By.cssSelector("#" + carouselId + " .active"));
								} catch (NoSuchElementException e) {
									System.out.println("Carousel " + carouselId + " : no active slide found");
								}
								final int slideCount = carouselSlides.size();
								int lft = 0;
								while ((activeSlide != null)&&(lft < slideCount)) {
									System.out.println("Carousel " + carouselId + " slide : "
											+ lft);

									// Screenshot slide
									
									File carouselScreenshot = null;

									try {
										String tmp = filename + "##" + dim.getWidth() + "x" + dim.getHeight();
										if (!variantURL.getValue().equals(""))
											tmp = tmp + "##" + variantURL.getValue();
										
										tmp = tmp+"carousel"+carouselId+"_slide"+lft;

										final File renamedFile = FileClient.getInstance().createOrGetFile(tmp, ".png", cache);
										// FileClient.getInstance().copyFile(scrFile, renamedFile);
										carouselScreenshot = renamedFile;
										//
										final Screenshot fullPageScreenshot = new AShot()
												.shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driver);

										final BufferedImage image = fullPageScreenshot.getImage();
										ImageIO.write(image, "png", carouselScreenshot);

									} catch (Exception e) {
										e.printStackTrace();
									}

									if (carouselScreenshot != null) {
										File destFile = FileClient.getInstance().move(carouselScreenshot, screenshotDir);
									}

									if (!carouselBtn.isDisplayed())
										break;
									carouselBtn.click();
									
									//wait
									Thread.sleep(5*1000);
									
									lft = lft + 1;
									try {
										activeSlide = driver.findElement(By.cssSelector("#" + carouselId + " .active"));
									} catch (NoSuchElementException e) {
										System.out.println(
												"Carousel " + carouselId + " : no active slide found after click");
										break;
									}
								}
							}
						}
					}
				}
			}

			if (currentUrl.endsWith("end.html")) {
				System.out.println("End page reached");
				break;
			}
			WebElement forwardBtn = null;
			try {
				forwardBtn = driver.findElement(By.cssSelector(".zo-forward"));
			} catch (NoSuchElementException e) {
			}

			if (forwardBtn == null) {
				try {
					forwardBtn = driver.findElement(By.cssSelector(".zofar-btn-forward"));
				} catch (NoSuchElementException e) {
				}
			}

			if (forwardBtn != null)
				forwardBtn.click();
			else {
				System.err.println("No forward Button found");
				break;
			}

			if (driver.getCurrentUrl().equals(currentUrl)) {
				LOGGER.error("loop : " + currentUrl);
				break;
			}
			currentUrl = driver.getCurrentUrl();
		}
		driver.quit();
	}
}
