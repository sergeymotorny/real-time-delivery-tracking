
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

    // Send initial coordinates from depot
    // Real movement simulation is handled by DriverApplication via Kafka → WebSocket
    const initialLat = 46.974429;
    const initialLng = 32.019642;
    socket.send(JSON.stringify({ uuid: myUUID, lat: initialLat, lng: initialLng }));
    console.log("Courier: sent starting coordinates from depot", initialLat, initialLng);
};

socket.onerror = function (error) {
    console.error("WebSocket (courier) Error:", error);
};

socket.onclose = function (event) {
    if (event.wasClean) {
        console.log(`WebSocket (courier) closed, code=${event.code}, cause=${event.reason}`);
    } else {
        console.error('WebSocket (courier) fell (unclean closure)');
    }
};
