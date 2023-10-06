package com.assign;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class App {

	// Function to calculate the time difference between two times
	private static double calculateTimeDifference(String startTime, String endTime) {
		String[] startTokens = startTime.split(":");
		String[] endTokens = endTime.split(":");

		if (startTokens.length != 2 || endTokens.length != 2) {
			return 0.0;
		}

		// Parse hours and minutes as integers
		int startHours = Integer.parseInt(startTokens[0]);
		int startMinutes = Integer.parseInt(startTokens[1]);
		int endHours = Integer.parseInt(endTokens[0]);
		int endMinutes = Integer.parseInt(endTokens[1]);
		return (endHours - startHours) + (endMinutes - startMinutes) / 60.0;
	}

	// Function to check if an employee has worked 7 consecutive days
	private static boolean hasWorked7ConsecutiveDays(String attendance) {
		int consecutiveDays = 0;
		for (char day : attendance.toCharArray()) {
			if (day == '1') {
				consecutiveDays++;
				if (consecutiveDays == 7) {
					return true;
				}
			} else {
				consecutiveDays = 0;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		String filePath = "D:\\sheet2.csv";

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
				CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader)) {

			// Iterate through each CSV record
			for (CSVRecord record : csvParser) {
				String employeeName = record.get("Employee Name");
				String timeIn = record.get("Time");
				String timeOut = record.get("Time Out");
				String timecardHours = record.get("Timecard Hours (as Time)");

				// Parse and analyze the data
				String[] startTimeTokens = timeIn.split(" ");
				String[] endTimeTokens = timeOut.split(" ");

				if (startTimeTokens.length != 2 || endTimeTokens.length != 2) {

					continue; // Skip this record and move to the next one
				}

				// Calculate the time difference between timeIn and timeOut
				double timeDifference = calculateTimeDifference(startTimeTokens[1], endTimeTokens[1]);
				boolean lessThan10HoursCondition = 1 < timeDifference && timeDifference < 10;
				boolean moreThan14HoursCondition = timeDifference > 14;

				boolean consecutiveDaysCondition = hasWorked7ConsecutiveDays(timecardHours);

				if (consecutiveDaysCondition) {
					System.out.println(employeeName + " has worked for 7 consecutive days.");
				}

				if (lessThan10HoursCondition) {
					System.out.println(employeeName + " has less than 10 hours between shifts.");
				}

				if (moreThan14HoursCondition) {
					System.out.println(employeeName + " has worked for more than 14 hours in a single shift.");
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
