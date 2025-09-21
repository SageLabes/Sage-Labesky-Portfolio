// list of locations to be filled in the dropdowns
var locations = [
    "Hartsfield-Jackson Atlanta International Airport",
    "Beijing Capital International Airport",
    "Dubai International Airport",
    "Los Angeles International Airport",
    "Tokyo Haneda Airport",
    "O'Hare International Airport (Chicago)",
    "London Heathrow Airport",
    "Hong Kong International Airport",
    "Shanghai Pudong International Airport",
    "Paris Charles de Gaulle Airport",
    "Amsterdam Schiphol Airport",
    "Guangzhou Baiyun International Airport",
    "Dallas/Fort Worth International Airport",
    "Denver International Airport",
    "Frankfurt Airport",
    "Singapore Changi Airport",
    "Istanbul Airport",
    "Seoul Incheon International Airport",
    "Jakarta Soekarno-Hatta International Airport",
    "Delhi Indira Gandhi International Airport"
];
//function to fill the locations dropdowns
function fillLocationsDropDown(){
    var start = document.getElementById("startingLocation");
    var end = document.getElementById("destination");

    for (var i = 0; i < locations.length; i++) {
        var opt = new Option(locations[i], locations[i]);
        start.add(opt);
        end.add(opt.cloneNode(true));
    }
}