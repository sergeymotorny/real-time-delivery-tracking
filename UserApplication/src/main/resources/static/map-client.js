
const isCourier = false;

//we get an element <div id = "map"> and its DATA attributes
const mapElement = document.getElementById("map");
if (!mapElement) {
    console.error("No found #map!");
    throw new Error("The #map element is absent!");
}

//client coordinates (where the courier goes)
const orderLat = parseFloat(mapElement.dataset.orderLat);
const orderLng = parseFloat(mapElement.dataset.orderLng);

//the starting coordinates of the courier (those who set the backend in createShipmentForOrder)
const initialCourierLat = parseFloat(mapElement.dataset.courierLat);
const initialCourierLng = parseFloat(mapElement.dataset.courierLng);

//check that the coordinates and the right
if (isNaN(orderLat) || isNaN(orderLng)) {
    console.error("The incorrect customer coordinates:", {
        orderLat: mapElement.dataset.orderLat,
        orderLng: mapElement.dataset.orderLng
    });
    throw new Error("Client coordinates are not correctly set");
}

//initialize the Leaflet map
const map = L.map('map', {
    center: [orderLat, orderLng],
    zoom: 13,
    minZoom: 4,
    maxZoom: 19,
    maxBounds: [
        [-60, -180],
        [80, 180]
    ],
    maxBoundsViscosity: 1.0
});

//add Tyl-layer (OpenStreetMap)
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    minZoom: 2,
    noWrap: true
}).addTo(map);

//preparation of icons: for the client and for the courier
const clientIcon = L.icon({
    iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34]
});

//courier icon
const courierIcon = L.icon({
    iconUrl: '/images/bus.png',
    iconSize: [36, 36],
    iconAnchor: [16, 36],
    popupAnchor: [0, -36]
});

//I immediately put the customer marker (destination)
const clientMarker = L.marker([orderLat, orderLng], { icon: clientIcon })
    .addTo(map)
    .bindPopup('Client (destination)')
    .openPopup();

// put the starting marker of the courier (if there is data)
let courierMarker = null;
if (!isNaN(initialCourierLat) && !isNaN(initialCourierLng)) {
    courierMarker = L.marker([initialCourierLat, initialCourierLng], {icon: courierIcon})
        .addTo(map)
        .bindPopup('courier: starting position');

    //we make so that the client and the courier are in the frame
    const group = new L.featureGroup([clientMarker, courierMarker]);
    map.fitBounds(group.getBounds().pad(0.5));
}

//object for storing routes and markers "in animation" by UUID:
const courierRoutes = {};

const socket = new WebSocket('ws://localhost:8081/ws/web'); //connect to Endpoint

//to distinguish your own messages
let myUUID = localStorage.getItem("myUUID");
if (!myUUID) {
    myUUID = crypto.randomUUID();
    localStorage.setItem("myUUID", myUUID);
}

//when opening WS
socket.onopen = () => console.log("WebSocket (client) connected!");
socket.onerror = e => console.error("WebSocket (client) Error:", e);

socket.onclose = evt => {
    if (event.wasClean) {
        console.log(`WebSocket (Client) closed, code=${event.code}, cause=${event.reason}`);
    } else {
        console.error('WebSocket (client) fell (unclean closure)');
    }
};

function decodePolyline(encoded) {
    let points = [];
    let index = 0, len = encoded.length;
    let lat = 0, lng = 0;

    while (index < len) {
        let b, shift = 0, result = 0;
        do {
            b = encoded.charCodeAt(index++) - 63;
            result |= (b & 0x1f) << shift;
            shift += 5;
        } while (b >= 0x20);
        let dlat = ((result & 1) ? ~(result >> 1) : (result >> 1));
        lat += dlat;

        shift = 0;
        result = 0;
        do {
            b = encoded.charCodeAt(index++) - 63;
            result |= (b & 0x1f) << shift;
            shift += 5;
        } while (b >= 0x20);
        let dlng = ((result & 1) ? ~(result >> 1) : (result >> 1));
        lng += dlng;

        points.push([lat / 1e5, lng / 1e5]);
    }
    return points;
}

//the function for building a "real" route through Openrauteservice
//drawRoute: animated construction of the route "On the way"
function drawRoute(uuid, start, end) {
    console.log(`\n[drawRoute] uuid=${uuid}, start=(${start.lat},${start.lng}), end=(${end.lat},${end.lng})`);

    //if the courier point coincides with the client’s point - we do not draw anything
    if (start.lat === end.lat && start.lng === end.lng) {
        console.log("[drawRoute] start === end, we miss the construction of the route!");
        return;
    }

    const apiKey = '#';
    const url = 'https://api.openrouteservice.org/v2/directions/driving-car';

    fetch(url, {
        method: 'POST',
        headers: {
            'Authorization': apiKey,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            coordinates: [
                [start.lng, start.lat],
                [end.lng, end.lat]
            ]
        })
    })
        .then(response => {
            if (!response.ok) throw new Error("ORS HTTP error " + response.status);
            return response.json();
        })
        .then(data => {
            console.log("[drawRoute] Answer from ORS:", data);

            if (!data.routes || data.routes.length === 0) {
                console.error("[drawRoute] No routes in the answer of ORS");
                return;
            }

            const route = data.routes[0];
            const encoded = route.geometry;
            console.log("[drawRoute] Encoded waterline:", encoded);

            //we decode in an array of dots
            const decodedCoords = decodePolyline(encoded);
            console.log("[drawRoute] Decoded points (first 5):", decodedCoords.slice(0,5));

            if (decodedCoords.length < 2) {
                console.warn("[drawRoute] After decoding less than two points - the line is not drawn.");
                return;
            }

            //remove the old line and marker if there is UUID for this
            if (courierRoutes[uuid]) {
                console.log("[drawRoute] Delete the previous line and a marker for uuid=", uuid);
                if (courierRoutes[uuid].line)   map.removeLayer(courierRoutes[uuid].line);
                if (courierRoutes[uuid].marker) map.removeLayer(courierRoutes[uuid].marker);
            }

            //delete the static marker Courier Marker (from initialCourier) if it is still hanging
            if (courierMarker) {
                console.log("[drawRoute] We delete the static marker courierMarker");
                map.removeLayer(courierMarker);
                courierMarker = null;
            }

            //we draw a new polonium "On the way"
            const line = L.polyline(decodedCoords, { color: 'blue', weight: 4 }).addTo(map);
            console.log("[drawRoute] Route line added: points=", decodedCoords.length);

            //create an animated marker that runs along the decodedCoords
            const marker = L.marker(decodedCoords[0], { icon: courierIcon }).addTo(map);
            let index = 0;
            function move() {
                if (index < decodedCoords.length) {
                    marker.setLatLng(decodedCoords[index]);
                    index++;
                    setTimeout(move, 650); //animation speed: 6500 ms between steps
                }
            }
            console.log("[drawRoute] Launch of marker animation, all steps:", decodedCoords.length);
            move();

            //we save in courierRoutes for this uuid
            courierRoutes[uuid] = {
                line: line,
                marker: marker,
                index: 0
            };
        })
        .catch(error => console.error("Route error (drawRoute):", error));
}

//processor of incoming websocket messages
socket.onmessage = function (event) {
    let data;
    try {
        data = JSON.parse(event.data);
    } catch (e) {
        console.error("[onmessage] Not parsed JSON:", event.data);
        return;
    }

    const {uuid, lat, lng} = data;
    if (uuid === myUUID) return; //we do not process our packages

    console.log("\n[onmessage] Received the coordinates of the courier:", lat, lng);

    //if the static marker courierMarker (from initialCourier) is still “hanging” - delete
    if (courierMarker) {
        console.log("[onmessage] Delete the static marker courierMarker");
        map.removeLayer(courierMarker);
        courierMarker = null;
    }


    //we call an animated construction of the route
    // start = { lat, lng } — the current point of the courier
    // end   = { lat: orderLat, lng: orderLng } — client's point
    drawRoute(uuid, { lat: lat, lng: lng }, { lat: orderLat, lng: orderLng });
};
