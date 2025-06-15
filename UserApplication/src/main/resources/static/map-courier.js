
//we connect to the same websocket endpoint
const socket = new WebSocket('ws://localhost:8081/ws/web');

//generation/reading UUID courier from localStorage
let myUUID = localStorage.getItem("myUUID");
if (!myUUID) {
    myUUID = crypto.randomUUID();
    localStorage.setItem("myUUID", myUUID);
}

socket.onopen = function () {
    console.log("✅ WebSocket (courier) connected");

    //immediately send coordinates from the base (hardcode, data-*)
    const initialLat = 46.974429;  //replace with dynamic if transmit from Thymeleaf
    const initialLng = 32.019642;
    socket.send(JSON.stringify({ uuid: myUUID, lat: initialLat, lng: initialLng }));
    console.log("Courier: sent the starting coordinates from the БД", initialLat, initialLng);

    //when WebSocket is ready, launch watchPosition
    if (navigator.geolocation) {
        navigator.geolocation.watchPosition(
            position => {
                const { latitude, longitude } = position.coords;
                socket.send(JSON.stringify({ uuid: myUUID, lat: latitude, lng: longitude }));
                console.log("Courier: Sends real coordinates", latitude, longitude);
            },
            error => {
                console.error("Courier geolocation error:", error);
            },
            {
                enableHighAccuracy: true,
                maximumAge: 0,
                timeout: 5000
            }
        );
    } else {
        alert("Geolocation is not supported by this browser!");
    }
};

socket.onerror = function (error) {
    console.error("WebSocket (courier) Error:", error);
};

socket.onclose = function (event) {
    if (event.wasClean) {
        console.log(`WebSocket (courier) closed, cored=${event.code}, cause=${event.reason}`);
    } else {
        console.error('WebSocket (courier) fell (unclean closure)');
    }
};