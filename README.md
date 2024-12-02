# Advent of code #
This is a community driven Java template and utility API for the annual [Advent of Code](https://adventofcode.com) event.

# Setup #

1. Clone this repository from GitHub as an IntelliJ repository (untested on other IDE's)
2. Add the `session_cookie` environment variable to your gradle run config and set it equal to that of your session cookie after logging in to your AoC account.
3. Execute the gradle application -> run task.

# Getting your session cookie #

In Chrome, or other Chromium browsers such as Opera, OperaGX etc
1. Hit ctrl + shift + J to open up developer tools
2. Navigate to the application tab
3. Click cookies
4. Copy the session cookie

![](/setup/4.png)

# Modifiying Gradle run configuration #
1. Go to your Gradle tasks and go to application -> run and right click to modify the run configurations
![](/setup/1.png)
2. Click this box to add an environment variable
![](/setup/2.png)
3. Name the environment variable `session_cookie` and then paste the value of your session cookie from your browser in to the value box.
![](/setup/3.png)
4. Hit okay and then execute the Gradle run task
