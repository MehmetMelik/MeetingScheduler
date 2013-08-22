package com.enscala.meetingscheduler.cmd;

import java.io.Console;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;


/**
 * This class prompts for user requests from the system.
 * @author Melik
 * 
 * TODO write more comments
 *
 */
public class TaskRunner {
	private static transient final Logger LOGGER = Logger.getLogger(TaskRunner.class);

	private static transient final String USAGE =
		"\n====== Usage ====== \n" +
		"add = add a schedule after prompting for file path \n" +
		"exit = exits the program \n" +
		"usage = prints this help";

	public static final void usage() {
		LOGGER.info(USAGE);
	}

	public static final void main(final String[] args) {

		Console c = System.console();

		if (c == null) {
			System.err.println("No console.");
			System.exit(1);
		}

		final GenericApplicationContext ctx = new GenericApplicationContext();
		final XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ctx);
		String appContext = System.getProperty("appContext");
		xmlReader.loadBeanDefinitions(new ClassPathResource(appContext));
		ctx.refresh();

		Scheduler scheduler = (Scheduler) ctx.getBean("scheduler");

		while (true) {
			
			System.out.print(">");
			
			String input = c.readLine();
			if (input.equalsIgnoreCase("usage")) {
				usage();
			}
			else if (input.equalsIgnoreCase("add")) {
				System.out.println("Schedule file?");
				String dataFile = c.readLine();
				scheduler.ingestSchedule(LOGGER, dataFile);
			}
			else if (input.equalsIgnoreCase("exit")) {
				System.exit(0);
			}
			else {
				System.out.println("Unrecognised command");
			}

		}
	}

}
