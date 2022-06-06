# Workshop

**configuration file setting:**

should be a file name .env in code/src/main/resources/ and contain the following details:
- Admin_username="<The value>"
- Admin_password="<The value>"
- Mod="<The value>"
- Database="<The value>"
- Payment_Connector="<The value>"
- Supply_Connector="<The value>"

  
**state file setting:**

should be a json file name state_init in code/src/main/resources/ and has the following attribute:
- functions: array of: 
  1. function: "name of the function to operate",
  2. args: array of the arguements of the function

  
for example {
             functions: [ {function: "functionA", 
                            args: [args1, args2, ... , argsK]
                        ]
              }
