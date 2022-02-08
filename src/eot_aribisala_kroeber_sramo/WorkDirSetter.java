package eot_aribisala_kroeber_sramo;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class WorkDirSetter {

	// define basic instance variables
	public String workdir;

	// constructor to set workdir
	public WorkDirSetter() {
		System.out.println("Please enter the current working directory.\n" +
						   "This needs to be the path to the folder where " +
						   "the input twitter.csv file resides.\n" +
						   "You can also simply hit enter to use the sample twitter " +
						   "file (if you cloned the whole project from github).\n" +
						   "Enter directory:");
		Scanner _userinput = new Scanner(System.in);

		// while loop until valid input is given
		while(true) {
			// parse user input
			String _user_work_dir = _userinput.nextLine().toString();
			try {
				// set workdir to default loc or user-specified dir
				if(_user_work_dir.isBlank()) {
					workdir = System.getProperty("user.dir");
				} else {
					workdir = _user_work_dir;	
				}
				// check if mandatory tweet file is present
				String[] filenames;
				File path = new File(workdir);
				filenames = path.list();
				if (Arrays.asList(filenames).contains("twitter.csv")) {
					_userinput.close();
					break;
				} else {
					System.out.println("Your specified directory does not contain " +
							           "the necessary file 'twitter.csv'");
				}		
			} catch (NullPointerException e) {
				System.out.println("Your specified directory does not exist.");
			}	
		}
	}

}
