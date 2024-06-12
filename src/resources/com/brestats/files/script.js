var markers = new Array();
var redMarkers = new Array();

var lat = 48.2; 
var lon = -3.0; 
var map = null;
var blueColoredMarker = null;
var canPlaceMarker = true;

var greyMarker = new L.icon({
    iconUrl: "https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-grey.png",
    shadowUrl: "https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png"
})

var blueMarker = new L.icon({
    iconUrl: "https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-blue.png",
    shadowUrl: "https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png"
})

var redMarker = new L.icon({
    iconUrl: "https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-red.png",
    shadowUrl: "https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png",
    iconAnchor: L.point(12, 41)
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
            marker.on("mouseover", function(ev) {
                canPlaceMarker = false

                setGreyMarker(blueColoredMarker.getLatLng().lat, blueColoredMarker.getLatLng().lng)
                setBlueMarker(this.getLatLng().lat, this.getLatLng().lng)
                invoke.receiveCoordinates(this.getLatLng().lat, this.getLatLng().lng)
            })

            marker.on("mouseout", function(ev) {
                canPlaceMarker = true;
            })
        }
    }
}

function setRedMarkers(latitudes, longitudes) {
    redMarkers.forEach(element => {
        map.removeLayer(element)
        console.log("removed")
    });
    redMarkers = new Array()

    for(var i = 0 ; i < latitudes.length ; i++) {
        var marker = new L.marker(L.latLng(latitudes[i], longitudes[i]), {icon: redMarker})

        redMarkers.push(marker)
        marker.addTo(map)
    }
}

function setGreyMarker(lat, lng) {
    var i = 0;
    var trouve = false;
    while(i < markers.length && !trouve) {
        if(markers[i].getLatLng().lat == lat && markers[i].getLatLng().lng == lng) {
            trouve = true;
            markers[i].setIcon(greyMarker);
        }
        i++;
    }
}

function setBlueMarker(lat, lng) {
    var i = 0;
    var trouve = false;
    while(i < markers.length && !trouve) {
        if(markers[i].getLatLng().lat == lat && markers[i].getLatLng().lng == lng) {
            trouve = true;
            markers[i].setIcon(blueMarker);
            blueColoredMarker = markers[i]
        }
        i++;
    }
}

function addMapClickListener() {
    map.addEventListener("click", function(ev) {
        if(canPlaceMarker) {
            console.log(ev.latlng.lat, ev.latlng.lng)
            markers.forEach(element => {
                map.removeLayer(element)
            });
            markers = new Array()
            
            markers.push(new L.marker(ev.latlng))
            markers[0].addTo(map)
            invoke.receiveCoordinates(ev.latlng.lat, ev.latlng.lng)
        }
    })
}

document.addEventListener("DOMContentLoaded", function() {
    map = L.map('mapDiv').setView([lat, lon], 8);
    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors',
    }).addTo(map);

    
})