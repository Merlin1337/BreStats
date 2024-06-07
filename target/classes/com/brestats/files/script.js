var markers = new Array();

var lat = 48.2; 
var lon = -3.0; 
var map = null;

var greyMarker = new L.icon({
    iconUrl: "https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-grey.png",
    shadowUrl: "https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png"
})

var blueMarker = new L.icon({
    iconUrl: "https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-blue.png",
    shadowUrl: "https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png"
})

function setMarkers(latitudes, longitudes) {
    markers.forEach(element => {
        map.removeLayer(element)
        console.log("removed")
    });
    markers = new Array()

    
    if(latitudes.length == longitudes.length) { 
        for(var i = 0 ; i < latitudes.length ; i++) {
            var marker = new L.marker(L.latLng(latitudes[i], longitudes[i]), {icon: greyMarker})

            markers.push(marker)
            marker.addTo(map)
        }
    }
}

function setGreyMarker(lat, lgn) {
    var i = 0;
    var trouve = false;
    while(i < markers.length || !trouve) {
        if(markers[i].getLatLng().lat == lat && markers[i].getLatLng().lng == lgn) {
            trouve = true;
            markers[i].setIcon(greyMarker);
        }
        i++;
    }
}

function setBlueMarker(lat, lgn) {
    var i = 0;
    var trouve = false;
    while(i < markers.length || !trouve) {
        if(markers[i].getLatLng().lat == lat && markers[i].getLatLng().lng == lgn) {
            trouve = true;
            markers[i].setIcon(blueMarker);
        }
        i++;
    }
}

document.addEventListener("DOMContentLoaded", function() {
    map = L.map('mapDiv').setView([lat, lon], 8);
    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors',
    }).addTo(map);

    map.addEventListener("click", function(ev) {
        console.log(ev.latlng.lat, ev.latlng.lng)
        markers.forEach(element => {
            map.removeLayer(element)
        });
        markers = new Array()
        
        markers.push(new L.marker(ev.latlng))
        markers[0].addTo(map)
        invoke.receiveCoordinates(ev.latlng.lat, ev.latlng.lng)
    })
})