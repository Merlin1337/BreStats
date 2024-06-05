var markers = new Array();

var lat = 48.2; 
var lon = -3.0; 
var map = null;

function setMarkers(latitudes, longitudes) {
    markers.forEach(element => {
        map.removeLayer(element)
        console.log("removed")
    });
    markers = new Array()

    
    if(latitudes.length == longitudes.length) { 
        for(var i = 0 ; i < latitudes.length ; i++) {
            var marker = new L.marker(L.latLng(latitudes[i], longitudes[i]))

            markers.push(marker)
            marker.addTo(map)
        }
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