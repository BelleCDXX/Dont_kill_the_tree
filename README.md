# Dont_kill_the_tree

This is an Android project.

## Util Package
There are several methods in the util package. One can make use of them if needed so you don't need to write your own.
### String calendarToString(Calendar calendar)
Convert a given calendar into string in the format of "MM/dd/yyyy"
### Calendar stringToCalendar(String date) throws ParseException
Convert a given date String into Calendar
### void toNearestDueDate(Calendar calendar)
Given a calendar, set the calendar to the soonest due date
For example, given 05/15/2016 20:47:55.789 will return 06/16/2016 00:00:00.000
### boolean isOnTime(Calendar dueDate, Calendar currentDate)
This method determines if a given due date is on time.
Give a specific date to compare, or null if you want to compare it to the current date
### Milestone getMilestoneById(List<Milestone> milestones, long id)
Get a Milestone by id from a Milestone list.
