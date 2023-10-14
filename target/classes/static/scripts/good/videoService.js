let addVideoForm =
    `<div class="modalWrapper">
        <div class="contentWrapper">
            <div>
                <div class="text-end mb-4">
                <button style="font-size: 20px" type="button" class="btn-close btn-close-white" onclick="FormService.disable(this.parentNode.parentNode.parentNode.parentNode)" aria-label="Close"></button>
            </div>
            <div class="contentBody">
                  <div>
                   <label for="images">Add a new video</label>
                   <input id="inputVideo" class="form-control" type="file">
                </div>
            </div>        
                <button class="btn btn-success mt-3" onclick="addVideo()">Add</button>
            </div>
        </div>
    </div>`


async function enableVideoDeleteForm(elem) {
    let id = elem.getAttribute("id");
    let form = `<div class="modalWrapper">
        <div class="contentWrapper">
            <div>
                <div class="text-end mb-4">
                <button style="font-size: 20px" type="button" class="btn-close btn-close-white" onclick="FormService.disable(this.parentNode.parentNode.parentNode.parentNode)" aria-label="Close"></button>
            </div>
            <div class="contentBody">
                <h5 style="color: white">Are you sure that you want to delete this video?</h5>
            </div>        
                <button class="btn btn-success mt-3" onclick="deleteVideoById(${id})">Delete</button>
            </div>
        </div>
    </div>`

    FormService.enable(form);
}

async function deleteVideoById(id) {
    await fetch(`/good/manufacture-machine/video/${id}/delete`, {
        method: "DELETE"
    }).then((res) => {
        RequestService.processResponseAndReload(res)
    })
}

async function addVideo() {
    let video = document.getElementById("inputVideo").files[0];

    let fileURL = URL.createObjectURL(video);
    let player = document.createElement("video");
    player.src = fileURL;
    player.setAttribute("id", "videoPlayer");

    document.body.appendChild(player);
    let pl = document.getElementById("videoPlayer");

    pl.onloadedmetadata = function () {
        let formData = new FormData();
        formData.append("video", video);
        formData.append("duration", getVideoDuration(pl.duration))
        formData.append("goodId", getQueryParam('id'))

        fetch("/good/manufacture-machine/video/new", {
            method: "POST",
            body: formData
        }).then((resp) => {
            console.log(resp.status)
            RequestService.processResponseAndReload(resp)
        })
    }
}

function getVideoDuration(duration) {
    return "00:" + formatTimeValue(Math.round(duration / 60).toString()) + ":"   + formatTimeValue(Math.round(duration % 60).toString())
}

function formatTimeValue(val) {
    if (val.length === 1) {
        return "0" + val;
    } else if (val === '0') {
        return "00"
    } else return val;
}
