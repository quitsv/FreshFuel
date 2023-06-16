# Cloud Computing Documentation
***
Don't forget to enables all services and API that needed in APIs and Services, and also create bucket to store data.
***
## Compute Engine to deploy model and Flask API
1. Make VM instance, with the required configuration.
   * Make firewall to allow port tcp 8080.
   * Add network tag to apply firewall rule.
2. Using SSH to connect VM instance and install Flask `pip install Flask`.
3. Make requirements.txt in the folder you want to deploy, write library that need to be installed such as pandas, tensorflow, etc.
4. `sudo apt update` and `sudo apt upgrade`.
5. Install dependencies using command `apt-get install Python Python3-pip` and `pip install --upgrade pip virtualenv`.
6. Upload folder to compute engine or clone from github.
7. cd to your directory folder and run command `pip install requirements.txt`.
8. Run the python file `pyhton3 main.py`.
9. Install and using pm2 to make your API always run.

## App Engine to deploy Node.js API
1. Clone files from github.
2. Create app.yaml files to configure deployment in the folder.
3. Run `npm install` to install all dependencies in package.json.
4. `gcloud app deploy`

## Deployed App
Node.js API: http://api.fresh-fuel.as.r.appspot.com
Flask API: http://34.101.62.126:8080

## List of Endpoint
POST /auth/register -> body: name, email, password, allergies
POST /auth/login -> body: email, password
POST /auth/logout

POST /predict -> body: recipeName, rating, allergies
GET /getRecipe?recipeName={recipe}
GET /search?recipeName=beans
GET /generateRandom?allergies={allergies}

GET /progress/user/{email}
GET /progress/user/{email}?date={date}
POST /progress/user/{email} -> body: date, breakfastMenu
PUT /progress/user/{email} -> body: date, lunchMenu, rating
PUT /progress/user/{email} -> body: date, dinnerMenu, rating
PUT /progress/user/{email} -> body: date, rating
