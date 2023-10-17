# Family Map - Server 
This project is the server side of the Family Map project. This project was the culmination of BYU's Advanced Programming Concepts course, otherwise known as C S 240. 

# About
The Family Map application gives users a geographical overview of their family history. It allows users to discover their origins and helps them visualize their origins. The client (an Android app) displays ancestor's events, plots them on a google map, and links related events together.

The Family Map Server provides an easy way to access simulated family history data. It provides several endpoints which allow clients to access and modify data which is stored in a SQLite database. The endpoints are as follows:

**/user/register:** Registers a new user into the database and gives them 4 generations of simulated family history data.

**/user/login:** Logs a user in, giving them an authtoken to use while accessing the database.

**/clear:** Clears all data from the database.

**/fill/[username]/{generations}:** Populates the database with up to 7 generations of family history data. If number of generations not provided, 4 generations are given.

**/load:** Clears all data from the database, and loads in new data.

**/person/[personID]:** Returns information on a specific person from the database.

**/person:** Returns a list of the current user's family members.

**/event/[eventID]:** Returns information on a specific event from the database.

**/event:** Returns all events for all of the current user's family members.

# More Information
See <a href="">Family Map Client</a> for application usage.
For more detailed project specifications, <a href="https://docs.google.com/document/d/1KqfmsqCAPkXThgg81YMif_oVQZ1jVJGmOwmIbYmDRsM/edit?usp=sharing">click here.</a>
