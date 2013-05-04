package tk.redwirepvp.kitapi.api;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;

import tk.redwirepvp.kitapi.Main;

public class ExClassLoader {
	protected Main plugin;
	private ClassLoader loader;
	private ClassLoader jarloader;

	public ExClassLoader(Main inheritance) {
		this.plugin = inheritance;
	}

	public List<Kit> load(String directory) {
		List<Kit> Classes = new ArrayList<Kit>();

		File dir = new File(directory);
		if (!dir.exists()) {
			dir.mkdir();
		}

		try {
			this.loader = new URLClassLoader(new URL[] { dir.toURI().toURL() },
					Kit.class.getClassLoader());
		} catch (MalformedURLException ex) {
			this.plugin.log.warning("ClassLoader encountered an exception: ");
			ex.printStackTrace();
			return Classes;
		}

		boolean loaded = false;
		String lastnameoa = "";

		for (File file : dir.listFiles()) {
			if (lastnameoa != "") {
				if (loaded)
					this.plugin.getServer().getConsoleSender()
							.sendMessage(ChatColor.GREEN + lastnameoa);
				else {
					this.plugin.getServer().getConsoleSender()
							.sendMessage(ChatColor.RED + lastnameoa);
				}
			}
			loaded = false;
			lastnameoa = file.getName();

			if (!file.getName().endsWith(".class")) {
				if (!file.getName().endsWith(".jar")) {
					this.plugin.getServer().getConsoleSender()
							.sendMessage(ChatColor.GRAY + file.getName());
					lastnameoa = "";
				} else {
					this.plugin.log.info("FOUND A JAR!");
					try {
						JarFile jarFile = new JarFile(file.getAbsolutePath());

						Enumeration<JarEntry> e = jarFile.entries();

						URL[] urls = { new URL("jar:file:"
								+ file.getAbsolutePath() + "!/") };
						this.jarloader = null;
						this.jarloader = new URLClassLoader(urls,
								Kit.class.getClassLoader());

						while (e.hasMoreElements()) {
							JarEntry je = (JarEntry) e.nextElement();
							if ((!je.isDirectory())
									&& (je.getName().endsWith(".class"))) {
								if ((je != null) && (je.getName() != null)) {
									String className = je.getName().substring(
											0, je.getName().length() - 6);
									className = className.replace('/', '.');
									try {
										Class<?> aclass = this.jarloader
												.loadClass(className);
										Object object = aclass.newInstance();
										if (!(object instanceof Kit)) {
											this.plugin.log
													.warning("Not a valid add-on: "
															+ aclass.getSimpleName());
										} else {
											Kit a = (Kit) object;

											if ((a == null)
													|| (a.getName() == null)) {
												this.plugin
														.getServer()
														.getConsoleSender()
														.sendMessage(
																ChatColor.RED
																		+ je.getName()
																		+ " is invalid!");
											} else {
												for (Kit ocl : Classes) {
													if (ocl.getName().equals(
															a.getName())) {
														this.plugin.log
																.warning(" CTF-Class "
																		+ ocl.getName()
																		+ " and "
																		+ a.getName()
																		+ " collapses in their commands! Class "
																		+ a.getName()
																		+ " is disabled!");
														a = null;
													}
												}

												a.onEnable();
												Classes.add(a);
												loaded = true;
												this.plugin
														.getServer()
														.getConsoleSender()
														.sendMessage(
																ChatColor.BLUE
																		+ "Inside .jar: "
																		+ a.getName()
																		+ ChatColor.GREEN
																		+ " loaded!");

												this.plugin.log.info("Loaded "
														+ a.getName()
														+ " v"
														+ a.getVersion()
														+ " by "
														+ (String) a
																.getAuthor());

												je = null;
											}
										}
									} catch (Exception ex) {
										this.plugin.log.warning("A "
												+ ex.getLocalizedMessage()
												+ " caused " + className
												+ " to fail to load!");
									} catch (Error ex) {
										this.plugin.log.warning("A "
												+ ex.getLocalizedMessage()
												+ " caused " + className
												+ " to fail to load!");
									}
								}
							}

						}

						e = null;
						this.jarloader = null;
						jarFile.close();
						jarFile = null;
					} catch (IOException e) {
						System.err.println("Error: " + e.getMessage());
					}

					loaded = false;
				}
			} else {
				String name = file.getName().substring(0,
						file.getName().lastIndexOf("."));
				try {
					Class<?> aclass = this.loader.loadClass(name);
					Object object = aclass.newInstance();
					if (!(object instanceof Kit)) {
						this.plugin.log.warning("Not a valid add-on: "
								+ aclass.getSimpleName());
					} else {
						Kit a = (Kit) object;
						for (Kit ocl : Classes) {
							if (ocl.getName().equals(a.getName())) {
								this.plugin.log
										.warning(" CTF-Class "
												+ ocl.getName()
												+ " and "
												+ a.getName()
												+ " collapses in their commands! Class "
												+ a.getName() + " is disabled!");
								a = null;
							}
						}

						a.onEnable();
						Classes.add(a);
						plugin.registerCommand(a.getName());
						loaded = true;
						this.plugin.log.info("Loaded " + a.getName() + " v"
								+ a.getVersion() + " by " + a.getAuthor());
					}
				} catch (Exception ex) {
					this.plugin.log.warning("A " + ex.getLocalizedMessage()
							+ " caused " + name + " to fail to load!");
				} catch (Error ex) {
					this.plugin.log.warning("A " + ex.getLocalizedMessage()
							+ " caused " + name + " to fail to load!");
				}

			}

		}

		if (Classes.size() == 0) {
			this.plugin
					.getServer()
					.getConsoleSender()
					.sendMessage(
							ChatColor.RED
									+ "[CTF] No classes were loaded ... download some!");
			this.plugin.getServer().getConsoleSender()
					.sendMessage(ChatColor.RED + "[CTF] Will disable me!!");

			this.plugin
					.getServer()
					.getPluginManager()
					.disablePlugin(
							this.plugin.getServer().getPluginManager()
									.getPlugin("kCTFKitsAPI"));
		}

		return Classes;
	}
}