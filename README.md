# Dont_kill_the_tree

This is an Android project.

## Notification
### About Notification
When a milestone is due, user will get a notification says 'Your tree is dying'.
If multiple milstones are due at the same time, user will get only one notification.
### How to Test Notification
First set clock to several minutes before 23:59pm, like 23:57
Then create project, edit project, complete project as you like. Recommend set the due date of an un-complete milestone as the day after current day, so that you will see a notification after several minutes.
You can alse see the log of notification service in Log.i by filter CXiong-log.

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
