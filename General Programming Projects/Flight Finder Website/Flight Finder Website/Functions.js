function populate(){ //Function to populate the table with the data from the csv file
    var files = document.getElementById("file").files;
    if(files.length <= 0 ){ //Checks to make sure a csv file has been uploaded
        alert("You have not uploaded a csv file, please upload one to continue");
        return;
    }
    //creates a new file reader object
    var reader = new FileReader();
    var file = files[0];
    reader.readAsText(file);
    //handles the file and loads it into the table
    reader.onload = function(e) {
        //variables to get text from file and access table in document
        var text = e.target.result;
        var rows = text.split('\n');
        var table = document.getElementById("previousFlights").getElementsByTagName("tbody");
        //clear the table
        table[0].innerHTML = "";
        //populate the table with the data from the csv file
        for (var i = 0; i < rows.length; i++) {
            var row = table[0].insertRow();
            dataPoint = rows[i].split(','); //splits the data into individual data points
            for (var j = 0; j < dataPoint.length; j++) {
                var dataSpot = row.insertCell(); //inserts the data into the table
                dataSpot.innerHTML = dataPoint[j];
            }
        }
    };
    localStorage.setItem("file", file);
}
//helper function to store information into local storage so that it can be accessed on the next page
function saveInfo(){
    localStorage.setItem("DepartureLocation", String(document.getElementById("startingLocation").value))
    localStorage.setItem("LandingLocation", String(document.getElementById("destination").value));
    localStorage.setItem("DepartureDate", String(document.getElementById("departureDate").value));
    localStorage.setItem("ReturnDate", String(document.getElementById("returnDate").value));
    localStorage.setItem("PassengerCount", String(document.getElementById("noOfPassengers").value));
    localStorage.setItem("BagCount", String(document.getElementById("noOfCheckedBags").value));
    location.href = "FlightResults.html";
}
//function to create a list of flight results based on the information provided by the user
function populateResultsTable(){
    var table = document.getElementById("previousFlights").getElementsByTagName("tbody");
    //gets the information required from local storage and stores it in an array
    var info = [];
    info[0] = localStorage.getItem("DepartureLocation");
    info[1] = localStorage.getItem("LandingLocation");
    info[2] = localStorage.getItem("DepartureDate");
    info[3] = localStorage.getItem("ReturnDate");
    //doesn't use a loop because I wasn't sure how to dynamically make new buttons with unique onclick functions
    //First row
    var row = table[0].insertRow();
    var dataSpot = row.insertCell();
    //variables to store a randomly generated flight number and random times for the two flights
    var flightNumber1 = Math.floor((Math.random()*1000));
    var departTimes = createRandomTimes();
    var returnTimes = createRandomTimes();
    dataSpot.innerHTML = flightNumber1;
    //makes a new cell in the table for the locations since they don't need to be edited
    for(i = 0; i < 2; i++){
        dataSpot = row.insertCell();
        dataSpot.innerHTML = info[i];
    }
    dataSpot = row.insertCell();
    dataSpot.innerHTML = info[2] + " " + departTimes[0];
    dataSpot = row.insertCell();
    dataSpot.innerHTML = info[3] + " " + returnTimes[0];
    //generates a random price for the flight
    var price1 = Math.floor((Math.random()*400)+100);
    row.insertCell().innerHTML = "$" + price1;
    //creates a button to select the flight and stores the information in local storage
    const selectButton1 = document.createElement("button");
    selectButton1.onclick = function(){
        location.href = "FlightDetails.html";
        localStorage.setItem("Price", price1);
        localStorage.setItem("FlightNumber", flightNumber1);
        localStorage.setItem("DepartureDate", info[2] + " " + departTimes[0]);
        localStorage.setItem("ReturnDate", info[3] + " " + returnTimes[0]);
    }
    selectButton1.textContent = "Select";
    selectButton1.className = "white-button";
    selectButton1.id = "selectButton";
    row.insertCell().appendChild(selectButton1);

    //Second row, basically identical to the first, just with different variable names
    var row = table[0].insertRow();
    var dataSpot = row.insertCell();
    var flightNumber2 = Math.floor((Math.random()*1000));
    dataSpot.innerHTML = flightNumber2;
    for(i = 0; i < 2; i++){
        dataSpot = row.insertCell();
        dataSpot.innerHTML = info[i];
    }

    dataSpot = row.insertCell();
    dataSpot.innerHTML = info[2] + " " + departTimes[1];
    dataSpot = row.insertCell();
    dataSpot.innerHTML = info[3] + " " + returnTimes[1];
    
    var price2 = Math.floor((Math.random()*400)+100);
    row.insertCell().innerHTML = "$" + price2;
    const selectButton2 = document.createElement("button");
    selectButton2.onclick = function(){
        location.href = "FlightDetails.html";
        localStorage.setItem("Price", price2);
        localStorage.setItem("FlightNumber", flightNumber2);
        localStorage.setItem("DepartureDate", info[2] + " " + departTimes[1]);
        localStorage.setItem("ReturnDate", info[3] + " " + returnTimes[1]);
    }
    selectButton2.textContent = "Select";
    selectButton2.className = "white-button";
    selectButton2.id = "selectButton";
    row.insertCell().appendChild(selectButton2);

    //Third row
    var row = table[0].insertRow();
    var dataSpot = row.insertCell();
    var flightNumber3 = Math.floor((Math.random()*1000));
    dataSpot.innerHTML = flightNumber3;
    for(i = 0; i < 2; i++){
        dataSpot = row.insertCell();
        dataSpot.innerHTML = info[i];
    }

    dataSpot = row.insertCell();
    dataSpot.innerHTML = info[2] + " " + departTimes[2];
    dataSpot = row.insertCell();
    dataSpot.innerHTML = info[3] + " " + returnTimes[2];
    
    var price3 = Math.floor((Math.random()*400)+100);
    row.insertCell().innerHTML = "$" + price3;
    const selectButton3 = document.createElement("button");
    selectButton3.onclick = function(){
        location.href = "FlightDetails.html";
        localStorage.setItem("Price", price3);
        localStorage.setItem("FlightNumber", flightNumber3);
        localStorage.setItem("DepartureDate", info[2] + " " + departTimes[2]);
        localStorage.setItem("ReturnDate", info[3] + " " + returnTimes[2]);
    }
    selectButton3.textContent = "Select";
    selectButton3.className = "white-button";
    selectButton3.id = "selectButton";
    row.insertCell().appendChild(selectButton3);
   
}
//function to get all of the stored data from local storage and display it on the page
function populateFlightDetails(){
    document.getElementById("price").innerHTML = "$" + localStorage.getItem("Price");
    document.getElementById("departLoc").innerHTML = "Departing from: " + localStorage.getItem("DepartureLocation");
    document.getElementById("destLoc").innerHTML = "Arriving at: " + localStorage.getItem("LandingLocation");
    document.getElementById("departDate").innerHTML = "Departing: " + localStorage.getItem("DepartureDate");
    document.getElementById("returnDate").innerHTML = "Returning: " + localStorage.getItem("ReturnDate");
    document.getElementById("flightNumber").innerHTML = "Flight Number: " + localStorage.getItem("FlightNumber");
}
//helper function to create random times for the flights
function createRandomTimes(){
    var hours = Math.floor((Math.random()*8)+1);
    var minutes = Math.floor((Math.random()*50)+10);
    var times = [];

    times[0] = hours + ":" + minutes;
    hours2 = hours + Math.floor((Math.random()*8)+1);
    minutes2 = Math.floor((Math.random()*50)+10);
    times[1] = hours2 + ":" + minutes2;

    hours3 = hours2 + Math.floor((Math.random()*8)+1);
    minutes3 = Math.floor((Math.random()*50)+10);
    times[2] = hours3 + ":" + minutes3;

    return times;
}

function addToPrevFlights(){ //Function to add a new flight to the csv file
    var files = document.getElementById("file").files;
    if(files.length == 0){ //checks to make sure a csv file has been uploaded
        alert("You have not uploaded a csv file, please upload one to continue");
        return;
    }
    //variables to get the data from the form in the html document
    var startingLocation = localStorage.getItem("DepartureLocation");
    var flightNumber = localStorage.getItem("FlightNumber");
    var destination = localStorage.getItem("LandingLocation");
    var departureDate = localStorage.getItem("DepartureDate");
    var returnDate = localStorage.getItem("ReturnDate");
    var noOfPassengers = String(document.getElementById("noOfPassengers").value);
    var noOfCheckedBags = String(document.getElementById("noOfCheckedBags").value);
    //checks to make sure all fields are filled out
    if (startingLocation == "" || destination == "" || departureDate == "" || returnDate == "" || noOfPassengers == "" || noOfCheckedBags == ""){
        alert("Error: Please fill out all fields");
        return;
    }
    var reader = new FileReader();
    var file = files[0];
    //creates a new line to add to the csv file using the information from the form
    var newLine = startingLocation + "," + destination + "," + departureDate + "," + returnDate + "," + noOfPassengers + "," + noOfCheckedBags;
    reader.readAsText(file);
    reader.onload = function(e) {
        var text = e.target.result;
        //adds the new line to the full text of the csv file
        text += "\n" + flightNumber + "," + newLine;
        text = text.toString();
        //creates a new blob to download the new csv file
        const blob = new Blob([text], { type: "text/csv" });
        //downloads the csv file
        const link = document.createElement("a");
        link.href = URL.createObjectURL(blob);
        link.download = "PreviousFlights.csv";
        document.body.appendChild(link);
        link.click();
        //redirects to the confirmation page
        location.href = "Confirmation.html";
    };
}
//function to open the user's email client with the flight information already filled out
function openEmail() {
    const recipient = "example@example.com";
    const subject = "This is my flight information!";
    const body = `Hello, I am emailing you to share my flight information. My flight number is ${localStorage.getItem("FlightNumber")}, I am departing from ${localStorage.getItem("DepartureLocation")} and arriving at ${localStorage.getItem("LandingLocation")}. I am departing on ${localStorage.getItem("DepartureDate")} and returning on ${localStorage.getItem("ReturnDate")}. I have ${localStorage.getItem("PassengerCount")} passengers and ${localStorage.getItem("BagCount")} checked bags.`;
    const mailtoLink = `mailto:${recipient}?subject=${encodeURIComponent(subject)}&body=${encodeURIComponent(body)}`;

    // Open the user's email client
    window.location.href = mailtoLink;
}