Rename "scheduler.bat.txt" to "scheduler.bat".
Run "scheduler.bat" on command prompt in a system with maven installed.
The program will as for a command, available commands:
-usage
-add
-exit
You need to run add to add a schedule file. Schedule files needs to be under  \MeetingScheduler\src\main\resources\schedulerfiles folder.
sample output of the program:
------
>usage
[com.enscala.meetingscheduler.cmd.TaskRunner.main()] INFO  TaskRunner -
====== Usage ======
add = add a schedule after prompting for file path
exit = exits the program
usage = prints this help
>add
Schedule file?
schedule.txt
[com.enscala.meetingscheduler.cmd.TaskRunner.main()] INFO  TaskRunner - 2011-03-
21
09:00 11:00 EMP002
2011-03-22
14:00 16:00 EMP003
16:00 17:00 EMP004
>
