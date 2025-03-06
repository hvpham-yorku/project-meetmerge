Product Backlog
MeetMerge: Calendar Availability Combiner
After discussing with my team, we've put together these user stories for our calendar combiner app. I tried to cover all the essential features we'll need plus some nice-to-have stuff for later sprints.
Core User Stories
1.	Link Input
o	As a user, I want to paste multiple Calendly links into the app so I can find when everyone is free without manually checking each calendar.
o	Criteria for satisfaction: 
	The interface should let me paste at least 5 different Calendly links
	The system should check if the links are valid Calendly URLs
	I should get some kind of error message if I paste an invalid link
	Should be able to add links one by one or all at once
2.	Google Calendar Support
o	As a user, I want to paste Google Calendar links too, since not everyone uses Calendly.
o	Criteria for satisfaction: 
	System needs to work with Google Calendar sharing links
	It should correctly pull availability info from these Google Calendar links
	Users should be able to mix Calendly and Google Calendar links together
3.	Finding Overlapping Times
o	As a user, I want the app to show me when everyone is available at the same time.
o	Criteria for satisfaction: 
	System needs to accurately find times that work for everyone
	It should handle different time zones correctly
	Results should clearly show the date and time ranges that work
4.	Showing Results Clearly
o	As a user, I want to see the available times in an easy-to-understand format.
o	Criteria for satisfaction: 
	Results should look like a calendar view
	Times should be in my local time zone
	If there are no overlapping times, it should tell me that
5.	Creating a New Calendly Link
o	As a user, I want to generate a new Calendly link with just the overlapping times so I can send one link to everyone.
o	Criteria for satisfaction: 
	System creates a new Calendly event showing only times when everyone's free
	The link should actually work when people click it
	I should be able to give the event a custom name and description
6.	Lettucemeet Integration
o	As a user, I want to create a Lettucemeet poll with the overlapping times since some people prefer that.
o	Criteria for satisfaction: 
	System should pre-fill a Lettucemeet poll with the times that work for everyone
	The link should work correctly
	I should be able to add a title and description for the poll
User Account Features
7.	Creating an Account
o	As a new user, I want to make an account so I can save my settings and previous calendar combinations.
o	Criteria for satisfaction: 
	Should be able to sign up with email and password
	System should check if my email is valid and password is strong enough
	I should get a confirmation email
	My account info should be stored securely
8.	Logging In
o	As a returning user, I want to log in to access my saved stuff.
o	Criteria for satisfaction: 
	System checks my username and password
	Keeps me logged in until I log out
	Has a "forgot password" option
9.	Saving Calendar Combinations
o	As a logged-in user, I want to save sets of calendar links so I don't have to paste them again next time.
o	Criteria for satisfaction: 
	I can give a name to a set of calendar links and save them
	I can see my saved combinations in my account dashboard
	I can view, edit, or delete saved combinations
Additional Functionality
10.	Date Range Filtering
o	As a user, I want to specify what dates I'm looking at so I don't get results from months away.
o	Criteria for satisfaction: 
	Can set start and end dates for my search
	System only looks at availability within those dates
	Default range should be reasonable (like next 2 weeks)
11.	Meeting Length Settings
o	As a user, I want to specify how long my meeting needs to be.
o	Criteria for satisfaction: 
	Can set minimum meeting length (30 mins, 1 hour, etc.)
	System only shows time slots long enough for my meeting
	Should tell me if no slots are long enough
12.	Email Sharing
o	As a user, I want to email the results to everyone so they can see when we're all free.
o	Criteria for satisfaction: 
	Can enter email addresses of participants
	System sends a nicely formatted email showing when everyone's free
	Email includes any Calendly or Lettucemeet links I generated
13.	Mobile-Friendly Design
o	As a mobile user, I want the app to work well on my phone so I can use it on the go.
o	Criteria for satisfaction: 
	Interface should look good and be usable on small screens
	All important functions work on mobile browsers
	Design is optimized for touchscreens
14.	Dark Mode Option
o	As a user, I want a dark mode to use at night or in dark rooms.
o	Criteria for satisfaction: 
	Can switch between light and dark modes
	Dark mode looks consistent across all pages
	App remembers my preference
Nice-to-Have Features
15.	Viewing History
o	As a logged-in user, I want to see my previous calendar combinations.
o	Criteria for satisfaction: 
	Dashboard shows recent combinations I've made
	History includes when I made them and a summary of results
	Can sort and filter my history entries
16.	Direct Calendar Connection
o	As a user, I want to connect my calendar directly instead of manually creating share links.
o	Criteria for satisfaction: 
	Can authorize the app to access my Google/Microsoft/Apple calendar
	System handles the authentication securely
	System correctly finds my availability from my connected calendar
17.	Team Management
o	As a team organizer, I want to save groups of people so I can quickly schedule with the same team again.
o	Criteria for satisfaction: 
	Can create and name different teams/groups
	Can add or remove people from groups
	Can select a saved group when creating a new calendar combination
18.	Exporting Options
o	As a user, I want to export the available times in different formats to use elsewhere.
o	Criteria for satisfaction: 
	System offers common export formats (CSV, iCal)
	Exported data includes all the important information
	Files download correctly to my device
19.	Recurring Meeting Support
o	As a team organizer, I want to find times that work regularly so I can schedule weekly/monthly meetings.
o	Criteria for satisfaction: 
	Can specify pattern (weekly, bi-weekly, monthly)
	System finds slots that work consistently for that pattern
	Results clearly show which slots work for recurring meetings
20.	Guest Access
o	As a user without an account, I want to use basic features without signing up.
o	Criteria for satisfaction: 
	Guest users can do basic calendar combining
	Results are available for at least 24 hours
	Guests are encouraged but not forced to create an account
21.	Buffer Time Settings
o	As a user, I want to add buffer time between meetings so I'm not rushing from one to the next.
o	Criteria for satisfaction: 
	Can set buffer time (15 mins, 30 mins, etc.)
	System considers this buffer when finding available slots
	Buffer preference can be saved in my settings
22.	Feedback System
o	As a user, I want to give feedback about the app so the developers know what to improve.
o	Criteria for satisfaction: 
	Can submit feedback through a form in the app
	Can give both a rating and written comments
	Get confirmation that my feedback was received
I think we should focus on the first 6 stories for Sprint 1 since they form the core functionality. Then we can add user accounts and the additional features in Sprint 2. The nice-to-have features could be considered if we have extra time or for future development after the course.

![image](https://github.com/user-attachments/assets/f40559d4-866f-46ed-b277-dce542ad2cd4)
