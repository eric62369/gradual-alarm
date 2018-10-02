# gradual-alarm
A specially designed alarm clock app that gradually wakes up the user

### Project Description / Ideation
This is my first Android app project, coming off of the fact that I cannot wake up in the morning with normal alarm clocks.
My mind has at this point become too used to hitting snooze every time my alarm clock goes off.

My solution was to make an alarm clock that starts playing a white noise sound an hour before the time the user sets. Once halfway through the alarm, more typical alarm clock noises will play and gradually increase volume in steps until the user's set time is reached, where by then the alarm is at a typical alarm clock volume.

### Specific challenges
The alarm clock timing event system that I used for the current implementation does work, and is very accurate with the time that it goes off at. However, there are big downsides that impair the app's functionality to the point where the alarms work very unreliably if the phone is not charging or the app not open in the background. For now, I decided that the system works well enough, but I did place a redesign of that system high on the priority list.

### Conclusion
This project was a great introduction into Android programming. It was also a great use of my current Java programming experience to apply it to a (incredibly small) real world problem. I have been using the alarm for awhile now, and it is indeed much more effective than any of my previous alarms.

### Next Steps
Reimplement the timing system by using some alternative background service, one that can run even though the app is closed, and the phone is in doze state.
