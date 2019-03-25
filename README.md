# MercedesIO Challenge

## Identification

Daniel Seara:

* school : Instituto Superior Técnico - ULisboa
* year : 2nd year
* email : daniel.gseara@gmail.com

## Overview

This back-end challenge consisted on making a Command Line Interface (CLI) that could check if a given number of services was up or down such as:

* github
* slack
* bitbucket

### What could be Improved

* input reading and parsig could me more flexible (Command design pattern could be implemented)
* flag mechanism is hardcoded for each command, could be refactored to be the same for all
* status command mmtf calculation not implemented
* can only restore previous state from a binary file
* More services can only be added in a txt and program needs to be restarted
* Information to fetch in the service HTML page is harcoded for each service
* More services could be added in config.txt

### Design Implementations

* Used java because of structures/objects at my disposal
* JSoup made it easy to parse an HTML page and find the information needed
* Some normal input verifications not done, because it is supposed to be used by people who know the tool (eg. not verifying if --refresh flag in fetch command is a Number)
* Local storage is only an array in the Service Object. Storage can only be saved to a file using the backup command
* Service is up if and only if all the information is in the page. In any other case is considered down
* File name to backup/restore needs to be the last thing written in the command

``` markdown
backup [optional args] file -> right
```

``` markdown
backup file [optional args] -> wrong
```

### Problems faced

* no ideia how to calculate mttf bases on collected records
* can't test if service is down until it is actually down
* lack of knowledge for certain problems

## Built With

* [Java](java-1.8.0_191)
