package com.ubs.opsit.interviews;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import static org.apache.commons.lang.math.NumberUtils.*;
import static com.ubs.opsit.interviews.ValidateUtils.*;

public class BerlinClock implements TimeConverter {

	@Override
    public String convertTime(String aTime){
    	Time time = Time.parse(aTime);
    	ClockState clockState = show(time);
    	return clockState.toString();
    }

    public static ClockState show(Time time){
    	return new ClockState(time);
    }

    /**  
     * Immutable ClockState represents clock's lights visible state.
     * State of each of rows is encoded as a String consisting of characters 'R','Y' and 'O'.
     */
    public static class ClockState {

    	final String second;
    	final String hour1;
    	final String hour2;
    	final String minutes1;
    	final String minutes2;

    	private ClockState(Time time) {
    		this.second = formatSecondsRow(time.seconds);
    		this.hour1 = formatHours1Row(time.hour);
    		this.hour2 = formatHours2Row(time.hour);
    		this.minutes1 = formatMinutes1Row(time.minutes);
    		this.minutes2 = formatMinutes2Row(time.minutes);
    	}

    	@Override
    	public String toString(){
	    	return new StrBuilder()
	    		.appendWithSeparators(
	    			new String[]{ second, hour1, hour2, minutes1, minutes2 }, 
	    			System.lineSeparator()
	    		).toString();
    	}

	    private String formatSecondsRow(int seconds){
	    	if(seconds%2==0) return "Y"; else return "O";
	    }

	    private String formatHours1Row(int hour){
	    	int numberOfRedCells = hour / 5;
	    	return formatRow("R", numberOfRedCells, 4);
	    }

	    private String formatHours2Row(int hour){
	    	int numberOfRedCells = hour % 5;
	    	return formatRow("R", numberOfRedCells, 4);
	    }

	    private String formatMinutes1Row(int minutes){
	    	char[] row = formatRow("Y", minutes / 5, 11).toCharArray();
	    	changeToRedIfYellow(row, 2);
	    	changeToRedIfYellow(row, 5);
	    	changeToRedIfYellow(row, 8);
	    	return new String(row);
	    }

	    private String formatMinutes2Row(int minutes){
	    	int numberOfRedCells = minutes % 5;
	    	return formatRow("Y", numberOfRedCells, 4);
	    }

	    private String formatRow(String light, int times, int length){
	    	return StringUtils.repeat(light, times) + StringUtils.repeat("O", length - times);
	    }

	    private void changeToRedIfYellow(char[] row, int index){
	    	if(row[index]=='Y') row[index] = 'R';
	    }
    }

    /**
     * Immutable Time encapsulates hour, minute and second of a day.
     */
    public static class Time {

    	final int hour;
    	final int minutes;
    	final int seconds;

    	public Time(int hour, int minutes, int seconds){
    		validateRangeFromZeroUpTo(24, hour, "hour"); 
    		validateRangeFromZeroUntil(60, minutes, "minutes"); 
    		validateRangeFromZeroUntil(60, seconds, "seconds"); 
    		this.hour = hour;
    		this.minutes = minutes;
    		this.seconds = seconds;
    	}

    	/**
    	 * Parses given string as a {@link Time}.
    	 * @param aTime time string in HH:mm:ss format
    	 */
    	public static Time parse(String aTime){
    		Validate.notNull(aTime,"Time.parse() `aTime` argument should not be null");
	    	validateMatchesPattern("\\d\\d:\\d\\d:\\d\\d", aTime, "aTime");
	    	String[] parts = aTime.split(":");
	    	int hour = toInt(parts[0]);
	    	int minutes = toInt(parts[1]);
	    	int seconds = toInt(parts[2]);
	    	return new Time(hour, minutes, seconds);
    	}

    	@Override
    	public String toString(){
    		return String.format("%02d:%02d:%02d",hour,minutes,seconds);
    	}
    }

}
