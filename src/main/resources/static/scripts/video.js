
let yourVideosList = document.getElementById("your-videos");

fetch("http://localhost:8080/video/all")
    .then(res => res.json())
    .then(res => {
        for(let video of res) {
            let li = document.createElement("li");
            let a = document.createElement("a");
            a.href = 'http://localhost:8080/video?id=' + video.id;
            a.innerHTML = video.name;
            li.appendChild(a);
            yourVideosList.appendChild(li);
        }
    });

    let id = new URLSearchParams(window.location.search).get("id");
    if(id !== undefined) {
        let videoScreen = document.getElementById("video-screen");
        videoScreen.src = "http://localhost:8080/video/upload?id=" + id;
    }

let form = document.getElementById('video-form');

form.addEventListener('submit', ev => {
    ev.preventDefault();
    let data = new FormData(form);
    fetch('http://localhost:8080/video/new', {
        method: 'POST',
        body: data
    }).then(result => result.text()).then(_ => {
        window.location.reload();
    });

});