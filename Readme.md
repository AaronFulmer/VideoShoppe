# Video Shoppe - CS 340 Spring 2019

Video Shoppe App created using Android Studio by Thomas Sullivan, Aaron Fulmer, Christian Guelledge, and Julia Rollins.

Personal Notes for the home menu financial statistics

The general idea is to have a bar chart at the top of the home screen that 
shows the amount of DVDs rented out each day over a 7 day period (Sunday-Saturday). 
The bottom left will have a gross profit estimator that takes the price of dvd rentals
times the amount of dvds sold for the past 30 days, and puts the $ amount.
The bottom right will have a week to week tracker of dvds rentals. So if last monday we
rented out 20 dvds, and this monday we rented 40, it would show +20 in green text or something.

NOTES FOR CODE:

Calendar java library uses the .get(Calendar.DAY_OF_WEEK) to return a integer value of 1-7 (Sunday - Saturday).

HOW TO IMPLEMENT:

Im thinking we have 7 integer values (Sunday - Saturday) that keeps track of the dvds sold on those days.
These 7 integer value will be what are inserted into the bar graph at the top to show how the week is going.
Will also need to figure out a way to keep "today" on the right side and the past days on the left of it in descending order.

Use a switch statement that declares the actual day of the week based on integer values, then have a "lastWeek" integer
value that is used to show the +/- from week to week.
